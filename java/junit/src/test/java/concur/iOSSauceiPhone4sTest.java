package concur;


import io.appium.java_client.ios.IOSDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.concurrent.TimeUnit;

/*  appium iphoneSaucetest */
public class iOSSauceiPhone4sTest {

    private WebDriver driver;
    public static final String USERNAME = "karthikbaputj";
    public static final String ACCESS_KEY = "1f04d210-949c-4c55-9ead-ab8e86243777";
    public static final String URL = "http://" + USERNAME + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:80/wd/hub";


    @Before
    public void setUp() throws Exception {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("appium-version", "1.4.16");
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "9.2");
        capabilities.setCapability("deviceName", "iPhone 4s");
        //capabilities.setCapability("app", "/Users/karthikt/GitHub/sample-code/sample-code/apps/ConcurGov/ConcurGov.app");
        capabilities.setCapability("app", "sauce-storage:ConcurGov.zip");
        capabilities.setCapability("bundleid","com.concur.concurgov.enterprisetest");
        capabilities.setCapability("name","iphone4ssimulatortest");

        driver = new IOSDriver(new URL(URL), capabilities);
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
    }


    /**
     * Page Object best practice is to describe interactions with target
     * elements by methods. These methods describe business logic of the page/screen.
     * Here test interacts with lazy instantiated elements directly.
     * It was done so just for obviousness
     */

    @Test
    public void uiLogin() throws Exception {
        try {
            driver.findElement(By.name("User name")).sendKeys("autoadmin@gsa-automation.gov");
            driver.findElement(By.name("Password or Pin")).sendKeys("dsfsd");
            driver.findElement(By.name("Done")).click();
            Assert.assertEquals(driver.findElement(By.name("Privacy Act Notice")).getText(), "Privacy Act Notice");
            driver.findElement(By.name("OK")).click();
            Assert.assertEquals(driver.findElement(By.name("Start by planning your trip")).getText(), "Start by planning your trip");
        } catch (Exception e) {
            System.out.print("hit exception" + e.getMessage());
        }
    }

    @Test
    public void uiLogout() throws Exception {
        try {
            uiLogin();
            Assert.assertEquals(driver.findElement(By.name("Start by planning your trip")).getText(), "Start by planning your trip");
            driver.findElement(By.name("Settings")).click();
        //driver.findElement(By.xpath("//UIAApplication[1]/UIAWindow[1]/UIATableView[1]/UIATableCell[8]/UIAStaticText[1]")).click();
        driver.findElement(By.name("Sign out")).click();
        Assert.assertEquals(driver.findElement(By.name("Username")).getText(), "Username");
        } catch (Exception e) {
            System.out.print("hit exception" + e.getMessage());
        }
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }
}
