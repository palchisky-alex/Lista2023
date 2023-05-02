package com.lista.automation.ui.pages.group;

import com.lista.automation.ui.core.utils.BasePage;

import static org.assertj.core.api.Assertions.assertThat;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;

import java.util.Arrays;
import java.util.Collections;

import static io.qameta.allure.Allure.step;

public class GroupsListPage extends BasePage {
    private Page page;

    public GroupsListPage(Page page) {
        super(page);
        this.page = page;
    }

    @Step("change group")
    public GroupsListPage configureGroup(ACTION act) {

        if (act.name().equals("Rename")) {
            step("click on button Rename group", () -> {
                clickBy(getByRoleWithText(AriaRole.BUTTON, act.name()), 0, true);
            });
            step("type new name", () -> {
                typeIn(getByPlaceholder("Group name"), generateGroup(true).getName());
                clickBy(getByRoleWithText(AriaRole.BUTTON, SubmitForm.LAST_ACTION.SAVE.name()), 0, true);
            });

        } else if (act.name().equals("Delete")) {
            step("click on button Delete group", () -> {
                clickBy(getByRoleWithText(AriaRole.BUTTON, act.name()), 0, true);
            });
            step("confirm deletion", () -> {
                clickBy(getLocator(".confirm-block button")
                        .getByText(SubmitForm.LAST_ACTION.DELETE.name()), 0, true);
            });

        } else if (act.name().equals("ClosePopup")) {
            step("UI: assert warning text of default group", () -> {
                String popupText = getInnerTextBy(".warning_popup").toLowerCase();

                assertThat(popupText).as("warning popup of group %s", popupText)
                        .isEqualToIgnoringWhitespace("renaming or deleting this group is not possible" +
                                " since it's managed by lista got it");

                step("close warning popup of default group", () -> {
                    clickBy(getLocator(".warning_popup button")
                            .getByText(SubmitForm.LAST_ACTION.GOT_IT.name().replace("_", " ")), 0, false);
                });
            });
        }
        return this;
    }

    @Step("add new group and save")
    public GroupPage addGroup(String name) {
        step("click on button +", () -> {
            clickBy("#add-button", 0, true);
        });
        step("type group name", () -> {
            typeIn(getByPlaceholder("Group name"), name);
        });
        step("click on button Save", () -> {
            clickBy(getLocator("form button").getByText(SubmitForm.LAST_ACTION.SAVE.name()), 0, true);
        });
        return new GroupPage(page);
    }


    @Step("select group by name")
    public GroupPage selectGroup(String groupName) {
        clickBy(getByText(groupName), 0, true);
        return new GroupPage(page);
    }

    @Step("long press")
    public GroupsListPage longPressOnGroup(String groupName) {
        clickBy(getByExactText(groupName), 2000, false);
        return this;
    }

    public static class SubmitForm extends GroupsListPage {
        private Page page;

        public SubmitForm(Page page) {
            super(page);
            this.page = page;

        }

        public enum LAST_ACTION {
            SAVE,
            CANCEL,
            DELETE,
            NO,
            GOT_IT

        }
    }

    public enum ACTION {
        Rename,
        Delete,
        AddClients,
        ClosePopup

    }

}
