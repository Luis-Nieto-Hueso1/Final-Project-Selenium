package com._2itesting.tests.pomClasses;

import com._2itesting.tests.Utils.Waiter;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;


// Page Object Model class for the checkout page
public class CheckoutPOM {
    private WebDriver driver;
    private final Waiter waiter;

    // Constructor
    public CheckoutPOM(WebDriver driver, Waiter waiter) {
        this.driver = driver;
        this.waiter = waiter;
        PageFactory.initElements(driver, this);
    }

    // === Locators ===
    @FindBy(id = "billing_first_name") private WebElement firstNameField;
    @FindBy(id = "billing_last_name") private WebElement lastNameField;
    @FindBy(id = "billing_address_1") private WebElement address1Field;
    @FindBy(id = "billing_address_2") private WebElement address2Field;
    @FindBy(id = "billing_city") private WebElement cityField;
    @FindBy(id = "billing_state") private WebElement countyField;
    @FindBy(id = "billing_postcode") private WebElement postcodeField;
    @FindBy(id = "billing_phone") private WebElement phoneField;
    @FindBy(id = "payment_method_cheque") private WebElement chequeRadio;
    @FindBy(css = "li.payment_method_cheque label") private WebElement chequeLabel;
    @FindBy(id = "place_order") private WebElement placeOrderButton;
    @FindBy(id = "payment_method_cheque") private WebElement chequeInput;


    /** Fills all visible billing details into the form. */
    public void fillBillingDetails(
            String firstName,
            String lastName,
            String address1,
            String address2,
            String city,
            String county,
            String postcode,
            String phone
    ) {
        clearAndType(firstNameField, firstName);
        clearAndType(lastNameField, lastName);
        clearAndType(address1Field, address1);
        clearAndType(address2Field, address2);
        clearAndType(cityField, city);
        clearAndType(countyField, county);
        clearAndType(postcodeField, postcode);
        clearAndType(phoneField, phone);
    }

    /** Helper to safely clear and type text into a field. */
    private void clearAndType(WebElement field, String value) {
        field.clear();
        field.sendKeys(value);
    }

    // Select cheque payment method
    // CheckoutPOM.java
    public void selectChequePayment() {
        waiter.clickable(org.openqa.selenium.By.cssSelector("li.payment_method_cheque label")); // wait until clickable

        if (chequeRadio.isSelected()) return; // already selected

        // Scroll into view
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", chequeLabel);

        // Try normal click, fall back to JS click
        try {
            chequeLabel.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", chequeLabel);
        }

        // Verify it's selected
        waiter.visible((By) chequeRadio);
    }


    public void placeOrder() {
        placeOrderButton.click();
    }
}
