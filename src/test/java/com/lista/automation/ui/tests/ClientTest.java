package com.lista.automation.ui.tests;

import com.lista.automation.api.pojo.client.ClientCreateRequest;
import com.lista.automation.api.pojo.client.ClientGetResponse;
import com.lista.automation.api.utils.services.DataGenerator;
import com.lista.automation.api.utils.services.RestWrapper;
import com.lista.automation.ui.core.BaseTest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


public class ClientTest extends BaseTest {

    @Test
    public void deleteClient() throws Exception {
        RestWrapper api = RestWrapper.loginAs("alex.palchisky@gmail.com", "123456");
        ClientCreateRequest simpleClient = DataGenerator.getSimpleData(ClientCreateRequest.class);
        String phoneNUmber = String.format(simpleClient.getPhone()).replaceAll("\\D", "");

        api.client.create(simpleClient, 201);
        ClientGetResponse client = api.client.find(phoneNUmber, 200);
        assertThat(client.getName()).as("client was found using the api").isEqualTo(simpleClient.getName());

        int api_clientListBeforeDeletion = api.client.findList(200).size();

        clientPage = calendar
                .routing()
                .toClientPage()
                .findClient(phoneNUmber);

        assertThat(clientPage.countClients()).as("search returns a least of one client In the UI").isGreaterThan(0);

        clientPage.deleteClient();
        int api_clientListAfterDeletion = api.client.findList(200).size();
        assertThat(api_clientListAfterDeletion).as("client was deleted").isEqualTo(api_clientListBeforeDeletion - 1);


    }
}
