package com.lista.automation.ui.core;

import com.lista.automation.api.Properties;
import com.lista.automation.ui.pages.LoginPage;
import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import org.testng.ITestResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

public class PlaywrightFactory {
    private final String videoPath = Properties.getProp().videoPath();
    private final String networkPath = Properties.getProp().networkPath();
    private final String screenPath = Properties.getProp().screenshotPath();
    private final String tracePath = Properties.getProp().tracePath();
    private final double randomChar = Math.random();
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
        tlPlaywright.set(Playwright.create());

        tlBrowser.set(getTlPlaywright().chromium().launch(new BrowserType.LaunchOptions()
                .setSlowMo(Properties.getProp().sloMotion())
                .setHeadless(Properties.getProp().mode())));

        tlContext.set(getTlBrowser().newContext(new Browser.NewContextOptions()
                        .setStorageStatePath(Paths.get(Properties.getProp().cookiesPath()))
                .setLocale(Properties.getProp().local())
                .setRecordHarPath(Paths.get(networkPath + randomChar + ".har"))
                .setRecordVideoDir(Paths.get(videoPath))
                .setViewportSize(Properties.getProp().viewportWidth(), Properties.getProp().viewportHeight())
                .setTimezoneId(Properties.getProp().timeZone())
                .setGeolocation(Properties.getProp().latitude(), Properties.getProp().longitude())
                .setPermissions(Collections.singletonList("notifications"))));

        getTlContext().tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));


        tlPage.set(getTlContext().newPage());
//        getTlPage().onRequest(request -> System.out.println(">> " + request.method() + " " + request.url()));
//        getTlPage().onResponse(response -> System.out.println("<<" + response.status() + " " + response.url()));
        getTlPage().navigate(Properties.getProp().pageCalendar());
        if(!getTlPage().url().contains("calendar")) {
            recordCookies();
        }

        return getTlPage();
    }

    public void stop(Method testInfo, ITestResult result) throws IOException {

        String formattedDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(Properties.getProp().dateTimePattern()));
        String videoName = getTlPage().video().path().getFileName().toString();

        String logName = String.format("%s_%s", formattedDateTime, testInfo.getName());
        String screenPathStr = screenPath + logName + ".zip";
        String tracePathStr = tracePath + logName + ".zip";

        Path screenFilePath = getPath(screenPathStr);
        Path zipFilePath = getPath(tracePathStr);
        Path networkFilePath = getPath(networkPath + randomChar + ".har");
        Path videoFilePath = getPath(videoPath + videoName);

        getTlContext().tracing().stop(new Tracing.StopOptions()
                .setPath(zipFilePath));

        byte[] screenshot = getTlPage().screenshot(new Page.ScreenshotOptions()
                .setPath(screenFilePath).setFullPage(true));

        getTlContext().close();
        getTlBrowser().close();

        byte[] videoContents = Files.readAllBytes(videoFilePath);
        byte[] zipContents = Files.readAllBytes(zipFilePath);
        byte[] networkContents = Files.readAllBytes(networkFilePath);

        if (result.getStatus() == 2) {
            Allure.addAttachment("SCREENSHOT_" + logName,
                    new ByteArrayInputStream(screenshot));

            Allure.addAttachment("TRACE_" + logName,
                    new ByteArrayInputStream(zipContents));

            Allure.addAttachment("NETWORK_" + logName,
                    new ByteArrayInputStream(networkContents));

            Allure.addAttachment("VIDEO_" + logName,
                    new ByteArrayInputStream(videoContents));
        }
    }

    private Path getPath(String customPath) {
        return Paths.get(customPath);
    }

    private void recordCookies() {
        new LoginPage(getTlPage()).login();
        getTlContext().storageState(new BrowserContext.StorageStateOptions().setPath(Paths.get(Properties.getProp().cookiesPath())));
    }

}
