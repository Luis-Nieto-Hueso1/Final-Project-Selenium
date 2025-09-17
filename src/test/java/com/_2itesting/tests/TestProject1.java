package com._2itesting.tests;

import com._2itesting.tests.Utils.*;
import com._2itesting.tests.basetest.BaseTest;
import com._2itesting.tests.pomClasses.*;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


import java.time.Duration;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TestProject1 extends BaseTest {


    @Test
    public void firstTest() {
        // Step 1: Navigate to login page and authenticate
        final String coupon = Helpers.TWO_I_DISCOUNT_COUPON;
        System.out.println("=== Starting Test Setup ===");
        CartPOM cart = new CartPOM(driver);
        NavPOM navPOM = new NavPOM(driver);
        waiter = new Waiter(driver, Duration.ofSeconds(10));

        InstanceHelpers instanceHelpers = new InstanceHelpers(driver);
        // Step 2: Navigate to shop and select product
        navPOM.navPageShop();
        navPOM.navPagePolo(); // later: navPOM.selectProductByName(productName)

        assertThat("Should be on Polo shirt product page", driver.getCurrentUrl(), containsString("/product/polo/"));

        navPOM.navAddCart();
        System.out.println("=== Product added to basket ===");

        waiter.clickable(By.linkText("View cart"));

        navPOM.navPageBasket();

        assertThat("Should be redirected to basket", driver.getCurrentUrl(), containsString("/cart/"));


        // BEFORE totals snapshot
        TotalsSnapshot before = TotalsSnapshot.of(cart.getSubtotalText(), "£0.00", // no discount yet
                cart.getShippingText(), cart.getTotalText());
        ReportUtils.logTotals("Totals BEFORE coupon", before);

        // Apply coupon
        cart.applyDiscountCode(coupon);
        System.out.println("=== Coupon applied ===");
        waiter.clickable(By.cssSelector("tr.cart-discount td"));

        // AFTER totals snapshot
        TotalsSnapshot after = TotalsSnapshot.of(cart.getSubtotalText(), cart.getDiscountText(), cart.getShippingText(), cart.getTotalText());
        ReportUtils.logTotals("Totals AFTER coupon", after);

        // Expectations — make them explicit in the log
        // If the coupon is 25% off, compute expected values
        var expectedDiscount = MoneyUtils.pct(after.subtotal(), 25);
        var expectedTotal = MoneyUtils.round2(after.subtotal().subtract(expectedDiscount).add(after.shipping()));
        ReportUtils.logExpectation(expectedDiscount, expectedTotal);

        // Assertions with 1p tolerance (BigDecimal compare)
        var penny = new java.math.BigDecimal("0.01");

        // Discount ≈ 25% of subtotal
        var discountDiff = after.discount().subtract(expectedDiscount).abs();
        assertThat("Discount should be 25% of subtotal (±1p)", discountDiff.compareTo(penny) <= 0);


        instanceHelpers.dragDropHelper(driver.findElement(By.linkText("My account")), 1000, 1);

        waiter.clickable(By.linkText("My account"));
//        navPOM.navMyAccount();

        driver.get(Helpers.ACCOUNT_URL);
//
        // Verify navigation to account page
        waiter.clickable(By.linkText("Log out"));
        navPOM.navLogout();

        System.out.println("=== Test Completed Successfully ===");
    }

    @Test
    public void secondTest() {

        // Step 2: Navigate to shop and select product
        NavPOM navPOM = new NavPOM(driver);
        CheckoutPOM checkoutPOM = new CheckoutPOM(driver);
        waiter = new Waiter(driver, Duration.ofSeconds(10));
        CartPOM applyDiscountPOM = new CartPOM(driver);
        CheckOrderNumberPOM checkOrderNumberPOM = new CheckOrderNumberPOM(driver);

        navPOM.navPageShop();
        navPOM.navPagePolo();

        // Verify navigation to Polo product page
        assertThat("Should be on Polo shirt product page", driver.getCurrentUrl(), containsString("https://www.edgewordstraining.co.uk/demo-site/product/polo/"));
        // Add product to cart
        navPOM.navAddCart();

        System.out.println("=== Product added to basket ===");
        // Wait for "View cart" link to be visible and click it
        InstanceHelpers instanceHelpers = new InstanceHelpers(driver);

        waiter.clickable(By.linkText("View cart"));

        navPOM.navPageBasket();
        // Verify navigation to cart page
        assertThat("Should be redirected to basket", driver.getCurrentUrl(), containsString("https://www.edgewordstraining.co.uk/demo-site/cart/"));
        // Step 3: Apply discount and verify calculations


        applyDiscountPOM.applyDiscountCode(Helpers.TWO_I_DISCOUNT_COUPON);

        navPOM.navCheckout();
        // Verify navigation to check out page
        assertThat("Should be redirected to checkout", driver.getCurrentUrl(), containsString("https://www.edgewordstraining.co.uk/demo-site/checkout/"));

        //Fill in the form

        checkoutPOM.fillBillingDetails("Luis", "Hueso", "Edgewords", "2itesting", "London", "camden", "SE10 9LS", " 07956987456");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        checkoutPOM.selectChequeSimple();

        waiter.clickable(By.id("place_order"));

        checkoutPOM.placeOrder();

        // Verify order confirmation page
        waiter.clickable(By.cssSelector(".order > strong"));

        String orderNumber = checkOrderNumberPOM.getOrderNumber();

        System.out.println("Order Number: " + orderNumber);

        navPOM.navMyAccount();
        checkOrderNumberPOM.clickOrders();
        // Verify navigation to orders page
        assertThat("Should be redirected to orders page", driver.getCurrentUrl(), containsString("https://www.edgewordstraining.co.uk/demo-site/my-account/orders/"));
        // Find all order number links in the orders table
        List<WebElement> orderLinks = driver.findElements(By.cssSelector("table.woocommerce-orders-table tbody tr td.woocommerce-orders-table__cell-order-number a"));

        // Check one of them matches the captured number (table shows “#12345”)
        boolean found = orderLinks.stream().map(WebElement::getText).map(txt -> txt.replace("#", "").trim()).anyMatch(txt -> txt.equals(orderNumber));

        assertThat("The same order should appear in My Account > Orders", found, is(true));
        navPOM.navMyAccount();

        navPOM.navLogout();
        System.out.println("=== Test Completed Successfully ===");
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


//@Test
//    public void secondTest1() {
//
//        System.out.println("=== Starting Test Setup ===");
//        driver.get(Helpers.LOGIN_URL);
//        driver.findElement(By.cssSelector(".woocommerce-store-notice__dismiss-link")).click();
//
//        // Inputs: make them explicit and parameterisable later
//        final String username = Helpers.USERNAME;
//        final String productName = "Polo"; // replace with param later
//        final String coupon = Helpers.TWO_I_DISCOUNT_COUPON;
//        final String password = Helpers.PASSWORD;
//        ReportUtils.logInputs(username, password, productName, coupon);
//
//        LoginPagePOM loginPagePOM = new LoginPagePOM(driver);
//        CartPOM cart = new CartPOM(driver);
//        NavPOM navPOM = new NavPOM(driver);
//
//        boolean loggedIn = loginPagePOM.login(username, Helpers.PASSWORD);
//        assertThat("login Successful", loggedIn, is(true));
//        assertThat("Should be redirected to account page after login", driver.getCurrentUrl(), containsString("my-account"));
//
//        navPOM.navPageBasket();
//        cart.clearCart();
//
//        InstanceHelpers instanceHelpers = new InstanceHelpers(driver);
//
//        System.out.println("=== Cart cleared successfully ===");
//
//        // Step 2: Navigate to shop and select product
//
//
//        navPOM.navPageShop();
//
//        navPOM.navPagePolo();
//
//        // Verify navigation to Polo product page
//        assertThat("Should be on Polo shirt product page", driver.getCurrentUrl(), containsString("https://www.edgewordstraining.co.uk/demo-site/product/polo/"));
//
//        // Add product to cart
//        navPOM.navAddCart();
//
//        System.out.println("=== Product added to basket ===");
//
//        // Wait for "View cart" link to be visible and click it
//
//
//        instanceHelpers.waitForElementToBeClickableHelper(By.linkText("View cart"), 7);
//
//        navPOM.navPageBasket();
//        // Verify navigation to cart page
//        assertThat("Should be redirected to basket", driver.getCurrentUrl(), containsString("https://www.edgewordstraining.co.uk/demo-site/cart/"));
//
//        // Step 3: Apply discount and verify calculations
//        CartPOM applyDiscountPOM = new CartPOM(driver);
//
//        applyDiscountPOM.applyDiscountCode(Helpers.TWO_I_DISCOUNT_COUPON);
//
//        navPOM.navCheckout();
//        // Verify navigation to checkout page
//        assertThat("Should be redirected to checkout", driver.getCurrentUrl(), containsString("https://www.edgewordstraining.co.uk/demo-site/checkout/"));
//
//        //Fill in the form
//        CheckoutPOM checkoutPOM = new CheckoutPOM(driver);
//        checkoutPOM.fillBillingDetails("Luis", "Hueso", "Edgewords", "2itesting", "London", "camden", "SE10 9LS", " 07956987456");
//
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//
//        checkoutPOM.selectChequeSimple();
//
//        instanceHelpers.waitForElementToBeClickableHelper(By.id("place_order"), 7);
//
//        checkoutPOM.placeOrder();
//
//        CheckOrderNumberPOM checkOrderNumberPOM = new CheckOrderNumberPOM(driver);
//
//        // Verify order confirmation page
//        instanceHelpers.waitForElementToBeClickableHelper(By.cssSelector(".order > strong"), 7);
//
//        String orderNumber = checkOrderNumberPOM.getOrderNumber();
//
//        System.out.println("Order Number: " + orderNumber);
//
////        navPOM.navPageShop();
//
//
////        instanceHelpers.waitForElementToBeClickableHelper(By.linkText("View cart"), 7);
//
//        navPOM.navMyAccount();
//        checkOrderNumberPOM.clickOrders();
//        // Verify navigation to orders page
//        assertThat("Should be redirected to orders page", driver.getCurrentUrl(), containsString("https://www.edgewordstraining.co.uk/demo-site/my-account/orders/"));
//        // Find all order number links in the orders table
//        List<WebElement> orderLinks = driver.findElements(By.cssSelector("table.woocommerce-orders-table tbody tr td.woocommerce-orders-table__cell-order-number a"));
//
//        // Check one of them matches the captured number (table shows “#12345”)
//        boolean found = orderLinks.stream().map(WebElement::getText).map(txt -> txt.replace("#", "").trim()).anyMatch(txt -> txt.equals(orderNumber));
//
//        assertThat("The same order should appear in My Account > Orders", found, is(true));
//        navPOM.navMyAccount();
//
//        navPOM.navLogout();
//
//
//        System.out.println("=== Test Completed Successfully ===");
//    }


}
