package com.lista.automation.ui.tests;

import com.lista.automation.api.pojo.client.ClientCreateRequest;
import com.lista.automation.api.pojo.client.ClientGetResponse;
import com.lista.automation.ui.core.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.testng.annotations.Test;

import static com.lista.automation.ui.core.utils.BasePage.generateClient;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Client UI GRUD")
public class ClientTest extends BaseTest {

    @Test
    @Description("UI: Delete client from UI")
    public void testDeleteClient() {
        step("UI: verify client can be deleted", () -> {

            step("API: generate simple client", () -> {
                ClientCreateRequest simpleClient = generateClient(true);
                String phoneNumber = simpleClient.getPhone().replaceAll("\\D+", "");

                api.client.create(simpleClient, 201);
                ClientGetResponse client = api.client.find(phoneNumber, 200);

                step("API: check that the simple client has been created", () -> {
                    assertThat(client.getName()).isEqualTo(simpleClient.getName());
                });

                step("UI: search a new client by phone number", () -> {
                    clientsPage = calendar
                            .routing()
                            .toClientPage()
                            .findClient(phoneNumber);

                    step("UI: search returns a one client", () -> {
                        assertThat(clientsPage.countClients())
                                .as("one client was found")
                                .isEqualTo(1);
                    });

                    step("UI: delete client", () -> {
                        clientsPage.deleteClient();

                        step("UI: search a deleted client by phone number", () -> {
                            clientsPage = calendar
                                    .routing()
                                    .toClientPage()
                                    .findClient(phoneNumber);

                            step("UI: no client found", () -> {
                                assertThat(clientsPage.countClients()).isEqualTo(0);
                            });

                        });
                    });
                });
            });
        });
    }

    @Test
    @Description("UI: Create client from UI")
    public void testClientCreate() {
        step("UI: verify client can be created", () -> {
            ClientCreateRequest simpleClient = calendar.routing()
                    .toClientPage()
                    .initAddingNewClient()
                    .setSimpleClient(true)
                    .submitNewClient();

            step("API: search the new client by phone", () -> {
                ClientGetResponse clientViaAPI = api.client
                        .find(simpleClient.getPhone()
                                .replaceAll("\\D+", ""), 200);

                step("API: assert that the client has been created by comparing name and address", () -> {
                    assertThat(simpleClient)
                            .usingRecursiveComparison()
                            .comparingOnlyFields("name", "address")
                            .isEqualTo(clientViaAPI);
                });
            });
        });
    }

    @Test
    @Description("UI: Update personal info of client from UI")
    void testClientUpdatePersonalInfo() {
        step("UI: verify client personal info can be change", () -> {

            step("API: generate simple client", () -> {
                ClientCreateRequest simpleClient = generateClient(true);
                String phoneNumber = simpleClient.getPhone().replaceAll("\\D+", "");

                api.client.create(simpleClient, 201);
                ClientGetResponse apiClient = api.client.find(phoneNumber, 200);

                step("API: check that the simple client has been created", () -> {
                    assertThat(apiClient.getName()).isEqualTo(simpleClient.getName());

                    step("UI: search a new client by phone number", () -> {
                        clientsPage = calendar
                                .routing()
                                .toClientPage()
                                .findClient(phoneNumber);

                        step("UI: search returns one client", () -> {
                            assertThat(clientsPage.countClients()).isEqualTo(1);
                        });
                        step("UI: edit client profile info", () -> {
                            ClientCreateRequest simpleClient2 = clientsPage
                                    .selectClientById(apiClient.getId())
                                    .initProfileInfoEdit()
                                    .cleanProfileInfo()
                                    .setPersonalInfo(true)
                                    .saveProfileInfo();

                            step("API: search client by new phone number", () -> {
                                ClientGetResponse clientViaAPI = api.client.find(simpleClient2.getPhone()
                                        .replaceAll("\\D+", ""), 200);

                                step("API: assert that the client profile info has been updated by comparing name and address", () -> {

                                    assertThat(simpleClient2)
                                            .usingRecursiveComparison()
                                            .comparingOnlyFields("name", "address")
                                            .isEqualTo(clientViaAPI);
                                });
                            });
                        });
                    });
                });
            });
        });
    }

    @Test
    @Description("UI: Update debts of client from UI")
    void testClientUpdateDebts() {
        step("UI: verify client debts can be change", () -> {

            step("API: generate simple client", () -> {
                ClientCreateRequest simpleClient = generateClient(true);
                String phoneNumber = simpleClient.getPhone().replaceAll("\\D+", "");

                api.client.create(simpleClient, 201);
                ClientGetResponse apiClient = api.client.find(phoneNumber, 200);

                step("API: check that the simple client has been created", () -> {
                    assertThat(apiClient.getName()).isEqualTo(simpleClient.getName());

                    step("UI: search a new client by phone number", () -> {
                        clientsPage = calendar
                                .routing()
                                .toClientPage()
                                .findClient(phoneNumber);

                        step("UI: search returns one client", () -> {
                            assertThat(clientsPage.countClients())
                                    .as("search returns a one client")
                                    .isEqualTo(1);
                        });
                        step("edit client debts", () -> {
                            clientsPage.selectClientById(apiClient.getId())
                                    .initDebtsEdit()
                                    .setDebts(simpleClient);

                            step("search client by new phone number via api", () -> {
                                ClientGetResponse clientViaAPI = api.client
                                        .find(simpleClient.getPhone()
                                                .replaceAll("\\D+", ""), 200);

                                step("assert via api that the client debts has been updated by comparing name and address", () -> {

                                    assertThat(simpleClient)
                                            .usingRecursiveComparison()
                                            .comparingOnlyFields("name", "address")
                                            .isEqualTo(clientViaAPI);
                                });
                            });
                        });
                    });
                });
            });
        });
    }

    @Test
    @Description("UI: Update notes of client from UI")
    void testClientUpdateNotes() {
        step("UI: verify client notes can be change", () -> {

            step("API: generate simple client", () -> {
                ClientCreateRequest simpleClient = generateClient(true);
                String phoneNumber = simpleClient.getPhone().replaceAll("\\D+", "");

                api.client.create(simpleClient, 201);
                ClientGetResponse apiClient = api.client.find(phoneNumber, 200);

                step("API: check that the simple client has been created", () -> {
                    assertThat(apiClient.getName()).as("client was created").isEqualTo(simpleClient.getName());

                    step("UI: search a new client by phone number", () -> {
                        clientsPage = calendar
                                .routing()
                                .toClientPage()
                                .findClient(phoneNumber);

                        step("UI: search returns one customer", () -> {
                            assertThat(clientsPage.countClients()).as("one client founded").isEqualTo(1);
                        });
                        step("UI: edit client notes", () -> {
                            clientsPage.selectClientById(apiClient.getId())
                                    .initNotesEdit()
                                    .setNotes(simpleClient);

                            step("API: search client by new phone number", () -> {
                                ClientGetResponse clientViaAPI = api.client
                                        .find(simpleClient.getPhone()
                                                .replaceAll("\\D+", ""), 200);

                                step("API: assert that the client notes has been updated by comparing name and address", () -> {

                                    assertThat(simpleClient)
                                            .usingRecursiveComparison()
                                            .comparingOnlyFields("name", "address")
                                            .isEqualTo(clientViaAPI);
                                });
                            });
                        });
                    });
                });
            });
        });
    }

    @Test
    @Description("UI: Update gallery of client from UI")
    void testClientUpdateGallery() {
        step("UI: verify client gallery can be change", () -> {

            step("API: generate simple client", () -> {
                ClientCreateRequest simpleClient = generateClient(true);
                String phoneNumber = simpleClient.getPhone().replaceAll("\\D+", "");

                api.client.create(simpleClient, 201);
                ClientGetResponse apiClient = api.client.find(phoneNumber, 200);

                step("API: check that the simple client has been created", () -> {
                    assertThat(apiClient.getName()).isEqualTo(simpleClient.getName());

                    step("UI: search a new client by phone number", () -> {
                        clientsPage = calendar
                                .routing()
                                .toClientPage()
                                .findClient(phoneNumber);

                        step("UI: search returns one client", () -> {
                            assertThat(clientsPage.countClients()).isEqualTo(1);
                        });
                        step("UI: edit client gallery", () -> {
                            clientsPage.selectClientById(apiClient.getId())
                                    .initGalleryEdit()
                                    .addNotesToPicture(simpleClient.getNotes());

                            step("API: search client by new phone number", () -> {
                                ClientGetResponse clientViaAPI = api.client
                                        .find(simpleClient.getPhone()
                                                .replaceAll("\\D+", ""), 200);

                                step("API: assert that the client notes has been updated by comparing name and address", () -> {

                                    assertThat(simpleClient)
                                            .usingRecursiveComparison()
                                            .comparingOnlyFields("name", "address")
                                            .isEqualTo(clientViaAPI);
                                });
                            });
                        });
                    });
                });
            });
        });
    }
}
