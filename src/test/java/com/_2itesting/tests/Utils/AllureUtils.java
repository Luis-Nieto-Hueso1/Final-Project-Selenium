package com._2itesting.tests.Utils;


import io.qameta.allure.Attachment;
import org.openqa.selenium.*;


public final class AllureUtils {
    private AllureUtils() {}

    @Attachment(value = "{name}", type = "image/png")
    public static byte[] attachScreenshot(WebDriver driver, String name) {
        if (!(driver instanceof TakesScreenshot ts)) {
            return ("Driver does not support screenshots").getBytes();
        }
        return ts.getScreenshotAs(OutputType.BYTES); // Allure embeds this as an image
    }
// Alternative method to save screenshot as a file (not used here)
    @Attachment(value = "{name} (HTML)", type = "text/html", fileExtension = ".html")
    public static byte[] attachPageSource(WebDriver driver, String name) {
        if (driver == null) return "No driver".getBytes();
        return driver.getPageSource().getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }

    @Attachment(value = "Current URL", type = "text/plain")
    public static String attachUrl(WebDriver driver) {
        return driver == null ? "No driver" : driver.getCurrentUrl();
    }


    public static void attachAllOnFailure(WebDriver driver) {
        attachScreenshot(driver, "Failure screenshot");
        attachPageSource(driver, "Failure page source");
        attachUrl(driver);
    }
}
