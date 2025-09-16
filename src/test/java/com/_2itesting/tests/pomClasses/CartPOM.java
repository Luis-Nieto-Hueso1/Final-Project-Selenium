package com._2itesting.tests.pomClasses;

import com._2itesting.tests.Utils.Helpers;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CartPOM {
    private WebDriver driver;

    public CartPOM(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
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


    // Locators
    @FindBy(css = "tr.cart-discount.coupon-2idiscount td")
    private WebElement discountAmount;

    @FindBy(css = ".order-total ")
    private WebElement totalElement;

    // Service Methods
    public String getDiscountAmount() {
        return discountAmount.getText();
    }

    public String getTotalAmount() {
        return totalElement.getText();


    }

    // Cart lines
    @FindBy(css = ".cart-subtotal .woocommerce-Price-amount")
    private WebElement subtotalAmount;

    // This row is created after applying code "2idiscount"
    @FindBy(css = "tr.cart-discount.coupon-2idiscount .woocommerce-Price-amount")
    private WebElement discountAmount1;

    // Shipping (first available rate amount)
    @FindBy(css = "tr.shipping .woocommerce-Price-amount")
    private WebElement shippingAmount;

    @FindBy(css = ".order-total .woocommerce-Price-amount")
    private WebElement orderTotalAmount;

    public String getSubtotalText() { return subtotalAmount.getText(); }
    public String getDiscountText() { return discountAmount1.getText(); }
    public String getShippingText() { return shippingAmount.getText(); }
    public String getTotalText()    { return orderTotalAmount.getText(); }

    @FindBy(css = "a.woocommerce-remove-coupon")
    private WebElement removeCouponLink;
    @FindBy(css = "a.remove"  )
    private WebElement removeClothes;
    public void clickRemoveCoupon() {
        removeCouponLink.click();
    }
    public void clickRemoveClothes() {
        removeClothes.click();
    }




}