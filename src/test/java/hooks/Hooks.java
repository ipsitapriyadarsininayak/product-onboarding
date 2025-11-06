package hooks;


import io.cucumber.java.*;
//import org.junit.Assume;
import org.openqa.selenium.WebDriver;
import drivers.BrowserDriver;
import utils.ConfigFileReader;
import pages.BasePage;
import utils.ExtentReportManager;
import com.aventstack.extentreports.Status;

import java.util.Collection;
import java.util.List;


public class Hooks {

    public static boolean stopScenario;
    private BasePage basePage;
    private static boolean isBrowserStarted = false;

    @Before
    public void setup(Scenario scenario){
        System.out.println(">>> [BEFORE HOOKS] >>>>");
        if(!isBrowserStarted) {
            isBrowserStarted=true;
            String browser = ConfigFileReader.get("browser"); //Default to chrome if not set
//            String browser = System.getProperty("browser", "chrome"); //Default to chrome if not set
            System.out.println("Running the test on browser ==> " + browser);
            if (browser == null || browser.trim().isEmpty()){
                throw new RuntimeException("Browser is not specified in config.properties");
            }
            BrowserDriver.initDriver(browser);
            BrowserDriver.getDriver();
            String featureName = scenario.getUri().getPath().replace(".feature", "").replaceAll(".*/", "");
            ExtentReportManager.createFeature(featureName);
            ExtentReportManager.createScenario(scenario.getName());
//            WebDriver driver = BrowserDriver.getDriver();C:\Users\2365350\IdeaProjects\Current project\PdxStibo\src\test\resources\data\TestDataEAN.xlsx
//            driver.get(ConfigFileReader.get("PdxUrl"));
        }
        System.out.println("Browser launched for : " + scenario.getName());

    }

    /*@AfterStep
    public void afterStep(Scenario scenario) {
        if (scenario.isFailed()) {
            ExtentReportManager.logStep("Step failed", Status.FAIL);
        } else {
            ExtentReportManager.logStep("Step passed", Status.PASS);
        }
    }*/

    /*@After
    public void afterScenario(Scenario scenario) {
        System.out.println("After Scenario: " + scenario.getName());

        if (scenario.isFailed()) {
            ExtentReportManager.logFailure(scenario);
            BrowserDriver.quitDriver();
            isBrowserStarted = false;
        } else {
            ExtentReportManager.logSuccess(scenario);
        }
    }*/

    /*@AfterAll
    public static void tearDown() {
        System.out.println("===== All Scenarios Finished =====");
        ExtentReportManager.flush(); // finalize report once
    }*/

        //BrowserDriver.quitDriver();
       // ExtentReportManager.flush();// Close browser to prevent further steps
            //isBrowserStarted=false;// Reset for next scenario
}



    /*public void afterScenario(Scenario scenario){
        System.out.println(">>> [AFTER HOOKS] >>>>");
        basePage = new BasePage();
        //Take screenshot after scenario
        basePage.takeScreenshot(scenario.getName());
        System.out.println("Screenshot Taken ");
*/


  /*  @AfterAll
    public static void tearDown() throws InterruptedException {
        Thread.sleep(1000);
      //BrowserDriver.quitDriver();
        ExtentReportManager.flush();
        //isBrowserStarted=false;// Quit the driver
    }*/

