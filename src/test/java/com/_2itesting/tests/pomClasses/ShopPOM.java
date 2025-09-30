package com._2itesting.tests.pomClasses;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.WebDriver;

import java.util.List;


public class ShopPOM {
    private WebDriver driver;

    public ShopPOM(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);

    }
    @FindBy(css = "h2.woocommerce-loop-product__title")
    private List<WebElement> productTitles;
    public WebElement getProductByName(String productName) {
        return productTitles.stream()
                .filter(title -> title.getText().trim().equals(productName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(productName + " product not found"));
    }

    public void selectProduct(String productName) {
        getProductByName(productName).click();
    }
    @FindBy(css = "li.product:nth-child(9) > a:nth-child(1) > img:nth-child(1)")
    public WebElement poloField;

    public void navPagePolo() {
        poloField.click();
    }

}