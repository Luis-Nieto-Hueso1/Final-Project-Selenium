package com._2itesting.tests.pomClasses;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

// Page Object Model class for the cart page
public class CartPOM {
    private WebDriver driver;
    private final WebDriverWait wait;

    // Constructor
    public CartPOM(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);


    }

    // Cart lines
    @FindBy(css = ".cart-subtotal .woocommerce-Price-amount")
    private WebElement subtotalAmount;
    // This row is created after applying code "2idiscount"
    @FindBy(css = "tr.cart-discount td")
    private WebElement discountAmount;
    // Shipping (first available rate amount)
    @FindBy(css = "tr.shipping .woocommerce-Price-amount")
    private WebElement shippingAmount;
    // Order Total
    @FindBy(css = ".order-total .woocommerce-Price-amount")
    private WebElement orderTotalAmount;
    // Coupon code field and button
    @FindBy(id = "coupon_code")
    private WebElement coupon;
    // Button to apply coupon
    @FindBy(name = "apply_coupon")
    private WebElement applyCouponButton;

    // Apply a coupon code by entering it in the field
    public void applyCoupon(String discount) {
        coupon.clear();              // optional: clears existing text
        coupon.sendKeys(discount);
    }

    // Click the "Apply coupon" button
    public void clickApplyCoupon() {
        applyCouponButton.click();
    }

    // Apply a discount code by entering it and clicking the button
    public void applyDiscountCode(String code) {
        applyCoupon(code);
        clickApplyCoupon();
    }

    // Subtotal text (right column, top row)
    public String getSubtotalText() {

        return subtotalAmount.getText();
    }

    // Discount text (right column, middle row)
    public String getDiscountText() {

        return discountAmount.getText();
    }

    // Shipping text (right column, middle row)
    public String getShippingText() {

        return shippingAmount.getText();
    }

    // Total text (right column, bottom row)
    public String getTotalText() {

        return orderTotalAmount.getText();
    }

    public void clearCart() {
        // Check if cart is already empty
        List<WebElement> cartItems = driver.findElements(By.cssSelector("a.remove"));
        if (cartItems.isEmpty()) {
            return; // Cart is already empty, nothing to do
        }

        // Remove all coupons first
        driver.findElements(By.cssSelector("a.woocommerce-remove-coupon"))
                .forEach(WebElement::click);


        // Remove all cart items
        driver.findElements(By.cssSelector("a.remove"))
                .forEach(WebElement::click);

    }
}


