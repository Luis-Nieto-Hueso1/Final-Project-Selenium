package com._2itesting.tests;

import com._2itesting.tests.Utils.*;
import com._2itesting.tests.basetest.BaseTest;
import com._2itesting.tests.models.BillingDetails;
import com._2itesting.tests.models.UserCredentials;
import com._2itesting.tests.pomClasses.*;
import com._2itesting.tests.steps.CheckoutSteps;
import com._2itesting.tests.steps.LoginSteps;
import com._2itesting.tests.steps.OrderVerificationSteps;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;


import java.time.Duration;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TestProjectSimpleTest extends BaseTest {


    @Test
    public void firstTest() {
        // Inputs: make them explicit and parameterizable later
        final String username = Helpers.USERNAME;
        final String productName = "Polo"; // replace with param later
        final String coupon = Helpers.TWO_I_DISCOUNT_COUPON;
        final String password = Helpers.PASSWORD;
        ReportUtils.logInputs(username, password, productName, coupon);
        CartPOM cart = new CartPOM(driver);
        NavPOM navPOM = new NavPOM(driver);

        UserCredentials user = new UserCredentials(username, password);
        LoginSteps loginSteps = new LoginSteps(driver);
        loginSteps.loginAs(user);

        navPOM.navPageBasket();
        cart.clearCart();

        System.out.println("=== Cart cleared successfully ===");
        // Step 1: Navigate to login page and authenticate

        System.out.println("=== Starting Test Setup ===");

        waiter = new Waiter(driver, Duration.ofSeconds(10));

        InstanceHelpers instanceHelpers = new InstanceHelpers(driver);
        // Step 2: Navigate to shop and select product
        navPOM.navPageShop();
        ShopPOM shopPOM = new ShopPOM(driver);

        shopPOM.selectProduct("Polo");

        navPOM.navAddCart();
        System.out.println("=== Product added to basket ===");

        waiter.clickable(By.linkText("View cart"));

        navPOM.navPageBasket();


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
        WebElement link = driver.findElement(By.linkText("My account"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
        navPOM.navMyAccount();


        // Verify navigation to account page
        waiter.clickable(By.linkText("Log out"));
        loginSteps.logout();
        System.out.println("=== Test Completed Successfully ===");
    }

    @Test
    public void secondTest() {

        // Inputs: make them explicit and parameterizable later
        final String username = Helpers.USERNAME;
        final String productName = "Polo"; // replace with param later
        final String coupon = Helpers.TWO_I_DISCOUNT_COUPON;
        final String password = Helpers.PASSWORD;
        ReportUtils.logInputs(username, password, productName, coupon);

        CartPOM cart = new CartPOM(driver);
        NavPOM navPOM = new NavPOM(driver);

        UserCredentials user = new UserCredentials(username, password);
        LoginSteps loginSteps = new LoginSteps(driver);
        loginSteps.loginAs(user);

        navPOM.navPageBasket();
        cart.clearCart();


        System.out.println("=== Cart cleared successfully ===");

        // Step 2: Navigate to shop and select product

        CheckoutPOM checkoutPOM = new CheckoutPOM(driver, waiter);
        waiter = new Waiter(driver, Duration.ofSeconds(10));
        CartPOM applyDiscountPOM = new CartPOM(driver);
        CheckOrderNumberPOM checkOrderNumberPOM = new CheckOrderNumberPOM(driver);

        navPOM.navPageShop();

        ShopPOM shopPOM = new ShopPOM(driver);
        shopPOM.selectProduct("Sunglasses");
        // Add product to cart
        navPOM.navAddCart();

        System.out.println("=== Product added to basket ===");

        waiter.clickable(By.linkText("View cart"));

        navPOM.navPageBasket();
        // Verify navigation to cart page
        assertThat("Should be redirected to basket", driver.getCurrentUrl(), containsString("https://www.edgewordstraining.co.uk/demo-site/cart/"));

        applyDiscountPOM.applyDiscountCode(Helpers.TWO_I_DISCOUNT_COUPON);

        navPOM.navCheckout();
        // Verify navigation to check out page
        assertThat("Should be redirected to checkout", driver.getCurrentUrl(), containsString("https://www.edgewordstraining.co.uk/demo-site/checkout/"));

        //Fill in the form
        BillingDetails details = new BillingDetails("Alice", "Smith", "123 Main St", "Apt4B2", "Birmingham", "West Midlands", "B1 1HQ", "07111222333");
        CheckoutSteps checkoutSteps = new CheckoutSteps(driver, waiter);
        checkoutSteps.fillBillingDetails(details);
        checkoutSteps.payByChequeAndPlaceOrder();

        OrderVerificationSteps orderVerificationSteps = new OrderVerificationSteps(driver);
        String orderNumber = orderVerificationSteps.captureOrderNumberOnConfirmation();

        System.out.println("Order Number: " + orderNumber);

        orderVerificationSteps.openMyOrders();
        // Verify navigation to orders page
        assertThat("Should be redirected to orders page", driver.getCurrentUrl(), containsString("https://www.edgewordstraining.co.uk/demo-site/my-account/orders/"));
        // Find all order number links in the orders table
        List<WebElement> orderLinks = driver.findElements(By.cssSelector("table.woocommerce-orders-table tbody tr td.woocommerce-orders-table__cell-order-number a"));

        // Check one of them matches the captured number (table shows “#12345”)
        boolean found = orderLinks.stream().map(WebElement::getText).map(txt -> txt.replace("#", "").trim()).anyMatch(txt -> txt.equals(orderNumber));

        assertThat("The same order should appear in My Account > Orders", found, is(true));
        loginSteps.logout();
        System.out.println("=== Test Completed Successfully ===");
    }


}
