package com.qa.automation.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportManager {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static synchronized void initReports() {
        if (extent == null) {
            try {
                Files.createDirectories(Paths.get("test-output"));
            } catch (IOException e) {
                throw new RuntimeException(
                        "Could not create test-output directory", e);
            }

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(
                    "test-output/ExtentReport.html");

            sparkReporter.config().setDocumentTitle(
                    "Selenium Automation Test Report");

            sparkReporter.config().setReportName(
                    "E-Commerce Website Test Results");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

            extent.setSystemInfo("Application", "SauceDemo");
            extent.setSystemInfo("Automation Tool", "Selenium");
            extent.setSystemInfo("Test Framework", "TestNG");
        }
    }

    public static void createTest(String testName) {
        test.set(extent.createTest(testName));
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void flushReports() {
        if (extent != null) {
            extent.flush();
        }
    }

    public static void removeTest() {
        test.remove();
    }
}
