package com.lista.automation.ui.core;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.TestInfo;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;

public class PlaywrightFactory {

    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;

    private static ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();
    private static ThreadLocal<Browser> tlBrowser = new ThreadLocal<>();
    private static ThreadLocal<BrowserContext> tlContext = new ThreadLocal<>();
    private static ThreadLocal<Page> tlPage = new ThreadLocal<>();

    public static Playwright getTlPlaywright() {
        return tlPlaywright.get();
    }

    public static Browser getTlBrowser() {
        return tlBrowser.get();
    }

    public static BrowserContext getTlContext() {
        return tlContext.get();
    }

    public static Page getTlPage() {
        return tlPage.get();
    }

    public Page initBrowser() {
//        playwright = Playwright.create();
        tlPlaywright.set(Playwright.create());

//        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
//                .setHeadless(false));
        tlBrowser.set(getTlPlaywright().chromium().launch(new BrowserType.LaunchOptions()
                .setSlowMo(0)
                .setHeadless(false)));

//        context = browser.newContext(new Browser.NewContextOptions()
//                .setViewportSize(2560, 1440)
//                .setDeviceScaleFactor(2)
//                .setLocale("de-DE")
//                .setTimezoneId("Europe/Berlin")
//                .setGeolocation(41.890221, 12.492348)
//                .setPermissions(Collections.singletonList("notifications")));
        tlContext.set(getTlBrowser().newContext(new Browser.NewContextOptions()
//                .setViewportSize(2560, 1440)
//                .setDeviceScaleFactor(2)
                .setLocale("de-DE")
                .setTimezoneId("Europe/Berlin")
                .setGeolocation(41.890221, 12.492348)
                .setPermissions(Collections.singletonList("notifications"))));

//        context.tracing().start(new Tracing.StartOptions()
//                .setScreenshots(true)
//                .setSnapshots(true)
//                .setSources(true));

        getTlContext().tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));

//        page = context.newPage();

        tlPage.set(getTlContext().newPage());
        getTlPage().onRequest(request -> System.out.println(">> " + request.method() + " " + request.url()));
        getTlPage().onResponse(response -> System.out.println("<<" + response.status() + " " + response.url()));
        getTlPage().navigate("https://test.atzma.im/en/calendar/");
        return getTlPage();
    }

    public void stop(TestInfo testInfo) {
        String formattedDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM"));

        String s = String.format("%s_%s", formattedDateTime, testInfo.getDisplayName());
        getTlContext().tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("src/test/resources/" + s + ".zip")));
        getTlContext().close();
        getTlBrowser().close();
    }
}
