package com.lista.automation.ui.pages.group;

import com.lista.automation.ui.core.utils.BasePage;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

import static io.qameta.allure.Allure.step;

public class GroupPage extends BasePage {
    private Page page;

    public GroupPage(Page page) {
        super(page);
        this.page = page;
    }

    @Step("return to list of groups from group")
    public void returnToListGroups() {
        step("click on button <", () -> {
            clickBy("#topnav .right-abs");
        });
    }

    @Step("click on button +")
    public GroupAddClientsPage initAddingMember() {
        clickBy("#add-button");
        return new GroupAddClientsPage(page);
    }
}
