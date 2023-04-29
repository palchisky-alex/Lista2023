package com.lista.automation.ui.tests;

import com.lista.automation.api.pojo.client.ClientCreateRequest;
import com.lista.automation.api.pojo.client.ClientGetResponse;
import com.lista.automation.ui.core.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import static com.lista.automation.ui.core.utils.BasePage.generateClient;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("GRUD Client UI")
public class ClientTest extends BaseTest {

    @Test
    public void testDeleteClient() {
        step("Verify client can be deleted from UI", () -> {

            step("generate simple client using api ", () -> {
                ClientCreateRequest simpleClient = generateClient(true);
                String phoneNumber = simpleClient.getPhone().replaceAll("\\D+", "");

                api.client.create(simpleClient, 201);
                ClientGetResponse client = api.client.find(phoneNumber, 200);

                step("check via api that the simple client has been created", () -> {
                    assertThat(client.getName()).isEqualTo(simpleClient.getName());
                });

                step("search a new client by phone number from UI", () -> {
                    clientsPage = calendar
                            .routing()
                            .toClientPage()
                            .findClient(phoneNumber);

                    step("assert - search returns a one client in the UI", () -> {
                        assertThat(clientsPage.countClients()).isEqualTo(1);
                    });

                    step("delete client from UI", () -> {
                        clientsPage.deleteClient();

                        step("search a deleted client by phone number from UI", () -> {
                            clientsPage = calendar
                                    .routing()
                                    .toClientPage()
                                    .findClient(phoneNumber);

                            step("assert - search returns a zero client in the UI", () -> {
                                assertThat(clientsPage.countClients()).isEqualTo(0);
                            });

                        });
                    });
                });
            });
        });
    }

    @Test
    public void testClientCreate() {
        step("Verify client can be created from UI", () -> {

            ClientCreateRequest simpleClient = calendar.routing()
                    .toClientPage()
                    .initAddingNewClient()
                    .setSimpleClient(true)
                    .submitNewClient();

            step("search the new client via api using phone", () -> {
                ClientGetResponse clientViaAPI = api.client.find(simpleClient.getPhone().replaceAll("\\D+", ""), 200);

                step("assert via api that the client has been created by comparing name and address", () -> {
                    assertThat(simpleClient)
                            .usingRecursiveComparison()
                            .comparingOnlyFields("name", "address")
                            .isEqualTo(clientViaAPI);
                });
            });
        });
    }

//    @Test
//    void testClientUpdatePersonalInfo() {
//        step("Verify client personal info can be change from UI", () -> {
//
//            step("generate simple client using api ", () -> {
//                ClientCreateRequest simpleClient = generateClient(true);
//                String phoneNumber = simpleClient.getPhone().replaceAll("\\D+", "");
//
//                api.client.create(simpleClient, 201);
//                ClientGetResponse apiClient = api.client.find(phoneNumber, 200);
//
//                step("check via api that the simple client has been created", () -> {
//                    assertThat(apiClient.getName()).isEqualTo(simpleClient.getName());
//
//                    step("search a new client by phone number", () -> {
//                        clientsPage = calendar
//                                .routing()
//                                .toClientPage()
//                                .findClient(phoneNumber);
//
//                        step("assert - search returns one customer in UI", () -> {
//                            assertThat(clientsPage.countClients()).isEqualTo(1);
//                        });
//                        step("edit client profile info", () -> {
//                            ClientCreateRequest simpleClient2 = clientsPage
//                                    .selectClientById(apiClient.getId())
//                                    .initProfileInfoEdit()
//                                    .cleanProfileInfo()
//                                    .setPersonalInfo(true)
//                                    .saveProfileInfo();
//
//                            step("search client by new phone number via api", () -> {
//                                ClientGetResponse clientViaAPI = api.client.find(simpleClient2.getPhone().replaceAll("\\D+", ""), 200);
//
//                                step("assert via api that the client profile info has been updated by comparing name and address", () -> {
//
//                                    assertThat(simpleClient2)
//                                            .usingRecursiveComparison()
//                                            .comparingOnlyFields("name", "address")
//                                            .isEqualTo(clientViaAPI);
//                                });
//                            });
//                        });
//                    });
//                });
//            });
//        });
//    }
//
//    @Test
//    void testClientUpdateDebts() {
//        step("Verify client debts can be change from UI", () -> {
//
//            step("generate simple client using api ", () -> {
//                ClientCreateRequest simpleClient = generateClient(true);
//                String phoneNumber = simpleClient.getPhone().replaceAll("\\D+", "");
//
//                api.client.create(simpleClient, 201);
//                ClientGetResponse apiClient = api.client.find(phoneNumber, 200);
//
//                step("check via api that the simple client has been created", () -> {
//                    assertThat(apiClient.getName()).isEqualTo(simpleClient.getName());
//
//                    step("search a new client by phone number", () -> {
//                        clientsPage = calendar
//                                .routing()
//                                .toClientPage()
//                                .findClient(phoneNumber);
//
//                        step("assert - search returns one customer in UI", () -> {
//                            assertThat(clientsPage.countClients()).isEqualTo(1);
//                        });
//                        step("edit client debts", () -> {
//                            clientsPage.selectClientById(apiClient.getId())
//                                    .initDebtsEdit()
//                                    .setDebts(simpleClient);
//
//                            step("search client by new phone number via api", () -> {
//                                ClientGetResponse clientViaAPI = api.client.find(simpleClient.getPhone().replaceAll("\\D+", ""), 200);
//
//                                step("assert via api that the client debts has been updated by comparing name and address", () -> {
//
//                                    assertThat(simpleClient)
//                                            .usingRecursiveComparison()
//                                            .comparingOnlyFields("name", "address")
//                                            .isEqualTo(clientViaAPI);
//                                });
//                            });
//                        });
//                    });
//                });
//            });
//        });
//    }
//
//    @Test
//    void testClientUpdateNotes() {
//        step("Verify client notes can be change from UI", () -> {
//
//            step("generate simple client using api ", () -> {
//                ClientCreateRequest simpleClient = generateClient(true);
//                String phoneNumber = simpleClient.getPhone().replaceAll("\\D+", "");
//
//                api.client.create(simpleClient, 201);
//                ClientGetResponse apiClient = api.client.find(phoneNumber, 200);
//
//                step("check via api that the simple client has been created", () -> {
//                    assertThat(apiClient.getName()).isEqualTo(simpleClient.getName());
//
//                    step("search a new client by phone number", () -> {
//                        clientsPage = calendar
//                                .routing()
//                                .toClientPage()
//                                .findClient(phoneNumber);
//
//                        step("assert - search returns one customer in UI", () -> {
//                            assertThat(clientsPage.countClients()).isEqualTo(1);
//                        });
//                        step("edit client notes", () -> {
//                            clientsPage.selectClientById(apiClient.getId())
//                                    .initNotesEdit()
//                                    .setNotes(simpleClient);
//
//                            step("search client by new phone number via api", () -> {
//                                ClientGetResponse clientViaAPI = api.client.find(simpleClient.getPhone().replaceAll("\\D+", ""), 200);
//
//                                step("assert via api that the client notes has been updated by comparing name and address", () -> {
//
//                                    assertThat(simpleClient)
//                                            .usingRecursiveComparison()
//                                            .comparingOnlyFields("name", "address")
//                                            .isEqualTo(clientViaAPI);
//                                });
//                            });
//                        });
//                    });
//                });
//            });
//        });
//    }
//
//    @Test
//    void testClientUpdateGallery() {
//        step("Verify client gallery can be change from UI", () -> {
//
//            step("generate simple client using api ", () -> {
//                ClientCreateRequest simpleClient = generateClient(true);
//                String phoneNumber = simpleClient.getPhone().replaceAll("\\D+", "");
//
//                api.client.create(simpleClient, 201);
//                ClientGetResponse apiClient = api.client.find(phoneNumber, 200);
//
//                step("check via api that the simple client has been created", () -> {
//                    assertThat(apiClient.getName()).isEqualTo(simpleClient.getName());
//
//                    step("search a new client by phone number", () -> {
//                        clientsPage = calendar
//                                .routing()
//                                .toClientPage()
//                                .findClient(phoneNumber);
//
//                        step("assert - search returns one customer in UI", () -> {
//                            assertThat(clientsPage.countClients()).isEqualTo(1);
//                        });
//                        step("edit client gallery", () -> {
//                            clientsPage.selectClientById(apiClient.getId())
//                                    .initGalleryEdit()
//                                    .loadPicture(simpleClient.getNotes());
//
//                            step("search client by new phone number via api", () -> {
//                                ClientGetResponse clientViaAPI = api.client.find(simpleClient.getPhone().replaceAll("\\D+", ""), 200);
//
//                                step("assert via api that the client notes has been updated by comparing name and address", () -> {
//
//                                    assertThat(simpleClient)
//                                            .usingRecursiveComparison()
//                                            .comparingOnlyFields("name", "address")
//                                            .isEqualTo(clientViaAPI);
//                                });
//                            });
//                        });
//                    });
//                });
//            });
//        });
//    }
}
