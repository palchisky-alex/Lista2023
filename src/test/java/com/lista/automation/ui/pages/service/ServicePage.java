package com.lista.automation.ui.pages.service;

import com.lista.automation.api.pojo.service.ServiceCreateRequest;
import com.lista.automation.ui.core.utils.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;

import java.util.regex.Pattern;

import static io.qameta.allure.Allure.step;

public class ServicePage extends BasePage {
    private Page page;
    private String inputServiceName = "[class*= 'services__container'] input";
    private String btnCategory = "[class*= 'category-title'] .arrow";
    private String btnAddCategory = "button.create-button:not([disabled])";
    private String inputDuration = "//*[text()='Duration']/..//*[@class='output-field']";
    private String inputPrice = "//*[text()='Price']/..//*[@class='output-field']";

    public ServicePage(Page page) {
        super(page);
        this.page = page;
    }

    @Step("set simple service")
    public ServicePage setSimpleService(ServiceCreateRequest simpleService) {

        typeIn(inputServiceName, simpleService.getServiceName());
        clickBy(btnCategory, 0, false);


        typeIn(getByPlaceholder("Pls enter the name of the new category"), simpleService.getServiceName());
        clickBy(btnAddCategory, 0, true);


        clickBy(inputDuration, 0, false);
        typeIn("[name='duration-input']", String.valueOf(simpleService.getServiceDuration()));


        clickBy(inputPrice, 0, false);
        typeIn("[class='input-field']", String.valueOf(simpleService.getPrice()));

        return this;
    }

    @Step("submit service")
    public void submitService(Act act) {
        clickBy(getByRole(AriaRole.BUTTON).filter(new Locator.FilterOptions()
                .setHasText(Pattern.compile(act.name(), Pattern.CASE_INSENSITIVE))), 0, false);
    }

    public enum Act {
        ADD,
        Update,
        Cancel,
        Delete
    }


}
