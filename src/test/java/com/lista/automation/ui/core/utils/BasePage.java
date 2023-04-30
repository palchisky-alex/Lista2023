package com.lista.automation.ui.core.utils;

import com.lista.automation.api.pojo.client.ClientCreateRequest;
import com.lista.automation.api.pojo.group.GroupCreateRequest;
import com.lista.automation.api.utils.DataGenerator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.LoadState;

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
        return page.getByRole(role, new Page.GetByRoleOptions().setName(Pattern.compile(htmlText, Pattern.CASE_INSENSITIVE)).setExact(true));
    }

    public Locator getByRole(AriaRole role) {
        return page.getByRole(role);
    }

    public void clickBy(String selector) {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.locator(selector).scrollIntoViewIfNeeded();
        page.waitForResponse(Response::ok, () -> {
            page.click(selector);
            System.out.println(">> api request from " + selector + " completed successfully");
        });
    }

    public void clickBy(Locator locator, int delay) {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        locator.scrollIntoViewIfNeeded();
        page.waitForResponse(Response::ok, () -> {
            locator.click(new Locator.ClickOptions().setDelay(delay));
            System.out.println(">> api request from " + locator + "" +
                    " completed successfully and delay " + delay + " sec");
        });
    }

    public void clickWithCoordinate(String selector, int x, int y) {
        page.waitForResponse(Response::ok, () -> {
            page.locator(selector).click(new Locator.ClickOptions().setPosition(x, y));
        });
    }

    public void typeIn(String selector, String text) {
        page.click(selector);
        page.locator(selector).clear();
        page.locator(selector).fill(text);
    }

    public void typeIn(Locator locator, String text) {
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
        page.waitForTimeout(msec);
    }

    public Locator getByText(String text) {
        page.getByText(text).scrollIntoViewIfNeeded();
        return page.getByText(Pattern.compile(text, Pattern.CASE_INSENSITIVE));
    }

    public Locator getByPlaceholder(String text) {
        return page.getByPlaceholder(text);
    }

    public Locator getByPlaceholder(Locator locator, String text) {
        return locator.getByPlaceholder(text);
    }

    public boolean isVisible(String selector) {
        return page.locator(selector).isVisible();
    }

    public static String getMethodName() {
        return Thread.currentThread().getStackTrace()[1].getMethodName();
    }

    public void waitForURL(String regex) {
        page.waitForURL(Pattern.compile(regex));
    }

    public int countElements(String selector) {
        return page.locator(selector).count();
    }

    public static LocalDate getCurrentTime() {
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

}
