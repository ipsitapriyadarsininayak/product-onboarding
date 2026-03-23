package pages;

import constants.ElementLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.PDXExcelReader;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static constants.ElementLocators.*;


public class PdxMasterPage extends BasePage {

    public void click_Master_Data() {
        waitForElementVisible(ElementLocators.MASTER_DATA_ICON_XPATH);
        clickElement(ElementLocators.MASTER_DATA_ICON_XPATH);
        System.out.println("MASTER_DATA_ICON clicked");
    }

    public void filterProductsThenAddToChannel(String excelPath, String SheetName, String columnHeader) throws Exception {

        Thread.sleep(5000);

        //  --- 1: Click on "Filter" option ---
        waitForElementVisible(ElementLocators.FILTER_ICON_XPATH);
        clickElement(ElementLocators.FILTER_ICON_XPATH);

        // Read Excel
        PDXExcelReader reader = new PDXExcelReader(excelPath, SheetName);
        int rowCount = reader.getRowCount();
        List<String> excelIds = new ArrayList<>();
        for (int i = 1; i <= rowCount; i++) {
            String id = reader.getCellValue(i, columnHeader);
            if (id != null && !id.trim().isEmpty()) {
                excelIds.add(id.trim());
            }
        }

        if (excelIds.isEmpty()) {
            throw new IllegalArgumentException("❌ No IDs found in Excel column : " +columnHeader);
        }

        // --- 2: Click on "List of values" ---
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        WebElement listOfValuesButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(LIST_OF_VALUES_XPATH)));
        listOfValuesButton.click();

        // --- 3: Wait for the text area and paste all IDs ---
        WebElement textArea = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(LIST_OF_VALUES_TEXTBOX_XPATH)));
        String allIds = String.join(",", excelIds); // Join all IDs with comma
        textArea.sendKeys(allIds); // Paste into text area
        textArea.sendKeys(Keys.ENTER);
        Thread.sleep(3000);

        // --- 4: Click on "Available in Channel"
        WebElement availableInChannel = wait.until(ExpectedConditions.elementToBeClickable
                (By.xpath(AVAILABLE_IN_CHANNEL_XPATH)));
        availableInChannel.click();


        // --- 5: Click on "Product Lifecycle Dates"
        waitForElementVisible(ElementLocators.PRODUCT_LIFECYCLE_DATES_XPATH);
        clickElement(ElementLocators.PRODUCT_LIFECYCLE_DATES_XPATH);

        // --- 6: Click on 'Apply' button
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.body.style.zoom='80%'"); // Adjust percentage as needed
        WebElement applyButton = driver.findElement(By.xpath(ElementLocators.APPLY_BUTTON_IN_FILTER_XPATH));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", applyButton);
        waitForElementVisible(ElementLocators.APPLY_BUTTON_IN_FILTER_XPATH);
        clickElement(ElementLocators.APPLY_BUTTON_IN_FILTER_XPATH);
        Thread.sleep(10000);

        // --- 7:  Select all Products
        WebElement checkbox = driver.findElement(By.xpath(SELECT_ALL_CHECKBOX_XPATH));
        checkbox.click();

        // Initialize Excel reader
        int retries = 3;
        while (retries-- > 0) {
            try {
                WebElement threeDots = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath(ElementLocators.THREE_DOTS_ICON_XPATH)
                ));
                threeDots.click();
                break;
            } catch (Exception e) {
                Thread.sleep(2000);
            }
        }

        // --- 8: Click on 'Add to Channel'
        driver.findElement(By.cssSelector(ElementLocators.ADD_TO_CHANNEL_XPATH)).click();
        Thread.sleep(5000);

        // --- 9: Click on "Select channel and category" radio button
        driver.findElement(By.xpath(CHANNEL_AND_CATEGORY_RADIO_XPATH)).click();
        Thread.sleep(5000);

        // --- 10: Select Channel
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CHANNEL_DROPDOWN_ARROW_XPATH)));
        dropdown.click();
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(BRANDS_AT_M_AND_S_DEV_XPATH)));
        option.click();
        Thread.sleep(5000);
        WebElement scrollContainer = driver.findElement(By.xpath(VERTICLE_SCROLL_XPATH));

        // Scroll to bottom
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollTop = arguments[0].scrollHeight;", scrollContainer);

        List<String> categoryList = new ArrayList<>();

        // Read and clean category values
        for (int i = 1; i <= rowCount; i++) {
            String category = reader.getCellValue(i, "Category");
            if (category != null && !category.trim().isEmpty()) {
                categoryList.add(category.trim());
            }
        }

        // Loop through categories and perform UI actions
        Set<String> seen = new HashSet<>();
        for (String raw : categoryList) {
            if(raw == null) continue;

            // Normalize to avoid duplicate categories
            String category = raw.trim();
            if (category.isEmpty()) continue;

            String key = category.toLowerCase(); // case-insensitive uniqueness

            if (seen.contains(key)) {
                // Skip duplicates
                continue;
            }
            seen.add(key);

            // --- 11: Click "Select a category"
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CATEGORY_DROPDOWN_ARROW_XPATH))).click();
            Thread.sleep(1000);

            // --- 12: Type category name
            WebElement inputField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath(SEARCH_CATEGORY_TEXTBOX_XPATH)
            ));
            inputField.clear();
            inputField.sendKeys(category);
            Thread.sleep(2000);

            // --- 13: Click on the category item
            WebElement categoryItem = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(SElECT_SEARCHED_CATEGORY_XPATH)));
            Thread.sleep(5000);
            categoryItem.click();

            // --- 14: Click 'Add products' button
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(ADD_PRODUCTS_BUTTON_XPATH))).click();
            Thread.sleep(10000);
        }
    }
}







































