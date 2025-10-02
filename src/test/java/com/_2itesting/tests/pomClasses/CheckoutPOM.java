package com._2itesting.tests.pomClasses;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

// Page Object Model class for the checkout page
public class CheckoutPOM {
    private WebDriver driver;
// Constructor
    public CheckoutPOM(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // First name field
    @FindBy(id = "billing_first_name")
    public WebElement firstNameField;
    // Last name field
    @FindBy(id = "billing_last_name")
    public WebElement lastNameField;
    // Address line 1 field
    @FindBy(id = "billing_address_1")
    public WebElement addressField;
    // Address line 2 field
    @FindBy(id = "billing_address_2")
    public WebElement address2Field;
    // City field
    @FindBy(id = "billing_city")
    public WebElement cityField;
    // State field
    @FindBy(id = "billing_state")
    public WebElement stateField;
    // Postcode field
    @FindBy(id = "billing_postcode")
    public WebElement postcodeField;
    // Phone field
    @FindBy(id = "billing_phone")
    public WebElement phoneField;
    // Cheque payment method radio button
    @FindBy(id = "payment_method_cheque")
    public WebElement chequeField;
    // Place order button
    @FindBy(id = "place_order")
    public WebElement placeOrderField;

    // Fill in billing details
    public void fillBillingDetails(String firstName, String lastName, String address, String address2, String city, String state, String postcode, String phone) {
        firstNameField.clear();
        firstNameField.sendKeys(firstName);

        lastNameField.clear();
        lastNameField.sendKeys(lastName);

        addressField.clear();
        addressField.sendKeys(address);

        address2Field.clear();
        address2Field.sendKeys(address2);

        cityField.clear();
        cityField.sendKeys(city);

        stateField.clear();
        stateField.sendKeys(state);

        postcodeField.clear();
        postcodeField.sendKeys(postcode); // must be a **valid postcode** like "SE10 9LS"

        phoneField.clear();
        phoneField.sendKeys(phone);

    }

    // Select cheque payment method
    public void selectChequeSimple() {
        By chequeInput = By.id("payment_method_cheque");
        By chequeLabel = By.cssSelector("li.payment_method_cheque label");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(6));
        // Check if already selected
        WebElement radio = driver.findElement(chequeInput);
        if (radio.isSelected()) return; // already selected
        // Scroll to and click the label
        WebElement label = wait.until(ExpectedConditions.elementToBeClickable(chequeLabel));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'})", label);
        try {
            label.click(); // normal click
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", label); // simple fallback
        }
        // Wait until selected
        wait.until(ExpectedConditions.elementSelectionStateToBe(chequeInput, true));
    }

    public void placeOrder() {
        placeOrderField.click();
    }
}
