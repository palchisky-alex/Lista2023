package com.lista.automation.api.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Created by Palchitsky Alex
 */
public class ConfigFileHandler {

    public static void changeLoginType(String newLoginType) throws IOException {
        Path configPath = Paths.get("src", "test", "resources", "config.properties");
        Properties props = new Properties();
        props.load(Files.newBufferedReader(configPath));
        props.setProperty("login.as", newLoginType);
        Files.write(configPath, props.toString().getBytes());
    }
}
