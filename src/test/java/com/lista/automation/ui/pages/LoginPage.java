package com.lista.automation.ui.pages;

import com.lista.automation.api.authentication.AuthPojo;
import com.lista.automation.api.authentication.Scope;
import com.lista.automation.api.authentication.ScopeFactory;
import com.lista.automation.ui.core.utils.BasePage;
import com.lista.automation.api.Properties;
import com.lista.automation.ui.pages.calendar.CalendarPage;
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
        String email = Properties.getProp().userNameAdmin();
        String pass = Properties.getProp().userPassAdmin();
        typeIn(INPUT_EMAIL_LOCATOR, email);
        typeIn(INPUT_PASSWORD_LOCATOR, pass);
        clickBy(getByRoleWithText(AriaRole.BUTTON, "login"),0,true);
        return new CalendarPage(page);
    }

    public CalendarPage scopeLogin(Scope user) {
        AuthPojo specForUser = ScopeFactory.getSpecFor(user);
        typeIn(INPUT_EMAIL_LOCATOR, specForUser.getEmail());
        typeIn(INPUT_PASSWORD_LOCATOR, specForUser.getCurrentPassword());
        clickBy(getByRoleWithText(AriaRole.BUTTON, "login"),0,true);
        return new CalendarPage(page);
    }

    public MenuPage clickMenuButton() {
        clickBy(BTN_MENU_LOCATOR, 0, true);
        return new MenuPage(page);
    }

}
