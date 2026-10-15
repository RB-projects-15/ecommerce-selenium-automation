package com.qa.automation.listeners;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Base64;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.qa.automation.driver.DriverManager;
import com.qa.automation.utils.ExtentReportManager;
import com.qa.automation.utils.ScreenshotUtils;

/**
 * Single place for reporting: builds the Extent report and, on failure, attaches a screenshot
 * (embedded as Base64 so the HTML report is self-contained) and saves a PNG under target/screenshots.
 *
 * <p>Registered through {@code META-INF/services/org.testng.ITestNGListener}.
 */
public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        ExtentReportManager.initReports();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String description = result.getMethod().getDescription();
        ExtentTest test = ExtentReportManager.createTest(testName(result), description == null ? "" : description);

        String[] groups = result.getMethod().getGroups();
        if (groups.length > 0) {
            test.assignCategory(groups);
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        activeTest(result).pass("Test passed");
        ExtentReportManager.removeTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        recordFailure(result, false);
        ExtentReportManager.removeTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // TestNG reports a failed attempt that will be retried as "skipped".
        if (result.getThrowable() != null) {
            recordFailure(result, true);
        } else {
            activeTest(result).skip("Test skipped");
        }
        ExtentReportManager.removeTest();
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.flushReports();
    }

    private void recordFailure(ITestResult result, boolean willRetry) {
        ExtentTest test = activeTest(result);

        byte[] png = ScreenshotUtils.capture(DriverManager.getDriverIfPresent());
        Path saved = ScreenshotUtils.save(png, result.getName());

        String message = willRetry ? "Attempt failed - the test will be retried" : "Test failed";
        if (png != null) {
            String base64 = Base64.getEncoder().encodeToString(png);
            test.fail(message + ": " + result.getThrowable(),
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64, "Failure screenshot").build());
        } else {
            test.fail(result.getThrowable());
        }
        if (saved != null) {
            test.info("Screenshot saved: " + saved);
        }
    }

    /** Current test entry, created on demand when TestNG skips a test without starting it. */
    private static ExtentTest activeTest(ITestResult result) {
        ExtentTest test = ExtentReportManager.getTest();
        return test != null ? test : ExtentReportManager.createTest(testName(result), "");
    }

    private static String testName(ITestResult result) {
        String name = result.getTestClass().getRealClass().getSimpleName() + " - " + result.getMethod().getMethodName();
        Object[] parameters = result.getParameters();
        return parameters.length == 0 ? name : name + " " + Arrays.deepToString(parameters);
    }
}
