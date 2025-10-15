package com._2itesting.tests.pomClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class ShopPOM {
    private final WebDriver driver;

    public ShopPOM(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "h2.woocommerce-loop-product__title")
    private List<WebElement> productTitles;

    public void clickProductByName(String productName) {
        WebElement product = productTitles.stream()
                .filter(title -> title.getText().trim().equalsIgnoreCase(productName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + productName));
        product.click();
    }

    public boolean isProductDisplayed(String productName) {
        return productTitles.stream()
                .anyMatch(title -> title.getText().trim().equalsIgnoreCase(productName));
    }
}