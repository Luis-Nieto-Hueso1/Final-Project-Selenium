package com._2itesting.tests.basetest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.fail;

public class BaseTest {
    public WebDriver driver;
    public StringBuffer verificationErrors  = new StringBuffer();

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();

    }

    @AfterEach
    public void tearDown(
    ) {
        driver.close();
        driver.quit();
        String verificationErrorsString = verificationErrors.toString(); //Post test - gather together any verification errors

        if(!verificationErrorsString.isEmpty()){ //If verificationErrorsString is *not* empty - there must have been errors...
            fail("Found verification errors :" + verificationErrorsString); //...So fail.
  }
}
}
