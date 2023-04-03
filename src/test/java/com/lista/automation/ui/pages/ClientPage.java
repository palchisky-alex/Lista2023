package com.lista.automation.ui.pages;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Request;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.AriaRole;

import java.util.Arrays;

public class ClientPage {
    private final Page page;
    private final String CLIENT_SEARCHING = ".search_wrap input";
    private final String CLIENT_SLOT = ".item a";
    private final String BTN_TRASH = ".item a";

    public ClientPage(Page page) {
        this.page = page;
    }

    public ClientPage findClient(String text) {
        String url = "https://test.atzma.im/clients?limit=40&offset=0&q=" + text;
        page.waitForResponse(Response::ok, () -> {
            page.locator(CLIENT_SEARCHING).type(text);
            System.out.println("GET-запрос выполнен успешно");
        });

        return this;
    }

    public ClientPage deleteClient() {
        page.getByRole(AriaRole.BUTTON).nth(1).click();
        page.locator(CLIENT_SLOT).click();
//        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Delete")).click();

        page.locator("button.activating").click();
        page.waitForResponse(Response::ok, () -> {
            page.locator(".deletePopup .yes-btn").click();
        });
        return this;
    }

    public int countClients() {
        return page.locator(CLIENT_SLOT).count();
    }
}
