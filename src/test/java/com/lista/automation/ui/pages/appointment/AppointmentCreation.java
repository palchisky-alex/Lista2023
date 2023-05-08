package com.lista.automation.ui.pages.appointment;

import com.lista.automation.api.pojo.client.ClientCreateRequest;
import com.lista.automation.ui.core.utils.BasePage;
import com.lista.automation.ui.pages.calendar.CalendarPage;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;

import static io.qameta.allure.Allure.step;

public class AppointmentCreation extends BasePage {
    private Page page;

    public AppointmentCreation(Page page) {
        super(page);
        this.page = page;
    }

    public SetClient setClient() {
        return new SetClient(page);
    }


    static public class SetClient extends BasePage {
        private Page page;

        public SetClient(Page page) {
            super(page);
            this.page = page;
        }

        @Step("Menu: SKIP")
        public SetService skip() {
            clickBy(getByRoleWithText(AriaRole.BUTTON, "Skip"), 0, true);
            return new SetService(page);
        }

        @Step("Menu: BACK")
        public SetClient back() {
            clickBy(getByRoleWithText(AriaRole.BUTTON, "Back"), 0, true);
            return this;
        }
        @Step("Menu: ADD AND CONTINUE")
        public SetService addAndContinue() {
            page.locator("//*[contains(text(), 'ADD AND CONTINUE')]/..").click();
//            clickBy(getByRoleWithText(AriaRole.BUTTON, "ADD AND CONTINUE"), 2, true);
            return new SetService(page);
        }

        public SetService searchClientAndPick(String clientName) {
            step("search client by name", () -> {
                typeIn(getByPlaceholder("Search by name or phone"), clientName);
                waitForTimeout(2000);
            });
            step("select a client", () -> {
                clickBy(getByText(clientName), 0, true);
                waitForURL("creating-appointment/selecting-procedure");
            });
            return new SetService(page);
        }
        public SetClient searchClientAndCreate(ClientCreateRequest client) {
            step("search client by name", () -> {
                typeIn(getByPlaceholder("Search by name or phone"), client.getName());
                waitForTimeout(2000);
            });
            step("fill client phone", () -> {
                typeIn(getByPlaceholder("Type phone number"), client.getPhone().replaceAll("\\D+", ""));
            });
            return this;
        }

        @Step("create a new client")
        public void createNewClient() {

        }

        static public class SetService extends BasePage {
            private Page page;

            public SetService(Page page) {
                super(page);
                this.page = page;
            }


            public SetService chooseServiceAndPick(String serviceName) {
                step("search service by name", () -> {
                    typeIn(getByPlaceholder("Search a service or create one"), "service_" + serviceName);
                    waitForTimeout(2000);
                });
                step("select service", () -> {
                    clickBy(".filter-procedures", 0, false);
                });
                return this;
            }

            @Step("Menu: NEXT")
            public Summary next() {
                clickBy(getByRoleWithText(AriaRole.BUTTON, "Next"), 0, true);
                return new Summary(page);
            }

            @Step("Menu: CANCEL")
            public SetClient cancel() {
                clickBy(getByRoleWithText(AriaRole.BUTTON, "Cancel"), 0, true);
                return new SetClient(page);
            }


            static public class Summary extends BasePage {
                private Page page;

                public Summary(Page page) {
                    super(page);
                    this.page = page;
                }

                @Step("Menu: SAVE")
                public CalendarPage save() {
                    clickBy(getByRoleWithText(AriaRole.BUTTON, "Save"), 0, true);
                    waitForURL("en/calendar");
                    return new CalendarPage(page);
                }

                @Step("Menu: BACK")
                public SetClient back() {
                    clickBy(getByRoleWithText(AriaRole.BUTTON, "Back"), 0, true);
                    return new SetService(page).cancel();
                }

            }
        }
    }


}
