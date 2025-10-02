package com._2itesting.tests.basetest;

import com._2itesting.tests.Utils.*;
import com._2itesting.tests.pomClasses.CartPOM;
import com._2itesting.tests.pomClasses.LoginPagePOM;
import com._2itesting.tests.pomClasses.NavPOM;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;

public class BaseTest {
    public WebDriver driver;
    public StringBuffer verificationErrors  = new StringBuffer();
    protected Waiter waiter;



    @BeforeEach
    public void setUp() {

        String browser = System.getProperty("browser", "chrome").toLowerCase();

        switch (browser) {
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "edge":
                driver = new EdgeDriver();
                break;
            case "chrome":
            default:
                driver = new ChromeDriver();
                break;
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        waiter = new Waiter(driver, Duration.ofSeconds(5));

        System.out.println("=== Starting Test Setup ===");
        driver.get(Helpers.LOGIN_URL);
        driver.findElement(By.cssSelector(".woocommerce-store-notice__dismiss-link")).click();
        // Inputs: make them explicit and parameterizable later
        final String username = Helpers.USERNAME;
        final String productName = "Polo"; // replace with param later
        final String coupon = Helpers.TWO_I_DISCOUNT_COUPON;
        final String password = Helpers.PASSWORD;
        ReportUtils.logInputs(username, password, productName, coupon);

        LoginPagePOM loginPagePOM = new LoginPagePOM(driver);
        CartPOM cart = new CartPOM(driver);
        NavPOM navPOM = new NavPOM(driver);

        boolean loggedIn = loginPagePOM.login(username, Helpers.PASSWORD);
        assertThat("login Successful", loggedIn, is(true));
        assertThat("Should be redirected to account page after login", driver.getCurrentUrl(), containsString("my-account"));

        navPOM.navPageBasket();
        cart.clearCart();


        System.out.println("=== Cart cleared successfully ===");
    }
    @RegisterExtension
    TestWatcher allureFailureWatcher = new TestWatcher() {
        @Override
        public void testFailed(ExtensionContext context, Throwable cause) {
            // Be defensive; only attempt if we still have a session
            if (driver instanceof RemoteWebDriver rwd && rwd.getSessionId() != null) {
                String name = "Failure — " + context.getDisplayName();
                AllureUtils.attachUrl(driver);
                AllureUtils.attachScreenshot(driver, name);
                AllureUtils.attachPageSource(driver, name);

            }
        }
    };


    @AfterEach
    void tearDown() {
        if (driver == null) return;

        try {

            // Only quit if the session is still valid
            if (driver instanceof RemoteWebDriver) {
                var rwd = (RemoteWebDriver) driver;
                if (rwd.getSessionId() != null) {
                    rwd.quit();
                }
            } else {
                // Non-remote drivers – just try and swallow if already closed
                driver.quit();
            }
        } catch (NoSuchSessionException ignored) {
            // Session already gone – nothing to do
        } catch (Exception ignored) {
            // Don’t fail the test on teardown noise
        } finally {
            driver = null;
        } }}


