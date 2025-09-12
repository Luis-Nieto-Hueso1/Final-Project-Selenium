package com._2itesting.tests.Utils;

/**
 * Configuration class to manage test data and URLs
 * Eliminates hard-coding of values in test methods
 */
public class Helpers {

    // Base URLs
    public static final String BASE_URL = "https://www.edgewordstraining.co.uk/demo-site";
    public static final String LOGIN_URL = BASE_URL + "/my-account/";

    // Test credentials
    public static final String USERNAME = "luis.hueso@2.com";
    public static final String PASSWORD = "luis.hueso";

    // Discount codes
    public static final String EDGEWORDS_COUPON = "Edgewords";
    public static final String TWO_I_DISCOUNT_COUPON = "2idiscount";

    // Expected discount percentages
    public static final double EDGEWORDS_DISCOUNT = 0.15; // 15%
    public static final double TWO_I_DISCOUNT = 0.25; // 25%

    // Timeouts
    public static final int DEFAULT_TIMEOUT = 10;
    public static final int PAGE_LOAD_TIMEOUT = 30;
}
