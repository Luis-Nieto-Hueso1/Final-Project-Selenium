package com._2itesting.tests.pomClasses;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ApplyDiscountPOM {
    private WebElement driver;

    public ApplyDiscountPOM(WebElement driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

//    WebElement cupon = driver.findElement(By.cssSelector("#coupon_code"));
//
//        cupon.sendKeys(Helpers.TWO_I_DISCOUNT_COUPON);
//        driver.findElement(By.name("apply_coupon")).
//
//    click();
//
//
//    WebElement shipping = driver.findElement(By.cssSelector(".shipping-calculator-button"));
//    JavascriptExecutor jsv = (JavascriptExecutor) driver;
//        jsv.executeScript("arguments[0].scrollIntoView({block:'center'});",shipping);

    @FindBy(css = "#coupon_code")
    private WebElement coupon;

    public ApplyDiscountPOM applyCupon(String discount ){
        coupon.clear();
        coupon.sendKeys(discount);
        return this;
    }
}
