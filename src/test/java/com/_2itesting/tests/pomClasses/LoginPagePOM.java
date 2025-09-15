package com._2itesting.tests.pomClasses;

import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

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

        boolean loggedIn = false;
        try {
            driver.switchTo().alert();
        } catch (NoAlertPresentException e) {
            loggedIn = true;

        }
        return loggedIn;
    }


}