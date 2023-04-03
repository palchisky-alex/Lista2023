package com.lista.automation.ui.tests;

import com.lista.automation.ui.core.BaseTest;
import com.lista.automation.ui.core.utils.CalendarView;
import org.junit.jupiter.api.Test;

public class CalendarPageTest extends BaseTest {

    @Test
    public void testCalendar() throws Exception {
        calendar.routing()
                .toSettingsPage()
                .toCalendarSettings()

                .changeCalendarView(CalendarView.Days);


    }

}
