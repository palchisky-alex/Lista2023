package com.lista.automation.ui.tests;

import com.lista.automation.ui.core.BaseTest;
import com.lista.automation.ui.core.utils.CalendarView;
import com.lista.automation.ui.core.utils.ViewStartOn;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.lista.automation.ui.core.utils.BasePage.getCurrentTime;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Calendar Settings")
@Feature("Change Calendar View")
public class SettingsPageTest extends BaseTest {

    @Test(dataProvider = "calendar_view")
    public void testCalendar(String selector, CalendarView views) throws Exception {
        calendar.routing()
                .toSettingsPage()
                .toCalendarSettings()
                .changeCalendarView(views)
                .backToSettingsPage();

        assertThat(calendar.routing().toCalendarPage().verifyCalendarView(selector))
                .as("calendar view changed to %s", views).isTrue();
    }

    @Test(dataProvider = "view_start_day")
    public void testSettings(String dayOfWeek, ViewStartOn viewStartOn, String cellDuration) throws Exception {
        calendar.routing()
                .toSettingsPage()
                .toCalendarSettings()
                .changeCalendarView(CalendarView.Weekly)

                .changeViewStartOn(viewStartOn)

                .changeEachCell(cellDuration)
                .backToSettingsPage();

        String calendarFirstDay = calendar.routing()
                .toCalendarPage()
                .getCalendarFirstDay();

        assertThat(calendarFirstDay).isEqualTo(dayOfWeek);

        String cellDurationResult = calendar.routing()
                .toCalendarPage()
                .getCellDuration();

        assertThat(cellDurationResult)
                .as("cell duration changed to %s", cellDuration)
                .isEqualTo(cellDuration);
    }


    @DataProvider(name = "calendar_view")
    public Object[][] testDataCalendarView() {
        return new Object[][]{
                {"agendaFourDay-view", CalendarView.Days},
                {"daily-view", CalendarView.Daily},
                {"agendaDay-view", CalendarView.Agenda},
                {"weekly-view", CalendarView.Weekly}
        };
    }

    @DataProvider(name = "view_start_day")
    public Object[][] testDataViewDay() {
        return new Object[][]{
                {"sun", ViewStartOn.Sunday, "5"},
                {"mon", ViewStartOn.Monday, "10"},
                {"tue", ViewStartOn.Tuesday, "15"},
                {"wed", ViewStartOn.Wednesday, "20"},
                {"thu", ViewStartOn.Thursday, "30"},
                {"fri", ViewStartOn.Friday, "60"},
                {"sat", ViewStartOn.Saturday, "60"}
        };
    }
}
