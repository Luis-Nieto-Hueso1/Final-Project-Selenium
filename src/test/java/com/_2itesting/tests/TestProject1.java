package com._2itesting.tests;

import com._2itesting.tests.Utils.Helpers;
import com._2itesting.tests.basetest.BaseTest;
import com._2itesting.tests.pomClasses.LoginPagePOM;
import com._2itesting.tests.pomClasses.NavPagePOM;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TestProject1 extends BaseTest {


    @Test
    public void firstTest() {

        // Step 1: Navigate to login page and authenticate
        //navigateShopAndLogin();
        System.out.println("=== Starting Test Setup ===");
        // Navigate directly to the My Account login page of the demo site
        driver.get(Helpers.LOGIN_URL);
        System.out.println("=== Navigating to shop ===");
        driver.findElement(By.cssSelector(".woocommerce-store-notice__dismiss-link")).click();
        LoginPagePOM loginPagePOM = new LoginPagePOM(driver);

        System.out.println("=== Enter Username ,Enter Password,Click Login ===");

        boolean loggedIn = loginPagePOM.login(Helpers.USERNAME,Helpers.PASSWORD);
        // Verify login was successful
        assertThat("login Suscesfull", loggedIn, is(true));
        // Verify login was successful
        assertThat("Should be redirected to account page after login",
                driver.getCurrentUrl(), containsString("my-account"));



        // Step 2: Navigate to shop and select product
        NavPagePOM navPagePOM = new NavPagePOM(driver);
        navPagePOM.navPageShop();
        navPagePOM.navPagePolo();

        navPagePOM.navAddCart();
        WebDriverWait myWait = new WebDriverWait(driver, Duration.ofSeconds(Helpers.DEFAULT_TIMEOUT)); //Willing to wait *up to* 10
        myWait.until(drv -> drv.findElement(By.linkText("View cart")).isDisplayed());
        navPagePOM.navPageBasket();
        assertThat("Should be redirected to basket",
                driver.getCurrentUrl(), containsString("https://www.edgewordstraining.co.uk/demo-site/cart/"));


//
//        myWait.until(drv -> drv.findElement(By.cssSelector(".cart-contents")).isDisplayed());
//
//        cartCheckout();
        // Step 3: Apply discount and verify calculations
        applyCupon();
        myWait.until(drv -> drv.findElement(By.cssSelector("tr.cart-discount.coupon-2idiscount td")).isDisplayed());
        WebElement discountAmount = driver.findElement(By.cssSelector("tr.cart-discount.coupon-2idiscount td"));
        String discountText = discountAmount.getText();
        System.out.println(discountText);

        WebElement totalElement = driver.findElement(By.cssSelector(".order-total "));
        String actualTotal = totalElement.getText();
        System.out.println(actualTotal);

    }

    // Handles the login process

    private void applyCupon(){

        WebElement cupon = driver.findElement(By.cssSelector("#coupon_code"));

        cupon.sendKeys(Helpers.TWO_I_DISCOUNT_COUPON);
        driver.findElement(By.name("apply_coupon")).click();


        WebElement shipping = driver.findElement(By.cssSelector(".shipping-calculator-button"));
        JavascriptExecutor jsv = (JavascriptExecutor) driver;
        jsv.executeScript("arguments[0].scrollIntoView({block:'center'});", shipping);
    }
}
