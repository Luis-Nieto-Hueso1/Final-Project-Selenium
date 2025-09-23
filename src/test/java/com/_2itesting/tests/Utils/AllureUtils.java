// src/test/java/util/AllureUtils.java
package com._2itesting.tests.Utils;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public final class AllureUtils {
    private AllureUtils() {}

    public static void attachScreenshot(WebDriver driver, String name) {
        if (driver == null) return;
        byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        Allure.addAttachment(name, new ByteArrayInputStream(bytes));
    }

    public static void attachPageSource(WebDriver driver, String name) {
        if (driver == null) return;
        String html = driver.getPageSource();
        Allure.addAttachment(name + " (HTML)", "text/html",
                new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)), ".html");
    }

    public static void attachUrl(WebDriver driver) {
        if (driver == null) return;
        Allure.addAttachment("Current URL", driver.getCurrentUrl());
    }
}