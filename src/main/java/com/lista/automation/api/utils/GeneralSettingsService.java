package com.lista.automation.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lista.automation.api.pojo.general_settings.GeneralSettingsPojo;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;

public class GeneralSettingsService extends RestService {
    public GeneralSettingsService(String cookie) {
        super(cookie);
    }

    @Override
    protected String getBasePath() {
            return "en/settings";
    }

    @Step("api: get services by date")
    public GeneralSettingsPojo getCalendarSettings(int expectStatus) {

        String response = given().spec(getSPEC_HTML()).log().all()
                .queryParam("page", "calendar")
                .get()
                .then().log().all().assertThat().statusCode(expectStatus)
                .extract().body().htmlPath().get().toString();

        return extractValue(response);
    }

    @Step("matcher: extract value from response HTML")
    private GeneralSettingsPojo extractValue(String response) {
        Pattern pattern = Pattern.compile("(?<=const\\s)(\\w+)\\s*=\\s*(['\"`])(.*?)\\2");
        Matcher matcher = pattern.matcher(response);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(3);
            json.put(key, value);
        }

        return deserializationToPojo(mapper, json);
    }

    @Step("objectMapper: deserialization into the `SettingsPojo` class")
    private GeneralSettingsPojo deserializationToPojo(ObjectMapper mapper, ObjectNode json) {
        try {
            return mapper.readValue(json.toString(), GeneralSettingsPojo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
