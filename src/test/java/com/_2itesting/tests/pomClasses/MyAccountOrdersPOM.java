package com._2itesting.tests.pomClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;
import java.util.stream.Collectors;

public class MyAccountOrdersPOM {
    private final WebDriver driver;

    public MyAccountOrdersPOM(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(linkText = "Orders")
    private WebElement ordersLink;

    public void clickOrdersTab() {
        ordersLink.click();
    }

    public List<String> getAllOrderNumbers() {
        List<WebElement> orderLinks = driver.findElements(
                By.cssSelector("table.woocommerce-orders-table tbody tr td.woocommerce-orders-table__cell-order-number a")
        );

        return orderLinks.stream()
                .map(WebElement::getText)
                .map(txt -> txt.replace("#", "").trim())
                .collect(Collectors.toList());
    }

    public boolean isOrderNumberDisplayed(String orderNumber) {
        return getAllOrderNumbers().contains(orderNumber);
    }
}