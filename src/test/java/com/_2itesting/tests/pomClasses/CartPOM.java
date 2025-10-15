package com._2itesting.tests.pomClasses;

import com._2itesting.tests.models.CartTotals;
import com._2itesting.tests.Utils.MoneyUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class CartPOM {
    private final WebDriver driver;

    public CartPOM(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = ".cart-subtotal .woocommerce-Price-amount")
    private WebElement subtotalAmount;

    @FindBy(css = "tr.cart-discount td")
    private WebElement discountAmount;

    @FindBy(css = "tr.shipping .woocommerce-Price-amount")
    private WebElement shippingAmount;

    @FindBy(css = ".order-total .woocommerce-Price-amount")
    private WebElement orderTotalAmount;

    @FindBy(id = "coupon_code")
    private WebElement couponField;

    @FindBy(name = "apply_coupon")
    private WebElement applyCouponButton;

    @FindBy(css = "a.remove")
    private List<WebElement> removeButtons;

    @FindBy(css = "a.woocommerce-remove-coupon")
    private List<WebElement> removeCouponButtons;

    // Single action methods
    public void enterCouponCode(String code) {
        couponField.clear();
        couponField.sendKeys(code);
    }

    public void clickApplyCoupon() {
        applyCouponButton.click();
    }

    public void clickRemoveItem(int index) {
        removeButtons.get(index).click();
    }

    public void clickRemoveAllCoupons() {
        removeCouponButtons.forEach(WebElement::click);
    }

    public void clickRemoveAllItems() {
        removeButtons.forEach(WebElement::click);
    }

    // Data retrieval - returns model object
    public CartTotals getTotals() {
        return new CartTotals(
                MoneyUtils.parse(subtotalAmount.getText()),
                isDiscountDisplayed() ? MoneyUtils.parse(discountAmount.getText()).abs() : MoneyUtils.parse("£0.00"),
                MoneyUtils.parse(shippingAmount.getText()),
                MoneyUtils.parse(orderTotalAmount.getText())
        );
    }

    public boolean isDiscountDisplayed() {
        try {
            return discountAmount.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCartEmpty() {
        return removeButtons.isEmpty();
    }

    public int getItemCount() {
        return removeButtons.size();
    }
}
