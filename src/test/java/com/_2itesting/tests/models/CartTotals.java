package com._2itesting.tests.models;

import java.math.BigDecimal;

public record CartTotals(
        BigDecimal subtotal,
        BigDecimal discount,
        BigDecimal shipping,
        BigDecimal total
) {}