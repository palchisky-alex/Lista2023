package com.lista.automation.ui.core.utils;

import com.lista.automation.api.pojo.appointment.AppointmentCreateRequest;
import com.lista.automation.api.pojo.service.ServiceCreateRequest;
import com.lista.automation.api.pojo.client.ClientCreateRequest;
import com.lista.automation.api.pojo.group.GroupCreateRequest;
import com.lista.automation.api.utils.DataGenerator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.jetbrains.annotations.Contract;

import java.io.ByteArrayInputStream;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

public class BasePage {
    private Page page;
    public static ClientCreateRequest simpleClient = null;
    public static GroupCreateRequest simpleGroup = null;
    public static ServiceCreateRequest simpleService = null;
    public static AppointmentCreateRequest simpleAppointment = null;

    public BasePage(Page page) {
        this.page = page;
    }

    public int getRandomInt(int from, int to) {
        return ThreadLocalRandom.current()
                .nextInt(from, to);
    }

    public Locator getByRoleWithText(AriaRole role, String htmlText) {
        return page.getByRole(role, new Page.GetByRoleOptions()
                .setName(Pattern.compile(htmlText, Pattern.CASE_INSENSITIVE)).setExact(true));
    }

    public Locator getByRole(AriaRole role) {
        return page.getByRole(role);
    }

    public void clickBy(String selector,int delay, boolean request) {
        attachAllureLog("click on element and wait 30000ms for response OK", selector,"");
        System.out.println(">> click on " + selector + " and wait for response OK <<");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.locator(selector).scrollIntoViewIfNeeded();

        if(request) {
            page.waitForResponse(Response::ok, () -> {
                page.locator(selector).click(new Locator.ClickOptions().setDelay(delay));
            });
        }
        else {
            page.locator(selector).click(new Locator.ClickOptions().setDelay(delay));
        }
    }

    public void clickBy(Locator locator, int delay, boolean request) {
        attachAllureLog("click on element and wait 30000ms for response OK",locator,"");
        System.out.println(">> click on " + locator + " and wait for response OK <<");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        locator.scrollIntoViewIfNeeded();

        if(request) {
            page.waitForResponse(Response::ok, () -> {
                locator.click(new Locator.ClickOptions().setDelay(delay));
            });
        }
        else {
            locator.click(new Locator.ClickOptions().setDelay(delay));
        }
    }

    public void dblClickBy(Locator locator, boolean request) {
        System.out.println(">> click on " + locator + " and wait for response OK <<");
        attachAllureLog("double click on element and wait 30000ms for response OK", locator,"");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        locator.scrollIntoViewIfNeeded();

        if(request) {
            page.waitForResponse(Response::ok, locator::dblclick);
        }
        else {
            locator.dblclick();
        }
    }

    public void clickWithCoordinate(String selector, int x, int y) {
            page.locator(selector).click(new Locator.ClickOptions().setPosition(x, y));
    }

    public void typeIn(String selector, String text) {
        page.click(selector);
        page.locator(selector).clear();
        page.locator(selector).fill(text);
    }

    public void typeIn(Locator locator, String text) {
        locator.click();
        locator.clear();
        locator.fill(text);
    }

    public void waitForLoadState() {
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    public Locator getLocator(String path) {
        return page.locator(path);
    }

    public String getInnerTextBy(String path) {
        return page.locator(path).innerText().toLowerCase().trim();
    }

    public void waitForTimeout(int msec) {
        Allure.addAttachment("wait msec", String.valueOf(msec));
        page.waitForTimeout(msec);
    }

    public Locator hasText(String selector, String text) {
       return getLocator(selector)
                .filter(new Locator.FilterOptions().setHasText(Pattern.compile(text)));
    }

    public Locator getByText(String text) {
        Locator locator = page.getByText(Pattern.compile(".*"+text+".*", Pattern.CASE_INSENSITIVE));
        locator.scrollIntoViewIfNeeded();
        return locator;
    }
    public Locator getByExactText(String text) {
        Locator locator = page.getByText(text, new Page.GetByTextOptions().setExact(true));
        locator.scrollIntoViewIfNeeded();
        return locator;
    }

    public Locator getByPlaceholder(String text) {
        return page.getByPlaceholder(text);
    }

    public boolean isVisible(String selector) {
        return page.locator(selector).isVisible();
    }
    public boolean isVisible(Locator locator) {
        return locator.isVisible();
    }

    public static String getMethodNaming() {
        return Thread.currentThread().getStackTrace()[1].getMethodName();
    }

    public void waitForURL(String regex) {
        Allure.addAttachment("wait for URL", regex);
        page.waitForURL(Pattern.compile(regex));
    }

    public int countElements(String selector) {
        Allure.addAttachment("count element", selector);
        return page.locator(selector).count();
    }

    @Contract(" -> new")
    public static String getCurrentTime(String pattern) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime roundedTime;

        if (pattern.equals("yyyy-MM-dd'T'HH:mm:ss")) {
            roundedTime = now.truncatedTo(ChronoUnit.MINUTES);
        } else if (pattern.equals("yyyy-MM-dd'T'HH:00:00")) {
            roundedTime = now.truncatedTo(ChronoUnit.HOURS);
        } else if (pattern.equals("yyyy-MM-dd")) {
            roundedTime = now.truncatedTo(ChronoUnit.DAYS);
        }
        else {
            return "no time";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return roundedTime.format(formatter);
    }

    @Step("generate data for client")
    public static ClientCreateRequest generateClient(boolean recreate) {
        if (simpleClient == null || recreate) {
            simpleClient = DataGenerator.getSimpleData(ClientCreateRequest.class);
        }
        return simpleClient;
    }

    @Step("generate data for group")
    public static GroupCreateRequest generateGroup(boolean recreate) {
        if (simpleGroup == null || recreate) {
            simpleGroup = DataGenerator.getSimpleData(GroupCreateRequest.class);
        }
        return simpleGroup;
    }
    @Step("generate data for service")
    public static ServiceCreateRequest generateService(boolean recreate) {
        if (simpleService == null || recreate) {
            simpleService = DataGenerator.getSimpleData(ServiceCreateRequest.class);
        }
        return simpleService;
    }

    private void attachAllureLog(String description, Locator locator, String text) {
        Allure.addAttachment(description, "locator/text: " + locator.toString()+"|"+text);

        byte[] screenshot = locator.screenshot(new Locator.ScreenshotOptions()
                .setPath(Paths.get(Properties.getProp().screenshotPath()+"screenshot.png")));
        Allure.addAttachment("element screenshot", new ByteArrayInputStream(screenshot));
    }
    private void attachAllureLog(String description, String selector, String text) {
        Allure.addAttachment(description, "selector/text: " + selector+"|"+text);

        byte[] screenshot = page.locator(selector).screenshot(new Locator.ScreenshotOptions()
                .setPath(Paths.get(Properties.getProp().screenshotPath()+"screenshot.png")));
        Allure.addAttachment("element screenshot", new ByteArrayInputStream(screenshot));
    }

}
