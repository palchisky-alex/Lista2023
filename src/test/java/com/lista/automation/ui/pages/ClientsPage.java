package com.lista.automation.ui.pages;

import com.lista.automation.ui.core.utils.BasePage;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import java.util.regex.Pattern;
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

    public ClientsPage findClient(String text) {
        page.waitForResponse(Response::ok, () -> {
            typeIn(CLIENT_SEARCHING, text);
            waitForLoadState();
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

    public AddClientPage initAddingNewClient() {
        clickBy("#root .floating-button");
        waitForURL("adding-client");
        return new AddClientPage(page);
    }
    public ClientProfilePage selectClientById(String id) {
        clickBy("//a[contains(@href, '"+id+"')]");
        return new ClientProfilePage(page);
    }

    public int countClients() {
        return countElements(CLIENT_SLOT);
    }
}
