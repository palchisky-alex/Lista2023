package com.lista.automation.ui.pages.group;

import com.lista.automation.ui.core.utils.BasePage;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;

import static io.qameta.allure.Allure.step;

public class GroupsListPage extends BasePage {
    private Page page;

    public GroupsListPage(Page page) {
        super(page);
        this.page = page;
    }

    @Step("change group")
    public GroupsListPage configureGroup(String groupName, ACTION act) {
        if (act.name().equals("Rename")) {
            initGroupMenu(groupName, act);
            step("type new name", () -> {
                typeIn(getByPlaceholder("Group name"), generateGroup(true).getName());
                clickBy(getByRoleWithText(AriaRole.BUTTON, SubmitForm.LAST_ACTION.SAVE.name()),0,true);
            });
        } else if (act.name().equals("Delete")) {
            initGroupMenu(groupName, act);
            step("click on button Delete", () -> {
                clickBy(getLocator(".confirm-block button")
                        .getByText(SubmitForm.LAST_ACTION.DELETE.name()),0,true);
            });
        }
        return this;
    }

    @Step("add new group and save")
    public GroupPage addGroup(String name) {
        step("click on button +", () -> {
            clickBy("#add-button",0,true);
        });
        step("type group name", () -> {
            typeIn(getByPlaceholder("Group name"), name);
        });
        step("click on button Save", () -> {
            clickBy(getLocator("form button").getByText(SubmitForm.LAST_ACTION.SAVE.name()),0,true);
        });
        return new GroupPage(page);
    }

    public void initGroupMenu(String groupName, ACTION act) {
        step("init group menu", () -> {
            clickBy(getByText(groupName),2000,true);
        });
        step("click on button Rename or Delete", () -> {
            clickBy(getByRoleWithText(AriaRole.BUTTON, act.name()),0,true);
        });
    }

    @Step("select group by name")
    public GroupPage selectGroup(String groupName) {
        clickBy(getByText(groupName),0,true);
        return new GroupPage(page);
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
            NO
        }
    }

    public enum ACTION {
        Rename,
        Delete,
        AddClients
    }

}
