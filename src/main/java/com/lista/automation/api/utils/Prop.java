package com.lista.automation.api.utils;


import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"file:./src/test/resources/config.properties", "system:env"})
public interface Prop extends Config {

    @Key("username")
    String username();
    @Key("password")
    String password();


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
}
