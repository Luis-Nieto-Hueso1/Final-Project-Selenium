package com._2itesting.tests.data;

import java.util.List;
import java.util.ArrayList;

// Utility class to provide test data for different scenarios
public class TestDataProvider {
// Test data for discount tests
    public static List<TestData> getDiscountTestData() {
        List<TestData> testDataList = new ArrayList<>();

        // Test Case 1: 25% discount with Polo shirt
        testDataList.add(new TestData("luis.hueso@.com", "luis.hueso", "Sunglasses", "2idiscount", "Luis", "Hueso", "Edgewords", "2itesting", "London", "Camden", "SE10 9LS", "07956987456", 25));

        // Test Case 2: 15% discount with different product
        testDataList.add(new TestData("luis.hueso@2.com", "luis.hueso", "Polo", "Edgewords", "John", "Doe", "Test Street", "Suite 100", "Manchester", "Greater Manchester", "M1 1AA", "07123456789", 15));

        return testDataList;
    }
// Test data for checkout tests
    public static List<TestData> getCheckoutTestData() {
        List<TestData> testDataList = new ArrayList<>();

        // Checkout Test Case 1
        testDataList.add(new TestData("luis.hueso@2.com", "luis.hueso", "Polo", "2idiscount", "Alice", "Smith", "123 Main St", "Apt 4B", "Birmingham", "West Midlands", "B1 1HQ", "07111222333", 25));

        // Checkout Test Case 2
        testDataList.add(new TestData("luis.hueso@2.com", "luis.hueso", "Sunglasses", "Edgewords", "Bob", "Johnson", "456 Oak Ave", "", "Leeds", "West Yorkshire", "LS1 1UR", "07444555666", 15));

        return testDataList;
    }
}
