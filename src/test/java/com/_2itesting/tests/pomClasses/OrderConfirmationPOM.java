package com._2itesting.tests.pomClasses;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class OrderConfirmationPOM {
    private final WebDriver driver;

    public OrderConfirmationPOM(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = ".order > strong")
    private WebElement orderNumberElement;

    public String getOrderNumber() {
        return orderNumberElement.getText().trim();
    }

    public boolean isOrderConfirmationDisplayed() {
        try {
            return orderNumberElement.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}