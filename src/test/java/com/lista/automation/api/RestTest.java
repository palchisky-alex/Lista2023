package com.lista.automation.api;

import com.lista.automation.api.pojo.client.ClientCreateRequest;
import com.lista.automation.api.pojo.client.ClientGetResponse;
import com.lista.automation.api.pojo.service.ServiceCreateRequest;
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



    @Owner("Alex")
    @Description("Create client and delete")

    void createClient() {
//        ServiceCreateRequest serviceData = ServiceCreateRequest.getInstance();
//        serviceData.setPrice(2).setServiceDuration(3);
//
//        api.service.create(serviceData, 201);


//        List<ClientGetResponse> clients = api.client().getList(200);
//        clients.forEach(System.out::println);
    }


}
