package com._2itesting.tests.pomClasses;

import com._2itesting.tests.Utils.Waiter;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CheckoutPOM {
    private final WebDriver driver;
    private final Waiter waiter;

    public CheckoutPOM(WebDriver driver, Waiter waiter) {
        this.driver = driver;
        this.waiter = waiter;
        PageFactory.initElements(driver, this);
    }

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

    // Single action methods
    public void enterFirstName(String firstName) {
        firstNameField.clear();
        firstNameField.sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        lastNameField.clear();
        lastNameField.sendKeys(lastName);
    }

    public void enterAddress1(String address) {
        address1Field.clear();
        address1Field.sendKeys(address);
    }

    public void enterAddress2(String address) {
        address2Field.clear();
        address2Field.sendKeys(address);
    }

    public void enterCity(String city) {
        cityField.clear();
        cityField.sendKeys(city);
    }

    public void enterCounty(String county) {
        countyField.clear();
        countyField.sendKeys(county);
    }

    public void enterPostcode(String postcode) {
        postcodeField.clear();
        postcodeField.sendKeys(postcode);
    }

    public void enterPhone(String phone) {
        phoneField.clear();
        phoneField.sendKeys(phone);
    }

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
        waiter.visible(By.id("payment_method_cheque"));
    }

    public boolean isChequeSelected() {
        return chequeRadio.isSelected();
    }
    public void clickPlaceOrder() {
        placeOrderButton.click();
    }
    public static class OrderConfirmationPOM {
    }
}
