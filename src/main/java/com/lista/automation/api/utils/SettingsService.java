package com.lista.automation.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lista.automation.api.pojo.calendar_settings.SettingsPojo;
import io.qameta.allure.Step;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;

public class SettingsService extends RestService {
    public SettingsService(String cookie) {
        super(cookie);
    }

    @Override
    protected String getBasePath() {
        return "en/settings";
    }

    @Step("api: get services by date")
    public SettingsPojo getCalendarSettings(int expectStatus) {

        String response = given().spec(getSPEC_HTML()).log().all()
                .queryParam("page", "calendar")
                .get()
                .then().assertThat().statusCode(expectStatus)
                .extract().body().htmlPath().get().toString();

        return extractValue(response);
    }
    @Step("matcher: extract value from response HTML")
    private SettingsPojo extractValue(String response) {
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
    private SettingsPojo deserializationToPojo(ObjectMapper mapper, ObjectNode json) {
        try {
            return mapper.readValue(json.toString(), SettingsPojo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
