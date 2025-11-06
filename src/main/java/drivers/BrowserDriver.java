package drivers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.poi.ss.formula.atp.Switch;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;


public class BrowserDriver {

    private  static  WebDriver driver;

   /* public static boolean isInitialized() {
        return driver != null;
    }*/


    public static void initDriver(String browser) {
        if (driver == null) {
            switch(browser.toLowerCase()){
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                   driver = new ChromeDriver();

                    break;

                case "edge":
                     WebDriverManager.edgedriver().setup();
                    driver = new EdgeDriver();

                    break;
                default:
                    throw new IllegalArgumentException("invalid browser:"+browser);
            }
            driver.manage().window().maximize(); //Maximize window
        }
    }

    public static WebDriver getDriver() {
        if (driver == null) {
            throw new IllegalStateException("WebDriver has not been initialized ");
        }
        return driver;
    }

    public static void quitDriver() {
        if (driver!= null) {
            driver.quit(); //quit and remove the instance
            driver = null;
        }
    }

}
