package com._2itesting.tests.steps;

import com._2itesting.tests.Utils.Waiter;
import com._2itesting.tests.models.BillingDetails;
import com._2itesting.tests.pomClasses.CheckoutPOM;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutSteps {
    private final WebDriver driver;
    private final CheckoutPOM checkout;
    private final Waiter waiter;

    public CheckoutSteps(WebDriver driver, Waiter waiter) {
        this.driver = driver;
        this.waiter = waiter;
        this.checkout = new CheckoutPOM(driver, waiter);
    }

    public void fillBillingDetails(BillingDetails details) {
        checkout.enterFirstName(details.firstName());
        checkout.enterLastName(details.lastName());
        checkout.enterAddress1(details.address1());
        checkout.enterAddress2(details.address2());
        checkout.enterCity(details.city());
        checkout.enterCounty(details.county());
        checkout.enterPostcode(details.postcode());
        checkout.enterPhone(details.phone());
    }

    public void selectChequePayment() {
        waiter.clickable(By.cssSelector("li.payment_method_cheque label"));
        checkout.selectChequePayment();
    }

    public void placeOrder() {
        waiter.clickable(By.id("place_order"));
        checkout.clickPlaceOrder();
        waiter.clickable(By.cssSelector(".order > strong"));
    }

    public void completeCheckoutWithCheque(BillingDetails details) {
        fillBillingDetails(details);
        selectChequePayment();
        placeOrder();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
