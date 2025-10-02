package com._2itesting.tests.Utils;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;

/**
 * Configuration class to manage test data and URLs
 * Eliminates hard-coding of values in test methods
 */
public class Helpers {

    // Base URLs
    public static final String BASE_URL = "https://www.edgewordstraining.co.uk/demo-site";
    public static final String LOGIN_URL = BASE_URL + "/my-account/";
    public static final String SHOP_URL = BASE_URL + "/shop/";
    public static final String CART_URL = BASE_URL + "/cart/";
    public static final String CHECKOUT_URL = BASE_URL + "/checkout/";
    public static final String ACCOUNT_URL = BASE_URL + "/my-account/";

    // Test credentials
    public static final String USERNAME = "luis.hueso@2.com";
    public static final String PASSWORD = "luis.hueso";

    // Discount codes
    public static final String TWO_I_DISCOUNT_COUPON = "2idiscount";

}
