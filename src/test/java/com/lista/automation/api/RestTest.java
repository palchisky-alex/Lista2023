package com.lista.automation.api;

import com.lista.automation.api.pojo.client.ClientCreateRequest;
import com.lista.automation.ui.core.BaseTest;
import org.testng.annotations.Listeners;

import static com.lista.automation.ui.core.utils.BasePage.generateClient;

@Listeners(TestListener.class)
public class RestTest extends BaseTest {

    void createService() {
        ClientCreateRequest simpleClient = generateClient(true);
        String phoneNumber = simpleClient.getPhone().replaceAll("\\D+", "");
        api.client().find2(phoneNumber);

    }

}
