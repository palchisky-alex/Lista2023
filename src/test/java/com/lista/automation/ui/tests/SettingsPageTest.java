package com.lista.automation.ui.tests;

import com.lista.automation.ui.core.BaseTest;
import com.lista.automation.ui.core.MaxConcurrencyCondition;
import com.lista.automation.ui.core.utils.CalendarView;
import com.lista.automation.ui.core.utils.ViewStartOn;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MaxConcurrencyCondition.class)
public class SettingsPageTest extends BaseTest {

    @Test
    @Owner("Alex")
    @Description("playwright")
    public void testSettings() throws Exception {
        calendar.routing()
                .toSettingsPage()
                .toCalendarSettings()

                .changeCalendarView(CalendarView.Days)
                .changeCalendarView(CalendarView.Agenda)
                .changeCalendarView(CalendarView.Daily)
                .changeCalendarView(CalendarView.Weekly)
                .changeCalendarView(CalendarView.Monthly)

                .changeViewStartOn(ViewStartOn.Friday)
                .changeViewStartOn(ViewStartOn.Monday)
                .changeViewStartOn(ViewStartOn.Saturday)
                .changeViewStartOn(ViewStartOn.Tuesday)
                .changeViewStartOn(ViewStartOn.Wednesday)

                .changeEachCell("20")
                .backToSettingsPage();

    }
}
