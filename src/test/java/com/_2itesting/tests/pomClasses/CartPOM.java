package com._2itesting.tests.pomClasses;

import com._2itesting.tests.Utils.Helpers;
import com._2itesting.tests.Utils.InstanceHelpers;
import net.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

public class CartPOM {
    private WebDriver driver;
    private final WebDriverWait wait;

    public CartPOM(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);


    }
    // Locators
    @FindBy(css = "tr.cart-discount td")
    private WebElement discountAmount;

    @FindBy(css = ".order-total ")
    private WebElement totalElement;

    // Cart lines
    @FindBy(css = ".cart-subtotal .woocommerce-Price-amount")
    private WebElement subtotalAmount;

    // This row is created after applying code "2idiscount"
    @FindBy(css = "tr.cart-discount td")
    private WebElement discountAmount1;

    // Shipping (first available rate amount)
    @FindBy(css = "tr.shipping .woocommerce-Price-amount")
    private WebElement shippingAmount;

    @FindBy(css = ".order-total .woocommerce-Price-amount")
    private WebElement orderTotalAmount;
    @FindBy(css = "a.woocommerce-remove-coupon")
    private List<WebElement> removeCouponSelector;
    @FindBy(css = "a.remove")
    private List<WebElement> removeClothes;

    @FindBy(id = "coupon_code")
    private WebElement coupon;

    @FindBy(name = "apply_coupon")
    private WebElement applyCouponButton;


    public void applyCoupon(String discount) {
        coupon.clear();              // optional: clears existing text
        coupon.sendKeys(discount);
    }

    public void clickApplyCoupon() {
        applyCouponButton.click();
    }

    // (Optional) combine into one convenience method
    public void applyDiscountCode(String code) {
        applyCoupon(code);
        clickApplyCoupon();
    }

    public Boolean isCouponAppliedMessageDisplayed() {
        String expectedMessage = "Coupon code applied successfully.";
        String actualMessage = Helpers.getNoticeMessage(driver);
        return actualMessage.contains(expectedMessage);
    }

    // Service Methods
    public String getDiscountAmount() {

        return discountAmount.getText();
    }

    public String getTotalAmount() {
        return totalElement.getText();


    }

    public String getSubtotalText() {

        return subtotalAmount.getText();
    }

    public String getDiscountText() {

        return discountAmount1.getText();
    }

    public String getShippingText() {

        return shippingAmount.getText();
    }

    public String getTotalText() {

        return orderTotalAmount.getText();
    }

    public void clearCart() {
        List<WebElement> removeCoupon = removeCouponSelector;
        for (WebElement btn : removeCoupon) {
            btn.click();
            // Optionally, wait a moment for the cart to update
            InstanceHelpers instanceHelpers = new InstanceHelpers(driver);
            instanceHelpers.waitForElementToBeClickableHelper(By.cssSelector("a.woocommerce-remove-coupon"), 5);
        }
        List<WebElement> removeButtons = removeClothes;
        for (WebElement btn : removeButtons) {
            btn.click();
            // Optionally, wait a moment for the cart to update
            InstanceHelpers instanceHelpers = new InstanceHelpers(driver);
            instanceHelpers.waitForElementToBeClickableHelper(By.cssSelector("a.remove"), 5);
        }
    }


}