package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class PDXPage {

    WebDriver driver;

    public boolean isProductImported(String productID) {


        WebElement productElement = driver.findElement(By.id("product-id"));
        return productElement.getText().contains(productID);
    }

    public void scrollToSeasonality() {
        WebElement seasonalitySection = driver.findElement(By.id("seasonality"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", seasonalitySection);
    }

    public void clickCore() {
        driver.findElement(By.id("core-button")).click();
    }

    public void selectCoreNewness(String value) {
        WebElement dropdown = driver.findElement(By.id("core-newness-dropdown"));
        Select select = new Select(dropdown);
        select.selectByVisibleText(value);
    }
}


