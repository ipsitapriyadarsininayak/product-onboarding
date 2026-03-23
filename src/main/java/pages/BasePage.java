package pages;

import drivers.BrowserDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Objects;

public class BasePage {
    protected static WebDriver driver;
    private static final int TIMEOUT = 80; //SECONDS

    public BasePage() {

        if (BrowserDriver.getDriver() == null) {
            BrowserDriver.initDriver("chrome"); // Or use config
        }

        this.driver = BrowserDriver.getDriver();
    }

    protected void enterText(String xpath, String text) {

        WebElement element = driver.findElement(By.xpath(xpath));
        element.clear();
        element.sendKeys(text);
        By textFieldLocator = By.xpath("/td[@class='cell-selected cell-selected-primary sheet-c");

        }



    protected static void clickElement(String xpath){
        driver.findElement(By.xpath(xpath)).click();
    }

    public static String getElementText(String xpath){
        return driver.findElement(By.xpath(xpath)).getText();
    }

    public static void waitForElementVisible(String xpath){
        new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT)).until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    }

    public void waitForPageLoad(){
        new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT)).until(
                webDriver -> Objects.equals(((JavascriptExecutor) webDriver)
                        .executeScript("return document.getState"), "completed"));
    }

    public String takeScreenshot(String scenarioName){
        String timeStamp = new SimpleDateFormat("ddmmyyyy_HHmmss").format(new Date());
        String screenshotName = scenarioName.replaceAll("", "_")+"_"+ timeStamp + ".png";
        String path = System.getProperty("user.dir")+"/target/screenshots/" + screenshotName;

        File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        File destFile = new File(path);

        try{
            FileUtils.copyFile(srcFile, destFile);
        }catch (IOException e){
            e.printStackTrace();
        }
        return path;
    }
}

