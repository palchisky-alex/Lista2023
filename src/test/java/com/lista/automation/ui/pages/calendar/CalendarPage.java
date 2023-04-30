package com.lista.automation.ui.pages.calendar;

import com.lista.automation.ui.core.utils.BasePage;
import com.lista.automation.ui.core.utils.RouteHelper;
import com.lista.automation.ui.pages.LoginPage;
import com.lista.automation.ui.pages.MenuPage;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

import java.time.Duration;
import java.time.LocalTime;

public class CalendarPage extends BasePage {
    private Page page;
    private final String BTN_MENU_LOCATOR = ".more_wrap";
    private final String INPUT_PASSWORD_LOCATOR = "//input[@type='password']";

    public CalendarPage(Page page) {
        super(page);
        this.page = page;

    }

    public String getCalendarPageURL() {
        return page.url();
    }

    public <T> T getPosition(Class<T> clazz) {
        if (isVisible(BTN_MENU_LOCATOR)) {
            return clazz.cast(new MenuPage(page));
        } else if (isVisible(INPUT_PASSWORD_LOCATOR)) {
            return clazz.cast(new LoginPage(page));
        } else {
            throw new IllegalArgumentException("Cannot determine page type");
        }
    }

    public MenuPage clickMenuButton() {
        clickBy(BTN_MENU_LOCATOR);
        return new MenuPage(page);
    }
    @Step("verify CalendarView")
    public boolean verifyCalendarView(String selector) {
        waitForLoadState();
        if(isVisible("#calendar [class*='"+selector+"']")) {
            return true;
        }
        return false;
    }
    @Step("fetch calendar first day name")
    public String getCalendarFirstDay() {
        return getInnerTextBy("(//*[contains(@class, 'day-header')])[1]").replaceAll("[\\d\\s]+", "");
    }
    @Step("fetch calendar cell duration")
    public String getCellDuration() {
        LocalTime time1 = LocalTime.parse(getInnerTextBy("tr[data-time]:nth-of-type(1)"));
        LocalTime time2 = LocalTime.parse(getInnerTextBy("tr[data-time]:nth-of-type(2)"));
        Duration duration = Duration.between(time1, time2).abs();
        return String.valueOf(duration.toMinutes());
    }


    public RouteHelper routing() {
        return new RouteHelper(page);
    }



}
