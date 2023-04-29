package com.lista.automation.ui.pages;

import com.lista.automation.ui.core.utils.BasePage;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

import static io.qameta.allure.Allure.step;


public class GalleryPage extends BasePage {
    private Page page;

    public GalleryPage(Page page) {
        super(page);
        this.page = page;
    }

    @Step("load gallery picture")
    public void addNotesToPicture(String notes) {
        step("type notes", () -> {
            getByPlaceholder("Add a caption...").fill(notes);
        });
        step("click Send button", () -> {
            clickBy("button .text-submit");
        });
    }
}
