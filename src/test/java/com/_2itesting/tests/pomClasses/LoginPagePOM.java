package com._2itesting.tests.pomClasses;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

// Page Object Model class for the login page
public class LoginPagePOM {
    private WebDriver driver;
// Constructor
    public LoginPagePOM(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);

    }

    // Username input field
    @FindBy(id = "username")
    public WebElement usernameField;
    // Password input field
    @FindBy(id = "password")
    public WebElement passwordField;
    // Submit button
    @FindBy(css = "button[name='login']")
    public WebElement submitButton;


    // Set username method
    public LoginPagePOM setUsername(String username) {
        usernameField.clear();
        usernameField.sendKeys(username);
        return this;
    }

    // Set password method
    public void setPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    // Submit form method
    public void submitForm() {
        submitButton.click();
    }

    // Login method that combines setting username, password, and submitting the form
    public boolean login(String username, String password) {
        setUsername(username).setPassword(password);
        submitForm();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.and(
                    ExpectedConditions.urlContains("/my-account/"),
                    ExpectedConditions.visibilityOfElementLocated(By.linkText("Log out"))
            ));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }


}