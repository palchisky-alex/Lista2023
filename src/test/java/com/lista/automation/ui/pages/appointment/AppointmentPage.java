package com.lista.automation.ui.pages.appointment;

import com.lista.automation.api.pojo.general_settings.GeneralSettingsPojo;
import com.lista.automation.ui.core.utils.BasePage;
import com.lista.automation.ui.pages.calendar.CalendarPage;
import com.lista.automation.ui.pages.group.GroupsListPage;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static io.qameta.allure.Allure.step;

/**
 * Created by Palchitsky Alex
 */
public class AppointmentPage extends BasePage {

    private Page page;
    private String btnDeleteAppointment = "button[class*='delete']";

    public AppointmentPage(Page page) {
        super(page);
        this.page = page;
    }

    public CalendarPage configureAppointment(ACTION action) {
        if (action.name().equals("DELETE")) {
            clickBy(btnDeleteAppointment, 0, true);

            step("confirm deletion", () -> {
                clickBy(getLocator(".confirm-block button")
                        .getByText(action.name()), 0, true);
                page.reload();
            });
        }
        waitForTimeout(3000);
        return new CalendarPage(page);
    }

    public enum ACTION {
        EDIT,
        COPY,
        DELETE

    }
}
