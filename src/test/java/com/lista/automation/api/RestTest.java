package com.lista.automation.api;

import com.lista.automation.api.pojo.client.ClientCreateRequest;
import com.lista.automation.api.pojo.client.ClientGetResponse;
import com.lista.automation.api.utils.DataGenerator;
import com.lista.automation.api.utils.RestWrapper;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.*;

@Listeners(TestListener.class)
public class RestTest {
    private static RestWrapper api;


    @Test
    @Owner("Alex")
    @Description("Create client and delete")
    void createClient() {

        ClientCreateRequest simpleClient = DataGenerator.getSimpleData(ClientCreateRequest.class);
        String client_id = api.client.create(simpleClient, 201);
        assertThat(client_id).as("client was created and has a ID").isNotNull();


        ClientGetResponse createdClient = api.client.find(simpleClient.getName(), 200);

        assertThat(simpleClient).extracting(ClientCreateRequest::getName)
                .as("created client has correct name")
                .isEqualTo(createdClient.getName());

        assertThat(simpleClient).extracting(ClientCreateRequest::getAddress)
                .as("created client has correct address")
                .isEqualTo(createdClient.getAddress());

        api.client.deleteAll(204);


//        List<ClientGetResponse> clients = api.client.getList(200);
//        clients.forEach(System.out::println);
    }


}
