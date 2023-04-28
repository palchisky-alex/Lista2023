package com.lista.automation.ui.pages;

import com.lista.automation.ui.core.utils.BasePage;
import com.microsoft.playwright.Page;


public class GalleryPage extends BasePage {
    private Page page;

    public GalleryPage(Page page) {
        super(page);
        this.page = page;
    }

    public void loadPicture(String notes) {
        getByPlaceholder("Add a caption...").fill(notes);
        clickBy("button .text-submit");
    }
}
