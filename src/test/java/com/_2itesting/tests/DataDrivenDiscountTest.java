package com._2itesting.tests;

import com._2itesting.tests.Utils.*;
import com._2itesting.tests.basetest.BaseTest;
import com._2itesting.tests.data.TestData;
import com._2itesting.tests.data.TestDataProvider;
import com._2itesting.tests.models.*;
import com._2itesting.tests.steps.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DataDrivenDiscountTest extends BaseTest {

    static Stream<TestData> discountTestData() {
        return TestDataProvider.getDiscountTestData().stream();
    }

    @ParameterizedTest(name = "Discount Test - {0}")
    @MethodSource("discountTestData")
    public void shouldCalculateDiscountCorrectly(TestData testData) {
        // GIVEN - User logs in with test credentials
        UserCredentials user = new UserCredentials(
                testData.getUsername(),
                testData.getPassword()
        );
        ProductData product = new ProductData(testData.getProductName());
        // Initialize step classes
        LoginSteps loginSteps = new LoginSteps(driver, waiter);
        ShoppingSteps shoppingSteps = new ShoppingSteps(driver, waiter);
        CartSteps cartSteps = new CartSteps(driver, waiter);

        ReportUtils.logInputs(
                testData.getUsername(),
                testData.getPassword(),
                testData.getProductName(),
                testData.getCoupon()
        );

        loginSteps.loginAs(user);
        assertThat("Should be logged in",
                loginSteps.getCurrentUrl(),
                containsString("/my-account/")
        );
        cartSteps.clearCart();

        // WHEN - User purchases product with coupon
        shoppingSteps.addProductToCart(product);
        assertThat("Should be on product page",
                shoppingSteps.getCurrentUrl(),
                containsString("/product/" + product.name().toLowerCase()));

        shoppingSteps.goToCart();

        CartTotals before = cartSteps.getCartTotals();
        ReportUtils.logTotals("Totals BEFORE coupon",
                TotalsSnapshot.of(
                        before.subtotal().toString(),
                        "£0.00",
                        before.shipping().toString(),
                        before.total().toString()
                ));

        cartSteps.applyCoupon(testData.getCoupon());

        // THEN - Discount calculation should match expected percentage
        CartTotals after = cartSteps.getCartTotals();
        ReportUtils.logTotals("Totals AFTER coupon",
                TotalsSnapshot.of(
                        after.subtotal().toString(),
                        after.discount().toString(),
                        after.shipping().toString(),
                        after.total().toString()
                ));

        BigDecimal expectedDiscount = MoneyUtils.pct(
                after.subtotal(),
                testData.getExpectedDiscountPercent()
        );
        BigDecimal expectedTotal = MoneyUtils.round2(
                after.subtotal()
                        .subtract(expectedDiscount)
                        .add(after.shipping())
        );

        ReportUtils.logExpectation(expectedDiscount, expectedTotal);

        BigDecimal penny = new BigDecimal("0.01");
        BigDecimal discountDiff = after.discount().subtract(expectedDiscount).abs();

        assertThat(
                "Discount should be " + testData.getExpectedDiscountPercent() + "% of subtotal (±1p)",
                discountDiff.compareTo(penny) <= 0
        );
        assertThat("Discount should be applied", cartSteps.isDiscountApplied());

        loginSteps.logout();
    }
}