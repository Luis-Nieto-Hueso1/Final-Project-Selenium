package com._2itesting.tests.Utils;

import java.math.BigDecimal;

public record TotalsSnapshot(BigDecimal subtotal, BigDecimal discount, BigDecimal shipping, BigDecimal total) {
    public static TotalsSnapshot of(String subtotalText, String discountText, String shippingText, String totalText) {
        return new TotalsSnapshot(
                MoneyUtils.parse(subtotalText),
                MoneyUtils.parse(discountText).abs(),   // "-£x.xx" -> x.xx
                MoneyUtils.parse(shippingText),
                MoneyUtils.parse(totalText)
        );
    }

    @Override public String toString() {
        return """
               Subtotal: %s
               Discount: %s
               Shipping: %s
               Total:    %s
               """.formatted(MoneyUtils.fmt(subtotal), MoneyUtils.fmt(discount),
                MoneyUtils.fmt(shipping), MoneyUtils.fmt(total));
    }
}
