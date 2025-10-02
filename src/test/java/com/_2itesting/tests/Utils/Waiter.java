package com._2itesting.tests.Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

// Utility class for explicit waits
public final class Waiter {
    private final WebDriverWait wait;

    // Constructor to initialize WebDriverWait with a specified timeout
    public Waiter(WebDriver driver, Duration timeout) {
        this.wait = new WebDriverWait(driver, timeout);
    }

    // Waits until the element located by the given By locator is clickable
    public WebElement clickable(By by) {
        return wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    // Waits until the element located by the given By locator is visible
    public WebElement visible(By by) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    // Waits until the current URL contains the specified fragment
    public boolean urlContains(String fragment) {
        return wait.until(ExpectedConditions.urlContains(fragment));
    }

    // Waits until the element located by the given By locator is invisible
    public boolean invisible(By by) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }
}
