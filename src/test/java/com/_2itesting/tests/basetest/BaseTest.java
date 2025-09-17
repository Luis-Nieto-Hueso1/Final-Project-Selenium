package com._2itesting.tests.basetest;

import com._2itesting.tests.Utils.Helpers;
import com._2itesting.tests.Utils.InstanceHelpers;
import com._2itesting.tests.Utils.ReportUtils;
import com._2itesting.tests.Utils.Waiter;
import com._2itesting.tests.pomClasses.CartPOM;
import com._2itesting.tests.pomClasses.LoginPagePOM;
import com._2itesting.tests.pomClasses.NavPOM;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;

public class BaseTest {
    public WebDriver driver;
    public StringBuffer verificationErrors  = new StringBuffer();
    protected Waiter waiter;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        waiter = new Waiter(driver, Duration.ofSeconds(10));

        System.out.println("=== Starting Test Setup ===");
        driver.get(Helpers.LOGIN_URL);
        driver.findElement(By.cssSelector(".woocommerce-store-notice__dismiss-link")).click();
        // Inputs: make them explicit and parameterisable later
        final String username = Helpers.USERNAME;
        final String productName = "Polo"; // replace with param later
        final String coupon = Helpers.TWO_I_DISCOUNT_COUPON;
        final String password = Helpers.PASSWORD;
        ReportUtils.logInputs(username, password, productName, coupon);

        LoginPagePOM loginPagePOM = new LoginPagePOM(driver);
        CartPOM cart = new CartPOM(driver);
        NavPOM navPOM = new NavPOM(driver);

        boolean loggedIn = loginPagePOM.login(username, Helpers.PASSWORD);
        assertThat("login Successful", loggedIn, is(true));
        assertThat("Should be redirected to account page after login", driver.getCurrentUrl(), containsString("my-account"));

        navPOM.navPageBasket();
        cart.clearCart();

        InstanceHelpers instanceHelpers = new InstanceHelpers(driver);

        System.out.println("=== Cart cleared successfully ===");
    }

    @AfterEach
    public void tearDown(
    ) {
        driver.close();
        driver.quit();
        String verificationErrorsString = verificationErrors.toString(); //Post test - gather together any verification errors

        if(!verificationErrorsString.isEmpty()){ //If verificationErrorsString is *not* empty - there must have been errors...
            fail("Found verification errors :" + verificationErrorsString); //...So fail.
  }
}
}
