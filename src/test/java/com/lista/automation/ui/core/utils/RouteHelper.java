package com.lista.automation.ui.core.utils;

import com.lista.automation.ui.pages.*;
import com.lista.automation.ui.pages.calendar.CalendarPage;
import com.lista.automation.ui.pages.client.ClientsPage;
import com.lista.automation.ui.pages.group.GroupsListPage;
import com.lista.automation.ui.pages.service.ServicesListPage;
import com.lista.automation.ui.pages.settings.SettingsAllPage;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class RouteHelper extends BasePage {
    private final Page page;
    private final String MENU_LIST_LOCATOR = ".menu-list";
    private final String BTN_MENU_LOCATOR = ".more_wrap";
    private final String BTN_MENU_LOCATOR2 = "button.menu_btn";

    public RouteHelper(Page page) {
        super(page);
        this.page = page;
    }

    @Step("open menu")
    public MenuPage openMenu() {
        page.reload();
        Object d = (!page.url().contains(Properties.getProp().pageLogin()))
                ? clickMenuButton()
                : new LoginPage(page).login().routing().clickMenuButton();
        return new MenuPage(page);
    }

    @Step("click on menu")
    private MenuPage clickMenuButton() {
        if(!page.url().contains("settings")) {
            clickBy(BTN_MENU_LOCATOR,0,false);
        }
        else clickBy(BTN_MENU_LOCATOR2,0,false);

        return new MenuPage(page);
    }

    @Step("close menu")
    public RouteHelper closeMenu() {
        page.reload();
        return this;
    }

    @Step("go to settings page")
    public SettingsAllPage toSettingsPage() throws Exception {
        openMenu().choosePage(Pages.Settings, SettingsAllPage.class);
        return new SettingsAllPage(page);
    }

    @Step("go to client page")
    public ClientsPage toClientPage() throws Exception {
        openMenu().choosePage(Pages.Client, ClientsPage.class);
        return new ClientsPage(page);
    }
    @Step("go to calendar page")
    public CalendarPage toCalendarPage() throws Exception {
        openMenu().choosePage(Pages.Calendar, CalendarPage.class);
        return new CalendarPage(page);
    }
    @Step("go to groups page")
    public GroupsListPage toGroupsListPage() throws Exception {
        openMenu().choosePage(Pages.Groups, GroupsListPage.class);
        return new GroupsListPage(page);
    }
    @Step("go to groups page")
    public ServicesListPage toServicesListPage() throws Exception {
        openMenu().choosePage(Pages.Services, ServicesListPage.class);
        return new ServicesListPage(page);
    }

}
