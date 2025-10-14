package com._2itesting.tests.steps;

import com._2itesting.tests.models.UserCredentials;
import com._2itesting.tests.pomClasses.LoginPagePOM;
import com._2itesting.tests.pomClasses.NavPOM;
import org.openqa.selenium.WebDriver;

public class LoginSteps {
    private final LoginPagePOM loginPage;
    private final NavPOM nav;

    public LoginSteps(WebDriver driver) {
        this.loginPage = new LoginPagePOM(driver);
        this.nav = new NavPOM(driver);
    }

    // A complete user workflow
    public void loginAs(UserCredentials user) {
        loginPage.setUsername(user.getUsername());
        loginPage.setPassword(user.getPassword());
        loginPage.submitForm();
    }

    public void logout() {
        nav.navMyAccount();
        nav.navLogout();
    }
}