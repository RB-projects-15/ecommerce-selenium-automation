package com.qa.automation.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.qa.automation.config.ConfigReader;

/** Owns the single ExtentReports instance and the per-thread ExtentTest. */
public final class ExtentReportManager {

    public static final String REPORT_DIR = "target/extent-report";
    public static final String REPORT_FILE = REPORT_DIR + "/ExtentReport.html";

    private static final ThreadLocal<ExtentTest> CURRENT_TEST = new ThreadLocal<>();
    private static ExtentReports extent;

    private ExtentReportManager() {
        // utility class
    }

    public static synchronized void initReports() {
        if (extent != null) {
            return;
        }
        try {
            Files.createDirectories(Paths.get(REPORT_DIR));
        } catch (IOException e) {
            throw new IllegalStateException("Could not create report directory " + REPORT_DIR, e);
        }

        ExtentSparkReporter reporter = new ExtentSparkReporter(REPORT_FILE);
        reporter.config().setDocumentTitle("Selenium Automation Test Report");
        reporter.config().setReportName("E-Commerce UI Test Results");

        extent = new ExtentReports();
        extent.attachReporter(reporter);
        extent.setSystemInfo("Application", "SauceDemo");
        extent.setSystemInfo("Base URL", ConfigReader.getBaseUrl());
        extent.setSystemInfo("Browser", ConfigReader.get("browser"));
        extent.setSystemInfo("Headless", ConfigReader.get("headless"));
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java", System.getProperty("java.version"));
    }

    public static synchronized ExtentTest createTest(String name, String description) {
        ExtentTest test = extent.createTest(name, description);
        CURRENT_TEST.set(test);
        return test;
    }

    public static ExtentTest getTest() {
        return CURRENT_TEST.get();
    }

    /** Adds a step to the current test's report; silently ignored when no test is active. */
    public static void log(String message) {
        ExtentTest test = CURRENT_TEST.get();
        if (test != null) {
            test.info(message);
        }
    }

    public static void removeTest() {
        CURRENT_TEST.remove();
    }

    public static synchronized void flushReports() {
        if (extent != null) {
            extent.flush();
        }
    }
}
