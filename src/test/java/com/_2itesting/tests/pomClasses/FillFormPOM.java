package com._2itesting.tests.pomClasses;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.nio.file.FileStore;

public class FillFormPOM {
    private WebDriver driver;

    public FillFormPOM(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "billing_first_name")
    public WebElement firstNameField;

    @FindBy(id = "billing_last_name")
    public WebElement lastNameField;

    @FindBy(id = "billing_address_1")
    public WebElement addressField;

    @FindBy(id = "billing_address_2")
    public WebElement address2Field;

    @FindBy(id = "billing_city")
    public WebElement cityField;

    @FindBy(id = "billing_state")
    public WebElement stateField;

    @FindBy(id = "billing_postcode")
    public WebElement postcodeField;

    @FindBy(id = "billing_phone")
    public WebElement phoneField;

    @FindBy(id = "payment_method_cheque")
    public WebElement chequeField;

    @FindBy(id = "place_order")
    public WebElement placeOrderField;

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

    public void ChequePayment() {
        chequeField.click();


    }
    public void placeOrder() {
        placeOrderField.click();
    }
}
