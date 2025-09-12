package com._2itesting.tests.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class InstanceHelpers {

    private WebDriver driver; //field in scope for helper methods in this class

    //Constructor - a method that runs automatically when an instance of this class is created
    public InstanceHelpers(WebDriver driver){
        this.driver = driver;
    }

    public WebElement waitForElementToBeClickableHelper(By locator, int timeoutInSeconds){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public Action dragDropHelper(WebElement elm, int movX, int smooth){
        Actions myActionChain = new Actions(driver);
        int stepSize = movX/smooth;
        myActionChain.moveToElement(elm)
                .clickAndHold(); //I FORGOT TO ACTUALLY CLICK THE LEFT MOUSE BUTTON...x_x
        for (int i=0; i<smooth; i++){
            myActionChain.moveByOffset(stepSize,0);

        }
        return myActionChain.release().build();
    }

}
