package com.qa.automation.listeners;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import com.qa.automation.config.ConfigReader;

/** Re-runs a failed test up to {@code retryCount} times to absorb one-off browser/network flakiness. */
public class RetryAnalyzer implements IRetryAnalyzer {

    // Keyed per test invocation (method + parameters) so data-driven rows are counted separately.
    private static final Map<String, Integer> ATTEMPTS = new ConcurrentHashMap<>();

    @Override
    public boolean retry(ITestResult result) {
        int maxRetries = ConfigReader.getInt("retryCount");
        String key = result.getTestClass().getName() + "." + result.getName()
                + Arrays.deepToString(result.getParameters());

        int used = ATTEMPTS.merge(key, 1, Integer::sum);
        return used <= maxRetries;
    }
}
