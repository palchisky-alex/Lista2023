package com.lista.automation.api.utils;


import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"file:./src/test/resources/config.properties", "system:env"})
public interface Prop extends Config {

    @Key("user.name.admin")
    String userNameAdmin();
    @Key("user.pass.admin")
    String userPassAdmin();

    @Key("user.name.junior")
    String userNameJunior();
    @Key("user.pass.junior")
    String userPassJunior();

    @Key("user.name.readonly")
    String userNameReadonly();
    @Key("user.pass.readonly")
    String userPassReadonly();

    @Key("headless.browser.mode")
    Boolean mode();
    @Key("slow.motion")
    Integer sloMotion();
    @Key("viewport.size.height")
    Integer viewportHeight();
    @Key("viewport.size.width")
    Integer viewportWidth();
    @Key("locale.ID")
    String local();
    @Key("timezone.ID")
    String timeZone();
    @Key("geolocation.latitude")
    Double latitude();
    @Key("geolocation.longitude")
    Double longitude();
    @Key("date.time.pattern")
    String dateTimePattern();

    @Key("baseURL")
    String baseURL();
    @DefaultValue("${baseURL}/en/calendar")
    String pageCalendar();
    @DefaultValue("${baseURL}/en/login")
    String pageLogin();

    @Key("path.trace")
    String tracePath();
    @Key("path.screenshot")
    String screenshotPath();
    @Key("path.video")
    String videoPath();
    @Key("path.network")
    String networkPath();
    @Key("path.cookies")
    String cookiesPath();

    @Key("path.image")
    String imagePath();
    @Key("path.image2")
    String imagePath2();

    @Key("path.schema.client")
    String schemaClient();
    @Key("path.schema.appointment.get")
    String schemaAppointmentGet();
    @Key("path.schema.appointment.delete")
    String schemaAppointmentDelete();
}
