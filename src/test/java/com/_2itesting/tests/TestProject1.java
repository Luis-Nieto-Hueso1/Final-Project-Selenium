package com._2itesting.tests;

import com._2itesting.tests.Utils.*;
import com._2itesting.tests.basetest.BaseTest;
import com._2itesting.tests.pomClasses.*;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


import java.util.List;

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

        // Instantiate POM for login interactions
        LoginPagePOM loginPagePOM = new LoginPagePOM(driver);

        System.out.println("=== Enter Username ,Enter Password,Click Login ===");

        // Perform login using POM method

        boolean loggedIn = loginPagePOM.login(Helpers.USERNAME, Helpers.PASSWORD);

        // Verify login was successful
        assertThat("login Successful", loggedIn, is(true));

        // Verify login was successful
        assertThat("Should be redirected to account page after login", driver.getCurrentUrl(), containsString("my-account"));


        // Step 2: Navigate to shop and select product
        NavPOM navPOM = new NavPOM(driver);

        navPOM.navPageShop();

        navPOM.navPagePolo();

        // Verify navigation to Polo product page
        assertThat("Should be on Polo shirt product page", driver.getCurrentUrl(), containsString("https://www.edgewordstraining.co.uk/demo-site/product/polo/"));

        // Add product to cart
        navPOM.navAddCart();

        System.out.println("=== Product added to basket ===");

        // Wait for "View cart" link to be visible and click it
        InstanceHelpers instanceHelpers = new InstanceHelpers(driver);

        instanceHelpers.waitForElementToBeClickableHelper(By.linkText("View cart"), 7);

        navPOM.navPageBasket();
        // Verify navigation to cart page
        assertThat("Should be redirected to basket", driver.getCurrentUrl(), containsString("https://www.edgewordstraining.co.uk/demo-site/cart/"));
        // Capture subtotal and total before applying discount
        CartPOM totals = new CartPOM(driver);
        double subtotalBefore = parseMoney(totals.getSubtotalText());
        double totalBefore = parseMoney(totals.getTotalText());

        totals.getTotalAmount();

        // Step 3: Apply discount and verify calculations
//
        totals.applyDiscountCode(Helpers.TWO_I_DISCOUNT_COUPON);
        // Optional assertion for coupon toast; left commented intentionally by author
        // assertThat("Coupon applied message should be displayed",
        //         applyDiscountPOM.isCouponAppliedMessageDisplayed(), is(true));

        System.out.println("=== Cupon Applied ===");


        instanceHelpers.waitForElementToBeClickableHelper(By.cssSelector("tr.cart-discount.coupon-2idiscount td"), 7);

        // Step 4: Verify discount calculations
        totals.getDiscountAmount();

        // Verify discount row is present
        WebElement discountAmount = driver.findElement(By.cssSelector("tr.cart-discount.coupon-2idiscount td"));
        String discountText = discountAmount.getText();
        System.out.println(discountText);
        // Verify discount row is present
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
        // assertThat("Total should decrease after coupon", total, lessThan(totalBefore));
        instanceHelpers.dragDropHelper(driver.findElement(By.linkText("My account")), 100, 100);
        instanceHelpers.waitForElementToBeClickableHelper(By.linkText("My account"), 7);


        navPOM.navMyAccount();
        // Verify navigation to account page
        instanceHelpers.waitForElementToBeClickableHelper(By.linkText("Log out"), 7);
        navPOM.navLogout();

        System.out.println("=== Test Completed Successfully ===");

    }

    @Test
    public void secondTest() {

        // Step 1: Navigate to login page and authenticate
        //navigateShopAndLogin();
        System.out.println("=== Starting Test Setup ===");

        // Navigate directly to the My Account login page of the demo site
        driver.get(Helpers.LOGIN_URL);

        // Dismiss any store notice if present
        driver.findElement(By.cssSelector(".woocommerce-store-notice__dismiss-link")).click();

        // Instantiate POM for login interactions
        LoginPagePOM loginPagePOM = new LoginPagePOM(driver);

        System.out.println("=== Enter Username ,Enter Password,Click Login ===");

        // Perform login using POM method
        boolean loggedIn = loginPagePOM.login(Helpers.USERNAME, Helpers.PASSWORD);

        // Verify login was successful
        assertThat("login Successful", loggedIn, is(true));

        // Verify login was successful
        assertThat("Should be redirected to account page after login", driver.getCurrentUrl(), containsString("my-account"));


        // Step 2: Navigate to shop and select product
        NavPOM navPOM = new NavPOM(driver);

        navPOM.navPageShop();

        navPOM.navPagePolo();

        // Verify navigation to Polo product page
        assertThat("Should be on Polo shirt product page", driver.getCurrentUrl(), containsString("https://www.edgewordstraining.co.uk/demo-site/product/polo/"));

        // Add product to cart
        navPOM.navAddCart();

        System.out.println("=== Product added to basket ===");

        // Wait for "View cart" link to be visible and click it
        InstanceHelpers instanceHelpers = new InstanceHelpers(driver);

        instanceHelpers.waitForElementToBeClickableHelper(By.linkText("View cart"), 7);

        navPOM.navPageBasket();
        // Verify navigation to cart page
        assertThat("Should be redirected to basket", driver.getCurrentUrl(), containsString("https://www.edgewordstraining.co.uk/demo-site/cart/"));

        // Step 3: Apply discount and verify calculations
        CartPOM applyDiscountPOM = new CartPOM(driver);

        applyDiscountPOM.applyDiscountCode(Helpers.TWO_I_DISCOUNT_COUPON);

        navPOM.navCheckout();
        // Verify navigation to checkout page
        assertThat("Should be redirected to checkout", driver.getCurrentUrl(), containsString("https://www.edgewordstraining.co.uk/demo-site/checkout/"));

        //Fill in the form
        CheckoutPOM checkoutPOM = new CheckoutPOM(driver);
        checkoutPOM.fillBillingDetails("Luis", "Hueso", "Edgewords", "2itesting", "London", "camden", "SE10 9LS", " 07956987456");

        instanceHelpers.waitForElementToBeClickableHelper(By.id("place_order"), 7);
        checkoutPOM.placeOrder();

        CheckOrderNumberPOM checkOrderNumberPOM = new CheckOrderNumberPOM(driver);

        // Verify order confirmation page
        instanceHelpers.waitForElementToBeClickableHelper(By.cssSelector(".order > strong"), 7);

        String orderNumber = checkOrderNumberPOM.getOrderNumber();

        System.out.println("Order Number: " + orderNumber);

//        navPOM.navPageShop();


//        instanceHelpers.waitForElementToBeClickableHelper(By.linkText("View cart"), 7);

        navPOM.navMyAccount();
        checkOrderNumberPOM.clickOrders();
        // Verify navigation to orders page
        assertThat("Should be redirected to orders page", driver.getCurrentUrl(), containsString("https://www.edgewordstraining.co.uk/demo-site/my-account/orders/"));
        // Find all order number links in the orders table
        List<WebElement> orderLinks = driver.findElements(
                By.cssSelector("table.woocommerce-orders-table tbody tr td.woocommerce-orders-table__cell-order-number a")
        );

        // Check one of them matches the captured number (table shows “#12345”)
        boolean found = orderLinks.stream()
                .map(WebElement::getText)
                .map(txt -> txt.replace("#", "").trim())
                .anyMatch(txt -> txt.equals(orderNumber));

        assertThat("The same order should appear in My Account > Orders", found, is(true));
        navPOM.navMyAccount();

        navPOM.navLogout();


        System.out.println("=== Test Completed Successfully ===");
    }
    @Test
    public void TestProjectIMporved() {
        System.out.println("=== Starting Test Setup ===");
        driver.get(Helpers.LOGIN_URL);
        driver.findElement(By.cssSelector(".woocommerce-store-notice__dismiss-link")).click();

        // Inputs: make them explicit and parameterisable later
        final String username = Helpers.USERNAME;
        final String productName = "Polo"; // replace with param later
        final String coupon = Helpers.TWO_I_DISCOUNT_COUPON;
        final String password = Helpers.PASSWORD;
        ReportUtils.logInputs(username,password, productName, coupon);

        LoginPagePOM loginPagePOM = new LoginPagePOM(driver);
        boolean loggedIn = loginPagePOM.login(username, Helpers.PASSWORD);
        assertThat("login Successful", loggedIn, is(true));
        assertThat("Should be redirected to account page after login",
                driver.getCurrentUrl(), containsString("my-account"));

        NavPOM navPOM = new NavPOM(driver);

        navPOM.navPageBasket();
        List<WebElement> removeCoupon = driver.findElements(By.cssSelector("a.woocommerce-remove-coupon"));
        for (WebElement btn : removeCoupon) {
            btn.click();
            // Optionally, wait a moment for the cart to update
            InstanceHelpers instanceHelpers = new InstanceHelpers(driver);
            instanceHelpers.waitForElementToBeClickableHelper(By.cssSelector("a.woocommerce-remove-coupon"), 5);
        }
        List<WebElement> removeButtons = driver.findElements(By.cssSelector("a.remove"));
        for (WebElement btn : removeButtons) {
            btn.click();
            // Optionally, wait a moment for the cart to update
            InstanceHelpers instanceHelpers = new InstanceHelpers(driver);
            instanceHelpers.waitForElementToBeClickableHelper(By.cssSelector("a.remove"), 5);
        }

        InstanceHelpers instanceHelpers = new InstanceHelpers(driver);


        System.out.println("=== Cart cleared successfully ===");

        navPOM.navPageShop();
        navPOM.navPagePolo(); // later: navPOM.selectProductByName(productName)

        assertThat("Should be on Polo shirt product page",
                driver.getCurrentUrl(), containsString("/product/polo/"));

        navPOM.navAddCart();
        System.out.println("=== Product added to basket ===");

        instanceHelpers.waitForElementToBeClickableHelper(By.linkText("View cart"), 7);

        navPOM.navPageBasket();

        assertThat("Should be redirected to basket",
                driver.getCurrentUrl(), containsString("/cart/"));

        CartPOM cart = new CartPOM(driver);

        // BEFORE totals snapshot
        TotalsSnapshot before = TotalsSnapshot.of(
                cart.getSubtotalText(),
                "£0.00", // no discount yet
                cart.getShippingText(),
                cart.getTotalText()
        );
        ReportUtils.logTotals("Totals BEFORE coupon", before);

        // Apply coupon
        cart.applyDiscountCode(coupon);
        System.out.println("=== Coupon applied ===");
        instanceHelpers.waitForElementToBeClickableHelper(
                By.cssSelector("tr.cart-discount td"), 7);

        // AFTER totals snapshot
        TotalsSnapshot after = TotalsSnapshot.of(
                cart.getSubtotalText(),
                cart.getDiscountText(),
                cart.getShippingText(),
                cart.getTotalText()
        );
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
        assertThat("Discount should be 25% of subtotal (±1p)",
                discountDiff.compareTo(penny) <= 0);

        // Total arithmetic
        var totalDiff = after.total().subtract(expectedTotal).abs();


        // Total decreased vs BEFORE
//        assertThat("Total should decrease after coupon",
//                after.total().compareTo(before.total()) < 0);

        instanceHelpers.dragDropHelper(driver.findElement(By.linkText("My account")), 100, 100);

        instanceHelpers.waitForElementToBeClickableHelper(By.linkText("My account"), 7);


        navPOM.navMyAccount();
        // Verify navigation to account page
        instanceHelpers.waitForElementToBeClickableHelper(By.linkText("Log out"), 7);
        navPOM.navLogout();
        // Verify logout was successful by checking for login form
        WebElement loginForm = driver.findElement(By.id("customer_login"));
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
    @Test
    public void clearCart() {
        System.out.println("=== Starting Test Setup ===");
        driver.get(Helpers.LOGIN_URL);
        driver.findElement(By.cssSelector(".woocommerce-store-notice__dismiss-link")).click();

        // Inputs: make them explicit and parameterisable later
        final String username = Helpers.USERNAME;
        final String productName = "Polo"; // replace with param later
        final String coupon = Helpers.TWO_I_DISCOUNT_COUPON;
        final String password = Helpers.PASSWORD;
        ReportUtils.logInputs(username,password, productName, coupon);

        LoginPagePOM loginPagePOM = new LoginPagePOM(driver);
        boolean loggedIn = loginPagePOM.login(username, Helpers.PASSWORD);
        assertThat("login Successful", loggedIn, is(true));
        assertThat("Should be redirected to account page after login",
                driver.getCurrentUrl(), containsString("my-account"));

        NavPOM navPOM = new NavPOM(driver);

        navPOM.navPageBasket();
        List<WebElement> removeButtons = driver.findElements(By.cssSelector("a.remove"));
        for (WebElement btn : removeButtons) {
            btn.click();
            // Optionally, wait a moment for the cart to update
            InstanceHelpers instanceHelpers = new InstanceHelpers(driver);
            instanceHelpers.waitForElementToBeClickableHelper(By.cssSelector("a.remove"), 5);
        }
        WebElement emptyCartMsg = driver.findElement(By.className("cart-empty"));
        assertThat("Cart should be empty", emptyCartMsg.getText(), containsString("Your cart is currently empty."));
        System.out.println("=== Cart cleared successfully ===");

    }

}
