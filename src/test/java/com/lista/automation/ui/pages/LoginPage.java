package com.lista.automation.ui.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class LoginPage {
    Page page;
    private final String INPUT_EMAIL_LOCATOR = "//input[@type='email']";
    private final String INPUT_PASSWORD_LOCATOR = "//input[@type='password']";
    private final String BTN_MENU_LOCATOR = ".more_wrap";

    public LoginPage(Page page) {
        this.page = page;
    }

    public CalendarPage getCalendarPage() {
        String url = page.url();
        if(url.equals("https://test.atzma.im/en/login/")) {
            login();
        }
        return new CalendarPage(page);
    }

    public CalendarPage login() {
        String email = "alex.palchisky@gmail.com";
        String pass = "123456";
        page.fill(INPUT_EMAIL_LOCATOR, email);
        page.fill(INPUT_PASSWORD_LOCATOR, pass);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("login")).click();
        return new CalendarPage(page);
    }
    public MenuPage clickMenuButton() {
        page.locator(BTN_MENU_LOCATOR).click();
        return new MenuPage(page);
    }

}
