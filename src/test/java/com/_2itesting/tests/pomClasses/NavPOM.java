package com._2itesting.tests.pomClasses;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NavPOM {
    private final WebDriver driver;

    public NavPOM(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(linkText = "Shop")
    private WebElement shopLink;

    @FindBy(linkText = "My account")
    private WebElement myAccountLink;

    @FindBy(linkText = "Cart")
    private WebElement viewCartLink;

    @FindBy(name = "add-to-cart")
    private WebElement addToCartButton;

    @FindBy(linkText = "Log out")
    private WebElement logoutLink;

    @FindBy(linkText = "Checkout")
    private WebElement checkoutButton;

    public void clickShop() {
        shopLink.click();
    }

    public void clickMyAccount() {
        myAccountLink.click();
    }

    public void clickViewCart() {
        viewCartLink.click();
    }

    public void clickAddToCart() {
        addToCartButton.click();
    }

    public void clickLogout() {
        logoutLink.click();
    }

    public void clickCheckout() {
        checkoutButton.click();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}