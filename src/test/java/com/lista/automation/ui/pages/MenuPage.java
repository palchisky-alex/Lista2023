package com.lista.automation.ui.pages;

import com.lista.automation.ui.core.utils.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.lista.automation.ui.core.utils.Pages;
import io.qameta.allure.Step;

import java.util.regex.Pattern;

import static io.qameta.allure.Allure.step;


public class MenuPage extends BasePage {
    Page page;
    private final String MENU_LIST_LOCATOR = ".menu-list";

    public MenuPage(Page page) {
        super(page);
        this.page = page;

    }


    @Step("select submenu")
    public <T> T choosePage(Pages calendarPages, Class<T> clazz) throws Exception {
        clickBy(getLocator(MENU_LIST_LOCATOR)
                .getByRole(AriaRole.LINK)
                .filter(new Locator.FilterOptions().setHasText(Pattern.compile(calendarPages.name()))),0,true);


        T pageInstance = clazz.getDeclaredConstructor(Page.class).newInstance(page);
        return clazz.cast(pageInstance);
    }


}
