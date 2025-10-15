package com._2itesting.tests.steps;

import com._2itesting.tests.Utils.Waiter;
import com._2itesting.tests.models.ProductData;
import com._2itesting.tests.pomClasses.NavPOM;
import com._2itesting.tests.pomClasses.ShopPOM;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ShoppingSteps {
    private final WebDriver driver;
    private final NavPOM nav;
    private final ShopPOM shop;
    private final Waiter waiter;

    public ShoppingSteps(WebDriver driver, Waiter waiter) {
        this.driver = driver;
        this.waiter = waiter;
        this.nav = new NavPOM(driver);
        this.shop = new ShopPOM(driver);
    }

    public void browseToShop() {
        nav.clickShop();
    }

    public void selectProduct(ProductData product) {
        shop.clickProductByName(product.name());
    }

    public void addCurrentProductToCart() {
        nav.clickAddToCart();
        waiter.clickable(By.linkText("View cart"));
    }

    public void addProductToCart(ProductData product) {
        browseToShop();
        selectProduct(product);
        addCurrentProductToCart();
    }

    public void viewCart() {
        nav.clickViewCart();
    }

    public void goToCart() {
        nav.clickViewCart();
        waiter.urlContains("/cart/");
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public boolean isProductAvailable(String productName) {
        return shop.isProductDisplayed(productName);
    }
}