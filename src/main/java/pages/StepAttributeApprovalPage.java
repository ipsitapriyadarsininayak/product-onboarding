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

public class StepAttributeApprovalPage extends BasePage {

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

    public void gotoAttributeApprovalAndVerify() {

        try {
            WebElement homepageLogo = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(M_AND_S_LOGO_HOME_XPATH)));
            homepageLogo.click();
            Thread.sleep(5000);
            waitForElementVisible(ElementLocators.ALL_USERS);
            clickElement(ElementLocators.ALL_USERS);
            Thread.sleep(5000);
            WebElement attributeApprovalBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(ATTRIBUTE_APPROVAL_LINK_XPATH)));
            attributeApprovalBtn.click();
            Thread.sleep(5000);
        } catch (RuntimeException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void filterProductsThenDoAttributeApproval(String excelPath, String SheetName, String columnHeader) throws InterruptedException, IOException {

        PDXExcelReader reader = new PDXExcelReader(excelPath, SheetName);
        int rowCount = reader.getRowCount();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        Actions actions = new Actions(driver);

        List<String> PDXProductIDs = new ArrayList<>();
        for (int i = 1; i <= rowCount; i++) {
            String PDXProductID = reader.getCellValue(i, columnHeader);
            if (PDXProductID != null && !PDXProductID.trim().isEmpty()) {
                PDXProductIDs.add(PDXProductID.trim());
            }
        }
        reader.close();

        if (PDXProductIDs.isEmpty()) {
            throw new RuntimeException("No PDX Product IDs found in Excel column: " + columnHeader);
        }

        // Concatenate all Product IDs into a single string
        String allProductIDs = String.join(",", PDXProductIDs); // Use "," if needed
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("waitScreenOverlayGlass")));

        // Apply filter using "Include only"
        WebElement filterHeader = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(PDX_PRODUCT_ID_HEADER_XPATH)));
        filterHeader.click();
        Thread.sleep(2000);

        WebElement filter = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(FILTER_BY_INCLUDE_ONLY_XPATH)));
        filter.click();
        Thread.sleep(2000);

        WebElement textArea = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(FILTER_TEXTBOX_XPATH)));
        textArea.clear();
        textArea.sendKeys(allProductIDs);
        Thread.sleep(5000);

        WebElement applyBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath(APPLY_FILTER_BUTTON_XPATH)));
        if (applyBtn.isEnabled()) {
            wait.until(ExpectedConditions.elementToBeClickable(applyBtn));
            actions.moveToElement(applyBtn).click().perform();
            System.out.println("✅ Applied filter for all BrandIDs");
            Thread.sleep(10000);
            try {
                WebElement selectAll = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath(SELECT_ALL_BUTTON_XPATH)));
                selectAll.click();
                System.out.println("✅ Selected all filtered items");
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println("❌ Select All failed: " + e.getMessage());
            }

            WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(APPROVAL_SUBMIT_BUTTON_XPATH)));
            submit.click();
            Thread.sleep(1000);
            WebElement text1 = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(APPROVAL_TEXT_AREA_XPATH)));
            text1.click();
            text1.sendKeys("Attribute approval");
            Thread.sleep(1000);

            WebElement ok = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(APPROVAL_OK_BUTTON_XPATH)));
            ok.click();
            Thread.sleep(5000);

            try {
                WebElement closePopup = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath(ATTRIBUTE_ALERT_POPUP_XPATH)));
                closePopup.click();
                System.out.println("✅ Popup closed successfully");

                WebElement navbarLogo = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath(M_AND_S_LOGO_HOME_XPATH)));

                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", navbarLogo);
                Thread.sleep(500);

                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", navbarLogo);
                System.out.println("✅ Clicked navbar logo successfully after closing popup.");
                Thread.sleep(2000);

            } catch (Exception e) {
                System.out.println("❌ Popup close failed or popup not found: " + e.getMessage());
            }

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
