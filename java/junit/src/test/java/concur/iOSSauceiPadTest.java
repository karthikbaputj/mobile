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


public class iOSSauceiPadTest {

    private WebDriver driver;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("appium-version", "1.4.16");
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "9.2");
        capabilities.setCapability("deviceName", "iPad 2");
        // For real device get the Device Build and for simulator tests get the simulator build.
        capabilities.setCapability("app", "/Users/karthikt/GitHub/sample-code/sample-code/apps/ConcurGov/ConcurGovDevice.trunk.BUILD6.REV401987.BUILDVER1511231437.ipa");
        //capabilities.setCapability("passed","true);

        driver = new IOSDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

    }


    @Test
    public void uiLoginLogout() throws Exception {

        driver.findElement(By.name("User name")).sendKeys("autoadmin@gsa-automation.gov");
        driver.findElement(By.name("Password or Pin")).sendKeys("dfsdf");
        driver.findElement(By.name("Done")).click();
        driver.findElement(By.name("OK")).click();
        Assert.assertEquals("Planning your trip screen", "Start by planning your trip", driver.findElement(By.name("Start by planning your trip")).getText());
        driver.findElement(By.name("Settings")).click();
        //driver.findElement(By.xpath("//UIAApplication[1]/UIAWindow[1]/UIATableView[1]/UIATableCell[8]/UIAStaticText[1]")).click();
        driver.findElement(By.name("Sign out")).click();
        Assert.assertEquals("Login screen is displayed", true , driver.findElement(By.name("User name")).isDisplayed());
    }


    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

}
