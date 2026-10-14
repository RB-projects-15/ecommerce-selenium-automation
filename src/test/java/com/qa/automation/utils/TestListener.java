
package com.qa.automation.utils;

import java.io.File;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.qa.automation.base.BaseTest;
import com.aventstack.extentreports.MediaEntityBuilder;

public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        ExtentReportManager.initReports();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentReportManager.createTest(
                result.getTestClass().getRealClass().getSimpleName()
                        + " - " + result.getMethod().getMethodName());

        ExtentReportManager.getTest().info("Test execution started");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentReportManager.getTest().pass("Test passed successfully");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        try {
            Object instance = result.getInstance();

            if (instance instanceof BaseTest) {
                BaseTest baseTest = (BaseTest) instance;

                String screenshotPath = ScreenshotUtils.captureScreenshot(
                        baseTest.driver, testName);

                if (screenshotPath != null
                        && new File(screenshotPath).exists()) {

                    ExtentReportManager.getTest().fail(
                            result.getThrowable(),
                            MediaEntityBuilder
                                    .createScreenCaptureFromPath(
                                            screenshotPath)
                                    .build());
                    return;
                }
            }
        } catch (Exception e) {
            System.err.println(
                    "Could not attach failure screenshot: "
                            + e.getMessage());
        }

        ExtentReportManager.getTest().fail(result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentReportManager.getTest().skip(result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.flushReports();
        ExtentReportManager.removeTest();
    }
}
