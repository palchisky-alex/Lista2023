package com.lista.automation.ui.tests;

import com.lista.automation.api.TestListener;
import com.lista.automation.api.pojo.service.ServiceCreateRequest;
import com.lista.automation.ui.core.BaseTest;
import com.lista.automation.ui.pages.service.ServicePage;
import com.lista.automation.ui.pages.service.ServicesListPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;

import static com.lista.automation.api.authentication.Scope.JUNIOR;
import static com.lista.automation.ui.core.utils.BasePage.generateService;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@Story("Verify services")
@Epic("Service UI GRUD")
@Feature("Service")
@Listeners(TestListener.class)
public class ServiceTest extends BaseTest {

    @Test
    @Description("UI: Delete service")
    public void testServiceDelete() throws Exception {

        ServiceCreateRequest simpleService = generateService(true);
        api.service().create(simpleService, 201);


        ServicesListPage servicesPage = calendar
                .routing()
                .toServicesListPage()
                .findService(simpleService.getServiceName());


        assertThat(servicesPage.countServices())
                .as("one service was found")
                .isEqualTo(1);


        servicesPage
                .findService(simpleService.getServiceName())
                .delete();


        servicesPage = calendar
                .routing()
                .toServicesListPage()
                .findService(simpleService.getServiceName());


        assertThat(servicesPage.countServices())
                .as("no service found")
                .isEqualTo(0);


    }

    @Test
    @Description("UI: Create service")
    public void testServiceCreate() throws Exception {

        ServiceCreateRequest simpleService = generateService(true);


        calendar.routing()
                .toServicesListPage()
                .initAddingNewService()
                .setSimpleService(simpleService)
                .submitService(ServicePage.Act.ADD);


        ServicesListPage servicesPage = calendar.routing()
                .toServicesListPage()
                .findService(simpleService.getServiceName());


        assertThat(servicesPage.countServices())
                .as("one service was found")
                .isEqualTo(1);


    }

    @Test
    @Description("UI: Update service")
    public void testServiceUpdate() throws Exception {

        api.service().deleteAll();
        ServiceCreateRequest simpleService = generateService(true);
        ServiceCreateRequest simpleService2 = generateService(true);

        api.service().create(simpleService, 201);


        calendar.routing()
                .toSettingsPage()
                .toCalendarSettings()
                .changeEachCell("5")
                .backToSettingsPage();


        ServicesListPage servicesPage = calendar
                .routing()
                .toServicesListPage()
                .findService(simpleService.getServiceName());

        assertThat(servicesPage.countServices())
                .as("one service was found")
                .isEqualTo(1);


        servicesPage
                .findService(simpleService.getServiceName())
                .selectService()
                .setSimpleService(simpleService2)
                .submitService(ServicePage.Act.Update);


        List<ServiceCreateRequest> servicesViaAPI = api.service().getServiceList(200);

        assertThat(servicesViaAPI)
                .as("verify that serviceName,serviceDuration,price updated")
                .as("Calendar cell duration - 5 min")
                .extracting("serviceName", "serviceDuration", "price")
                .contains(tuple(simpleService2.getServiceName(),
                        simpleService2.getServiceDuration(),
                        simpleService2.getPrice()));


    }
}
