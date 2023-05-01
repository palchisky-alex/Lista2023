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
        clickBy(".icon-del-wrap");
        return new Menu(page);
    }

    static public class Menu extends BasePage {
        public Menu(Page page) {
            super(page);
        }

        public Menu setMenuOptions(Opts opts) {
            step("click on button '" + opts.name() + "'", () -> {
                clickBy(getByRole(AriaRole.BUTTON).filter(new Locator.FilterOptions().setHasText(opts.name().replace("_", " "))), 0);

            });
            if (opts.name().equals("Delete")) {
                step("confirm deletion", () -> {
                    clickBy(getLocator(".confirm-block button")
                            .getByText(GroupsListPage.SubmitForm.LAST_ACTION.DELETE.name()), 0);
                });
            }
            return this;
        }

        public enum Opts {
            Select_All,
            Cancel,
            Delete
        }
    }

}
