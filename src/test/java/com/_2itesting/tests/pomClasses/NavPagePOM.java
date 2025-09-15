package com._2itesting.tests.pomClasses;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NavPagePOM {
    private WebDriver driver;

    public NavPagePOM(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);

    }

    @FindBy(linkText = "Shop")
    public WebElement shopField;
    @FindBy(linkText = "Cart")
    public WebElement basketField;
    @FindBy(linkText = "Checkout")
    public WebElement checkoutField;
    @FindBy(name = "add-to-cart")
    public WebElement addCartField;
    @FindBy(css = "li.product:nth-child(9) > a:nth-child(1) > img:nth-child(1)")
    public WebElement poloField;
    @FindBy(linkText = "My account")
    public WebElement myAccountField;
    @FindBy(linkText = "Log out")
    public WebElement logoutButton;



    public void navPageShop() {
        shopField.click();
    }

    public void navPageBasket() {
        basketField.click();

    }

    public void navCheckout() {
        checkoutField.click();
    }

    public void navAddCart() {
        addCartField.click();
    }

    public void navPagePolo() {
        poloField.click();
    }

    public void navMyAccount() {
        myAccountField.click();
    }
    public void navLogout() {
        logoutButton.click();
    }



}
