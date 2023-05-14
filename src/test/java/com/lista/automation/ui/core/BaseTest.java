package com.lista.automation.ui.core;

import com.lista.automation.api.utils.RestWrapper;
import com.lista.automation.ui.pages.calendar.CalendarPage;
import com.lista.automation.ui.pages.client.ClientsPage;
import com.microsoft.playwright.Page;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import java.io.IOException;
import java.lang.reflect.Method;

public class BaseTest {

    PlaywrightFactory pf;
    Page page;

    protected RestWrapper api;
    protected CalendarPage calendar;
    protected ClientsPage clientsPage;

    @BeforeTest
    public void setFilter() {
        RestAssured.filters(new AllureRestAssured());
    }

    @BeforeMethod
    public void setUp() {
        pf = new PlaywrightFactory();
        page = pf.initBrowser();

        calendar = new CalendarPage(page);
        String permissionLevel = calendar.getPermissionLevel();
        api = RestWrapper.loginAs(permissionLevel);

    }

    @AfterMethod
    public void tearDown(Method testInfo, ITestResult iTestResult) throws IOException {
        pf.stop(testInfo, iTestResult);

    }


}
