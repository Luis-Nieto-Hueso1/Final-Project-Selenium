package com._2itesting.tests.steps;

import com._2itesting.tests.pomClasses.CheckOrderNumberPOM;
import com._2itesting.tests.pomClasses.NavPOM;
import org.openqa.selenium.WebDriver;

public class OrderVerificationSteps {
    private final NavPOM nav;
    private final CheckOrderNumberPOM orders;

    public OrderVerificationSteps(WebDriver driver) {
        this.nav = new NavPOM(driver);
        this.orders = new CheckOrderNumberPOM(driver);
    }

    public String captureOrderNumberOnConfirmation() {
        return orders.getOrderNumber();
    }

    public void openMyOrders() {
        nav.navMyAccount();
        orders.clickOrders();
    }
}
