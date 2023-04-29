package com.lista.automation.ui.tests;

import com.lista.automation.ui.core.BaseTest;
import com.lista.automation.ui.core.utils.CalendarView;
import com.lista.automation.ui.core.utils.ViewStartOn;
import groovy.lang.Category;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.lista.automation.ui.core.utils.BasePage.getCurrentTime;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Calendar Settings")
public class SettingsPageTest extends BaseTest {

    @Test(dataProvider = "calendar_view")
    public void testCalendar(String selector, CalendarView views) {
        step("Change calendar view via UI", () -> {
            calendar.routing()
                    .toSettingsPage()
                    .toCalendarSettings()
                    .changeCalendarView(views)
                    .backToSettingsPage();

            step("Verify that calendar view has been changed from UI", () -> {
                assertThat(calendar.routing().toCalendarPage().verifyCalendarView(selector))
                        .as("calendar view changed to %s", views).isTrue();
            });
        });
    }

    @Test(dataProvider = "view_start_day")
    public void testSettings(String dayOfWeek, ViewStartOn viewStartOn, String cellDuration) {
        step("Change calendar week first day and cell duration via UI", () -> {
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
            step("Verify calendar first day", () -> {
                assertThat(calendarFirstDay).as("calendar first day is %s", dayOfWeek).isEqualTo(dayOfWeek);
            });

            String cellDurationResult = calendar.routing()
                    .toCalendarPage()
                    .getCellDuration();

            step("Verify calendar cell duration", () -> {
                assertThat(cellDurationResult)
                        .as("cell duration changed to %s minutes", cellDuration)
                        .isEqualTo(cellDuration);
            });
        });
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
