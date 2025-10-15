package com._2itesting.tests.steps;

import com._2itesting.tests.Utils.Waiter;
import com._2itesting.tests.models.CartTotals;
import com._2itesting.tests.pomClasses.CartPOM;
import com._2itesting.tests.pomClasses.NavPOM;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartSteps {
    private final WebDriver driver;
    private final CartPOM cart;
    private final NavPOM nav;
    private final Waiter waiter;

    public CartSteps(WebDriver driver, Waiter waiter) {
        this.driver = driver;
        this.waiter = waiter;
        this.cart = new CartPOM(driver);
        this.nav = new NavPOM(driver);
    }

    public void navigateToCart() {
        nav.clickViewCart();
        waiter.urlContains("/cart/");
    }

    public void applyCoupon(String couponCode) {
        cart.enterCouponCode(couponCode);
        cart.clickApplyCoupon();
        waiter.clickable(By.cssSelector("tr.cart-discount td"));
    }

    public void clearCart() {
        navigateToCart();

        if (cart.isCartEmpty()) {
            return;
        }

        cart.clickRemoveAllCoupons();
        cart.clickRemoveAllItems();
    }

    public CartTotals getCartTotals() {
        return cart.getTotals();
    }

    public void proceedToCheckout() {
        nav.clickCheckout();
        waiter.urlContains("/checkout/");
    }

    public boolean isCartEmpty() {
        return cart.isCartEmpty();
    }

    public int getItemCount() {
        return cart.getItemCount();
    }

    public boolean isDiscountApplied() {
        return cart.isDiscountDisplayed();
    }
}
