package com.lista.automation.api;

import com.lista.automation.api.pojo.client.ClientCreateRequest;
import com.lista.automation.ui.core.BaseTest;
import io.restassured.response.Response;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.lista.automation.ui.core.utils.BasePage.generateClient;
import static io.restassured.RestAssured.given;

@Listeners(TestListener.class)
public class RestTest extends BaseTest {

    void createService() {
        ClientCreateRequest simpleClient = generateClient(true);
        String phoneNumber = simpleClient.getPhone().replaceAll("\\D+", "");
        Response response = api.client().find2(phoneNumber);




//        ServiceCreateRequest serviceData = ServiceCreateRequest.getInstance();
//        serviceData.setPrice(2).setServiceDuration(3);
//
//        api.service.create(serviceData, 201);


//        List<ClientGetResponse> clients = api.client().getList(200);
//        clients.forEach(System.out::println);
    }


}
