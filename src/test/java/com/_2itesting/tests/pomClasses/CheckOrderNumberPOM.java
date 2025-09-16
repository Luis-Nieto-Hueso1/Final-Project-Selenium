package com._2itesting.tests.pomClasses;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CheckOrderNumberPOM    {
    WebDriver driver;
    public CheckOrderNumberPOM(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);

    }

   @FindBy(css = ".order > strong")
    public WebElement orderNumberField;
    @FindBy(linkText = "Orders")
    public WebElement ordersField;

    public void clickOrders() {
        ordersField.click();
    }

    public String getOrderNumber() {
        return orderNumberField.getText();
    }
}
