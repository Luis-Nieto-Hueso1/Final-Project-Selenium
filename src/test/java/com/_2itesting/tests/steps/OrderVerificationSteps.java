package com._2itesting.tests.steps;

import com._2itesting.tests.models.OrderConfirmation;
import com._2itesting.tests.pomClasses.MyAccountOrdersPOM;
import com._2itesting.tests.pomClasses.NavPOM;
import com._2itesting.tests.pomClasses.OrderConfirmationPOM;
import org.openqa.selenium.WebDriver;

public class OrderVerificationSteps {
    private final WebDriver driver;
    private final OrderConfirmationPOM orderConfirmation;
    private final MyAccountOrdersPOM myAccountOrders;
    private final NavPOM nav;

    public OrderVerificationSteps(WebDriver driver) {
        this.driver = driver;
        this.orderConfirmation = new OrderConfirmationPOM(driver);
        this.myAccountOrders = new MyAccountOrdersPOM(driver);
        this.nav = new NavPOM(driver);
    }

    public OrderConfirmation captureOrderNumber() {
        String orderNumber = orderConfirmation.getOrderNumber();
        return new OrderConfirmation(orderNumber);
    }

    public void navigateToMyOrders() {
        nav.clickMyAccount();
        myAccountOrders.clickOrdersTab();
    }

    public boolean isOrderInHistory(String orderNumber) {
        return myAccountOrders.isOrderNumberDisplayed(orderNumber);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}