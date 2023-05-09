package com.lista.automation.ui.tests;

import com.lista.automation.api.pojo.appointment.AppointmentGetRequest;
import com.lista.automation.api.pojo.client.ClientCreateRequest;
import com.lista.automation.api.pojo.general_settings.GeneralSettingsPojo;
import com.lista.automation.api.pojo.service.ServiceCreateRequest;
import com.lista.automation.ui.core.BaseTest;
import com.lista.automation.ui.core.utils.CalendarView;
import com.lista.automation.ui.core.utils.ViewStartOn;
import com.lista.automation.ui.pages.appointment.AppointmentPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static com.lista.automation.ui.core.utils.BasePage.*;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Appointment UI GRUD")
@Feature("Appointment")
public class AppointmentTest extends BaseTest {

    @Test
    @Description("Create appointment in UI")
    public void testCreateAppointment() {
        String appointmentTime = "12:00";
        step("Preconditions: change calendar view -> get calendar settings ->" +
                " delete all appointments -> create client & service", () -> {

            step("UI: Change calendar view", () -> {
                calendar.routing()
                        .toSettingsPage()
                        .toCalendarSettings()
                        .changeCalendarView(CalendarView.Daily)
                        .changeEachCell("5")
                        .backToSettingsPage();

                step("API: get calendar settings", () -> {
                    GeneralSettingsPojo generalSettings = api.generalSettingsService().getCalendarSettings(200);
                    String dayOfTest = getCurrentTime("yyyy-MM-dd");
                    String from = dayOfTest + "T" + generalSettings.getShowCalendarFrom();
                    String to = dayOfTest + "T" + generalSettings.getShowCalendarTo();

                    step("API: delete all appointments", () -> {
                        api.appointment().deleteAll(from, to, 200);

                        step("API: create client & service", () -> {
                            ClientCreateRequest simpleClient = generateClient(true);
                            ServiceCreateRequest simpleService = generateService(true);

                            String clientID = api.client().create(simpleClient, 201);
                            String serviceID = api.service().create(simpleService, 201);
                            List<ServiceCreateRequest> serviceByID = api.service().getServiceByID(serviceID, 200);

                            step("UI: create appointment", () -> {
                                calendar.routing()
                                        .toCalendarPage()
                                        .setAppointment(appointmentTime)
                                        .setClient()
                                        .searchClientAndPick(simpleClient.getName())
                                        .chooseServiceAndPick(simpleService.getServiceName())
                                        .next()
                                        .save();

                                step("API: get appointment", () -> {
                                    List<AppointmentGetRequest> appointmentsByDate = api.appointment().getAppointmentsByDate(from, to, 200);
                                    LocalTime time = LocalTime.parse(appointmentTime, DateTimeFormatter.ofPattern("HH:mm"));
                                    LocalTime modifiedTime = time.plusMinutes(simpleService.getServiceDuration());
                                    String appointmentEND = modifiedTime.format(DateTimeFormatter.ofPattern("HH:mm"));

                                    step("API: assert created appointment", () -> {
                                        assertThat(appointmentsByDate)
                                                .as("create appointment at %s", appointmentTime)
                                                .as("verify appointment: " +
                                                        "client name, start time, end time," +
                                                        " service name, service duration, total price")
                                                .flatMap(
                                                        AppointmentGetRequest::getClientName,
                                                        AppointmentGetRequest::getStart,
                                                        AppointmentGetRequest::getEnd,
                                                        app -> app.getServices().get(0).getServiceName(),
                                                        app -> app.getServices().get(0).getDuration(),
                                                        app -> app.getServices().get(0).getPrice())

                                                .contains(
                                                        simpleClient.getName(),
                                                        dayOfTest + " " + appointmentTime,
                                                        dayOfTest + " " + appointmentEND,
                                                        "service_" + simpleService.getServiceName(),
                                                        simpleService.getServiceDuration(),
                                                        simpleService.getPrice());

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
    @Description("Create empty appointment in UI")
    public void testCreateEmptyAppointment() {
        String appointmentTime = "12:30";
        step("Preconditions: change calendar view -> get calendar settings ->" +
                " delete all appointments -> create client & service", () -> {

            step("UI: Change calendar view", () -> {
                calendar.routing()
                        .toSettingsPage()
                        .toCalendarSettings()
                        .changeCalendarView(CalendarView.Daily)
                        .changeEachCell("30")
                        .backToSettingsPage();

                step("API: get calendar settings", () -> {
                    GeneralSettingsPojo generalSettings = api.generalSettingsService().getCalendarSettings(200);
                    String dayOfTest = getCurrentTime("yyyy-MM-dd");
                    String from = dayOfTest + "T" + generalSettings.getShowCalendarFrom();
                    String to = dayOfTest + "T" + generalSettings.getShowCalendarTo();

                    step("API: delete all appointments", () -> {
                        api.appointment().deleteAll(from, to, 200);

                        step("UI: create appointment", () -> {
                            calendar.routing()
                                    .toCalendarPage()
                                    .setAppointment(appointmentTime)
                                    .setClient()
                                    .skip()
                                    .next()
                                    .save();

                            step("API: get appointment", () -> {
                                List<AppointmentGetRequest> appointmentsByDate = api.appointment().getAppointmentsByDate(from, to, 200);

                                step("API: assert created appointment", () -> {
                                    assertThat(appointmentsByDate)
                                            .as("create empty appointment at %s", appointmentTime).flatMap(
                                                    AppointmentGetRequest::getClientId,
                                                    AppointmentGetRequest::getStart,
                                                    app -> app.getServices().size(),
                                                    AppointmentGetRequest::getTotalPrice)

                                            .contains(-1, dayOfTest + " " + appointmentTime, 0, 0);

                                });
                            });
                        });
                    });
                });
            });
        });

    }

    @Test
    @Description("Add new client during appointment creation in UI")
    public void testAddClientDuringAppointmentCreation() {
        String appointmentTime = "12:30";
        step("Preconditions: change calendar view -> get calendar settings ->" +
                " delete all appointments -> create client & service", () -> {

            step("UI: Change calendar view", () -> {
                calendar.routing()
                        .toSettingsPage()
                        .toCalendarSettings()
                        .changeCalendarView(CalendarView.Daily)
                        .changeEachCell("30")
                        .backToSettingsPage();

                step("API: get calendar settings", () -> {
                    GeneralSettingsPojo generalSettings = api.generalSettingsService().getCalendarSettings(200);
                    String dayOfTest = getCurrentTime("yyyy-MM-dd");
                    String from = dayOfTest + "T" + generalSettings.getShowCalendarFrom();
                    String to = dayOfTest + "T" + generalSettings.getShowCalendarTo();

                    step("API: delete all appointments", () -> {
                        api.appointment().deleteAll(from, to, 200);

                        step("API: generate client data", () -> {
                            ClientCreateRequest simpleClient = generateClient(true);

                            step("UI: create appointment", () -> {
                                calendar.routing()
                                        .toCalendarPage()
                                        .setAppointment(appointmentTime)
                                        .setClient()
                                        .searchClientAndCreate(simpleClient)
                                        .addAndContinue()
                                        .next()
                                        .save();

                                step("API: get appointment", () -> {
                                    List<AppointmentGetRequest> appointmentsByDate = api.appointment().getAppointmentsByDate(from, to, 200);

                                    step("API: assert created appointment", () -> {
                                        assertThat(appointmentsByDate)
                                                .as("create appointment at %s", appointmentTime).flatMap(
                                                        AppointmentGetRequest::getClientName,
                                                        AppointmentGetRequest::getStart,
                                                        app -> app.getServices().size(),
                                                        AppointmentGetRequest::getTotalPrice)

                                                .contains(simpleClient.getName(), dayOfTest + " " + appointmentTime, 0, 0);
                                    });
                                });
                            });
                        });
                    });
                });
            });
        });
    }

    @Test(dataProvider = "calendar_view")
    @Description("Change calendar view and delete appointment in UI")
    public void testDeleteAppointment(CalendarView views, ViewStartOn viewStartOn, String cellDuration) {

        String appointmentTime = "12:30";
        step("Preconditions: change calendar view -> create client & service " +
                "-> create appointment -> delete appointment", () -> {

            step("UI: change calendar week first day and cell duration", () -> {
                calendar.routing()
                        .toSettingsPage()
                        .toCalendarSettings()
                        .changeCalendarView(views)

                        .changeViewStartOn(viewStartOn)
                        .changeEachCell(cellDuration)
                        .backToSettingsPage();

                step("API: get calendar settings", () -> {
                    GeneralSettingsPojo generalSettings = api.generalSettingsService().getCalendarSettings(200);
                    String dayOfTest = getCurrentTime("yyyy-MM-dd") + "T";
                    String from = dayOfTest + generalSettings.getShowCalendarFrom();
                    String to = dayOfTest + generalSettings.getShowCalendarTo();

                    step("API: delete all appointments in day of test", () -> {
                        api.appointment().deleteAll(from, to, 200);

                        step("API: create client & service", () -> {
                            ClientCreateRequest simpleClient = generateClient(true);
                            ServiceCreateRequest simpleService = generateService(true);

                            String clientID = api.client().create(simpleClient, 201);
                            String serviceID = api.service().create(simpleService, 201);

                            step("API: get Service HTML and convert to Service POJO class", () -> {
                                List<ServiceCreateRequest> serviceByID = api.service().getServiceByID(serviceID, 200);
                                List<String> serviceJsonList = api.service().convertServiceToJson(serviceByID);

                                step("API: create appointment", () -> {
                                    api.appointment().create(simpleClient, clientID, serviceJsonList, dayOfTest + appointmentTime, 201);

                                    step("API: get appointment ID", () -> {
                                        List<AppointmentGetRequest> appointments = api.appointment().getAppointmentsByDate(from, to, 200);
                                        Integer appID = appointments.stream()
                                                .filter(app -> app.getStart().contains(appointmentTime))
                                                .map(AppointmentGetRequest::getAppointmentID)
                                                .collect(Collectors.toList()).get(0);

                                        step("UI: delete appointment", () -> {
                                            calendar.routing()
                                                    .toCalendarPage()
                                                    .enterTheAppointmentSlot(appID, views)
                                                    .configureAppointment(AppointmentPage.ACTION.DELETE);

                                            step("API: get appointment from day of test and verify deletion", () -> {
                                                List<Integer> appointmentIDs = api.appointment().getAppointmentsByDate(from, to, 200).stream()
                                                        .map(AppointmentGetRequest::getAppointmentID)
                                                        .collect(Collectors.toList());

                                                assertThat(appointmentIDs)
                                                        .as("List of appointments does not contains ID %s", appID)
                                                        .as("Calendar view: %s", views)
                                                        .as("Calendar start day: %s", viewStartOn)
                                                        .as("Calendar cell duration: %s", cellDuration)
                                                        .as("Appointment at %s o'clock deleted", appointmentTime)
                                                        .doesNotContain(appID);
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
    @Description("Change appointment time by drag and drop in UI")
    public void testDragAndDropAppointment() {
        String appointmentTime = "12:00";
        String appointmentTime2 = "16:00";

        step("Preconditions: change calendar view -> get calendar settings ->" +
                "delete all appointments -> create client & service", () -> {

            step("UI: Change calendar view", () -> {
                calendar.routing()
                        .toSettingsPage()
                        .toCalendarSettings()
                        .changeCalendarView(CalendarView.Daily)
                        .changeEachCell("60")
                        .backToSettingsPage();

                step("API: get calendar settings", () -> {
                    GeneralSettingsPojo generalSettings = api.generalSettingsService().getCalendarSettings(200);
                    String dayOfTest = getCurrentTime("yyyy-MM-dd");
                    String from = dayOfTest + "T" + generalSettings.getShowCalendarFrom();
                    String to = dayOfTest + "T" + generalSettings.getShowCalendarTo();

                    step("API: delete all appointments", () -> {
                        api.appointment().deleteAll(from, to, 200);

                        step("API: create empty appointment", () -> {
                            api.appointment().create(simpleClient, "-1", new ArrayList<>(), dayOfTest + appointmentTime, 201);

                            step("API: get appointment IDs before dragging", () -> {
                                List<AppointmentGetRequest> appointmentsByDate = api.appointment().getAppointmentsByDate(from, to, 200);

                                Integer appID = appointmentsByDate.stream()
                                        .filter(app -> app.getStart().contains(appointmentTime))
                                        .map(AppointmentGetRequest::getAppointmentID)
                                        .collect(Collectors.toList()).get(0);

                                step("API: assert created appointment", () -> {
                                    assertThat(appointmentsByDate)
                                            .as("create appointment at %s", appointmentTime)
                                            .flatMap(AppointmentGetRequest::getStart)
                                            .contains(dayOfTest + " " + appointmentTime);


                                    step("UI: drag and drop the appointment", () -> {
                                        calendar.routing().toCalendarPage().dragAndDropSlot(appID, appointmentTime2);

                                        step("API: get appointment after dragging", () -> {
                                            List<AppointmentGetRequest> appointmentsByDateAfterDragging = api.appointment().getAppointmentsByDate(from, to, 200);

                                            step("API: assert dragged appointment", () -> {
                                                assertThat(appointmentsByDateAfterDragging)
                                                        .as("appointment moved from %s to %s", appointmentTime, appointmentTime2)
                                                        .as("Calendar view: %s", CalendarView.Daily)
                                                        .as("Calendar cell duration: 60 min")
                                                        .flatMap(AppointmentGetRequest::getStart)
                                                        .contains(dayOfTest + " " + appointmentTime2);
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


    @DataProvider(name = "calendar_view")
    public Object[][] testDataViewDay() {
        return new Object[][]{
                {CalendarView.Weekly, ViewStartOn.Sunday, "5"},
                {CalendarView.Daily, ViewStartOn.Monday, "10"},
                {CalendarView.Monthly, ViewStartOn.Tuesday, "15"},
                {CalendarView.Days, ViewStartOn.Wednesday, "20"},
                {CalendarView.Daily, ViewStartOn.Thursday, "30"},
                {CalendarView.Weekly, ViewStartOn.Friday, "60"},
                {CalendarView.Agenda, ViewStartOn.Sunday, "30"}

        };
    }
}
