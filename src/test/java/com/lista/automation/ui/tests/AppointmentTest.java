package com.lista.automation.ui.tests;

import com.lista.automation.api.TestListener;
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
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.lista.automation.api.authentication.Scope.ADMIN;
import static com.lista.automation.ui.core.utils.BasePage.*;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Appointment UI GRUD")
@Feature("Appointment")
@Listeners(TestListener.class)
public class AppointmentTest extends BaseTest {

    @Test
    @Description("Create appointment in UI")
    public void testCreateAppointment() throws Exception {
        String appointmentTime = "12:00";
        String dayOfTest = getCurrentTime("yyyy-MM-dd");


        calendar.routing()
                .toSettingsPage()
                .toCalendarSettings()
                .changeCalendarView(CalendarView.Daily)
                .changeEachCell("5")
                .backToSettingsPage();


        GeneralSettingsPojo generalSettings = api.generalSettingsService().getCalendarSettings();
        String from = getCalendarTimeRange("From", generalSettings);
        String to = getCalendarTimeRange("To", generalSettings);


        api.appointment().deleteAll(ADMIN.getValue(), from, to);


        ClientCreateRequest simpleClient = generateClient(true);
        ServiceCreateRequest simpleService = generateService(true);

        api.client().create(simpleClient, 201);
        api.service().create(simpleService, 201);


        calendar.routing()
                .toCalendarPage()
                .setAppointment(appointmentTime)
                .setClient()
                .searchClientAndPick(simpleClient.getName())
                .chooseServiceAndPick(simpleService.getServiceName())
                .next()
                .save();


        List<AppointmentGetRequest> appointmentsByDate = api.appointment().getAppointmentsByDate(ADMIN.getValue(), from, to);
        LocalTime time = LocalTime.parse(appointmentTime, DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime modifiedTime = time.plusMinutes(simpleService.getServiceDuration());
        String appointmentEND = modifiedTime.format(DateTimeFormatter.ofPattern("HH:mm"));


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


    }

    //TODO change UI settings to API

    @Test
    @Description("Create empty appointment in UI")
    public void testCreateEmptyAppointment() throws Exception {
        String appointmentTime = "12:30";
        String dayOfTest = getCurrentTime("yyyy-MM-dd");


        calendar.routing()
                .toSettingsPage()
                .toCalendarSettings()
                .changeCalendarView(CalendarView.Daily)
                .changeEachCell("30")
                .backToSettingsPage();


        GeneralSettingsPojo generalSettings = api.generalSettingsService().getCalendarSettings();
        String from = getCalendarTimeRange("From", generalSettings);
        String to = getCalendarTimeRange("To", generalSettings);


        api.appointment().deleteAll(ADMIN.getValue(), from, to);


        calendar.routing()
                .toCalendarPage()
                .setAppointment(appointmentTime)
                .setClient()
                .skip()
                .next()
                .save();


        List<AppointmentGetRequest> appointmentsByDate = api.appointment().getAppointmentsByDate(ADMIN.getValue(), from, to);


        assertThat(appointmentsByDate)
                .as("create empty appointment at %s", appointmentTime).flatMap(
                        AppointmentGetRequest::getClientId,
                        AppointmentGetRequest::getStart,
                        app -> app.getServices().size(),
                        AppointmentGetRequest::getTotalPrice)

                .contains(-1, dayOfTest + " " + appointmentTime, 0, 0);


    }

    @Test
    @Description("Add new client during appointment creation in UI")
    public void testAddClientDuringAppointmentCreation() throws Exception {
        String appointmentTime = "12:30";
        String dayOfTest = getCurrentTime("yyyy-MM-dd");


        calendar.routing()
                .toSettingsPage()
                .toCalendarSettings()
                .changeCalendarView(CalendarView.Daily)
                .changeEachCell("30")
                .backToSettingsPage();


        GeneralSettingsPojo generalSettings = api.generalSettingsService().getCalendarSettings();
        String from = getCalendarTimeRange("From", generalSettings);
        String to = getCalendarTimeRange("To", generalSettings);


        api.appointment().deleteAll(ADMIN.getValue(), from, to);


        ClientCreateRequest simpleClient = generateClient(true);


        calendar.routing()
                .toCalendarPage()
                .setAppointment(appointmentTime)
                .setClient()
                .searchClientAndCreate(simpleClient)
                .addAndContinue()
                .next()
                .save();


        List<AppointmentGetRequest> appointmentsByDate = api.appointment().getAppointmentsByDate(ADMIN.getValue(), from, to);


        assertThat(appointmentsByDate)
                .as("create appointment at %s", appointmentTime).flatMap(
                        AppointmentGetRequest::getClientName,
                        AppointmentGetRequest::getStart,
                        app -> app.getServices().size(),
                        AppointmentGetRequest::getTotalPrice)

                .contains(simpleClient.getName(), dayOfTest + " " + appointmentTime, 0, 0);

    }

    @Test(dataProvider = "calendar_view")
    @Description("Change calendar view and delete appointment in UI")
    public void testDeleteAppointment(CalendarView views, ViewStartOn viewStartOn, String cellDuration) throws Exception {
        String appointmentTime = "12:30";
        String dayOfTest = getCurrentTime("yyyy-MM-dd") + "T";


        calendar.routing()
                .toSettingsPage()
                .toCalendarSettings()
                .changeCalendarView(views)

                .changeViewStartOn(viewStartOn)
                .changeEachCell(cellDuration)
                .backToSettingsPage();


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


        calendar.routing()
                .toCalendarPage()
                .enterTheAppointmentSlot(appID, views)
                .configureAppointment(AppointmentPage.ACTION.DELETE);


        List<Integer> appointmentIDs = api.appointment().getAppointmentsByDate(ADMIN.getValue(), from, to).stream()
                .map(AppointmentGetRequest::getAppointmentID)
                .collect(Collectors.toList());

        assertThat(appointmentIDs)
                .as("List of appointments does not contains ID %s", appID)
                .as("Calendar view: %s", views)
                .as("Calendar start day: %s", viewStartOn)
                .as("Calendar cell duration: %s", cellDuration)
                .as("Appointment at %s o'clock deleted", appointmentTime)
                .doesNotContain(appID);

    }

    @Test
    @Description("Change appointment time by drag and drop in UI")
    public void testDragAndDropAppointment() throws Exception {
        String appointmentTime = "12:00";
        String appointmentTime2 = "16:00";
        String dayOfTest = getCurrentTime("yyyy-MM-dd");


        calendar.routing()
                .toSettingsPage()
                .toCalendarSettings()
                .changeCalendarView(CalendarView.Daily)
                .changeEachCell("60")
                .backToSettingsPage();


        GeneralSettingsPojo generalSettings = api.generalSettingsService().getCalendarSettings();
        String from = getCalendarTimeRange("From", generalSettings);
        String to = getCalendarTimeRange("To", generalSettings);


        api.appointment().deleteAll(ADMIN.getValue(), from, to);


        api.appointment().create(ADMIN.getValue(), simpleClient, "-1", new ArrayList<>(), dayOfTest + appointmentTime);


        List<AppointmentGetRequest> appointmentsByDate = api.appointment().getAppointmentsByDate(ADMIN.getValue(), from, to);

        Integer appID = appointmentsByDate.stream()
                .filter(app -> app.getStart().contains(appointmentTime))
                .map(AppointmentGetRequest::getAppointmentID)
                .collect(Collectors.toList()).get(0);


        assertThat(appointmentsByDate)
                .as("create appointment at %s", appointmentTime)
                .flatMap(AppointmentGetRequest::getStart)
                .contains(dayOfTest + " " + appointmentTime);


        calendar.routing().toCalendarPage().dragAndDropSlot(appID, appointmentTime2);


        List<AppointmentGetRequest> appointmentsByDateAfterDragging = api.appointment().getAppointmentsByDate(ADMIN.getValue(), from, to);


        assertThat(appointmentsByDateAfterDragging)
                .as("appointment moved from %s to %s", appointmentTime, appointmentTime2)
                .as("Calendar view: %s", CalendarView.Daily)
                .as("Calendar cell duration: 60 min")
                .flatMap(AppointmentGetRequest::getStart)
                .contains(dayOfTest + " " + appointmentTime2);

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
