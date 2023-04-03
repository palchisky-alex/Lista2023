package com.lista.automation.ui.pages;

import com.lista.automation.ui.pages.CalendarSettingsPage;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class SettingsAllPage {
    private Page page;
    private final String BTN_SETTINGS = "//a[@href = '/en/calendar']";
    private final String BTN_SETTINGS2 = "//a[@href = '/en/ca']";

    public SettingsAllPage(Page page) {
        this.page = page;
    }

    public CalendarSettingsPage toCalendarSettings() {
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Calendar Settings")).click();
        return new CalendarSettingsPage(page);
    }



}
