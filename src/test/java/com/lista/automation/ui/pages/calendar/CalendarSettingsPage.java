package com.lista.automation.ui.pages.calendar;

import com.lista.automation.ui.core.utils.BasePage;
import com.lista.automation.ui.pages.settings.SettingsAllPage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.lista.automation.ui.core.utils.CalendarView;
import com.lista.automation.ui.core.utils.ViewStartOn;
import io.qameta.allure.Step;

import java.util.regex.Pattern;

import static io.qameta.allure.Allure.step;

public class CalendarSettingsPage extends BasePage {
    private Page page;
    private final String selectOptions = ".select";
    private final String header = "#header";
    private final String backToSettingsPage = "button[class*=back]";

    public CalendarSettingsPage(Page page) {
        super(page);
        this.page = page;
    }

    @Step("change Calendar View")
    public CalendarSettingsPage changeCalendarView(CalendarView view) {

        clickBy(getByText("Calendar View")
                .locator("..")
                .locator(selectOptions), 0, false);


        clickBy(getByRole(AriaRole.LISTITEM).filter(new Locator.FilterOptions()
                .setHasText(Pattern.compile(view.name(), Pattern.CASE_INSENSITIVE))), 0, false);

        return this;
    }

    @Step("change first day of week")
    public CalendarSettingsPage changeViewStartOn(ViewStartOn view) {

        clickBy(getByText("View Starts On")
                .locator("..")
                .locator(selectOptions), 0, false);


        clickBy(getByRole(AriaRole.LISTITEM).filter(new Locator.FilterOptions()
                .setHasText(Pattern.compile(view.name(), Pattern.CASE_INSENSITIVE))), 0, false);

        return this;
    }

    @Step("change cell duration")
    public CalendarSettingsPage changeEachCell(String text) {

        clickBy(getByText("Each cell in the calendar")
                .locator("..")
                .locator(selectOptions), 0, false);


        clickBy(getLocator(".select-options")
                .getByRole(AriaRole.LISTITEM)
                .filter(new Locator.FilterOptions()
                        .setHasText(Pattern.compile("^" + text + "$"))), 0, false);

        return this;
    }

    @Step("click on backToSettingsPage button")
    public SettingsAllPage backToSettingsPage() {
        clickBy(getLocator(header).locator(backToSettingsPage), 0, false);
        return new SettingsAllPage(page);
    }
}
