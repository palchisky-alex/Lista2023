package com.lista.automation.ui.pages.calendar;

import com.lista.automation.api.authentication.Scope;
import com.lista.automation.ui.core.utils.BasePage;
import com.lista.automation.ui.core.utils.CalendarView;
import com.lista.automation.ui.core.utils.RouteHelper;
import com.lista.automation.ui.pages.LoginPage;
import com.lista.automation.ui.pages.MenuPage;
import com.lista.automation.ui.pages.appointment.AppointmentCreation;
import com.lista.automation.ui.pages.appointment.AppointmentPage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

import java.time.Duration;
import java.time.LocalTime;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
        clickBy(BTN_MENU_LOCATOR, 0, true);
        return new MenuPage(page);
    }

    @Step("verify CalendarView")
    public boolean verifyCalendarView(String selector) {
        waitForLoadState();
        if (isVisible("#calendar [class*='" + selector + "']")) {
            return true;
        }
        return false;
    }

    @Step("go to current date")
    public void goToCurrentDate() {
        String btnToday = "button[class*='today_wrap']:not([disabled])";
        if (isVisible(btnToday)) {
            System.out.println("button TODAY is visible");
            clickBy(btnToday, 0, false);
        }
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

    @Step("enter the empty slot")
    public AppointmentCreation setAppointment(String time) {
        removeWeekendMarker();
        String fullPrefix = time + ":00";
        clickBy("tr[data-time= '" + fullPrefix + "']", 0, false);

        waitForURL("creating-appointment/choosing-client");
        return new AppointmentCreation(page);
    }

    @Step("enter the appointment slot")
    public AppointmentPage enterTheAppointmentSlot(int appointmentID, CalendarView views) {
        clickBy("[data-appointment_id='" + appointmentID + "']", 0, true);

        step("if calendar view is Monthly, need click twice", () -> {
            if (views.name().equals("Monthly")) {
                clickBy("[data-appointment_id='" + appointmentID + "']", 0, true);
            }
        });
        waitForURL("appointments");
        return new AppointmentPage(page);
    }

    @Step("enter the appointment slot")
    public AppointmentPage dragAndDropSlot(int appointmentID, String time) {
        removeWeekendMarker();
        String fullPrefix = time + ":00";
        getLocator("[data-appointment_id='" + appointmentID + "']")
                .dragTo(getLocator("tr[data-time= '" + fullPrefix + "']"));

        clickBy(getLocator("#modal-background .yes-btn"), 0, true);
        return new AppointmentPage(page);
    }

    public CalendarPage loginAs(Scope user) {
        clearCookies();
        return new LoginPage(page).scopeLogin(user);
    }

    public void removeWeekendMarker() {
        step("remove element form DOM for weekend", () -> {
            Locator locator = page.locator(".fc-nonbusiness.fc-bgevent");
            if (locator.isVisible()) {
                locator.evaluate("element => element.style.display = 'none'");
            }
        });
    }

    public CalendarPage switchWorkerId(Scope scope) {
        String activeSwiper = ".swiper-cont  [class~=active]";
        step("change worker", () -> {
            String currentWorkerID = getAttribute(activeSwiper, "data-index");
            String scopeID = String.valueOf(scope.getValue());

            if (!currentWorkerID.equals(scopeID)) {
                clickBy(".swiper-cont  [data-index='" + scope.getValue() + "']", 0, false);
            }
        });
        waitForTimeout(2000);
        step("change worker", () -> {
            String currentWorkerID = getAttribute(activeSwiper, "data-index");
            String scopeID = String.valueOf(scope.getValue());

            assertThat(currentWorkerID).as("worker %s is active", scope.name()).isEqualTo(scopeID);
        });

        return this;
    }


    public RouteHelper routing() {
        return new RouteHelper(page);
    }


}
