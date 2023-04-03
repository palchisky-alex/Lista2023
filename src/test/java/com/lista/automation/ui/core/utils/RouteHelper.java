package com.lista.automation.ui.core.utils;

import com.lista.automation.ui.pages.ClientPage;
import com.lista.automation.ui.pages.LoginPage;
import com.lista.automation.ui.pages.MenuPage;
import com.lista.automation.ui.pages.SettingsAllPage;
import com.microsoft.playwright.Page;

public class RouteHelper {
    private final Page page;
    private SettingsAllPage settingsPage;
    private ClientPage clientPage;
    private final String MENU_LIST_LOCATOR = ".menu-list";
    private final String BTN_MENU_LOCATOR = ".more_wrap";
    private final String BTN_MENU_LOCATOR2 = "button.menu_btn";

    public RouteHelper(Page page) {
        this.page = page;
        settingsPage = new SettingsAllPage(page);
        clientPage = new ClientPage(page);
    }

    public MenuPage openMenu() {
        page.reload();
        Object d = (!page.url().contains(("https://test.atzma.im/en/login")))
                ? clickMenuButton()
                : new LoginPage(page).login().routing().clickMenuButton();
        return new MenuPage(page);
    }

    private MenuPage clickMenuButton() {
        System.out.println("URL -" + page.url());
        if(!page.url().contains("settings")) {
            page.locator(BTN_MENU_LOCATOR).click();
        }
        else page.locator(BTN_MENU_LOCATOR2).click();

        return new MenuPage(page);
    }

    public RouteHelper closeMenu() {
        page.reload();
        return this;
    }

    public SettingsAllPage toSettingsPage() throws Exception {
        openMenu().choosePage(Pages.Settings, SettingsAllPage.class);
        return settingsPage;
    }

    public ClientPage toClientPage() throws Exception {
        openMenu().choosePage(Pages.Client, ClientPage.class);
        return clientPage;
    }
}
