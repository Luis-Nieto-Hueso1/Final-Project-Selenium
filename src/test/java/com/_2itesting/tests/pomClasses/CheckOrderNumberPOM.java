package com._2itesting.tests.pomClasses;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CheckOrderNumberPOM {
    WebDriver driver;

    public CheckOrderNumberPOM(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);

    }

    // Find the order number element using CSS selector
    @FindBy(css = ".order > strong")
    public WebElement orderNumberField;
    // Find the "Orders" link using link text
    @FindBy(linkText = "Orders")
    public WebElement ordersField;

    // Click the "Orders" link
    public void clickOrders() {
        ordersField.click();
    }

    // Get the order number text
    public String getOrderNumber() {
        return orderNumberField.getText();
    }
}
