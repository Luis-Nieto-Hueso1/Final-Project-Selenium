package com._2itesting.tests.steps;

import com._2itesting.tests.Utils.Helpers;
import com._2itesting.tests.Utils.Waiter;
import com._2itesting.tests.models.UserCredentials;
import com._2itesting.tests.pomClasses.LoginPagePOM;
import com._2itesting.tests.pomClasses.NavPOM;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginSteps {
    private final WebDriver driver;
    private final LoginPagePOM loginPage;
    private final NavPOM nav;
    private final Waiter waiter;

    public LoginSteps(WebDriver driver, Waiter waiter) {
        this.driver = driver;
        this.waiter = waiter;
        this.loginPage = new LoginPagePOM(driver);
        this.nav = new NavPOM(driver);
    }

    public void navigateToLoginPage() {
        driver.get(Helpers.LOGIN_URL);
    }

    public void loginWith(UserCredentials credentials) {
        loginPage.enterUsername(credentials.getUsername());
        loginPage.enterPassword(credentials.getPassword());
        loginPage.clickSubmit();
        waiter.visible(By.linkText("Log out"));
    }

    public void loginAs(UserCredentials credentials) {
        navigateToLoginPage();
        loginWith(credentials);
    }

    public void logout() {
        nav.clickMyAccount();
        waiter.clickable(By.linkText("Log out"));
        nav.clickLogout();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}