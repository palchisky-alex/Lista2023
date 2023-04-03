package com.lista.automation.api;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

public class TestListener implements TestWatcher {
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        Allure.getLifecycle().getCurrentTestCase();
    }
    @Override
    public void testSuccessful(ExtensionContext context) {
        Allure.getLifecycle().getCurrentTestCase();
    }
}
