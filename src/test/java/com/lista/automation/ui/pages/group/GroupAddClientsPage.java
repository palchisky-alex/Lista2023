package com.lista.automation.ui.pages.group;

import com.lista.automation.ui.core.utils.BasePage;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;

import static io.qameta.allure.Allure.step;

public class GroupAddClientsPage extends BasePage {
    private Page page;

    public GroupAddClientsPage(Page page) {
        super(page);
        this.page = page;
    }

    @Step("search member")
    public GroupAddClientsPage searchMember(String name) {
        getByPlaceholder("Search by name or mobile").fill(name);
        waitForTimeout(3000);
        return this;
    }

    public GroupAddClientsPage addToGroup() {
        step("click on searching result", () -> {
            clickBy("li.client");
        });
        step("click on button Done", () -> {
            clickBy(getByRoleWithText(AriaRole.BUTTON, "done"), 0);
        });
        return this;
    }

}
