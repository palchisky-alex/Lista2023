package com.lista.automation.ui.pages;

import com.lista.automation.ui.core.utils.BasePage;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class LoginPage extends BasePage {
    Page page;
    private final String INPUT_EMAIL_LOCATOR = "//input[@type='email']";
    private final String INPUT_PASSWORD_LOCATOR = "//input[@type='password']";
    private final String BTN_MENU_LOCATOR = ".more_wrap";

    public LoginPage(Page page) {
        super(page);
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
        typeIn(INPUT_EMAIL_LOCATOR, email);
        typeIn(INPUT_PASSWORD_LOCATOR, pass);
        getByRoleWithText(AriaRole.BUTTON, "login").click();
        return new CalendarPage(page);
    }
    public MenuPage clickMenuButton() {
        clickBy(BTN_MENU_LOCATOR);
        return new MenuPage(page);
    }

}
