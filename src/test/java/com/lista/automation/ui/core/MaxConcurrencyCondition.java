package com.lista.automation.ui.core;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Optional;
import java.util.concurrent.Semaphore;

public class MaxConcurrencyCondition implements ExecutionCondition {
    private static final Semaphore SEMAPHORE = new Semaphore(Runtime.getRuntime().availableProcessors());

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext extensionContext) {
        Optional<Integer> maxConcurrency = extensionContext.getConfigurationParameter("maxConcurrency", Integer::valueOf);
        int permits = maxConcurrency.orElse(1);

        if (SEMAPHORE.tryAcquire(permits)) {
            return ConditionEvaluationResult.enabled("Concurrency level is within limit");
        } else {
            return ConditionEvaluationResult.disabled("Concurrency level exceeded the limit");
        }
    }
}
