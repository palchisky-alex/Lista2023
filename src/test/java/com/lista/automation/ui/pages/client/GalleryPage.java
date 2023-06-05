package com.lista.automation.ui.pages.client;

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

    @Step("add note to picture")
    public GalleryPage addNotesToPicture(String notes) {

            getByPlaceholder("Add a caption...").fill(notes);


            clickBy("button .text-submit",0,true);

        return this;
    }
}
