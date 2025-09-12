package com._2itesting.tests.pomClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DiscountCheckPOM {
    private WebDriver driver;

    public DiscountCheckPOM(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
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

}