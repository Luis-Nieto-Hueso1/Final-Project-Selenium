package com._2itesting.tests.steps;

import com._2itesting.tests.Utils.Waiter;
import com._2itesting.tests.pomClasses.CartPOM;
import com._2itesting.tests.pomClasses.NavPOM;
import com._2itesting.tests.pomClasses.ShopPOM;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ShoppingSteps {
    private final NavPOM nav;
    private final ShopPOM shop;
    private final CartPOM cart;
    private final Waiter waiter;

    public ShoppingSteps(WebDriver driver, Waiter waiter) {
        this.waiter = waiter;
        this.nav = new NavPOM(driver);
        this.shop = new ShopPOM(driver);
        this.cart = new CartPOM(driver);
    }

    public void addProductToCart(String productName) {
        nav.navPageShop();
        shop.selectProduct(productName);
        nav.navAddCart();
        waiter.clickable(By.linkText("View cart")); // Wait for confirmation
    }

    public void applyCouponInCart(String couponCode) {
        nav.navPageBasket();
        cart.applyDiscountCode(couponCode);
        waiter.clickable(By.cssSelector("tr.cart-discount td")); // Wait for update
    }

    public void clearCart() {
        nav.navPageBasket();
        cart.clearCart();
    }
}