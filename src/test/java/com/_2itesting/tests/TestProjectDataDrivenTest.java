package com._2itesting.tests;//  Updated Data-Driven Test Class


import com._2itesting.tests.Utils.*;
import com._2itesting.tests.basetest.BaseTest;
import com._2itesting.tests.data.TestData;
import com._2itesting.tests.data.TestDataProvider;
import com._2itesting.tests.pomClasses.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.*;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TestProjectDataDrivenTest extends BaseTest {

    // Method to provide test data for discount tests
    static Stream<TestData> discountTestData() {
        return TestDataProvider.getDiscountTestData().stream();
    }

    // Method to provide test data for checkout tests
    static Stream<TestData> checkoutTestData() {
        return TestDataProvider.getCheckoutTestData().stream();
    }

    @ParameterizedTest(name = "Discount Test - {0}")
    @MethodSource("discountTestData")
    public void testDiscountCalculation(TestData testData) {

        ReportUtils.logInputs(testData.getUsername(), testData.getPassword(), testData.getProductName(), testData.getCoupon());

        LoginPagePOM loginPagePOM = new LoginPagePOM(driver);
        CartPOM cart = new CartPOM(driver);
        NavPOM navPOM = new NavPOM(driver);

        boolean loggedIn = loginPagePOM.login(testData.getUsername(),  testData.getPassword());
        assertThat("login Successful", loggedIn, is(true));
        assertThat("Should be redirected to account page after login", driver.getCurrentUrl(), containsString("my-account"));

        navPOM.navPageBasket();
        cart.clearCart();


        System.out.println("=== Cart cleared successfully ===");

        System.out.println("=== Running Discount Test with: " + testData + " ===");


        waiter = new Waiter(driver, Duration.ofSeconds(10));
        InstanceHelpers instanceHelpers = new InstanceHelpers(driver);

        // Log test inputs
        ReportUtils.logInputs(testData.getUsername(), testData.getPassword(),
                testData.getProductName(), testData.getCoupon());

        // Navigate to shop and select product
        navPOM.navPageShop();

        // Dynamic product selection based on test data
        selectProductByName(testData.getProductName());


        assertThat("Should be on product page", driver.getCurrentUrl(),
                containsString("/product/" + testData.getProductName().toLowerCase()));
        // Add product to cart
        navPOM.navAddCart();
        System.out.println("=== Product added to basket ===");
        // Go to cart
        waiter.clickable(By.linkText("View cart"));
        navPOM.navPageBasket();
        assertThat("Should be redirected to basket", driver.getCurrentUrl(), containsString("/cart/"));

        // Capture totals before discount
        TotalsSnapshot before = TotalsSnapshot.of(cart.getSubtotalText(), "£0.00",
                cart.getShippingText(), cart.getTotalText());
        ReportUtils.logTotals("Totals BEFORE coupon", before);

        // Apply discount code from test data
        cart.applyDiscountCode(testData.getCoupon());
        System.out.println("=== Coupon applied: " + testData.getCoupon() + " ===");
        waiter.clickable(By.cssSelector("tr.cart-discount td"));



        // Capture totals after discount
        TotalsSnapshot after = TotalsSnapshot.of(cart.getSubtotalText(), cart.getDiscountText(),
                cart.getShippingText(), cart.getTotalText());
        ReportUtils.logTotals("Totals AFTER coupon", after);

        // Calculate expected values using test data
        var expectedDiscount = MoneyUtils.pct(after.subtotal(), testData.getExpectedDiscountPercent());
        var expectedTotal = MoneyUtils.round2(after.subtotal().subtract(expectedDiscount).add(after.shipping()));
        ReportUtils.logExpectation(expectedDiscount, expectedTotal);

        // Assertions
        var penny = new java.math.BigDecimal("0.01");
        var discountDiff = after.discount().subtract(expectedDiscount).abs();
        assertThat("Discount should be " + testData.getExpectedDiscountPercent() + "% of subtotal (±1p)",
                discountDiff.compareTo(penny) <= 0);

        instanceHelpers.dragDropHelper(driver.findElement(By.linkText("My account")), 1000, 1);
        WebElement link = driver.findElement(By.linkText("My account"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
//        link.click();
        navPOM.navMyAccount();


        waiter.clickable(By.linkText("Log out"));
        navPOM.navLogout();

        System.out.println("=== Discount Test Completed Successfully ===");
    }

    @ParameterizedTest(name = "Checkout Test - {0}")
    @MethodSource("checkoutTestData")
    public void testCheckoutProcess(TestData testData) {
        ReportUtils.logInputs(testData.getUsername(), testData.getPassword(), testData.getProductName(), testData.getCoupon());

        LoginPagePOM loginPagePOM = new LoginPagePOM(driver);
        CartPOM cart = new CartPOM(driver);
        NavPOM navPOM = new NavPOM(driver);

        boolean loggedIn = loginPagePOM.login(testData.getUsername(),  testData.getPassword());
        assertThat("login Successful", loggedIn, is(true));
        assertThat("Should be redirected to account page after login", driver.getCurrentUrl(), containsString("my-account"));

        navPOM.navPageBasket();
        cart.clearCart();


        System.out.println("=== Cart cleared successfully ===");


        System.out.println("=== Running Checkout Test with: " + testData + " ===");

        CheckoutPOM checkoutPOM = new CheckoutPOM(driver);

        CheckOrderNumberPOM checkOrderNumberPOM = new CheckOrderNumberPOM(driver);
        waiter = new Waiter(driver, Duration.ofSeconds(10));

        // Log test inputs
        ReportUtils.logInputs(testData.getUsername(), testData.getPassword(),
                testData.getProductName(), testData.getCoupon());

        // Navigate to shop and add product
        navPOM.navPageShop();
        selectProductByName(testData.getProductName());

        assertThat("Should be on product page", driver.getCurrentUrl(),
                containsString("/product/" + testData.getProductName().toLowerCase()));

        navPOM.navAddCart();
        System.out.println("=== Product added to basket ===");

        waiter.clickable(By.linkText("View cart"));
        navPOM.navPageBasket();
        assertThat("Should be redirected to basket", driver.getCurrentUrl(), containsString("/cart/"));

        // Apply discount
        cart.applyDiscountCode(testData.getCoupon());
        System.out.println("=== Applied coupon: " + testData.getCoupon() + " ===");

        // Navigate to checkout
        navPOM.navCheckout();
        assertThat("Should be redirected to checkout", driver.getCurrentUrl(), containsString("/checkout/"));

        // Fill billing details using test data
        checkoutPOM.fillBillingDetails(
                testData.getFirstName(), testData.getLastName(), testData.getAddress(),
                testData.getAddress2(), testData.getCity(), testData.getState(),
                testData.getPostcode(), testData.getPhone()
        );

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        checkoutPOM.selectChequeSimple();
        waiter.clickable(By.id("place_order"));
        checkoutPOM.placeOrder();

        // Capture order number
        waiter.clickable(By.cssSelector(".order > strong"));
        String orderNumber = checkOrderNumberPOM.getOrderNumber();
        System.out.println("Order Number: " + orderNumber);

        // Verify order in account
        navPOM.navMyAccount();
        checkOrderNumberPOM.clickOrders();
        assertThat("Should be redirected to orders page", driver.getCurrentUrl(),
                containsString("/my-account/orders/"));

        // Verify order appears in list
        List<WebElement> orderLinks = driver.findElements(
                By.cssSelector("table.woocommerce-orders-table tbody tr td.woocommerce-orders-table__cell-order-number a")
        );

        boolean found = orderLinks.stream()
                .map(WebElement::getText)
                .map(txt -> txt.replace("#", "").trim())
                .anyMatch(txt -> txt.equals(orderNumber));

        assertThat("The order should appear in My Account > Orders", found, is(true));

        navPOM.navMyAccount();
        navPOM.navLogout();

        System.out.println("=== Checkout Test Completed Successfully ===");
    }

    // Helper method to select product by name dynamically
    private void selectProductByName(String productName) {
        ShopPOM shopPOM = new ShopPOM(driver);
        shopPOM.selectProduct(productName);  // Uses the dynamic method
        }
}
