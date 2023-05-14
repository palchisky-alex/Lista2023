package com.lista.automation.api.utils;

import com.lista.automation.api.authentication.AuthAPI;
import com.lista.automation.api.authentication.AuthPojo;
import com.lista.automation.api.authentication.Scope;
import com.lista.automation.api.authentication.ScopeFactory;
import com.lista.automation.api.services.*;
import lombok.Getter;

import static com.lista.automation.api.authentication.Scope.ADMIN;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


public class RestWrapper {
    private static final String key = "5531a58348162222";
    private String permissionLevel;
    @Getter
    private String cookie;

    @Getter
    private AuthPojo authPojo;
    private ClientService client;
    private GroupsService group;
    private ServService service;
    private AppointmentService appointment;
    private GeneralSettingsService generalSettingsService;
    private CalendarSettingsService calendarSettingsService;

    public RestWrapper(String cookie) {
        this.cookie = cookie;
    }

    public static RestWrapper loginAs(String permissionLevel) {
        String myCookie = AuthAPI.getToken(ScopeFactory.getSpecFor(ADMIN)).extract().cookie(key);
        if (myCookie.isEmpty()) {
            throw new RuntimeException("Cookies were not received");
        }
        assertThat(permissionLevel).describedAs("first login always as Admin").isEqualTo("admin");
        System.out.println("<< end of method: " + Thread.currentThread().getStackTrace()[1].getMethodName().toUpperCase());
        return new RestWrapper(key + "=" + myCookie);
    }

    public RestWrapper scopeLogin(Scope user) {
        String myCookie = AuthAPI.getToken(ScopeFactory.getSpecFor(user)).extract().cookie(key);
        if (myCookie.isEmpty()) {
            throw new RuntimeException("Cookies were not received");
        }
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
