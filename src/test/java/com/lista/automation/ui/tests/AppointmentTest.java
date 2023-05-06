package com.lista.automation.ui.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lista.automation.api.pojo.appointment.AppointmentCreateRequest;
import com.lista.automation.api.pojo.calendar_settings.SettingsPojo;
import com.lista.automation.api.pojo.client.ClientCreateRequest;
import com.lista.automation.api.pojo.service.ServiceCreateRequest;
import com.lista.automation.ui.core.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import static com.lista.automation.ui.core.utils.BasePage.*;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Appointment UI GRUD")
@Feature("Appointment")
public class AppointmentTest extends BaseTest {

    @Test
    public void testCreateAppointment() {
        String appointmentTime = "12:30";
//        step("Preconditions: get calendar settings -> delete all appointments -> create client & service", () -> {
//
//            step("API: get calendar settings", () -> {
//                SettingsPojo calendarSettings = api.settingsService.getCalendarSettings(200);
//                String dayOfTest = getCurrentTime("yyyy-MM-dd") + "T";
//                String from = dayOfTest + calendarSettings.getShowCalendarFrom();
//                String to = dayOfTest + calendarSettings.getShowCalendarTo();
//
//                step("API: delete all appointments", () -> {
//                    api.appointment.deleteAll(from, to, 200);
//
//                    step("API: create client & service", () -> {
//                        ClientCreateRequest simpleClient = generateClient(true);
//                        ServiceCreateRequest simpleService = generateService(true);
//
//                        String clientID = api.client.create(simpleClient, 201);
//                        String serviceID = api.service.create(simpleService, 201);
//                        List<ServiceCreateRequest> serviceByID = api.service.getServiceByID(serviceID, 200);
//
//                        step("UI: create appointment", () -> {
//                            calendar.routing()
//                                    .toCalendarPage()
//                                    .setAppointment(appointmentTime)
//                                    .setClient()
//                                    .searchClientAndPick(simpleClient.getName())
//                                    .chooseServiceAndPick(simpleService.getServiceName())
//                                    .next()
//                                    .save();
//
//                            step("API: get appointment", () -> {
//                                List<AppointmentCreateRequest> appointmentsByDate = api.appointment.getAppointmentsByDate(from, to, 200);
//                                LocalTime time = LocalTime.parse(appointmentTime, DateTimeFormatter.ofPattern("HH:mm"));
//                                LocalTime modifiedTime = time.plusMinutes(simpleService.getServiceDuration());
//                                String modifiedTimeString = modifiedTime.format(DateTimeFormatter.ofPattern("HH:mm"));
//
//                                step("API: assert created appointment", () -> {
//                                    assertThat(appointmentsByDate)
//                                            .as("create appointment at %s", appointmentTime).flatExtracting(
//                                                    AppointmentCreateRequest::getClientName,
//                                                    AppointmentCreateRequest::getStart,
//                                                    AppointmentCreateRequest::getPhone,
//                                                    appointment -> appointment.getServices().get(0).getServiceName(),
//                                                    appointment -> appointment.getServices().get(0).getServiceDuration())
//
//                                            .contains(simpleClient.getName(),
//                                                    appointmentsByDate.get(0).getClientName(),
//                                                    appointmentsByDate.get(0).getStart(),
//                                                    appointmentsByDate.get(0).getPhone(),
//                                                    serviceByID.get(0).getServiceName(),
//                                                    serviceByID.get(0).getServiceDuration());
//                                });
//                            });
//                        });
//                    });
//                });
//            });
//        });
    }

    @Test
    public void testDelete() throws JsonProcessingException, InterruptedException {
        String appointmentTime = "12:30";

        String today = getCurrentTime("yyyy-MM-dd'T'");
        api.appointment.getAppointmentsByDate(today + "T12:00", today + "T15:00", 200);

        ClientCreateRequest simpleClient = generateClient(true);
        ServiceCreateRequest simpleService = generateService(true);

        String clientID = api.client.create(simpleClient, 201);
        String serviceID = api.service.create(simpleService, 201);

        List<ServiceCreateRequest> serviceByID = api.service.getServiceByID(serviceID, 200);
        List<String> serviceJsonList = api.service.convertServiceToJson(serviceByID);

        api.appointment.create(simpleClient, clientID, serviceJsonList, today + appointmentTime, 201);

        SettingsPojo calendarSettings = api.settingsService.getCalendarSettings(200);

//        String dayOfTest = getCurrentTime("yyyy-MM-dd") + "T";
//        String from = dayOfTest + calendarSettings.getShowCalendarFrom();
//        String to = dayOfTest + calendarSettings.getShowCalendarTo();
//        api.appointment.deleteAll(from, to, 200);
    }
}
