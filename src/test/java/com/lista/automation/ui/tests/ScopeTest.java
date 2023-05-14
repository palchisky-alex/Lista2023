package com.lista.automation.ui.tests;

import com.lista.automation.api.pojo.appointment.AppointmentGetRequest;
import com.lista.automation.api.pojo.client.ClientCreateRequest;
import com.lista.automation.api.pojo.client.ClientGetResponse;
import com.lista.automation.api.pojo.general_settings.GeneralSettingsPojo;
import com.lista.automation.api.pojo.group.GroupCreateRequest;
import com.lista.automation.api.pojo.group.GroupsGetResponse;
import com.lista.automation.api.pojo.service.ServiceCreateRequest;
import com.lista.automation.ui.core.BaseTest;
import com.lista.automation.ui.pages.calendar.CalendarPage;
import com.lista.automation.ui.pages.group.GroupsListPage;
import com.lista.automation.ui.pages.service.ServicesListPage;
import com.microsoft.playwright.Locator;
import io.qameta.allure.Description;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.lista.automation.api.authentication.Scope.*;
import static com.lista.automation.ui.core.utils.BasePage.*;
import static com.lista.automation.ui.core.utils.CalendarView.Daily;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Palchitsky Alex
 */
public class ScopeTest extends BaseTest {

    @Test
    @Description("UI: admin worker create group ->" +
            "login to junior account and verify visibility of group ->" +
            "delete group within Junior account")
    public void testScopeAdmin_GroupVisibility() {
        step("API: create group within admin account", () -> {
            GroupCreateRequest simpleGroup = generateGroup(true);
            int groupID = api.group().create(simpleGroup, 201);

            step("UI: login as Junior", () -> {
                GroupsListPage groupPage =
                        calendar.loginAs(JUNIOR)
                                .routing()
                                .toGroupsListPage();

                step("JS: assert Junior account", () -> {
                    assertThat(groupPage.getPermissionLevel().toUpperCase())
                            .as("junior account").isEqualTo(JUNIOR.name());

                    step("UI: assert", () -> {
                        assertThat(groupPage.getLocator("[group-id='" + groupID + "']").isVisible())
                                .as("group exists within the Junior account")
                                .isTrue();

                        step("UI: delete group within the Junior account", () -> {
                            groupPage
                                    .longPressOnGroup(simpleGroup.getName())
                                    .configureGroup(GroupsListPage.ACTION.Delete);

                            step("UI: assert", () -> {
                                assertThat(!groupPage.getLocator("[group-id='" + groupID + "']").isVisible())
                                        .as("deleted group does not exists within the Junior account")
                                        .isTrue();
                            });
                        });
                    });
                });
            });
        });
    }

    @Test
    @Description("UI: admin worker create service ->" +
            "login to junior account and verify visibility of service ->" +
            "delete service within Junior account")
    public void testScopeAdmin_ServiceVisibility() {
        step("API: create service within Admin account", () -> {
            ServiceCreateRequest simpleService = generateService(true);
            api.service().create(simpleService, 201);

            step("UI: login as Junior and search the service", () -> {
                ServicesListPage servicesPage =
                        calendar.loginAs(JUNIOR)
                                .routing()
                                .toServicesListPage()
                                .findService(simpleService.getServiceName());

                step("JS: assert Junior account", () -> {
                    assertThat(servicesPage.getPermissionLevel().toUpperCase())
                            .as("junior account").isEqualTo(JUNIOR.name());

                    step("UI: assert", () -> {
                        assertThat(servicesPage.countServices())
                                .as("the one service was found within the Junior account")
                                .isEqualTo(1);

                        step("UI: delete service within the Junior account", () -> {
                            servicesPage
                                    .findService(simpleService.getServiceName())
                                    .delete();

                            step("UI: search a service within the Junior account", () -> {
                                calendar
                                        .routing()
                                        .toServicesListPage()
                                        .findService(simpleService.getServiceName());

                                step("UI: assert", () -> {
                                    assertThat(servicesPage.countServices())
                                            .as("deleted service was not found within the Junior account")
                                            .isEqualTo(0);
                                });
                            });
                        });
                    });
                });
            });
        });
    }

    @Test
    @Description("UI: admin worker create client ->" +
            "login to junior account and verify visibility of client ->" +
            "delete client within Junior account")
    public void testScopeAdmin_ClientVisibility() {
        step("API: create client", () -> {
            ClientCreateRequest simpleClient = generateClient(true);
            String phoneNumber = simpleClient.getPhone().replaceAll("\\D+", "");

            api.client().create(simpleClient, 201);
            ClientGetResponse client = api.client().find(phoneNumber);

            step("API: check that the client has been created", () -> {
                assertThat(client.getName()).isEqualTo(simpleClient.getName());
            });

            step("UI: search a new client by phone number", () -> {
                clientsPage = calendar.loginAs(JUNIOR)
                        .routing()
                        .toClientPage()
                        .findClient(phoneNumber);

                step("JS: assert Junior account", () -> {
                    assertThat(clientsPage.getPermissionLevel().toUpperCase())
                            .as("junior account").isEqualTo(JUNIOR.name());

                    step("UI: assert that search returns a one client within the Junior account", () -> {
                        assertThat(clientsPage.countClients())
                                .as("one client was found")
                                .isEqualTo(1);


                        step("UI: delete client within the Junior account", () -> {
                            clientsPage.deleteClient();

                            step("UI: search client within the Junior account", () -> {
                                clientsPage = calendar
                                        .routing()
                                        .toClientPage()
                                        .findClient(phoneNumber);

                                step("UI: assert", () -> {
                                    assertThat(clientsPage.countClients())
                                            .as("client not found within the Junior account").isEqualTo(0);
                                });
                            });
                        });
                    });
                });
            });
        });
    }

    @Test()
    @Description("UI: admin worker create appointment ->" +
            "login as junior worker and verify invisibility of appointment ->" +
            "login as readonly worker and verify invisibility of appointment")
    public void testScopeAdmin_AppointmentVisibility() {
        String appointmentTime = "12:30";
        String dayOfTest = getCurrentTime("yyyy-MM-dd") + "T";

        step("Preconditions: change calendar view -> create client & service " +
                "-> create appointment -> delete appointment", () -> {

            step("API: change calendar view", () -> {
                api.calendarSettingsService().update(Daily.name().toLowerCase(), "calendar_view");
                api.calendarSettingsService().update("30", "slotDuration");

                step("API: get calendar settings", () -> {
                    GeneralSettingsPojo generalSettings = api.generalSettingsService().getCalendarSettings();
                    String from = getCalendarTimeRange("From", generalSettings);
                    String to = getCalendarTimeRange("To", generalSettings);

                    step("API: delete all appointments in day of test", () -> {
                        api.appointment().deleteAll(ADMIN.getValue(), from, to);

                        step("API: create client & service", () -> {
                            ClientCreateRequest simpleClient = generateClient(true);
                            ServiceCreateRequest simpleService = generateService(true);

                            String clientID = api.client().create(simpleClient, 201);
                            int serviceID = api.service().create(simpleService, 201);

                            step("API: get Service HTML and convert to Service POJO class", () -> {
                                List<ServiceCreateRequest> serviceByID = api.service().getServiceByID(serviceID, 200);
                                List<String> serviceJsonList = api.service().convertServiceToJson(serviceByID);

                                step("API: create appointment", () -> {
                                    api.appointment().create(ADMIN.getValue(), simpleClient, clientID, serviceJsonList, dayOfTest + appointmentTime);

                                    step("API: get appointment ID", () -> {
                                        List<AppointmentGetRequest> appointments = api.appointment().getAppointmentsByDate(ADMIN.getValue(), from, to);
                                        Integer appID = appointments.stream()
                                                .filter(app -> app.getStart().contains(appointmentTime))
                                                .map(AppointmentGetRequest::getAppointmentID)
                                                .collect(Collectors.toList()).get(0);

                                        step("UI: login to Junior account", () -> {
                                            boolean isAppointmentVisible = calendar
                                                    .loginAs(JUNIOR)
                                                    .routing()
                                                    .toCalendarPage().isVisible("[data-appointment_id='" + appID + "']");

                                            step("UI: assert", () -> {
                                                assertThat(isAppointmentVisible)
                                                        .as("admin appointment does not visible within Junior account").isFalse();

                                            });
                                        });
                                        step("UI: login to READONLY account", () -> {
                                            boolean isAppointmentVisible = calendar
                                                    .loginAs(READONLY)
                                                    .isVisible("[data-appointment_id='" + appID + "']");

                                            step("UI: assert", () -> {
                                                assertThat(isAppointmentVisible)
                                                        .as("admin appointment does not visible within READONLY account").isFalse();

                                            });
                                        });
                                    });
                                });
                            });
                        });
                    });
                });
            });
        });

    }


    @Test
    @Description("UI: junior worker create group ->" +
            "login to admin account and verify visibility of group ->" +
            "delete group within Admin account")
    public void testScopeJunior_GroupVisibility() {
        step("API: create group within Junior account", () -> {
            GroupCreateRequest simpleGroup = generateGroup(true);
            int groupID = api.scopeLogin(JUNIOR).group().create(simpleGroup, 201);

            step("UI: login as Admin and get all groups", () -> {
                GroupsListPage groupPage =
                        calendar
                                .routing()
                                .toGroupsListPage();

                List<Locator> groupsList = groupPage.getGroupsList();

                step("JS: assert Admin account", () -> {
                    assertThat(groupPage.getPermissionLevel().toUpperCase())
                            .as("admin account").isEqualTo(ADMIN.name());

                    step("UI: assert that the group is visible within the Admin account", () -> {
                        List<String> listOfGroupNames = groupsList.stream().map(Locator::innerText).collect(Collectors.toList());
                        assertThat(listOfGroupNames).as("the group is visible").contains(simpleGroup.getName());

                        step("UI: delete group within the Admin account", () -> {
                            groupPage
                                    .longPressOnGroup(simpleGroup.getName())
                                    .configureGroup(GroupsListPage.ACTION.Delete);

                            step("UI: assert that group deleted within the Admin account", () -> {
                                assertThat(api.scopeLogin(ADMIN).group().getGroupsOfClient())
                                        .extracting(GroupsGetResponse::getId)
                                        .doesNotContain(groupID);
                            });
                        });
                    });
                });
            });
        });
    }

    @Test
    @Description("UI: junior worker create service ->" +
            "login to admin account and verify visibility of service ->" +
            "delete service within Admin account")
    public void testScopeJunior_ServiceVisibility() {
        step("API: create service within Junior account", () -> {
            ServiceCreateRequest simpleService = generateService(true);
            api.scopeLogin(JUNIOR).service().create(simpleService, 201);

            step("UI: login as Admin and search the service", () -> {
                ServicesListPage servicesPage =
                        calendar
                                .routing()
                                .toServicesListPage()
                                .findService(simpleService.getServiceName());

                step("JS: assert Admin account", () -> {
                    assertThat(servicesPage.getPermissionLevel().toUpperCase())
                            .as("admin account").isEqualTo(ADMIN.name());

                    step("UI: assert that the service is visible within the Admin account", () -> {
                        assertThat(servicesPage.countServices())
                                .as("one service was found")
                                .isEqualTo(1);

                        step("UI: delete service within the Admin account", () -> {
                            servicesPage
                                    .findService(simpleService.getServiceName())
                                    .delete();

                            step("UI: search a service within the Admin account", () -> {
                                calendar
                                        .routing()
                                        .toServicesListPage()
                                        .findService(simpleService.getServiceName());

                                step("UI: search for deleted service within the Admin account", () -> {
                                    assertThat(servicesPage.countServices())
                                            .as("no service found")
                                            .isEqualTo(0);
                                });
                            });
                        });
                    });
                });
            });
        });
    }

    @Test
    @Description("UI: junior worker create client ->" +
            "login to admin account and verify visibility of client ->" +
            "delete client within Admin account")
    public void testScopeJunior_ClientVisibility() {
        step("API: create client within Junior account", () -> {
            ClientCreateRequest simpleClient = generateClient(true);
            String phoneNumber = simpleClient.getPhone().replaceAll("\\D+", "");

            api.scopeLogin(JUNIOR).client().create(simpleClient, 201);
            ClientGetResponse client = api.scopeLogin(JUNIOR).client().find(phoneNumber);

            step("API: check that the client has been created within Junior account", () -> {
                assertThat(client.getName()).isEqualTo(simpleClient.getName());
            });

            step("UI: search a new client by phone number within Admin account", () -> {
                clientsPage = calendar
                        .routing()
                        .toClientPage()
                        .findClient(phoneNumber);

                step("JS: assert Admin account", () -> {
                    assertThat(clientsPage.getPermissionLevel().toUpperCase())
                            .as("admin account").isEqualTo(ADMIN.name());

                    step("UI: assert that search returns a one client within the Admin account", () -> {
                        assertThat(clientsPage.countClients())
                                .as("one client was found by phone")
                                .isEqualTo(1);

                        step("UI: delete client within the Admin account", () -> {
                            clientsPage.deleteClient();

                            step("UI: search deleted client", () -> {
                                clientsPage = calendar
                                        .routing()
                                        .toClientPage()
                                        .findClient(phoneNumber);

                                step("UI: assert that client deleted within Admin account", () -> {
                                    assertThat(clientsPage.countClients()).isEqualTo(0);
                                });
                            });
                        });
                    });
                });
            });
        });
    }

    @Test()
    @Description("UI: junior worker create appointment ->" +
            "login as readonly worker and verify invisibility of appointment ->" +
            "login as admin worker and verify invisibility of appointment")
    public void testScopeJunior_AppointmentVisibility() {
        String appointmentTime = "12:30";
        String dayOfTest = getCurrentTime("yyyy-MM-dd") + "T";

        step("Preconditions: change calendar view -> create client & service " +
                "-> create appointment -> delete appointment", () -> {

            step("API: change calendar view and cell duration", () -> {
                api.calendarSettingsService().update(Daily.name().toLowerCase(), "calendar_view");
                api.calendarSettingsService().update("30", "slotDuration");

                step("API: get calendar settings", () -> {
                    GeneralSettingsPojo generalSettings = api.generalSettingsService().getCalendarSettings();
                    String from = getCalendarTimeRange("From", generalSettings);
                    String to = getCalendarTimeRange("To", generalSettings);


                    step("API: create client & service within Junior account", () -> {
                        ClientCreateRequest simpleClient = generateClient(true);
                        ServiceCreateRequest simpleService = generateService(true);

                        String clientID = api.scopeLogin(JUNIOR).client().create(simpleClient, 201);
                        int serviceID = api.scopeLogin(JUNIOR).service().create(simpleService, 201);

                        step("API: get Service HTML and convert to Service POJO class within Junior account", () -> {
                            List<ServiceCreateRequest> serviceByID = api.scopeLogin(JUNIOR).service().getServiceByID(serviceID, 200);
                            List<String> serviceJsonList = api.scopeLogin(JUNIOR).service().convertServiceToJson(serviceByID);

                            step("API: create appointment within Junior account", () -> {
                                api.appointment()
                                        .create(JUNIOR.getValue(), simpleClient, clientID, serviceJsonList, dayOfTest + appointmentTime);

                                step("API: get appointment ID within Junior account", () -> {
                                    List<AppointmentGetRequest> appointments = api.appointment().getAppointmentsByDate(JUNIOR.getValue(), from, to);
                                    Integer appID = appointments.stream()
                                            .filter(app -> app.getStart().contains(appointmentTime))
                                            .map(AppointmentGetRequest::getAppointmentID)
                                            .collect(Collectors.toList()).get(0);

                                    step("UI: login to READONLY account", () -> {
                                        boolean isAppointmentVisible = calendar
                                                .loginAs(READONLY)
                                                .isVisible("[data-appointment_id='" + appID + "']");

                                        step("UI: assert that Junior appointment does not visible within READONLY account", () -> {
                                            assertThat(isAppointmentVisible)
                                                    .as("junior appointment not visible").isFalse();

                                        });
                                    });
                                    step("UI: login to Admin account", () -> {
                                        boolean isAppointmentVisible = calendar
                                                .loginAs(ADMIN)
                                                .routing()
                                                .toCalendarPage().isVisible("[data-appointment_id='" + appID + "']");

                                        step("UI: assert that Admin appointment does not visible within Junior account", () -> {
                                            assertThat(isAppointmentVisible)
                                                    .as("junior appointment not visible").isFalse();

                                        });
                                    });

                                });
                            });
                        });
                    });
                });
            });
        });

    }

    @Test()
    @Description("UI: admin/junior/readonly create appointments ->" +
            "switching between workers -> verify appointment visibility of each worker")
    public void testScopeSwiper_AppointmentVisibility() {
        String dayOfTest = getCurrentTime("yyyy-MM-dd");

        step("API: change calendar view", () -> {
            api.calendarSettingsService().update(Daily.name().toLowerCase(), "calendar_view");
            api.calendarSettingsService().update("30", "slotDuration");


            step("API: get calendar settings", () -> {
                GeneralSettingsPojo generalSettings = api.generalSettingsService().getCalendarSettings();
                String from = getCalendarTimeRange("From", generalSettings);
                String to = getCalendarTimeRange("To", generalSettings);


                step("API: create Admin appointment", () -> {
                    String adminAppointmentTime = "11:30";

                    step("API: delete all appointments in day of test", () -> {
                        api.appointment().deleteAll(ADMIN.getValue(), from, to);

                        step("API: create appointment", () -> {
                            api.appointment().create(ADMIN.getValue(), simpleClient, "-1", new ArrayList<>(), dayOfTest + adminAppointmentTime);

                            step("API: get appointment ID", () -> {
                                List<AppointmentGetRequest> appointmentsByDate = api.appointment().getAppointmentsByDate(ADMIN.getValue(), from, to);
                                int appID = appointmentsByDate.stream()
                                        .filter(app -> app.getStart().contains(adminAppointmentTime))
                                        .map(AppointmentGetRequest::getAppointmentID)
                                        .collect(Collectors.toList()).get(0);

                                step("API: assert created appointment", () -> {
                                    assertThat(appointmentsByDate)
                                            .as("create empty appointment at %s", adminAppointmentTime)
                                            .as("assert client ID")
                                            .as("assert appointment start")
                                            .as("assert appointment service quantity")
                                            .as("assert appointment total price").flatMap(
                                                    AppointmentGetRequest::getClientId,
                                                    AppointmentGetRequest::getStart,
                                                    app -> app.getServices().size(),
                                                    AppointmentGetRequest::getTotalPrice)

                                            .contains(-1, dayOfTest + " " + adminAppointmentTime, 0, 0);

                                    step("UI: switch worker to Admin", () -> {
                                        CalendarPage calendarPage = calendar.routing().toCalendarPage().switchWorkerId(ADMIN);


                                        step("UI: assert", () -> {
                                            assertThat(calendarPage.isVisible("[data-appointment_id='" + appID + "']"))
                                                    .as("admin appointment %s visible within Admin account", appID).isTrue();

                                        });

                                    });
                                });
                            });
                        });
                    });
                    step("API: create Junior appointment", () -> {
                        String juniorAppointmentTime = "12:30";

                        step("API: delete all appointments in day of test", () -> {
                            api.appointment().deleteAll(JUNIOR.getValue(), from, to);

                            step("API: create appointment", () -> {
                                api.appointment().create(JUNIOR.getValue(),
                                        simpleClient, "-1", new ArrayList<>(), dayOfTest + juniorAppointmentTime);

                                step("API: get appointment ID", () -> {
                                    List<AppointmentGetRequest> appointmentsByDate =
                                            api.appointment().getAppointmentsByDate(JUNIOR.getValue(), from, to);

                                    Integer appID = appointmentsByDate.stream()
                                            .filter(app -> app.getStart().contains(juniorAppointmentTime))
                                            .map(AppointmentGetRequest::getAppointmentID)
                                            .collect(Collectors.toList()).get(0);

                                    step("API: assert created appointment", () -> {
                                        assertThat(appointmentsByDate)
                                                .as("create empty appointment at %s", juniorAppointmentTime)
                                                .as("assert client ID")
                                                .as("assert appointment start")
                                                .as("assert appointment service quantity")
                                                .as("assert appointment total price").flatMap(
                                                        AppointmentGetRequest::getClientId,
                                                        AppointmentGetRequest::getStart,
                                                        app -> app.getServices().size(),
                                                        AppointmentGetRequest::getTotalPrice)

                                                .contains(-1, dayOfTest + " " + juniorAppointmentTime, 0, 0);

                                        step("UI: switch worker to JUNIOR", () -> {
                                            CalendarPage calendarPage = calendar.routing().toCalendarPage().switchWorkerId(JUNIOR);


                                            step("UI: assert", () -> {
                                                assertThat(calendarPage.isVisible("[data-appointment_id='" + appID + "']"))
                                                        .as("junior appointment visible within Junior account").isTrue();

                                            });

                                        });

                                    });
                                });
                            });
                        });
                    });
                    step("API: create READONLY appointment", () -> {
                        String readonlyAppointmentTime = "13:30";

                        step("API: delete all appointments in day of test", () -> {
                            api.appointment().deleteAll(READONLY.getValue(), from, to);

                            step("API: create appointment", () -> {
                                api.appointment().create(READONLY.getValue(),
                                        simpleClient, "-1", new ArrayList<>(), dayOfTest + readonlyAppointmentTime);

                                step("API: get appointment ID", () -> {
                                    List<AppointmentGetRequest> appointmentsByDate =
                                            api.appointment().getAppointmentsByDate(READONLY.getValue(), from, to);

                                    Integer appID = appointmentsByDate.stream()
                                            .filter(app -> app.getStart().contains(readonlyAppointmentTime))
                                            .map(AppointmentGetRequest::getAppointmentID)
                                            .collect(Collectors.toList()).get(0);

                                    step("API: assert created appointment", () -> {
                                        assertThat(appointmentsByDate)
                                                .as("create empty appointment at %s", readonlyAppointmentTime)
                                                .as("assert client ID")
                                                .as("assert appointment start")
                                                .as("assert appointment service quantity")
                                                .as("assert appointment total price").flatMap(
                                                        AppointmentGetRequest::getClientId,
                                                        AppointmentGetRequest::getStart,
                                                        app -> app.getServices().size(),
                                                        AppointmentGetRequest::getTotalPrice)

                                                .contains(-1, dayOfTest + " " + readonlyAppointmentTime, 0, 0);

                                        step("UI: switch worker to READONLY", () -> {
                                            CalendarPage calendarPage = calendar.routing().toCalendarPage().switchWorkerId(READONLY);

                                            step("UI: assert", () -> {
                                                assertThat(calendarPage.isVisible("[data-appointment_id='" + appID + "']"))
                                                        .as("readonly appointment visible within READONLY account").isTrue();

                                            });

                                        });

                                    });
                                });
                            });
                        });
                    });

                });
            });
        });
    }
}
