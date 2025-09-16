package com._2itesting.tests.Utils;

import java.math.BigDecimal;

public final class ReportUtils {
    private ReportUtils() {}

    public static void logInputs(String username,String password, String productName, String coupon) {
        System.out.println("=== Test Inputs ===");
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        System.out.println("Product:  " + productName);
        System.out.println("Coupon:   " + coupon);
        System.out.println("===================");
    }

    public static void logTotals(String title, TotalsSnapshot t) {
        System.out.println("=== " + title + " ===");
        System.out.print(t.toString());
        System.out.println("======================");
    }

    public static void logExpectation(BigDecimal expectedDiscount, BigDecimal expectedTotal) {
        System.out.println("Expected Discount: " + MoneyUtils.fmt(expectedDiscount));
        System.out.println("Expected Total:    " + MoneyUtils.fmt(expectedTotal));
    }
}
