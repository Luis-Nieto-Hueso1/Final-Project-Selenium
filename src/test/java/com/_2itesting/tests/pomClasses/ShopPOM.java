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


    public void navPagePolo() {

        poloField.click();
    }

}