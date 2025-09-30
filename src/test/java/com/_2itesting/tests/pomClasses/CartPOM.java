package com._2itesting.tests.pomClasses;

import com._2itesting.tests.Utils.Helpers;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
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
    // Do NOT cache lists via @FindBy. Use By locators and re-find every loop.
    private final By itemRemove = By.cssSelector("tr.cart_item a.remove");
    private final By couponRemove = By.cssSelector("a.woocommerce-remove-coupon");
    private final By wooOverlay = By.cssSelector(".blockUI, .blockOverlay"); // WooCommerce ajax overlay


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
        driver.switchTo().defaultContent();
        removeAll(By.cssSelector("a.woocommerce-remove-coupon")); // coupon remove links
        removeAll(By.cssSelector("a.remove"));                     // cart line-item remove links
    }

    private void removeAll(By clickLocator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        int guard = 100; // safety

        while (guard-- > 0) {
            List<WebElement> links = driver.findElements(clickLocator);
            if (links.isEmpty()) return;

            int before = links.size();
            WebElement link = wait.until(ExpectedConditions.elementToBeClickable(links.get(0)));
            link.click();

            // WooCommerce updates via AJAX. Wait for either the clicked element to go stale
            // or the number of matching links to drop. This avoids chasing parent rows.
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.stalenessOf(link),
                    ExpectedConditions.numberOfElementsToBeLessThan(clickLocator, before)
            ));
        }
    }

    }


