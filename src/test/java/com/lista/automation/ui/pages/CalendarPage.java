package com.lista.automation.ui.pages;

import com.microsoft.playwright.Page;
import com.lista.automation.ui.core.utils.RouteHelper;

public class CalendarPage {
    private Page page;
    private RouteHelper route;
    private final String BTN_MENU_LOCATOR = ".more_wrap";
    private final String INPUT_PASSWORD_LOCATOR = "//input[@type='password']";

    public CalendarPage(Page page) {
        this.page = page;
        route = new RouteHelper(page);
    }

    public String getCalendarPageURL() {
        return page.url();
    }

    public <T> T getPosition(Class<T> clazz) {
        if (page.locator(BTN_MENU_LOCATOR).isVisible()) {
            return clazz.cast(new MenuPage(page));
        } else if (page.locator(INPUT_PASSWORD_LOCATOR).isVisible()) {

            return clazz.cast(new LoginPage(page));
        } else {
            throw new IllegalArgumentException("Cannot determine page type");
        }
    }

    public MenuPage clickMenuButton() {
        page.locator(BTN_MENU_LOCATOR).click();
        return new MenuPage(page);
    }

    public RouteHelper routing() {
        return new RouteHelper(page);
    }


}
