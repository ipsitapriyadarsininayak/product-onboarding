package pages;

import constants.ElementLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

            // Select All
            try {
                WebElement selectAll = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath(SELECT_ALL_BUTTON_XPATH)));
                selectAll.click();
                System.out.println("✅ Selected all filtered items");
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println("❌ Select All failed: " + e.getMessage());
            }

            WebElement threeDots = driver.findElement(By.xpath("//i[text()='more_horiz']"));
            threeDots.click();

            // Submit
            WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(APPROVAL_SUBMIT_BUTTON_XPATH)));
            submit.click();
            Thread.sleep(1000);

            WebElement approvalText = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(APPROVAL_TEXT_AREA_XPATH)));
            approvalText.click();
            approvalText.sendKeys("Asset approval");
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
