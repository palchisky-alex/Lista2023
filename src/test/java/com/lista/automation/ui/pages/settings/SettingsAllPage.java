package com.lista.automation.ui.pages.settings;

import com.lista.automation.ui.core.utils.BasePage;
import com.lista.automation.ui.pages.calendar.CalendarSettingsPage;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;

public class SettingsAllPage extends BasePage {
    private Page page;
    private final String BTN_SETTINGS = "//a[@href = '/en/calendar']";
    private final String BTN_SETTINGS2 = "//a[@href = '/en/ca']";

    public SettingsAllPage(Page page) {
        super(page);
        this.page = page;
    }

    @Step("go to Calendar Settings")
    public CalendarSettingsPage toCalendarSettings() {
        clickBy(getByRoleWithText(AriaRole.LINK, "Calendar Settings"),0,true);
        return new CalendarSettingsPage(page);
    }



}
