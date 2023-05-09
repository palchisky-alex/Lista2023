package com.lista.automation.api.utils;

import com.lista.automation.api.authentication.AuthAPI;
import com.lista.automation.api.services.*;


public class RestWrapper {
    private String cookie;
    private ClientService client;
    private GroupsService group;
    private ServService service;
    private AppointmentService appointment;
    private GeneralSettingsService generalSettingsService;
    private CalendarSettingsService calendarSettingsService;

    public RestWrapper(String cookie) {
        this.cookie = cookie;
    }

    public static RestWrapper loginAs() {
        String key = "5531a58348162222";

        String myCookie = AuthAPI.login().extract().cookie(key);
        if (myCookie.isEmpty()) {
            throw new RuntimeException("Cookies were not received");
        }

        System.out.println("<< end of method: " + Thread.currentThread().getStackTrace()[1].getMethodName().toUpperCase());
        return new RestWrapper(key + "=" + myCookie);
    }
    public CalendarSettingsService calendarSettingsService() {
        if(calendarSettingsService == null) {
            calendarSettingsService = new CalendarSettingsService(cookie);
        }
        return calendarSettingsService;
    }
    public ClientService client() {
        if(client == null) {
            client = new ClientService(cookie);
        }
        return client;
    }
    public GeneralSettingsService generalSettingsService() {
        if(generalSettingsService == null) {
            generalSettingsService = new GeneralSettingsService(cookie);
        }
        return generalSettingsService;
    }
    public AppointmentService appointment() {
        if(appointment == null) {
            appointment = new AppointmentService(cookie);
        }
        return appointment;
    }
    public ServService service() {
        if(service == null) {
            service = new ServService(cookie);
        }
        return service;
    }
    public GroupsService group() {
        if(group == null) {
            group = new GroupsService(cookie);
        }
        return group;
    }
}
