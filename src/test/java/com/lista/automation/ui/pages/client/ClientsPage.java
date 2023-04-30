package com.lista.automation.ui.pages.client;

import com.lista.automation.ui.core.utils.BasePage;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;

import static io.qameta.allure.Allure.step;

public class ClientsPage extends BasePage {
    private final Page page;
    private final String CLIENT_SEARCHING = ".search_wrap input";
    private final String CLIENT_SLOT = ".item a";
    private final String BTN_TRASH = ".item a";

    public ClientsPage(Page page) {
        super(page);
        this.page = page;
    }

    @Step("search client")
    public ClientsPage findClient(String text) {
        page.waitForResponse(Response::ok, () -> {
            typeIn(CLIENT_SEARCHING, text);
            waitForTimeout(3000);
            System.out.println("GET- find Client запрос выполнен успешно");
        });

        return this;
    }

    public ClientsPage deleteClient() {
        step("click on trash button", () -> {
            getByRole(AriaRole.BUTTON).nth(1).click();
        });
        step("select client checkbox", () -> {
            clickBy(CLIENT_SLOT);
        });
        step("click on delete button", () -> {
            clickBy("button.activating");
        });
        step("confirm deletion", () -> {
            page.waitForResponse(Response::ok, () -> {
                clickBy(".deletePopup .yes-btn");
            });
        });
        return this;
    }

    @Step("init new client adding")
    public AddClientPage initAddingNewClient() {
        clickBy("#root .floating-button");
        waitForURL("adding-client");
        return new AddClientPage(page);
    }
    @Step("select client by ID")
    public ClientProfilePage selectClientById(String id) {
        clickBy("//a[contains(@href, '"+id+"')]");
        return new ClientProfilePage(page);
    }

    @Step("count clients")
    public int countClients() {
        return countElements(CLIENT_SLOT);
    }
}
