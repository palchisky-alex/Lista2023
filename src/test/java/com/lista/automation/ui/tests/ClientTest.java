package com.lista.automation.ui.tests;

import com.lista.automation.api.pojo.client.ClientCreateRequest;
import com.lista.automation.api.pojo.client.ClientGetResponse;
import com.lista.automation.ui.core.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;
import static com.lista.automation.ui.core.utils.BasePage.generateClient;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;


@Epic("Client UI GRUD")
@Feature("Client")
public class ClientTest extends BaseTest {

    @Test
    @Description("UI: Delete client")
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
    @Description("UI: Create client")
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

                step("API: assert that the client has been created", () -> {
                    assertThat(clientViaAPI).extracting("name", "address")
                            .contains(simpleClient.getName(), simpleClient.getAddress());
                });
            });
        });
    }

    @Test
    @Description("UI: Update personal info of client")
    void testClientUpdatePersonalInfo() {
        step("UI: verify client personal info can be change", () -> {
            api.client.deleteAll(204);
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

                                step("API: assert that the client exists after personal info update", () -> {
                                    assertThat(clientViaAPI).extracting("name", "address")
                                            .contains(simpleClient2.getName(), simpleClient2.getAddress());
                                });
                            });
                        });
                    });
                });
            });
        });
    }

    @Test
    @Description("UI: Update debts of client")
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

                                step("API: assert that the client exists after debts update", () -> {
                                    assertThat(clientViaAPI).extracting("name", "address", "phone")
                                            .contains(simpleClient.getName(), simpleClient.getAddress(), phoneNumber);
                                });
                            });
                        });
                    });
                });
            });
        });
    }

    @Test
    @Description("UI: Update notes of client")
    void testClientUpdateNotes() {
        step("UI: verify client notes can be change", () -> {

            step("API: generate simple client", () -> {
                ClientCreateRequest simpleClient = generateClient(true);
                ClientCreateRequest simpleClient2 = generateClient(true);
                String phoneNumber = simpleClient.getPhone().replaceAll("\\D+", "");

                api.client.create(simpleClient, 201);
                ClientGetResponse apiClient = api.client.find(phoneNumber, 200);

                step("API: check that the simple client has been created", () -> {
                    assertThat(apiClient.getName()).as("client was created").isEqualTo(simpleClient.getName());

                    step("UI: search a client by phone number", () -> {
                        clientsPage = calendar
                                .routing()
                                .toClientPage()
                                .findClient(phoneNumber);

                        step("UI: search returns one client", () -> {
                            assertThat(clientsPage.countClients()).as("one client founded").isEqualTo(1);
                        });
                        step("UI: edit client notes", () -> {
                            clientsPage.selectClientById(apiClient.getId())
                                    .initNotesEdit()
                                    .setNotes(simpleClient2);

                            step("API: search client by phone number", () -> {
                                ClientGetResponse clientViaAPI = api.client
                                        .find(simpleClient.getPhone()
                                                .replaceAll("\\D+", ""), 200);

                                step("API: assert that the client exists after notes update", () -> {
                                    assertThat(clientViaAPI).extracting("name", "address", "phone")
                                            .contains(simpleClient.getName(), simpleClient.getAddress(), phoneNumber);
                                });
                            });
                        });
                    });
                });
            });
        });
    }

    @Test
    @Description("UI: Update gallery of client")
    void testClientUpdateGallery() {
        step("UI: verify client gallery can be change", () -> {

            step("API: generate simple client", () -> {
                ClientCreateRequest simpleClient = generateClient(true);
                String phoneNumber = simpleClient.getPhone().replaceAll("\\D+", "");

                api.client.create(simpleClient, 201);
                ClientGetResponse apiClient = api.client.find(phoneNumber, 200);

                step("API: check that the simple client has been created", () -> {
                    assertThat(apiClient.getName()).isEqualTo(simpleClient.getName());

                    step("UI: search a client by phone number", () -> {
                        clientsPage = calendar
                                .routing()
                                .toClientPage()
                                .findClient(phoneNumber);

                        step("UI: search returns one client", () -> {
                            assertThat(clientsPage.countClients()).isEqualTo(1);
                        });

                        step("UI: edit client gallery", () -> {
                            clientsPage.selectClientById(apiClient.getId())
                                    .editGallery()
                                    .addNotesToPicture(simpleClient.getNotes());

                            step("API: search client by phone number", () -> {
                                ClientGetResponse clientViaAPI = api.client
                                        .find(simpleClient.getPhone()
                                                .replaceAll("\\D+", ""), 200);

                                step("API: assert that the client exists after gallery update", () -> {
                                    assertThat(clientViaAPI).extracting("name", "address", "phone")
                                            .contains(simpleClient.getName(), simpleClient.getAddress(), phoneNumber);

                                });
                            });
                        });
                    });
                });
            });
        });
    }
}
