package com._2itesting.tests.steps;

import com._2itesting.tests.Utils.Waiter;
import com._2itesting.tests.models.BillingDetails;
import com._2itesting.tests.pomClasses.CheckoutPOM;
import com._2itesting.tests.pomClasses.NavPOM;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com._2itesting.tests.Utils.*;
public class CheckoutSteps {

    private final NavPOM nav;
    private final CheckoutPOM checkout;
    private final Waiter waiter;

    // Pass the shared Waiter from BaseTest
    public CheckoutSteps(WebDriver driver, Waiter waiter) {
        this.nav = new NavPOM(driver);
        this.checkout = new CheckoutPOM(driver, waiter);
        this.waiter = waiter;
    }

    public void goToCheckout() { nav.navCheckout(); }

    public void fillBillingDetails(BillingDetails details) {
        checkout.fillBillingDetails(
                details.firstName(),
                details.lastName(),
                details.address1(),
                details.address2(),
                details.city(),
                details.county(),
                details.postcode(),
                details.phone()
        );
    }

    public void payByChequeAndPlaceOrder() {
        waiter.clickable(By.cssSelector("li.payment_method_cheque label"));
        checkout.selectChequePayment();
        waiter.clickable(By.id("place_order"));
        checkout.placeOrder();
    }
}
