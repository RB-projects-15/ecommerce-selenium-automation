package com.qa.automation.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public final class ScreenshotUtils {

    private static final Path SCREENSHOT_DIR = Paths.get("target", "screenshots");
    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtils() {
        // utility class
    }

    /** Captures the browser viewport as PNG bytes, or {@code null} if that is not possible. */
    public static byte[] capture(WebDriver driver) {
        if (!(driver instanceof TakesScreenshot screenshotDriver)) {
            return null;
        }
        try {
            return screenshotDriver.getScreenshotAs(OutputType.BYTES);
        } catch (RuntimeException e) {
            System.err.println("Could not capture screenshot: " + e.getMessage());
            return null;
        }
    }

    /** Saves PNG bytes under target/screenshots and returns the file path, or {@code null} on failure. */
    public static Path save(byte[] png, String testName) {
        if (png == null) {
            return null;
        }
        try {
            Files.createDirectories(SCREENSHOT_DIR);
            String safeName = testName.replaceAll("[^a-zA-Z0-9-_]", "_");
            Path file = SCREENSHOT_DIR.resolve(safeName + "_" + LocalDateTime.now().format(TIMESTAMP) + ".png");
            Files.write(file, png);
            return file;
        } catch (IOException e) {
            System.err.println("Could not save screenshot: " + e.getMessage());
            return null;
        }
    }
}
