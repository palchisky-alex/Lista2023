package com.lista.automation.ui.pages;

import com.lista.automation.ui.core.utils.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.lista.automation.ui.core.utils.Pages;

import java.util.regex.Pattern;


public class MenuPage extends BasePage {
    Page page;
    private final String MENU_LIST_LOCATOR = ".menu-list";

    public MenuPage(Page page) {
        super(page);
        this.page = page;

    }


    public <T> T choosePage(Pages calendarPages, Class<T> clazz) throws Exception {
        getLocator(MENU_LIST_LOCATOR)
                .getByRole(AriaRole.LINK)
                .filter(new Locator.FilterOptions().setHasText(Pattern.compile(calendarPages.name()))).click();

        T pageInstance = clazz.getDeclaredConstructor(Page.class).newInstance(page);
        return clazz.cast(pageInstance);
    }










}
