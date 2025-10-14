package com._2itesting.tests.DinamicTest;


import com._2itesting.tests.Utils.*;
import com._2itesting.tests.basetest.BaseTest;
import com._2itesting.tests.data.TestData;
import com._2itesting.tests.data.TestDataProvider;
import com._2itesting.tests.models.BillingDetails;
import com._2itesting.tests.models.UserCredentials;
import com._2itesting.tests.pomClasses.*;
import com._2itesting.tests.steps.CheckoutSteps;
import com._2itesting.tests.steps.LoginSteps;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class DynamicDataDrivenTests extends BaseTest {
    // Helper methods to run the actual test logic

    @TestFactory
    Collection<DynamicTest> dynamicDiscountTests() {
        return TestDataProvider.getDiscountTestData().stream()
                .map(testData -> DynamicTest.dynamicTest(
                        "Dynamic Test: " + testData.toString(),
                        () -> runDiscountTest(testData)
                ))
                .toList();
    }

    @TestFactory
    Stream<DynamicTest> dynamicCheckoutTests() {
        return TestDataProvider.getCheckoutTestData().stream()
                .map(testData -> DynamicTest.dynamicTest(
                        "Dynamic Checkout: " + testData.getCoupon() + " with " + testData.getFirstName(),
                        () -> runCheckoutTest(testData)
                ));
    }

    private void runDiscountTest(TestData testData) {
        // Step 1: Navigate to login page and authenticate
        final String coupon = Helpers.TWO_I_DISCOUNT_COUPON;
        System.out.println("=== Starting Test Setup ===");
        CartPOM cart = new CartPOM(driver);
        NavPOM navPOM = new NavPOM(driver);
        ShopPOM shopPOM = new ShopPOM(driver);

        waiter = new Waiter(driver, Duration.ofSeconds(10));

        InstanceHelpers instanceHelpers = new InstanceHelpers(driver);
        // Step 2: Navigate to shop and select product
        navPOM.navPageShop();
        shopPOM.navPagePolo(); // later: navPOM.selectProductByName(productName)

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

    private void runCheckoutTest(TestData testData) {


        ReportUtils.logInputs(testData.getUsername(), testData.getPassword(), testData.getProductName(), testData.getCoupon());

        CartPOM cart = new CartPOM(driver);
        NavPOM navPOM = new NavPOM(driver);
        UserCredentials user = new UserCredentials(testData.getUsername(), testData.getPassword());
        LoginSteps loginSteps = new LoginSteps(driver);
        loginSteps.loginAs(user);

        navPOM.navPageBasket();
        cart.clearCart();


        System.out.println("=== Cart cleared successfully ===");

        System.out.println("=== Running Discount Test with: " + testData + " ===");
        // Step 2: Navigate to shop and select product

        CheckoutPOM checkoutPOM = new CheckoutPOM(driver, waiter);

        waiter = new Waiter(driver, Duration.ofSeconds(10));
        CartPOM applyDiscountPOM = new CartPOM(driver);
        CheckOrderNumberPOM checkOrderNumberPOM = new CheckOrderNumberPOM(driver);
        ShopPOM shopPOM = new ShopPOM(driver);

        navPOM.navPageShop();
        shopPOM.navPagePolo();

        // Verify navigation to Polo product page
        assertThat("Should be on Polo shirt product page", driver.getCurrentUrl(), containsString("https://www.edgewordstraining.co.uk/demo-site/product/polo/"));
        // Add product to cart
        navPOM.navAddCart();

        System.out.println("=== Product added to basket ===");
        // Wait for "View cart" link to be visible and click it

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


        BillingDetails details = new BillingDetails("Alice","Smith","123 Main St","Apt4B2","Birmingham","West Midlands","B1 1HQ","07111222333");
        CheckoutSteps checkoutSteps = new CheckoutSteps(driver,waiter);
        checkoutSteps.fillBillingDetails(details);


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
}
