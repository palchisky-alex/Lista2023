package com.lista.automation.ui.pages.group;

import com.lista.automation.ui.core.utils.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
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

    @Step("initiate group menu")
    public Menu initMenuForMember() {
        step("click on button icon-del-wrap", () -> {
            clickBy(".icon-del-wrap");
        });
        step("select member in group", () -> {
            getLocator("li.client").click();
            waitForTimeout(2000);
        });
        return new Menu(page);
    }

    static public class Menu extends BasePage {
        public Menu(Page page) {
            super(page);
        }

        public void setMenuOptions(Opts opts) {
            if (opts.name().equals("Delete")) {
                step("click on button Delete clients from the group", () -> {
                    getByRole(AriaRole.BUTTON).filter(new Locator.FilterOptions().setHasText(opts.name())).click();
                });
                step("click on button Delete", () -> {
                    clickBy(getLocator(".confirm-block button")
                            .getByText(GroupsListPage.SubmitForm.LAST_ACTION.DELETE.name()), 0);
                });
            }
        }

        public enum Opts {
            Select,
            Cancel,
            Delete
        }
    }

}
