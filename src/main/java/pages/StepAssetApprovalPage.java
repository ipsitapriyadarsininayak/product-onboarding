package pages;

import constants.ElementLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.PDXExcelReader;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static constants.ElementLocators.*;

public class StepAssetApprovalPage extends BasePage {

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    Actions actions = new Actions(driver);
    JavascriptExecutor js = (JavascriptExecutor) driver;

    public void gotoAssetApprovalAndVerify() {

        try {
            WebElement homePageLogo = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(M_AND_S_LOGO_HOME_XPATH)));
            homePageLogo.click();
            Thread.sleep(5000);
            waitForElementVisible(ElementLocators.ALL_USERS);
            clickElement(ElementLocators.ALL_USERS);
            Thread.sleep(10000);
            WebElement assetApprovalBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(ASSET_APPROVAL_LINK_XPATH)));
            assetApprovalBtn.click();
        } catch (RuntimeException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void filterProductsThenDoAssetApproval(String excelPath, String SheetName, String columnHeader) throws InterruptedException, IOException {

        PDXExcelReader reader = new PDXExcelReader(excelPath, SheetName);
        int rowCount = reader.getRowCount();

        List<String> BrandIDs = new ArrayList<>();
        for (int i = 1; i <= rowCount; i++) {
            String BrandID = reader.getCellValue(i, columnHeader);
            if (BrandID != null && !BrandID.trim().isEmpty()) {
                BrandIDs.add(BrandID.trim());
            }
        }
        reader.close();

        if (BrandIDs.isEmpty()) {
            throw new RuntimeException("No BrandIDs found in Excel column: " + columnHeader);
        }

        // Concatenate all BrandIDs into a single string
        String allBrandIDs = String.join(",", BrandIDs); // Use "\n" or "," based on UI support

        // Apply filter
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("waitScreenOverlayGlass")));

        WebElement filterHeader = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(BRAND_REF_ID_HEADER_XPATH)));
        filterHeader.click();
        Thread.sleep(2000);

        WebElement filterOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(FILTER_BY_INCLUDE_ONLY_XPATH)));
        filterOption.click();
        Thread.sleep(2000);

        WebElement textArea = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(FILTER_TEXTBOX_XPATH)));
        textArea.clear();
        textArea.sendKeys(allBrandIDs);
        Thread.sleep(3000);

        WebElement applyBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath(APPLY_FILTER_BUTTON_XPATH)));

        if (applyBtn.isEnabled()) {
            wait.until(ExpectedConditions.elementToBeClickable(applyBtn));
            actions.moveToElement(applyBtn).click().perform();
            System.out.println("✅ Applied filter for all BrandIDs");
            Thread.sleep(10000);

            // Clear 'Message to Supplier' field before proceeding to Approve
            try {
                WebElement selectAll = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath(SELECT_ALL_BUTTON_XPATH)));
                selectAll.click();
                System.out.println("✅ Selected all filtered items");
                Thread.sleep(1000);

                // Scroll slightly to bring column into view
                WebElement scrollable = driver.findElement(By.xpath("//div[contains(@class, 'sheet-scroll-container')]"));
                js.executeScript("arguments[0].scrollLeft += 100;", scrollable);
                Thread.sleep(3000);

                // Scroll header into view
                WebElement messageToSupplierHeader = driver.findElement(By.xpath(MESSAGE_TO_SUPPLIER_HEADER_ASSET_XPATH));
                js.executeScript("arguments[0].scrollIntoView({block:'center'})", messageToSupplierHeader);
                Thread.sleep(2000);

                // Get all UI cells for Message To Supplier column
                List<WebElement> uiCells = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.xpath(MESSAGE_TO_SUPPLIER_COLUMN_ASSET_XPATH)));

                int processCount = uiCells.size();
                System.out.println("Processing " + processCount + " rows for Message To Supplier...");

                // Iterate and clear each cell
                for (int i = 0; i < processCount; i++) {

                    WebElement cell = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("(//td[@data-col='16'])[" + (i + 1) + "]")));
                    js.executeScript("arguments[0].scrollIntoView({block:'center'})", cell);
                    cell.click();
                    actions.moveToElement(cell).click().perform();
                    Thread.sleep(1000);

                    // Enter edit mode
                    actions.sendKeys(Keys.ENTER).perform();

                    // Wait for editor
                    WebElement activeEditor = (WebElement) js.executeScript("return document.activeElement;");

                    // Clear and type Excel value
                    assert activeEditor != null;
                    actions.moveToElement(activeEditor)
                            .click()
                            .pause(Duration.ofMillis(200))
                            .keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                            .sendKeys(Keys.DELETE)
                            .pause(Duration.ofMillis(200))
                            .perform();

                    // Commit
                    actions.sendKeys(Keys.ENTER).perform();
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                System.out.println("❌ Select All failed or Message not cleared : " + e.getMessage());
            }

            // Submit
            WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(APPROVAL_SUBMIT_BUTTON_XPATH)));
            submit.click();
            Thread.sleep(1000);

            WebElement approvalText = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(APPROVAL_TEXT_AREA_XPATH)));
            approvalText.click();
            approvalText.sendKeys("Asset approved");
            Thread.sleep(1000);

            WebElement ok = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(APPROVAL_OK_BUTTON_XPATH)));
            ok.click();
            Thread.sleep(5000);

            System.out.println("🎯 Finished bulk processing and returned to main screen.");
        } else {
            // ✅ Apply Filter is disabled → click navbar logo and return to main screen
            System.out.println("⚠ Apply filter is disabled. Clicking navbar logo to return to main screen...");


            try {
                // Click filter header again to close the panel
                WebElement clearFilter = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath(CLEAR_FILTER_BUTTON_XPATH)));
                clearFilter.click();
                Thread.sleep(2000);

                System.out.println("✅ Closed filter panel successfully.");
            } catch (Exception e) {
                System.out.println("❌ Failed to close filter panel: " + e.getMessage());
            }

            // Scroll to navbar logo
            WebElement homePageLogo = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath(M_AND_S_LOGO_HOME_XPATH)));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", homePageLogo);
            Thread.sleep(500);

            // Click using JavaScript to bypass overlay issues
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", homePageLogo);
            System.out.println("✅ Clicked navbar logo successfully.");
            Thread.sleep(2000);
        }
    }
}
