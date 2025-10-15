package com._2itesting.tests;

import com._2itesting.tests.Utils.*;
import com._2itesting.tests.basetest.BaseTest;
import com._2itesting.tests.data.TestData;
import com._2itesting.tests.data.TestDataProvider;
import com._2itesting.tests.models.*;
import com._2itesting.tests.steps.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DataDrivenCheckoutTest extends BaseTest {

    static Stream<TestData> checkoutTestData() {
        return TestDataProvider.getCheckoutTestData().stream();
    }

    @ParameterizedTest(name = "Checkout Test - {0}")
    @MethodSource("checkoutTestData")
    public void shouldCompleteCheckoutSuccessfully(TestData testData) {
        // GIVEN - Authenticated user with product ready for checkout
        UserCredentials user = new UserCredentials(
                testData.getUsername(),
                testData.getPassword()
        );
        ProductData product = new ProductData(testData.getProductName());
        BillingDetails billingDetails = new BillingDetails(
                testData.getFirstName(),
                testData.getLastName(),
                testData.getAddress(),
                testData.getAddress2(),
                testData.getCity(),
                testData.getState(),
                testData.getPostcode(),
                testData.getPhone()
        );

        LoginSteps loginSteps = new LoginSteps(driver, waiter);
        ShoppingSteps shoppingSteps = new ShoppingSteps(driver, waiter);
        CartSteps cartSteps = new CartSteps(driver, waiter);
        CheckoutSteps checkoutSteps = new CheckoutSteps(driver, waiter);
        OrderVerificationSteps orderSteps = new OrderVerificationSteps(driver);

        ReportUtils.logInputs(
                testData.getUsername(),
                testData.getPassword(),
                testData.getProductName(),
                testData.getCoupon()
        );

        loginSteps.loginAs(user);
        cartSteps.clearCart();

        // WHEN - User completes full checkout flow
        shoppingSteps.addProductToCart(product);
        cartSteps.navigateToCart();
        cartSteps.applyCoupon(testData.getCoupon());

        assertThat("Cart should have items", cartSteps.getItemCount(), greaterThan(0));
        assertThat("Discount should be applied", cartSteps.isDiscountApplied());

        cartSteps.proceedToCheckout();


        checkoutSteps.completeCheckoutWithCheque(billingDetails);

        // THEN - Order should be created and visible in order history
        OrderConfirmation order = orderSteps.captureOrderNumber();
        System.out.println("Order Number: " + order.orderNumber());

        orderSteps.navigateToMyOrders();
        assertThat("Should be on orders page",
                orderSteps.getCurrentUrl(),
                containsString("/my-account/orders/"));

        assertThat("Order should appear in order history",
                orderSteps.isOrderInHistory(order.orderNumber()),
                is(true));

        loginSteps.logout();
        System.out.println("=== Checkout Test Completed Successfully ===");
    }
}