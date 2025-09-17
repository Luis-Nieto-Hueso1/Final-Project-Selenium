package com._2itesting.tests.pomClasses;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPagePOM {
    private WebDriver driver;

    public LoginPagePOM(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);

    }

    @FindBy(id = "username")
    public WebElement usernameField;

    @FindBy(id = "password")
    public WebElement passwordField;

    @FindBy(css = "button[name='login']")
    public WebElement submitButton;



    public LoginPagePOM setUsername(String username) {
        usernameField.clear();
        usernameField.sendKeys(username);
        return this;
    }

    public void setPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void submitForm() {
        submitButton.click();
    }

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