package com.lista.automation.ui.pages;

import com.lista.automation.ui.core.utils.BasePage;
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
        step("open CalendarView slot", () -> {
            getByText("Calendar View")
                    .locator("..")
                    .locator(selectOptions).click();
        });
        step("select view", () -> {
            getByRole(AriaRole.LISTITEM).filter(new Locator.FilterOptions()
                    .setHasText(Pattern.compile(view.name(), Pattern.CASE_INSENSITIVE))).click();
        });
        return this;
    }

    @Step("change first day of week")
    public CalendarSettingsPage changeViewStartOn(ViewStartOn view) {
        step("open ViewStartsOn slot", () -> {
            getByText("View Starts On")
                    .locator("..")
                    .locator(selectOptions).click();
        });
        step("select day", () -> {
            getByRole(AriaRole.LISTITEM).filter(new Locator.FilterOptions()
                    .setHasText(Pattern.compile(view.name(), Pattern.CASE_INSENSITIVE))).click();
        });
        return this;
    }

    @Step("change cell duration")
    public CalendarSettingsPage changeEachCell(String text) {
        step("open cell duration slot", () -> {
            getByText("Each cell in the calendar")
                    .locator("..")
                    .locator(selectOptions).click();
        });
        step("select duration", () -> {
            getLocator(".select-options")
                    .getByRole(AriaRole.LISTITEM)
                    .filter(new Locator.FilterOptions()
                            .setHasText(Pattern.compile("^"+text+"$"))).click();
        });
        return this;
    }

    @Step("click on backToSettingsPage button")
    public SettingsAllPage backToSettingsPage() {
        getLocator(header).locator(backToSettingsPage).click();
        return new SettingsAllPage(page);
    }
}
