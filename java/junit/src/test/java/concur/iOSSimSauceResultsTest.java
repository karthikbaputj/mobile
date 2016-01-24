package concur;

import com.sauce.common.SauceOnDemandAuthentication;
import com.sauce.common.SauceOnDemandSessionIdProvider;
import com.sauce.junit.ConcurrentParameterized;
import com.sauce.junit.SauceOnDemandTestWatcher;
import org.junit.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * Demonstrates how to write a JUnit test that runs tests against Sauce Labs using multiple browsers in parallel.
 * <p/>
 * The test is annotated with the {@link ConcurrentParameterized} test runner...
 * <p/>
 * The test also includes the {@link SauceOnDemandTestWatcher}...
 *
 * @author Ross Rowe
 */
@RunWith(ConcurrentParameterized.class)
public class iOSSimSauceResultsTest implements SauceOnDemandSessionIdProvider {

    /**
     * Constructs a {@link SauceOnDemandAuthentication} instance using the supplied user name/access key.  To use the authentication
     * supplied by environment variables or from an external file, use the no-arg {@link SauceOnDemandAuthentication} constructor.
     */
    public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication();

    /**
     * JUnit Rule which will mark the Sauce Job as passed/failed when the test succeeds or fails.
     */
    @Rule
    public SauceOnDemandTestWatcher resultReportingTestWatcher = new SauceOnDemandTestWatcher(this, USERNAME, ACCESS_KEY,true);

    private String device;
    private String os;
    private String version;
    /**
     *
     */
    private String sessionId;
    public static final String USERNAME = "karthikbaputj";
    public static final String ACCESS_KEY = "1f04d210-949c-4c55-9ead-ab8e86243777";
    public static final String URL = "http://" + USERNAME + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:80/wd/hub";


    public iOSSimSauceResultsTest(String os, String version, String device) {
        super();
        this.os = os;
        this.version = version;
        this.device = device;
    }

    @ConcurrentParameterized.Parameters
    public static LinkedList browsersStrings() throws Exception {

        LinkedList browsers = new LinkedList();
        browsers.add(new String[]{"iOS", "8.1", "iPhone 4s"});
        browsers.add(new String[]{"iOS", "9.2", "iPhone 6"});
        browsers.add(new String[]{"iOS", "9.0", "iPhone 5s"}); 
        return browsers;
    }

    private WebDriver driver;

    @Before
    public void setUp() throws Exception {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", device);
        if (version != null) {
            capabilities.setCapability("platformVersion", version);
        }
        capabilities.setCapability("platformName", os);

        capabilities.setCapability("appium-version", "1.4.16");
        //capabilities.setCapability("platformName", "iOS");
        //capabilities.setCapability("platformVersion", "9.2");
        //capabilities.setCapability("deviceName", "iPhone 4s");
        capabilities.setCapability("app", "sauce-storage:ConcurGov.zip");
        //capabilities.setCapability("bundleid","com.concur.concurgov.enterprisetest");
        capabilities.setCapability("name",os + "_" + device + "_" + version + "_" + "Test");
        capabilities.setCapability("public","public");
        capabilities.setCapability("tags","smoke");
        //capabilities.setCapability("build", "build_1007");


        this.driver = new RemoteWebDriver(new URL(URL), capabilities);
        this.sessionId = (((RemoteWebDriver) driver).getSessionId()).toString();

        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
    }


    @Test
    public void uiLoginLogout() throws Exception {
        driver.findElement(By.name("User name")).clear();
        driver.findElement(By.name("User name")).sendKeys("autoadmin@gsa-automation.gov");
        driver.findElement(By.name("Password or Pin")).sendKeys("outtask1");
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

    @Override
    public String getSessionId() {
        return sessionId;
    }
}