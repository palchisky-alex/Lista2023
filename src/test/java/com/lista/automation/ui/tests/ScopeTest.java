package com.lista.automation.ui.tests;

import com.lista.automation.api.TestListener;
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
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Listeners;
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

@Epic("Scope UI")
@Feature("Scope shifting")
@Listeners(TestListener.class)
public class ScopeTest extends BaseTest {

    @Test
    @Description("UI: admin worker create group ->" +
            "login to junior account and verify visibility of group ->" +
            "delete group within Junior account")
    public void testScopeAdmin_GroupVisibility() throws Exception {

        GroupCreateRequest simpleGroup = generateGroup(true);
        int groupID = api.group().create(simpleGroup, 201);


        GroupsListPage groupPage =
                calendar.loginAs(JUNIOR)
                        .routing()
                        .toGroupsListPage();


        assertThat(groupPage.getPermissionLevel().toUpperCase())
                .as("junior account").isEqualTo(JUNIOR.name());


        assertThat(groupPage.getLocator("[group-id='" + groupID + "']").isVisible())
                .as("group exists within the Junior account")
                .isTrue();


        groupPage
                .longPressOnGroup(simpleGroup.getName())
                .configureGroup(GroupsListPage.ACTION.Delete);


        assertThat(!groupPage.getLocator("[group-id='" + groupID + "']").isVisible())
                .as("deleted group does not exists within the Junior account")
                .isTrue();

    }

    @Test
    @Description("UI: admin worker create service ->" +
            "login to junior account and verify visibility of service ->" +
            "delete service within Junior account")
    public void testScopeAdmin_ServiceVisibility() throws Exception {

        ServiceCreateRequest simpleService = generateService(true);
        api.service().create(simpleService, 201);


        ServicesListPage servicesPage =
                calendar.loginAs(JUNIOR)
                        .routing()
                        .toServicesListPage()
                        .findService(simpleService.getServiceName());


        assertThat(servicesPage.getPermissionLevel().toUpperCase())
                .as("junior account").isEqualTo(JUNIOR.name());


        assertThat(servicesPage.countServices())
                .as("the one service was found within the Junior account")
                .isEqualTo(1);


        servicesPage
                .findService(simpleService.getServiceName())
                .delete();


        calendar
                .routing()
                .toServicesListPage()
                .findService(simpleService.getServiceName());


        assertThat(servicesPage.countServices())
                .as("deleted service was not found within the Junior account")
                .isEqualTo(0);

    }

    @Test
    @Description("UI: admin worker create client ->" +
            "login to junior account and verify visibility of client ->" +
            "delete client within Junior account")
    public void testScopeAdmin_ClientVisibility() throws Exception {

        ClientCreateRequest simpleClient = generateClient(true);
        String phoneNumber = simpleClient.getPhone().replaceAll("\\D+", "");

        api.client().create(simpleClient, 201);
        ClientGetResponse client = api.client().find(phoneNumber);


        assertThat(client.getName()).isEqualTo(simpleClient.getName());


        clientsPage = calendar.loginAs(JUNIOR)
                .routing()
                .toClientPage()
                .findClient(phoneNumber);


        assertThat(clientsPage.getPermissionLevel().toUpperCase())
                .as("junior account").isEqualTo(JUNIOR.name());


        assertThat(clientsPage.countClients())
                .as("one client was found")
                .isEqualTo(1);


        clientsPage.deleteClient();


        clientsPage = calendar
                .routing()
                .toClientPage()
                .findClient(phoneNumber);


        assertThat(clientsPage.countClients())
                .as("client not found within the Junior account").isEqualTo(0);

    }

    @Test()
    @Description("UI: admin worker create appointment ->" +
            "login as junior worker and verify invisibility of appointment ->" +
            "login as readonly worker and verify invisibility of appointment")
    public void testScopeAdmin_AppointmentVisibility() throws Exception {
        String appointmentTime = "12:30";
        String dayOfTest = getCurrentTime("yyyy-MM-dd") + "T";


        api.calendarSettingsService().update(Daily.name().toLowerCase(), "calendar_view");
        api.calendarSettingsService().update("30", "slotDuration");


        GeneralSettingsPojo generalSettings = api.generalSettingsService().getCalendarSettings();
        String from = getCalendarTimeRange("From", generalSettings);
        String to = getCalendarTimeRange("To", generalSettings);


        api.appointment().deleteAll(ADMIN.getValue(), from, to);


        ClientCreateRequest simpleClient = generateClient(true);
        ServiceCreateRequest simpleService = generateService(true);

        String clientID = api.client().create(simpleClient, 201);
        int serviceID = api.service().create(simpleService, 201);


        List<ServiceCreateRequest> serviceByID = api.service().getServiceByID(serviceID, 200);
        List<String> serviceJsonList = api.service().convertServiceToJson(serviceByID);


        api.appointment().create(ADMIN.getValue(), simpleClient, clientID, serviceJsonList, dayOfTest + appointmentTime);


        List<AppointmentGetRequest> appointments = api.appointment().getAppointmentsByDate(ADMIN.getValue(), from, to);
        Integer appID = appointments.stream()
                .filter(app -> app.getStart().contains(appointmentTime))
                .map(AppointmentGetRequest::getAppointmentID)
                .collect(Collectors.toList()).get(0);


        boolean isAppointmentVisible = calendar
                .loginAs(JUNIOR)
                .routing()
                .toCalendarPage().isVisible("[data-appointment_id='" + appID + "']");


        assertThat(isAppointmentVisible)
                .as("admin appointment does not visible within Junior account").isFalse();


        boolean isAppointmentVisible2 = calendar
                .loginAs(READONLY)
                .isVisible("[data-appointment_id='" + appID + "']");


        assertThat(isAppointmentVisible2)
                .as("admin appointment does not visible within READONLY account").isFalse();

    }


    @Test
    @Description("UI: junior worker create group ->" +
            "login to admin account and verify visibility of group ->" +
            "delete group within Admin account")
    public void testScopeJunior_GroupVisibility() throws Exception {

        GroupCreateRequest simpleGroup = generateGroup(true);
        int groupID = api.scopeLogin(JUNIOR).group().create(simpleGroup, 201);


        GroupsListPage groupPage =
                calendar
                        .routing()
                        .toGroupsListPage();

        List<Locator> groupsList = groupPage.getGroupsList();


        assertThat(groupPage.getPermissionLevel().toUpperCase())
                .as("admin account").isEqualTo(ADMIN.name());


        List<String> listOfGroupNames = groupsList.stream().map(Locator::innerText).collect(Collectors.toList());
        assertThat(listOfGroupNames).as("the group is visible").contains(simpleGroup.getName());


        groupPage
                .longPressOnGroup(simpleGroup.getName())
                .configureGroup(GroupsListPage.ACTION.Delete);


        assertThat(api.scopeLogin(ADMIN).group().getGroupsOfClient())
                .extracting(GroupsGetResponse::getId)
                .doesNotContain(groupID);

    }

    @Test
    @Description("UI: junior worker create service ->" +
            "login to admin account and verify visibility of service ->" +
            "delete service within Admin account")
    public void testScopeJunior_ServiceVisibility() throws Exception {

        ServiceCreateRequest simpleService = generateService(true);
        api.scopeLogin(JUNIOR).service().create(simpleService, 201);


        ServicesListPage servicesPage =
                calendar
                        .routing()
                        .toServicesListPage()
                        .findService(simpleService.getServiceName());


        assertThat(servicesPage.getPermissionLevel().toUpperCase())
                .as("admin account").isEqualTo(ADMIN.name());


        assertThat(servicesPage.countServices())
                .as("one service was found")
                .isEqualTo(1);


        servicesPage
                .findService(simpleService.getServiceName())
                .delete();


        calendar
                .routing()
                .toServicesListPage()
                .findService(simpleService.getServiceName());


        assertThat(servicesPage.countServices())
                .as("no service found")
                .isEqualTo(0);

    }

    @Test
    @Description("UI: junior worker create client ->" +
            "login to admin account and verify visibility of client ->" +
            "delete client within Admin account")
    public void testScopeJunior_ClientVisibility() throws Exception {

        ClientCreateRequest simpleClient = generateClient(true);
        String phoneNumber = simpleClient.getPhone().replaceAll("\\D+", "");

        api.scopeLogin(JUNIOR).client().create(simpleClient, 201);
        ClientGetResponse client = api.scopeLogin(JUNIOR).client().find(phoneNumber);


        assertThat(client.getName()).isEqualTo(simpleClient.getName());


        clientsPage = calendar
                .routing()
                .toClientPage()
                .findClient(phoneNumber);


        assertThat(clientsPage.getPermissionLevel().toUpperCase())
                .as("admin account").isEqualTo(ADMIN.name());


        assertThat(clientsPage.countClients())
                .as("one client was found by phone")
                .isEqualTo(1);


        clientsPage.deleteClient();


        clientsPage = calendar
                .routing()
                .toClientPage()
                .findClient(phoneNumber);


        assertThat(clientsPage.countClients()).isEqualTo(0);

    }

    @Test()
    @Description("UI: junior worker create appointment ->" +
            "login as readonly worker and verify invisibility of appointment ->" +
            "login as admin worker and verify invisibility of appointment")
    public void testScopeJunior_AppointmentVisibility() throws Exception {
        String appointmentTime = "12:30";
        String dayOfTest = getCurrentTime("yyyy-MM-dd") + "T";


        api.calendarSettingsService().update(Daily.name().toLowerCase(), "calendar_view");
        api.calendarSettingsService().update("30", "slotDuration");


        GeneralSettingsPojo generalSettings = api.generalSettingsService().getCalendarSettings();
        String from = getCalendarTimeRange("From", generalSettings);
        String to = getCalendarTimeRange("To", generalSettings);


        ClientCreateRequest simpleClient = generateClient(true);
        ServiceCreateRequest simpleService = generateService(true);

        String clientID = api.scopeLogin(JUNIOR).client().create(simpleClient, 201);
        int serviceID = api.scopeLogin(JUNIOR).service().create(simpleService, 201);


        List<ServiceCreateRequest> serviceByID = api.scopeLogin(JUNIOR).service().getServiceByID(serviceID, 200);
        List<String> serviceJsonList = api.scopeLogin(JUNIOR).service().convertServiceToJson(serviceByID);


        api.appointment()
                .create(JUNIOR.getValue(), simpleClient, clientID, serviceJsonList, dayOfTest + appointmentTime);


        List<AppointmentGetRequest> appointments = api.appointment().getAppointmentsByDate(JUNIOR.getValue(), from, to);
        Integer appID = appointments.stream()
                .filter(app -> app.getStart().contains(appointmentTime))
                .map(AppointmentGetRequest::getAppointmentID)
                .collect(Collectors.toList()).get(0);


        boolean isAppointmentVisible = calendar
                .loginAs(READONLY)
                .isVisible("[data-appointment_id='" + appID + "']");


        assertThat(isAppointmentVisible)
                .as("junior appointment not visible").isFalse();


        boolean isAppointmentVisible2 = calendar
                .loginAs(ADMIN)
                .routing()
                .toCalendarPage().isVisible("[data-appointment_id='" + appID + "']");


        assertThat(isAppointmentVisible2)
                .as("junior appointment not visible").isFalse();


    }

    @Test()
    @Description("UI: admin/junior/readonly create appointments ->" +
            "switching between workers -> verify appointment visibility of each worker")
    public void testScopeSwiper_AppointmentVisibility() throws Exception {
        String dayOfTest = getCurrentTime("yyyy-MM-dd");


        api.calendarSettingsService().update(Daily.name().toLowerCase(), "calendar_view");
        api.calendarSettingsService().update("30", "slotDuration");


        GeneralSettingsPojo generalSettings = api.generalSettingsService().getCalendarSettings();
        String from = getCalendarTimeRange("From", generalSettings);
        String to = getCalendarTimeRange("To", generalSettings);


        String adminAppointmentTime = "11:30";


        api.appointment().deleteAll(ADMIN.getValue(), from, to);


        api.appointment().create(ADMIN.getValue(), simpleClient, "-1", new ArrayList<>(), dayOfTest + adminAppointmentTime);


        List<AppointmentGetRequest> appointmentsByDate = api.appointment().getAppointmentsByDate(ADMIN.getValue(), from, to);
        int appID = appointmentsByDate.stream()
                .filter(app -> app.getStart().contains(adminAppointmentTime))
                .map(AppointmentGetRequest::getAppointmentID)
                .collect(Collectors.toList()).get(0);


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


        CalendarPage calendarPage = calendar.routing().toCalendarPage().switchWorkerId(ADMIN);


        assertThat(calendarPage.isVisible("[data-appointment_id='" + appID + "']"))
                .as("admin appointment %s visible within Admin account", appID).isTrue();


        String juniorAppointmentTime = "12:30";


        api.appointment().deleteAll(JUNIOR.getValue(), from, to);


        api.appointment().create(JUNIOR.getValue(),
                simpleClient, "-1", new ArrayList<>(), dayOfTest + juniorAppointmentTime);


        appointmentsByDate =
                api.appointment().getAppointmentsByDate(JUNIOR.getValue(), from, to);

        appID = appointmentsByDate.stream()
                .filter(app -> app.getStart().contains(juniorAppointmentTime))
                .map(AppointmentGetRequest::getAppointmentID)
                .collect(Collectors.toList()).get(0);


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


        calendarPage = calendar.routing().toCalendarPage().switchWorkerId(JUNIOR);


        assertThat(calendarPage.isVisible("[data-appointment_id='" + appID + "']"))
                .as("junior appointment visible within Junior account").isTrue();


        String readonlyAppointmentTime = "13:30";


        api.appointment().deleteAll(READONLY.getValue(), from, to);


        api.appointment().create(READONLY.getValue(),
                simpleClient, "-1", new ArrayList<>(), dayOfTest + readonlyAppointmentTime);


        appointmentsByDate =
                api.appointment().getAppointmentsByDate(READONLY.getValue(), from, to);

        appID = appointmentsByDate.stream()
                .filter(app -> app.getStart().contains(readonlyAppointmentTime))
                .map(AppointmentGetRequest::getAppointmentID)
                .collect(Collectors.toList()).get(0);


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


        calendarPage = calendar.routing().toCalendarPage().switchWorkerId(READONLY);


        assertThat(calendarPage.isVisible("[data-appointment_id='" + appID + "']"))
                .as("readonly appointment visible within READONLY account").isTrue();


    }
}
