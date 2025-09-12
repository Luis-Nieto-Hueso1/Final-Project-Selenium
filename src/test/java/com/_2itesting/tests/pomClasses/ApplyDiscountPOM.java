package com._2itesting.tests.pomClasses;

import com._2itesting.tests.Utils.Helpers;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ApplyDiscountPOM {

    private WebDriver driver;

    public ApplyDiscountPOM(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this); // initialise @FindBy elements
    }






    @FindBy(id = "coupon_code")
    private WebElement coupon;

    @FindBy(name = "apply_coupon")
    private WebElement applyCouponButton;


    public void applyCoupon(String discount) {
        coupon.clear();              // optional: clears existing text
        coupon.sendKeys(discount);
    }

    public void clickApplyCoupon() {
        applyCouponButton.click();
    }

    // (Optional) combine into one convenience method
    public void applyDiscountCode(String code) {
        applyCoupon(code);
        clickApplyCoupon();
    }

    public Boolean isCouponAppliedMessageDisplayed() {
        String expectedMessage = "Coupon code applied successfully.";
        String actualMessage = Helpers.getNoticeMessage(driver);
        return actualMessage.contains(expectedMessage);
    }
}
