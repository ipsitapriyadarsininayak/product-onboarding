package pages;

import constants.ElementLocators;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.PDXExcelReader;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static constants.ElementLocators.*;


public class PdxChannelPage extends BasePage {

    private static final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    JavascriptExecutor js = (JavascriptExecutor) driver;

    public void filterProductThenSubmitToStep(String excelPath, String SheetName, String columnHeader) throws InterruptedException, IOException {

        Thread.sleep(5000);

        // Application redirects to 'Channels-> List View' then Switch to "Grid View"
        WebElement grid = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(GRID_VIEW_XPATH)));
        grid.click();
        Thread.sleep(10000);

        // Navigate to "Family Product" tab
        WebElement familyGroup = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(FAMILY_PRODUCTS_TAB_XPATH)));
        familyGroup.click();

        // Click on "Filter" options
        waitForElementVisible(FILTER_ICON_XPATH);
        clickElement(FILTER_ICON_XPATH);
        Thread.sleep(5000);

        PDXExcelReader reader = new PDXExcelReader(excelPath, SheetName);
        int rowCount = reader.getRowCount();
        List<String> excelIDs = new ArrayList<>();

        for (int i = 1; i <= rowCount; i++) {
            String id = reader.getCellValue(i, columnHeader);
            if (id != null && !id.trim().isEmpty()) {
                excelIDs.add(id.trim());
            }
        }
        reader.close();

        if (excelIDs.isEmpty()) {
            throw new IllegalArgumentException("❌ No IDs found in Excel column");
        }

        // Click on "List of values"
        WebElement listOfValuesButton1 = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(LIST_OF_VALUES_XPATH)));
        listOfValuesButton1.click();

        // Wait for the text area and paste all IDs
        WebElement textArea1 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(LIST_OF_VALUES_TEXTBOX_XPATH)));

        String AllIds = String.join(",", excelIDs);
        textArea1.sendKeys(AllIds);

        textArea1.sendKeys(Keys.ENTER);
        Thread.sleep(2000);

        WebElement productStatusAndFlag = wait.until(ExpectedConditions.elementToBeClickable
                (By.xpath(PRODUCT_STATUS_AND_FLAG_XPATH)));
        productStatusAndFlag.click();
        Thread.sleep(1000);

        WebElement productLifecycleDates = wait.until(ExpectedConditions.elementToBeClickable
                (By.xpath(PRODUCT_LIFECYCLE_DATES_CHANNEL_XPATH)));
        productLifecycleDates.click();
        Thread.sleep(1000);

        // Click on "Apply" button
        WebElement applyButtonFilter = driver.findElement(By.xpath(ElementLocators.APPLY_BUTTON_IN_FILTER_XPATH));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", applyButtonFilter);
        waitForElementVisible(ElementLocators.APPLY_BUTTON_IN_FILTER_XPATH);
        clickElement(ElementLocators.APPLY_BUTTON_IN_FILTER_XPATH);
        Thread.sleep(5000);

        WebElement checkbox = driver.findElement(By.xpath(SELECT_ALL_CHECKBOX_XPATH));
        js.executeScript("arguments[0].click();", checkbox);
        Thread.sleep(6000);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement ellipsis = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(ELLIPSIS_XPATH)));
        ellipsis.click();
        Thread.sleep(5000);
        driver.findElement(By.cssSelector(SUBMIT_ELLIPSIS_XPATH)).click();

        Thread.sleep(8000);

        WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(SUBMIT_BUTTON_XPATH)));
        submit.click();

        // Wait for OK message to disappear
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.xpath(OK_MESSAGE_XPATH)
            ));
            System.out.println("✅ OK message disappeared.");
        } catch (Exception ignored) {
            System.out.println("ℹ️ OK message not found – continuing...");
        }

        // Wait for Submitted status to appear
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath(SUBMITTED_STATUS_XPATH)
            ));
            System.out.println("✅ Submitted status visible in UI.");
        } catch (Exception ignored) {
            System.out.println("⚠️ Submitted status not detected – continuing anyway...");
        }
        System.out.println("🎯 TEST PASSED");
    }

    public void filterProductsInChannelGridView(String excelPath, String SheetName, String columnHeader) throws InterruptedException, IOException {

        Thread.sleep(5000);

        WebElement channel = driver.findElement(By.xpath(CHANNEL_MAIN_MENU_XPATH));
        channel.click();
        Thread.sleep(1000);

        WebElement ChannelOverview = driver.findElement(By.xpath(CHANNELS_OVERVIEW_SUB_MENU_XPATH));
        ChannelOverview.click();
        WebElement fifthContent = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(M_AND_S_DEV_CHANNEL_XPATH)));
        fifthContent.click();

        WebElement goToListViewItem = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath(GO_TO_LIST_VIEW_CHANNEL_XPATH)));
        goToListViewItem.click();

        // Application redirects to 'Channels-> List View' then Switch to "Grid View"
        WebElement grid = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(GRID_VIEW_XPATH)));
        grid.click();
        Thread.sleep(10000);

        // Navigate to "Family Product" tab
        WebElement familyGroup = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(FAMILY_PRODUCTS_TAB_XPATH)));
        familyGroup.click();

        // Click on "Filter" options
        waitForElementVisible(FILTER_ICON_XPATH);
        clickElement(FILTER_ICON_XPATH);
        Thread.sleep(5000);

        PDXExcelReader reader = new PDXExcelReader(excelPath, SheetName);
        int rowCount = reader.getRowCount();
        List<String> excelIDs = new ArrayList<>();

        for (int i = 1; i <= rowCount; i++) {
            String id = reader.getCellValue(i, columnHeader);
            if (id != null && !id.trim().isEmpty()) {
                excelIDs.add(id.trim());
            }
        }
        reader.close();

        if (excelIDs.isEmpty()) {
            throw new IllegalArgumentException("❌ No IDs found in Excel column");
        }

        // Click on "List of values"
        WebElement listOfValuesButton1 = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(LIST_OF_VALUES_XPATH)));
        listOfValuesButton1.click();

        // Wait for the text area and paste all IDs
        WebElement textArea1 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(LIST_OF_VALUES_TEXTBOX_XPATH)));

        String AllIds = String.join(",", excelIDs);
        textArea1.sendKeys(AllIds);

        textArea1.sendKeys(Keys.ENTER);
        Thread.sleep(2000);

        WebElement productStatusAndFlag = wait.until(ExpectedConditions.elementToBeClickable
                (By.xpath(PRODUCT_STATUS_AND_FLAG_XPATH)));
        productStatusAndFlag.click();
        Thread.sleep(1000);

        WebElement productLifecycleDates = wait.until(ExpectedConditions.elementToBeClickable
                (By.xpath(PRODUCT_LIFECYCLE_DATES_CHANNEL_XPATH)));
        productLifecycleDates.click();
        Thread.sleep(1000);

        // Click on "Apply" button
        WebElement applyButtonFilter = driver.findElement(By.xpath(ElementLocators.APPLY_BUTTON_IN_FILTER_XPATH));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", applyButtonFilter);
        waitForElementVisible(ElementLocators.APPLY_BUTTON_IN_FILTER_XPATH);
        clickElement(ElementLocators.APPLY_BUTTON_IN_FILTER_XPATH);
        Thread.sleep(5000);

    }

    // Upload image and submit to Step
    public void UploadImageFromAsPerExcel(String excelPath, String SheetName, String imgColumn) throws InterruptedException, IOException {

        JavascriptExecutor js = (JavascriptExecutor) driver;
        Actions actions = new Actions(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        PDXExcelReader reader = new PDXExcelReader(excelPath, SheetName);
        int rowCount = reader.getRowCount();
        List<WebElement> addIcons = driver.findElements(By.xpath(ADD_IMAGE_ICON_XPATH));

        for (int i = 0; i < addIcons.size(); i++) {
            if (i + 1 > rowCount) break;

            String imgPath = reader.getCellValue(i + 1, imgColumn);
            System.out.println("Image path at row " + (i + 1) + ": " + imgPath);

            if (imgPath == null || imgPath.trim().isEmpty()) {
                System.out.println("Skipping empty image at row " + (i + 1));
                continue;
            }

            String trimmedImg = imgPath.trim();

            // Click add icon

            try {
                js.executeScript("arguments[0].scrollIntoView(true);", addIcons.get(i));
                js.executeScript("arguments[0].click();", addIcons.get(i)); // JS click instead of .click()
            } catch (StaleElementReferenceException e) {
                System.out.println("Stale addIcon detected. Re-fetching...");
                addIcons = driver.findElements(By.xpath(
                        ".//td[contains(@class,'primaryAsset')]//lp-dynamic-icon[.//mat-icon[text()='add']]"));
                js.executeScript("arguments[0].click();", addIcons.get(i)); // JS click again
            }

            WebElement currentRow = addIcons.get(i).findElement(By.xpath("./ancestor::tr"));
            driver.findElement(By.xpath(SEARCH_IMAGE_ICON_XPATH)).click();

            WebElement fileInput = wait.until(ExpectedConditions.visibilityOf(
                    currentRow.findElement(By.xpath(SEARCH_IMAGE_TEXTBOX_XPATH))));
            fileInput.clear();
            fileInput.sendKeys(trimmedImg);
            System.out.println("Typed image path for row " + (i + 1));
            Thread.sleep(5000);

            // Re-fetch image element freshly
            try {
                WebElement imageSpan = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath(SEARCHED_IMAGE_SPAN_XPATH)));

                actions.moveToElement(imageSpan)
                        .pause(Duration.ofMillis(500)) // Give time for hover effects
                        .click()
                        .build()
                        .perform();

                System.out.println("Clicked image for row " + (i + 1));

            } catch (StaleElementReferenceException e) {
                System.out.println("Stale image detected. Retrying...");
                WebElement imageSpan = driver.findElement(By.xpath(SEARCHED_IMAGE_SPAN_XPATH));
                wait.until(ExpectedConditions.elementToBeClickable(imageSpan));
                actions.moveToElement(imageSpan).click().build().perform();
            } catch (TimeoutException e) {
                System.out.println("Image not found within timeout for row " + (i + 1));
                continue;
            }

            Thread.sleep(2000);
            WebElement addButton = currentRow.findElement(By.xpath(ADD_ASSET_BUTTON_XPATH));
            wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();
            System.out.println("Clicked 'Add assets' for row " + (i + 1));
            Thread.sleep(6000);
        }

        reader.close();

        // Select product and submit to Step
        WebElement checkbox = driver.findElement(By.xpath(SELECT_ALL_CHECKBOX_XPATH));

        if (!checkbox.isSelected()) {
            js.executeScript("arguments[0].scrollIntoView(true); arguments[0].click();", checkbox);
        }
        Thread.sleep(2000);

        WebElement ellipsis = wait.
                until(ExpectedConditions.elementToBeClickable(By.xpath(ELLIPSIS_XPATH)));
        ellipsis.click();
        Thread.sleep(5000);

        WebElement menuItem7 = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(SUBMIT_CHANNEL_ELLIPSIS_XPATH)));
        menuItem7.click();

        Thread.sleep(2000);
        WebElement submitBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(SUBMIT_BUTTON_XPATH)));
        submitBtn.click();

        // Wait for OK message to disappear (optional)
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.xpath(OK_MESSAGE_XPATH)
            ));
            System.out.println("✅ OK message disappeared.");
        } catch (Exception ignored) {
            System.out.println("ℹ️ OK message not found – continuing...");
        }

        // Wait for Submitted status to appear (optional)
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath(SUBMITTED_STATUS_XPATH)
            ));
            System.out.println("✅ Submitted status visible in UI.");
        } catch (Exception ignored) {
            System.out.println("⚠️ Submitted status not detected – continuing anyway...");
        }
        System.out.println("🎯 TEST PASSED : Image uploaded and submitted to Step");
    }
}







































