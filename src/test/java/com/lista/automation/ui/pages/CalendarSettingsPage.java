package com.lista.automation.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.lista.automation.ui.core.utils.CalendarView;
import com.lista.automation.ui.core.utils.ViewStartOn;

import java.util.regex.Pattern;

public class CalendarSettingsPage {
    private Page page;
    private final String generalSettingsArea = ".general-settings";
    private final String selectOptions = ".select";
    private final String header = "#header";
    private final String backToSettingsPage = "button[class*=back]";

    public CalendarSettingsPage(Page page) {
        this.page = page;
    }

    public CalendarSettingsPage changeCalendarView(CalendarView view) {
        page.getByText(Pattern.compile("Calendar View"))
                .locator("..")
                .locator(selectOptions).click();

        page.getByRole(AriaRole.LISTITEM).filter(new Locator.FilterOptions()
                .setHasText(Pattern.compile(view.name(), Pattern.CASE_INSENSITIVE))).click();
        return this;
    }

    public CalendarSettingsPage changeViewStartOn(ViewStartOn view) {
        page.getByText(Pattern.compile("View Starts On"))
                .locator("..")
                .locator(selectOptions).click();

        page.getByRole(AriaRole.LISTITEM).filter(new Locator.FilterOptions()
                .setHasText(Pattern.compile(view.name(), Pattern.CASE_INSENSITIVE))).click();
        return this;
    }

    public CalendarSettingsPage changeEachCell(String text) {
        page.getByText(Pattern.compile("Each cell in the calendar"))
                .locator("..")
                .locator(selectOptions).click();

        page.getByRole(AriaRole.LISTITEM).getByText(text).click();
        return this;
    }

    public SettingsAllPage backToSettingsPage() {
        page.locator(header).locator(backToSettingsPage).click();
        return new SettingsAllPage(page);
    }
}
