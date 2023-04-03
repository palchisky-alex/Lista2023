package com.lista.automation.ui.core;

import com.lista.automation.ui.pages.ClientPage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.lista.automation.ui.pages.CalendarPage;
import com.lista.automation.ui.pages.SettingsAllPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

public class BaseTest {

    PlaywrightFactory pf;
    Page page;
    protected Locator locator;
//    RouteHelper routing;
    protected CalendarPage calendar;
    protected SettingsAllPage settingsPage;
    protected ClientPage clientPage;

    @BeforeEach
    public void setUp() {
        pf = new PlaywrightFactory();
        page = pf.initBrowser();
        calendar = new CalendarPage(page);
//        settings = new SettingsPage(page);
//        routing = new RouteHelper(page);

    }

    @AfterEach
    public void tearDown(TestInfo testInfo){
        pf.stop(testInfo);

    }


}
