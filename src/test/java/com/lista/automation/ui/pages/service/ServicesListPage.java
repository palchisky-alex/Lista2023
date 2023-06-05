package com.lista.automation.ui.pages.service;

import com.lista.automation.ui.core.utils.BasePage;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

import static org.assertj.core.api.Assertions.assertThat;
import static io.qameta.allure.Allure.step;

public class ServicesListPage extends BasePage {
    private Page page;

    public ServicesListPage(Page page) {
        super(page);
        this.page = page;
    }

    @Step("search a service")
    public ServicesListPage findService(String text) {
        typeIn(getByPlaceholder("Type service name"), text);
        waitForTimeout(2000);
        return this;
    }

    @Step("mark a service")
    private ServicesListPage markService() {
        clickBy(getLocator("label"), 0, true);
        return this;
    }

    @Step("select a service")
    public ServicePage selectService() {
        clickBy(getLocator("#category-container"), 0, true);
        return new ServicePage(page);
    }

    @Step("init new service adding")
    public ServicePage initAddingNewService() {
        clickBy(".add-button", 0, true);
        waitForURL("catalog/services/adding");
        return new ServicePage(page);
    }

    @Step("delete service")
    public ServicesListPage delete() {
        markService();

        clickBy(".delete-btn", 0, true);
        clickBy(getLocator("#modal-content .yes-btn"), 0, true);
        waitForTimeout(2000);

        assertThat(!isVisible("#modal-content")).as("modal popup closed").isTrue();
        return this;
    }

    @Step("count services")
    public int countServices() {
        return countElements("#category-container");
    }
}
