package com.lista.automation.ui.core;

import com.lista.automation.api.utils.RestWrapper;
import com.lista.automation.ui.core.utils.Properties;
import com.lista.automation.ui.pages.calendar.CalendarPage;
import com.lista.automation.ui.pages.client.ClientsPage;
import com.lista.automation.ui.pages.service.ServicesListPage;
import com.microsoft.playwright.Page;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.io.IOException;
import java.lang.reflect.Method;

public class BaseTest {

    PlaywrightFactory pf;
    Page page;

    protected RestWrapper api;
    protected CalendarPage calendar;
    protected ClientsPage clientsPage;

    @BeforeMethod
    public void setUp() {
        api = RestWrapper.loginAs(Properties.getProp().username().replaceAll("\"",""),
                Properties.getProp().password().replaceAll("\"",""));
        pf = new PlaywrightFactory();
        page = pf.initBrowser();
        calendar = new CalendarPage(page);
    }

    @AfterMethod
    public void tearDown(Method testInfo, ITestResult iTestResult) throws IOException {
        pf.stop(testInfo, iTestResult);

    }


}
