package concur;

import io.appium.java_client.ios.IOSDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.concurrent.TimeUnit;


public class iOSPageObjectTest {

    private WebDriver driver;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("appium-version", "1.4.16");
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "8.1");
        capabilities.setCapability("deviceName", "iPhone 6");
        // simulator run
        capabilities.setCapability("app", "/Users/karthikt/GitHub/mobile/java/junit/src/Resources/simulator/ConcurGovSimulator.trunk.BUILD32.REV404911.BUILDVER1601070825.ipa");
        // Real device run
        //capabilities.setCapability("app", "/Users/karthikt/GitHub/mobile/java/junit/src/Resources/real/ConcurGovDevice.trunk.BUILD36.REV404911.BUILDVER1601070832.ipa");
        //capabilities.setCapability("passed","true);

        driver = new IOSDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

    }


    @Test
    public void uiLoginLogout() throws Exception {

        driver.findElement(By.name("User name")).clear();
        driver.findElement(By.name("User name")).sendKeys("autoadmin@gsa-automation.gov");
        driver.findElement(By.name("Password or Pin")).sendKeys("sdfds");
        driver.findElement(By.name("Done")).click();
        driver.findElement(By.name("OK")).click();
        Assert.assertEquals("Planning your trip screen", "Start by planning your trip", driver.findElement(By.name("Start by planning your trip")).getText());
        driver.findElement(By.name("Settings")).click();
        //driver.findElement(By.xpath("//UIAApplication[1]/UIAWindow[1]/UIATableView[1]/UIATableCell[8]/UIAStaticText[1]")).click();
        driver.findElement(By.name("Sign out")).click();
        Assert.assertEquals("Login screen is displayed", true , driver.findElement(By.name("User name")).isDisplayed());

        //} catch (Exception e) {
        //    System.out.print("hit exception" + e.getMessage());
        //}
    }



    @After
    public void tearDown() throws Exception {
        driver.quit();
    }
}
