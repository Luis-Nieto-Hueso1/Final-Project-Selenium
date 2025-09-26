package com._2itesting.tests.pomClasses;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.WebDriver;


public class ShopPOM {
    private WebDriver driver;

    public ShopPOM(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);

    }

    @FindBy(css = "li.product:nth-child(9) > a:nth-child(1) > img:nth-child(1)")
    public WebElement poloField;

    @FindBy(css = " a.button.product_type_simple.add_to_cart_button.ajax_add_to_cart.added")
    public WebElement beanieField;

    public void navPagePolo() {

        poloField.click();
    }
    public void navPageBeanie() {

        beanieField.click();
    }

}