package com._2itesting.tests.Utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;


public class InstanceHelpers {

    private WebDriver driver; //field in scope for helper methods in this class

    //Constructor - a method that runs automatically when an instance of this class is created
    public InstanceHelpers(WebDriver driver){
        this.driver = driver;
    }


    public Action dragDropHelper(WebElement elm, int movX, int smooth){
        Actions myActionChain = new Actions(driver);
        int stepSize = movX/smooth;
        myActionChain.moveToElement(elm)
                .clickAndHold(); // hold down the mouse button
        for (int i=0; i<smooth; i++){
            myActionChain.moveByOffset(stepSize,0);

        }
        return myActionChain.release().build();
    }

}
