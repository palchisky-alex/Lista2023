package com.lista.automation.ui.core.utils;

import com.lista.automation.api.pojo.client.ClientCreateRequest;
import com.lista.automation.api.pojo.group.GroupCreateRequest;
import com.lista.automation.api.utils.DataGenerator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Allure;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import java.io.ByteArrayInputStream;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

public class BasePage {
    private Page page;
    public static ClientCreateRequest simpleClient = null;
    public static GroupCreateRequest simpleGroup = null;

    public BasePage(Page page) {
        this.page = page;
    }

    public int getRandomInt(int from, int to) {
        return ThreadLocalRandom.current()
                .nextInt(from, to);
    }

    public Locator getByRoleWithText(AriaRole role, String htmlText) {
        Locator locator = page.getByRole(role, new Page.GetByRoleOptions()
                .setName(Pattern.compile(htmlText, Pattern.CASE_INSENSITIVE)).setExact(true));
        attachAllureLog("get ByRole with text", locator, htmlText);
        return locator;
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
        page.waitForResponse(Response::ok, () -> {
            page.locator(selector).click(new Locator.ClickOptions().setPosition(x, y));
        });
    }

    public void typeIn(String selector, String text) {
        attachAllureLog("type some text",selector,text);
        page.click(selector);
        page.locator(selector).clear();
        page.locator(selector).fill(text);
    }

    public void typeIn(Locator locator, String text) {
        attachAllureLog("type some text",locator,text);
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
        attachAllureLog("get inner text",path,"");
        return page.locator(path).innerText().toLowerCase().trim();
    }

    public void waitForTimeout(int msec) {
        Allure.addAttachment("wait msec", String.valueOf(msec));
        page.waitForTimeout(msec);
    }

    public Locator getByText(String text) {
        Locator locator = page.getByText(Pattern.compile(text, Pattern.CASE_INSENSITIVE));
        locator.scrollIntoViewIfNeeded();
        return locator;
    }
    public Locator getByExactText(String text) {
        Locator locator = page.getByText(text, new Page.GetByTextOptions().setExact(true));
        locator.scrollIntoViewIfNeeded();
        return locator;
    }

    public Locator getByPlaceholder(String text) {
        Locator locator = page.getByPlaceholder(text);
        attachAllureLog("get element by placeholder", locator, text);
        return locator;
    }

    public boolean isVisible(String selector) {
        attachAllureLog("is element visible", selector,"");
        return page.locator(selector).isVisible();
    }

    public static String getMethodName() {
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
    public static @NotNull LocalDate getCurrentTime() {
        return LocalDate.now();
    }

    public static ClientCreateRequest generateClient(boolean recreate) {
        if (simpleClient == null || recreate) {
            simpleClient = DataGenerator.getSimpleData(ClientCreateRequest.class);
        }
        return simpleClient;
    }

    public static GroupCreateRequest generateGroup(boolean recreate) {
        if (simpleGroup == null || recreate) {
            simpleGroup = DataGenerator.getSimpleData(GroupCreateRequest.class);
        }
        return simpleGroup;
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
