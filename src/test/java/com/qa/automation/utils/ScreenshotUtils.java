
package com.qa.automation.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtils {

    public static String captureScreenshot(
            WebDriver driver, String testName) {

        if (driver == null) {
            return null;
        }

        try {
            Path screenshotDirectory = Paths.get("test-output", "screenshots");

            Files.createDirectories(screenshotDirectory);

            String timestamp = new SimpleDateFormat(
                    "yyyyMMdd_HHmmss").format(new Date());

            String safeTestName = testName.replaceAll(
                    "[^a-zA-Z0-9-_]", "_");

            Path destination = screenshotDirectory.resolve(
                    safeTestName + "_" + timestamp + ".png");

            File source = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.FILE);

            Files.copy(
                    source.toPath(),
                    destination,
                    StandardCopyOption.REPLACE_EXISTING);

            return destination.toAbsolutePath().toString();

        } catch (IOException | RuntimeException e) {
            System.err.println(
                    "Could not capture screenshot: " + e.getMessage());
            return null;
        }
    }
}
