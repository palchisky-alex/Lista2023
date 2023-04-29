package com.lista.automation.ui.pages;

import com.lista.automation.ui.core.utils.BasePage;
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
        getByRoleWithText(AriaRole.LINK, "Calendar Settings").click();
        return new CalendarSettingsPage(page);
    }



}
