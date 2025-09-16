package com._2itesting.tests.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

public final class MoneyUtils {
    private MoneyUtils() {}

    // Parse strings like "£33.95", "-£4.50", "Total: £12.00"
    public static BigDecimal parse(String text) {
        if (text == null) return BigDecimal.ZERO;
        String cleaned = text.replaceAll("[^0-9.,-]", "").replace(",", ".");
        if (cleaned.isBlank()) return BigDecimal.ZERO;
        // Last numeric token wins
        String[] parts = cleaned.trim().split("\\s+");
        String candidate = parts[parts.length - 1];
        return new BigDecimal(candidate).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal pct(BigDecimal base, int percent) {
        return base.multiply(BigDecimal.valueOf(percent)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    public static BigDecimal round2(BigDecimal v) {
        return v.setScale(2, RoundingMode.HALF_UP);
    }

    public static String fmt(BigDecimal v) {
        // Keep it simple and explicit for logs
        return String.format(Locale.UK, "£%s", v.toPlainString());
    }
}
