// Advanced Data-Driven Test Examples
package com._2itesting.tests.DinamicTest;

import com._2itesting.tests.Utils.*;
import com._2itesting.tests.basetest.BaseTest;
import com._2itesting.tests.data.CSVTestDataProvider;
import com._2itesting.tests.data.TestData;
import com._2itesting.tests.pomClasses.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.openqa.selenium.By;

import java.time.Duration;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DrivenTests extends BaseTest {

    // 1. Using CSV file as data source
    @ParameterizedTest(name = "CSV Test - {index}: {0}")
    @CsvFileSource(resources = "/test-data.csv", numLinesToSkip = 1)
    public void testWithCSVFile(String username, String password, String productName, String coupon,
                                String firstName, String lastName, String address, String address2,
                                String city, String state, String postcode, String phone, int expectedDiscountPercent) {

        TestData testData = new TestData(username, password, productName, coupon, firstName, lastName,
                address, address2, city, state, postcode, phone, expectedDiscountPercent);

        runDiscountTest(testData);
    }

    // 2. Using inline CSV data
    @ParameterizedTest(name = "Inline CSV Test - Coupon: {3}, Discount: {12}%")
    @CsvSource({
            "luis.hueso@2.com, luis.hueso, Polo, 2idiscount, Luis, Hueso, Edgewords, 2itesting, London, Camden, SE10 9LS, 07956987456, 25",
            "luis.hueso@2.com, luis.hueso, Polo, Edgewords, John, Doe, Test Street, Suite 100, Manchester, Greater Manchester, M1 1AA, 07123456789, 15"
    })
    public void testWithInlineCSV(String username, String password, String productName, String coupon,
                                  String firstName, String lastName, String address, String address2,
                                  String city, String state, String postcode, String phone, int expectedDiscountPercent) {

        TestData testData = new TestData(username, password, productName, coupon, firstName, lastName,
                address, address2, city, state, postcode, phone, expectedDiscountPercent);

        runDiscountTest(testData);
    }

    // 3. Using method source with external CSV loading
    static Stream<TestData> csvTestData() {
        return CSVTestDataProvider.loadTestDataFromCSV("src/test/resources/test-data.csv").stream();
    }

    @ParameterizedTest(name = "External CSV Test - {0}")
    @MethodSource("csvTestData")
    public void testWithExternalCSV(TestData testData) {
        runDiscountTest(testData);
    }

    // 4. Using ValueSource for simple data
    @ParameterizedTest(name = "Coupon Test - {0}")
    @ValueSource(strings = {"2idiscount", "Edgewords"})
    public void testDifferentCoupons(String couponCode) {
        TestData testData = new TestData(
                "luis.hueso@2.com", "luis.hueso", "Polo", couponCode,
                "Test", "User", "Test Address", "", "Test City", "Test State",
                "SE10 9LS", "07956987456",
                couponCode.equals("2idiscount") ? 25 : 15
        );

        runDiscountTest(testData);
    }

    // 5. Using EnumSource for predefined values
    enum DiscountType {
        TWO_I_DISCOUNT("2idiscount", 25),
        EDGEWORDS_DISCOUNT("Edgewords", 15);

        private final String code;
        private final int percent;

        DiscountType(String code, int percent) {
            this.code = code;
            this.percent = percent;
        }

        public String getCode() { return code; }
        public int getPercent() { return percent; }
    }

    @ParameterizedTest(name = "Enum Test - {0}")
    @EnumSource(DiscountType.class)
    public void testWithEnum(DiscountType discountType) {
        TestData testData = new TestData(
                "luis.hueso@2.com", "luis.hueso", "Polo", discountType.getCode(),
                "Enum", "Test", "Enum Address", "", "Enum City", "Enum State",
                "SE10 9LS", "07956987456", discountType.getPercent()
        );

        runDiscountTest(testData);
    }

    // 6. Combined parameters using ArgumentsSource
    static class CustomArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of("2idiscount", 25, "Luis", "Hueso"),
                    Arguments.of("Edgewords", 15, "John", "Doe"),
                    Arguments.of("2idiscount", 25, "Alice", "Smith")
            );
        }
    }

    @ParameterizedTest(name = "Custom Arguments - {0} with {2} {3}")
    @ArgumentsSource(CustomArgumentsProvider.class)
    public void testWithCustomArguments(String coupon, int discount, String firstName, String lastName) {
        TestData testData = new TestData(
                "luis.hueso@2.com", "luis.hueso", "Polo", coupon,
                firstName, lastName, "Test Address", "", "Test City", "Test State",
                "SE10 9LS", "07956987456", discount
        );

        runDiscountTest(testData);
    }

    // 7. Using multiple data sources in one test
    @ParameterizedTest(name = "Multi-Source Test - Product: {0}, Coupon: {1}")
    @CsvSource({
            "Polo, 2idiscount",
            "Polo, Edgewords"
    })
    public void testMultipleScenarios(String productName, String couponCode) {
        int expectedDiscount = couponCode.equals("2idiscount") ? 25 : 15;

        TestData testData = new TestData(
                "luis.hueso@2.com", "luis.hueso", productName, couponCode,
                "Multi", "Test", "Multi Address", "", "Multi City", "Multi State",
                "SE10 9LS", "07956987456", expectedDiscount
        );

        runDiscountTest(testData);
    }

    // Common test execution method
    private void runDiscountTest(TestData testData) {
        System.out.println("=== Running Test with: " + testData + " ===");

        CartPOM cart = new CartPOM(driver);
        NavPOM navPOM = new NavPOM(driver);
        waiter = new Waiter(driver, Duration.ofSeconds(10));

        try {
            // Navigate to shop and select product
            navPOM.navPageShop();
            selectProductByName(testData.getProductName());

            assertThat("Should be on product page", driver.getCurrentUrl(),
                    containsString("/product/" + testData.getProductName().toLowerCase()));

            navPOM.navAddCart();
            System.out.println("=== Product added to basket ===");

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

            System.out.println("=== Test Completed Successfully ===");

        } catch (Exception e) {
            throw e;
        }
    }



    // Helper method to select product by name dynamically
    private void selectProductByName(String productName) {
        switch (productName.toLowerCase()) {
            case "polo":
                ShopPOM shopPOM = new ShopPOM(driver);
                shopPOM.navPagePolo();
                break;
            // Add more products as needed
            case "hoodie":
                // Add hoodie selection logic
                break;
            case "jeans":
                // Add jeans selection logic
                break;
            default:
                throw new IllegalArgumentException("Product not supported: " + productName);
        }
    }
}


