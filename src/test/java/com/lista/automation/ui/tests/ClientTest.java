package com.lista.automation.ui.tests;

import com.lista.automation.api.Properties;
import com.lista.automation.api.TestListener;
import com.lista.automation.api.assert_response.RestResponse;
import com.lista.automation.api.pojo.client.ClientCreateRequest;
import com.lista.automation.api.pojo.client.ClientGetResponse;
import com.lista.automation.ui.core.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;

import static com.lista.automation.ui.core.utils.BasePage.generateClient;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;


@Epic("Client UI GRUD")
@Feature("Client")
@Listeners(TestListener.class)
public class ClientTest extends BaseTest {

    @Test
    public void testGetUsers() {
        RestResponse<List<ClientGetResponse>> usersResponse = api.client().findListNew();
        usersResponse.validate().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath(Properties.getProp().schemaClient()));
    }

    @Test
    @Description("UI: Delete client")
    public void testDeleteClient() throws Exception {


        ClientCreateRequest simpleClient = generateClient(true);
        String phoneNumber = simpleClient.getPhone().replaceAll("\\D+", "");

        api.client().create(simpleClient, 201);
        ClientGetResponse client = api.client().find(phoneNumber);


        assertThat(client.getName()).isEqualTo(simpleClient.getName());


        clientsPage = calendar
                .routing()
                .toClientPage()
                .findClient(phoneNumber);


        assertThat(clientsPage.countClients())
                .as("one client was found")
                .isEqualTo(1);


        clientsPage.deleteClient();


        clientsPage = calendar
                .routing()
                .toClientPage()
                .findClient(phoneNumber);


        assertThat(clientsPage.countClients()).isEqualTo(0);

    }

    @Test
    @Description("UI: Create client")
    public void testClientCreate() throws Exception {

        ClientCreateRequest simpleClient = calendar.routing()
                .toClientPage()
                .initAddingNewClient()
                .setSimpleClient(true)
                .submitNewClient();


        ClientGetResponse clientViaAPI = api.client()
                .find(simpleClient.getPhone()
                        .replaceAll("\\D+", ""));


        assertThat(clientViaAPI).extracting("name", "address")
                .contains(simpleClient.getName(), simpleClient.getAddress());

    }

    @Test
    @Description("UI: Update personal info of client")
    void testClientUpdatePersonalInfo() throws Exception {

        api.client().deleteAll(204);

        ClientCreateRequest simpleClient = generateClient(true);
        String phoneNumber = simpleClient.getPhone().replaceAll("\\D+", "");

        api.client().create(simpleClient, 201);
        ClientGetResponse apiClient = api.client().find(phoneNumber);


        assertThat(apiClient.getName()).isEqualTo(simpleClient.getName());


        clientsPage = calendar
                .routing()
                .toClientPage()
                .findClient(phoneNumber);


        assertThat(clientsPage.countClients()).isEqualTo(1);


        ClientCreateRequest simpleClient2 = clientsPage
                .selectClientById(apiClient.getId())
                .initProfileInfoEdit()
                .cleanProfileInfo()
                .setPersonalInfo(true)
                .saveProfileInfo();


        ClientGetResponse clientViaAPI = api.client().find(simpleClient2.getPhone()
                .replaceAll("\\D+", ""));


        assertThat(clientViaAPI).extracting("name", "address")
                .contains(simpleClient2.getName(), simpleClient2.getAddress());

    }

    @Test
    @Description("UI: Update debts of client")
    void testClientUpdateDebts() throws Exception {


        ClientCreateRequest simpleClient = generateClient(true);
        String phoneNumber = simpleClient.getPhone().replaceAll("\\D+", "");

        api.client().create(simpleClient, 201);
        ClientGetResponse apiClient = api.client().find(phoneNumber);


        assertThat(apiClient.getName()).isEqualTo(simpleClient.getName());


        clientsPage = calendar
                .routing()
                .toClientPage()
                .findClient(phoneNumber);


        assertThat(clientsPage.countClients())
                .as("search returns a one client")
                .isEqualTo(1);


        clientsPage.selectClientById(apiClient.getId())
                .initDebtsEdit()
                .setDebts(simpleClient);


        ClientGetResponse clientViaAPI = api.client()
                .find(simpleClient.getPhone()
                        .replaceAll("\\D+", ""));


        assertThat(clientViaAPI).extracting("name", "address", "phone")
                .contains(simpleClient.getName(), simpleClient.getAddress(), phoneNumber);

    }

    @Test
    @Description("UI: Update notes of client")
    void testClientUpdateNotes() throws Exception {

        ClientCreateRequest simpleClient = generateClient(true);
        ClientCreateRequest simpleClient2 = generateClient(true);
        String phoneNumber = simpleClient.getPhone().replaceAll("\\D+", "");

        api.client().create(simpleClient, 201);
        ClientGetResponse apiClient = api.client().find(phoneNumber);


        assertThat(apiClient.getName()).as("client was created").isEqualTo(simpleClient.getName());


        clientsPage = calendar
                .routing()
                .toClientPage()
                .findClient(phoneNumber);


        assertThat(clientsPage.countClients()).as("one client founded").isEqualTo(1);


        clientsPage.selectClientById(apiClient.getId())
                .initNotesEdit()
                .setNotes(simpleClient2);


        ClientGetResponse clientViaAPI = api.client()
                .find(simpleClient.getPhone()
                        .replaceAll("\\D+", ""));


        assertThat(clientViaAPI).extracting("name", "address", "phone")
                .contains(simpleClient.getName(), simpleClient.getAddress(), phoneNumber);

    }

    @Test
    @Description("UI: Update gallery of client")
    void testClientUpdateGallery() throws Exception {

        ClientCreateRequest simpleClient = generateClient(true);
        String phoneNumber = simpleClient.getPhone().replaceAll("\\D+", "");

        api.client().create(simpleClient, 201);
        ClientGetResponse apiClient = api.client().find(phoneNumber);


        assertThat(apiClient.getName()).isEqualTo(simpleClient.getName());


        clientsPage = calendar
                .routing()
                .toClientPage()
                .findClient(phoneNumber);

        assertThat(clientsPage.countClients()).isEqualTo(1);


        clientsPage.selectClientById(apiClient.getId())
                .editGallery()
                .addNotesToPicture(simpleClient.getNotes());


        ClientGetResponse clientViaAPI = api.client()
                .find(simpleClient.getPhone()
                        .replaceAll("\\D+", ""));


        assertThat(clientViaAPI).extracting("name", "address", "phone")
                .contains(simpleClient.getName(), simpleClient.getAddress(), phoneNumber);

    }

}
