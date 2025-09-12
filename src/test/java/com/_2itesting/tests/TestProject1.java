package com._2itesting.tests;

import com._2itesting.tests.Utils.Helpers;
import com._2itesting.tests.Utils.InstanceHelpers;
import com._2itesting.tests.basetest.BaseTest;
import com._2itesting.tests.pomClasses.ApplyDiscountPOM;
import com._2itesting.tests.pomClasses.DiscountCheckPOM;
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
        // Dismiss any store notice if present
        driver.findElement(By.cssSelector(".woocommerce-store-notice__dismiss-link")).click();
        LoginPagePOM loginPagePOM = new LoginPagePOM(driver);

        System.out.println("=== Enter Username ,Enter Password,Click Login ===");
        // Perform login using POM method
        boolean loggedIn = loginPagePOM.login(Helpers.USERNAME, Helpers.PASSWORD);
        // Verify login was successful
        assertThat("login Suscesfull", loggedIn, is(true));
        // Verify login was successful
        assertThat("Should be redirected to account page after login", driver.getCurrentUrl(), containsString("my-account"));


        // Step 2: Navigate to shop and select product
        NavPagePOM navPagePOM = new NavPagePOM(driver);

        navPagePOM.navPageShop();

        navPagePOM.navPagePolo();

        // Verify navigation to Polo product page
        assertThat("Should be on Polo shirt product page", driver.getCurrentUrl(), containsString("https://www.edgewordstraining.co.uk/demo-site/product/polo/"));
        // Add product to cart
        navPagePOM.navAddCart();

        System.out.println("=== Product added to basket ===");

        // Wait for "View cart" link to be visible and click it
        InstanceHelpers instanceHelpers = new InstanceHelpers(driver);

        instanceHelpers.waitForElementToBeClickableHelper(By.linkText("View cart"), 7);

        navPagePOM.navPageBasket();
        // Verify navigation to cart page
        assertThat("Should be redirected to basket", driver.getCurrentUrl(), containsString("https://www.edgewordstraining.co.uk/demo-site/cart/"));

        DiscountCheckPOM totals = new DiscountCheckPOM(driver);
        double subtotalBefore = parseMoney(totals.getSubtotalText());
        double totalBefore = parseMoney(totals.getTotalText());

        totals.getTotalAmount();

        // Step 3: Apply discount and verify calculations
        ApplyDiscountPOM applyDiscountPOM = new ApplyDiscountPOM(driver);
        applyDiscountPOM.applyDiscountCode(Helpers.TWO_I_DISCOUNT_COUPON);
//        assertThat("Coupon applied message should be displayed",
//                applyDiscountPOM.isCouponAppliedMessageDisplayed(), is(true));


        System.out.println("=== Cupon Applied ===");


        instanceHelpers.waitForElementToBeClickableHelper(By.cssSelector("tr.cart-discount.coupon-2idiscount td"), 7);

        // Step 4: Verify discount calculations
        totals.getDiscountAmount();


        WebElement discountAmount = driver.findElement(By.cssSelector("tr.cart-discount.coupon-2idiscount td"));
        String discountText = discountAmount.getText();
        System.out.println(discountText);

        WebElement totalElement = driver.findElement(By.cssSelector(".order-total "));
        String actualTotal = totalElement.getText();
        System.out.println(actualTotal);


        // 7) Extract and parse monetary values
        double subtotal = parseMoney(totals.getSubtotalText());              // should match subtotalBefore
        double discount = Math.abs(parseMoney(totals.getDiscountText()));    // "-£4.50" -> 4.50
        double shipping = parseMoney(totals.getShippingText());              // e.g. £3.95
        double total = parseMoney(totals.getTotalText());

        double expectedDiscount = round2(subtotal * 0.25);
        assertThat("Discount should be ~25% of subtotal", Math.abs(discount - expectedDiscount), lessThanOrEqualTo(0.01));

        // 8) Assert total arithmetic: subtotal - discount + shipping == total (to within 1p)
        double expectedTotal = round2(subtotal - discount + shipping);
        assertThat("Total equals subtotal - discount + shipping (±1p)", Math.abs(total - expectedTotal), lessThanOrEqualTo(0.01));

        // 9) Also prove total decreased compared to before
        assertThat("Total should decrease after coupon", total, lessThan(totalBefore));


    }

    private double parseMoney(String text) {
        // Keep digits, dot, comma, and minus; normalise comma to dot
        String cleaned = text.replaceAll("[^0-9.,-]", "").replace(",", ".");
        // If there are multiple numeric tokens, take the last
        String[] parts = cleaned.trim().split("\\s+");
        String candidate = parts[parts.length - 1];
        return Double.parseDouble(candidate);
    }

    private double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }


}
