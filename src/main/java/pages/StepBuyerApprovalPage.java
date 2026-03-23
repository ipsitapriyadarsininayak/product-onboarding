package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.PDXExcelReader;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static constants.ElementLocators.*;

public class StepBuyerApprovalPage extends BasePage {

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    Actions actions = new Actions(driver);
    JavascriptExecutor js = (JavascriptExecutor) driver;

    public void filterProductsByPDXProductID(String excelPath, String SheetName, String columnHeader) throws IOException, InterruptedException {

        PDXExcelReader reader = new PDXExcelReader(excelPath, SheetName);
        int rowCount = reader.getRowCount();
        System.out.println("Row count from sheet '" + SheetName + "': " + rowCount);

        List<String> PDXProductIDs = new ArrayList<>();

        for (int i = 1; i <= rowCount; i++) {
            String PDXProductID;

            if (SheetName.equalsIgnoreCase("consignment")) {
                PDXProductID = reader.getCellValue(i, columnHeader); // logic for Sheet1
            } else if (SheetName.equalsIgnoreCase("wholesale")) {
                PDXProductID = reader.getCellValue(i, columnHeader); // logic for Sheet2 (can be different if needed)
            } else {
                PDXProductID = reader.getCellValue(i, columnHeader); // default logic
            }

            if (PDXProductID == null
                    || PDXProductID.isEmpty()
                    || PDXProductID.equalsIgnoreCase(columnHeader)) {
                // skip blanks or accidental header rows
                continue;
            }

            // ✅ Add only once
            PDXProductIDs.add(PDXProductID);
        }
        reader.close();

        PDXProductIDs = PDXProductIDs.stream().distinct().toList();
        // Optional: log or assert
        if (PDXProductIDs.isEmpty()) {
            throw new RuntimeException("No PDX Product IDs found in Excel column: " + columnHeader +
                    " (sheet: " + SheetName + ")");
        }
        System.out.println("✅ Loaded " + PDXProductIDs.size() + " PDX Product IDs from Excel.");

        String allIdsText = String.join(",", PDXProductIDs);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("waitScreenOverlayGlass")));

        // Open filter in 'PDX Product ID' column and performs filter
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
        textArea.sendKeys(allIdsText);

        // (Optional) small pause if UI is sluggish
        Thread.sleep(2000);

        // Locate Apply button after entering product ID
        WebElement applyBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath(APPLY_FILTER_BUTTON_XPATH)));

        // Wait until Apply button clickable
        if (applyBtn.isEnabled()) {
            wait.until(ExpectedConditions.elementToBeClickable(applyBtn));
            actions.moveToElement(applyBtn).click().perform();
            System.out.println("Clicked Apply filter for Product ID: " + allIdsText);
            Thread.sleep(10000);

            List<WebElement> rows = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                    By.xpath("//table//tr//td")));
            Thread.sleep(2000);

            if (rows.isEmpty()) {
                System.out.println("No results found after applying PDX Product IDs. Skipping further actions...");
            } else {
                // ✅ Select all product rows
                try {
                    WebElement selectAll = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath(SELECT_ALL_BUTTON_XPATH)));
                    selectAll.click();
                    System.out.println("Selected all rows after filtering PDX Product IDs list.");

                } catch (Exception e) {
                    System.out.println("Failed to click Select All: " + e.getMessage());
                }
            }

        } else {
            // ✅ Apply Filter is disabled → click navbar logo and continue
            System.out.println("Apply filter is disabled after entering PDX Product IDs. Clicking navbar logo...");
            try {
                // Click on Home
                WebElement navbarLogo = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath(M_AND_S_LOGO_HOME_XPATH)));
                navbarLogo.click();
                Thread.sleep(2000);
            } catch (Exception e) {
                System.out.println("Failed to click Home : " + e.getMessage());
            }
            System.out.println("Row count from sheet '" + SheetName + "': " + rowCount);
        }
    }

    public void setWaitingForAssetStatus(String excelPath, String SheetName, String columnHeader) {

        try {
            WebElement scrollableDiv = driver.findElement(By.xpath(SCROLLBAR_DIV_XPATH));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollLeft += 200;", scrollableDiv);

            Thread.sleep(2000);

            // Scroll to Proposal Status header
            WebElement proposalStatusHeader = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(PROPOSAL_STATUS_HEADER_XPATH)));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", proposalStatusHeader);
            System.out.println("Proposal Status column is now visible!");
        } catch (Exception e) {
            System.err.println("Error while scrolling or locating Proposal Status: " + e.getMessage());
        }

        try {
            // Read Excel values
            PDXExcelReader reader = new PDXExcelReader(excelPath, SheetName);
            int rowCount = reader.getRowCount();
            System.out.println("Row count from sheet '" + SheetName + "': " + rowCount);

            List<String> proposalStatusValues = new ArrayList<>();
            for (int i = 1; i <= rowCount; i++) {
                String proposalStatusValue = reader.getCellValue(i, columnHeader);
                System.out.println("Read value from row " + i + ": '" + proposalStatusValue + "'");
                if (proposalStatusValue == null || proposalStatusValue.trim().isEmpty() ||
                        proposalStatusValue.trim().equalsIgnoreCase(columnHeader.trim())) {
                    continue;
                }
                proposalStatusValues.add(proposalStatusValue.trim());
            }
            reader.close();

            if (proposalStatusValues.isEmpty()) {
                throw new RuntimeException("No Proposal Status values found in sheet: " + SheetName);
            }

            // Get all UI rows for Proposal Status column
            List<WebElement> uiCells = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                    By.xpath(PROPOSAL_STATUS_COLUMN_XPATH)));

            int processCount = Math.min(uiCells.size(), proposalStatusValues.size());
            System.out.println("Processing " + processCount + " rows...");

            // Iterate and update each row
            for (int i = 0; i < processCount; i++) {
                String excelValue = proposalStatusValues.get(i);
                WebElement targetCell = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("(//td[@data-col='10'])[" + (i + 1) + "]")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", targetCell);
                targetCell.click();
                actions.sendKeys(Keys.ENTER).perform();

                // Wait for editor
                WebElement editorPanel = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("div.sheet-edit-mode-editor-overlay-panel")));
                WebElement activeEditor = (WebElement) ((JavascriptExecutor) driver)
                        .executeScript("return document.activeElement;");

                // Clear and type new value
                actions.moveToElement(activeEditor)
                        .click()
                        .pause(Duration.ofMillis(200))
                        .keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                        .sendKeys(Keys.DELETE)
                        .pause(Duration.ofMillis(200))
                        .sendKeys(excelValue)
                        .perform();

                // Read back value
                String typedValue = (String) ((JavascriptExecutor) driver)
                        .executeScript("return ('value' in arguments[0]) ? arguments[0].value : arguments[0].textContent;", activeEditor);

                System.out.println("Excel Value: " + excelValue);
                System.out.println("UI Value: " + typedValue);

                assert typedValue != null;
                if (typedValue.trim().equalsIgnoreCase(excelValue.trim())) {
                    actions.sendKeys(Keys.ENTER).perform();
                    System.out.println("✅ Waiting For Asset(WA) status set successfully!");
                } else {
                    System.out.println("❌ Value mismatch – not committing");
                    Thread.sleep(500);
                    actions.moveByOffset(50, 50).click().perform();
                    Thread.sleep(1000);
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to select Proposal Status: " + e.getMessage());
        }
    }

    public void setMessageToSupplier(String excelPath, String SheetName, String columnHeader) throws InterruptedException, IOException {

        // Scroll slightly to bring column into view
        WebElement scrollable = driver.findElement(By.xpath("//div[contains(@class, 'sheet-scroll-container')]"));
        js.executeScript("arguments[0].scrollLeft += 100;", scrollable);
        Thread.sleep(3000);

        // Scroll header into view
        WebElement messageToSupplierHeader = driver.findElement(By.xpath(MESSAGE_TO_SUPPLIER_HEADER_XPATH));
        js.executeScript("arguments[0].scrollIntoView({block:'center'})", messageToSupplierHeader);
        Thread.sleep(2000);

        // Read Excel values
        PDXExcelReader reader = new PDXExcelReader(excelPath, SheetName);
        int rowCount = reader.getRowCount();
        System.out.println("Row count from sheet '" + SheetName + "': " + rowCount);

        List<String> messageValues = new ArrayList<>();
        for (int i = 1; i <= rowCount; i++) {
            String messageValue = reader.getCellValue(i, columnHeader);
            System.out.println("Read value from row " + i + ": '" + messageValue + "'");
            if (messageValue == null || messageValue.trim().isEmpty() ||
                    messageValue.trim().equalsIgnoreCase(columnHeader.trim())) {
                continue;
            }
            messageValues.add(messageValue.trim());
        }
        reader.close();

        if (messageValues.isEmpty()) {
            throw new RuntimeException("No Proposal Status values found in sheet: " + SheetName);
        }

        // Get all UI cells for Message To Supplier column
        List<WebElement> uiCells = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath(MESSAGE_TO_SUPPLIER_COLUMN_XPATH)));

        int processCount = Math.min(uiCells.size(), messageValues.size());
        System.out.println("Processing " + processCount + " rows for Message To Supplier...");

        // Iterate and update each cell
        for (int i = 0; i < processCount; i++) {
            String excelValue = messageValues.get(i);

            WebElement cell = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//td[@data-col='11'])[" + (i + 1) + "]")));
            js.executeScript("arguments[0].scrollIntoView({block:'center'})", cell);
            cell.click();
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
                    .sendKeys(excelValue)
                    .perform();

            // Commit
            actions.sendKeys(Keys.ENTER).perform();
            Thread.sleep(1000);

            // Verify
            WebElement committedCell = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("(//td[@data-col='11'])[" + (i + 1) + "]")));
            String uiText = committedCell.getText().trim();

            System.out.println("Excel Value: '" + excelValue + "' | UI Value: '" + uiText + "'");

            if (uiText.equalsIgnoreCase(excelValue.trim())) {
                System.out.println("✅ Message To Supplier inserted successfully");
            } else {
                System.out.println("❌ Value mismatch – retrying...");
                Thread.sleep(500);
                cell.click();
                actions.sendKeys(Keys.ENTER).perform();
            }
        }
    }

    public void selectReasonsForRework(String excelPath, String sheetName, String columnHeader) throws InterruptedException, IOException {

        // Scroll slightly to bring column into view
        WebElement scrollable = driver.findElement(By.xpath("//div[contains(@class, 'sheet-scroll-container')]"));
        js.executeScript("arguments[0].scrollLeft += 200;", scrollable);
        Thread.sleep(2000);

        // Scroll header into view
        WebElement reasonsForRework = driver.findElement(By.xpath(REASONS_FOR_REWORK_HEADER_XPATH));
        js.executeScript("arguments[0].scrollIntoView({block:'center'})", reasonsForRework);
        Thread.sleep(2000);

        // Read Excel values
        PDXExcelReader reader = new PDXExcelReader(excelPath, sheetName);
        int rowCount = reader.getRowCount();
        List<String> reasonValues = new ArrayList<>();

        for (int i = 1; i <= rowCount; i++) {
            String value = reader.getCellValue(i, columnHeader);
            if (value == null || value.trim().isEmpty() || value.trim().equalsIgnoreCase(columnHeader)) {
                continue;
            }
            reasonValues.add(value.trim());
        }
        reader.close();

        if (reasonValues.isEmpty()) {
            throw new RuntimeException("No Reasons For Rework values found in sheet: " + sheetName);
        }

        // Get all UI cells for Reasons For Rework column
        List<WebElement> uiCells = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath(REASONS_FOR_REWORK_COLUMN_XPATH)));

        int processCount = Math.min(uiCells.size(), reasonValues.size());
        System.out.println("Processing " + processCount + " rows for Reasons For Rework...");

        // Iterate and update each cell
        for (int i = 0; i < processCount; i++) {

            String excelValue = reasonValues.get(i);

            WebElement cell = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//td[@data-col='12'])[" + (i + 1) + "]")));
            js.executeScript("arguments[0].scrollIntoView({block:'center'})", cell);
            cell.click();
            Thread.sleep(1000);

            actions.sendKeys(Keys.ENTER).perform();
            Thread.sleep(1000);

            // Select option from dropdown
            try {
                // WAIT until Value Editor dropdown becomes VISIBLE
                WebElement dropdownReasonForRework = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath(REASONS_FOR_REWORK_DROPDOWN_XPATH)));

                // Use Select ONLY after visibility
                Select select = new Select(dropdownReasonForRework);
                select.selectByVisibleText(excelValue);

            } catch (Exception e) {
                // Fallback: keyboard-based selection (React-safe)
                actions.sendKeys(excelValue)
                        .sendKeys(Keys.ENTER)
                        .perform();
            }

            // Click Save button
            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(REASONS_FOR_REWORK_DROPDOWN_SAVE_BUTTON_XPATH)));
            saveButton.click();
            Thread.sleep(1000);

            System.out.println("Excel Value: '" + excelValue + "' ✅ Reasons for Rework selected and saved successfully!");
        }
    }
}
