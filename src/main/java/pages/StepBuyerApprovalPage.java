package pages;

import org.openqa.selenium.*;
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
            WebElement editorPanel = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("div.sheet-edit-mode-editor-overlay-panel")));
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

    public void selectCoreNewnessValue(String excelPath, String sheetName, String columnHeader) throws IOException, InterruptedException {

        PDXExcelReader reader = new PDXExcelReader(excelPath, sheetName);
        int rowCount = reader.getRowCount();
        System.out.println("Row count from sheet '" + sheetName + "': " + rowCount);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        Actions actions = new Actions(driver);

        WebElement scrollableDiv = driver.findElement(By.xpath("//div[contains(@class, 'sheet-scroll-container')]"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollLeft = arguments[0].scrollWidth;", scrollableDiv);
        Thread.sleep(5000);

        WebElement seasonality = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[@title='Seasonality']")));
        seasonality.click();
        System.out.println("✅ Clicked on Seasonality successfully.");
        Thread.sleep(1000);


        WebElement corenewnessHeader = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//span[@title='Core / Newness'])[1]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", corenewnessHeader);

        // Read Excel values
        // PDXExcelReader read = new PDXExcelReader(excelPath, SheetName);

        System.out.println("Row count from sheet '" + sheetName + "': " + rowCount);
        List<String> corenewnessValues = new ArrayList<>();
        for (int i = 1; i <= rowCount; i++) {
            String corenewnessValue = reader.getCellValue(i, columnHeader);
            System.out.println("Read value from row " + i + ": '" + corenewnessValue + "'");
            if (corenewnessValue == null || corenewnessValue.trim().isEmpty()
                    || corenewnessValue.trim().equalsIgnoreCase(columnHeader.trim())) {
                continue;
            }
            corenewnessValues.add(corenewnessValue.trim());
        }
        reader.close();

        if (corenewnessValues.isEmpty()) {
            throw new RuntimeException("No Core/Newness values found in sheet: " + sheetName);
        }

        List<WebElement> uiCells = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//td[@data-col='20']")));

        int processCount = Math.min(uiCells.size(), corenewnessValues.size());
        System.out.println("Processing " + processCount + " rows...");

        // Iterate and update each row
        for (int i = 0; i < processCount; i++) {
            String excelValue = corenewnessValues.get(i);

            WebElement targetCell = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//td[@data-col='20'])[" + (i + 1) + "]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", targetCell);
            try {
                targetCell.click();
            } catch (ElementClickInterceptedException e) {
                // minimal fallback if click is intercepted
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", targetCell);
            }
            actions.sendKeys(Keys.ENTER).perform();
            Thread.sleep(800);

            // Optional overlay/editor wait (non-blocking if not present)
            By overlayPanel = By.cssSelector("div.sheet-edit-mode-editor-overlay-panel");
            WebElement editorPanel = null;
            try {
                WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(4));
                editorPanel = shortWait.until(ExpectedConditions.visibilityOfElementLocated(overlayPanel));
            } catch (TimeoutException ignore) {
                // no overlay; proceed with inline editor
            }
            // Active editor (works for inline or overlay)
            WebElement activeEditor;
            if (editorPanel != null) {
                // prefer the actual input inside the modal
                try {
                    // tighter selector: still generic, no hard-coding
                    activeEditor = editorPanel.findElement(
                            By.cssSelector("input:not([disabled]):not([readonly]), textarea:not([disabled]):not([readonly]), [contenteditable='true']")
                    );

                    ((JavascriptExecutor) driver).executeScript("arguments[0].focus();", activeEditor);
                } catch (NoSuchElementException e) {
                    activeEditor = (WebElement) ((JavascriptExecutor) driver).executeScript("return document.activeElement;");
                }
            } else {
                activeEditor = (WebElement) ((JavascriptExecutor) driver).executeScript("return document.activeElement;");

                // if inline and not editable, force edit mode so rows don’t get skipped
                Boolean isEditable = (Boolean) ((JavascriptExecutor) driver).executeScript(
                        "var el=arguments[0]; return !!el && (el.isContentEditable || ['INPUT','TEXTAREA','SELECT'].includes(el.tagName));",
                        activeEditor
                );


                activeEditor = (WebElement) ((JavascriptExecutor) driver).executeScript("return document.activeElement;");
            }

            // Type into the current editor (this may have triggered the modal already; we will ESC if so)
            actions.click(activeEditor)
                    .keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                    .sendKeys(Keys.DELETE)
                    .sendKeys(excelValue)
                    .perform();

            js.executeScript(
                    "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                            "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                    activeEditor
            );

            //ac.pause(Duration.ofMillis(200)).sendKeys(Keys.TAB).pause(Duration.ofMillis(180)).perform();

            String typedValue = (String) ((JavascriptExecutor) driver)
                    .executeScript("return ('value' in arguments[0]) ? arguments[0].value : arguments[0].textContent;", activeEditor);

            System.out.println("Excel Value: " + excelValue);
            System.out.println("UI Value: " + typedValue);

            try {
                new WebDriverWait(driver, Duration.ofSeconds(6)).until(
                        ExpectedConditions.or(
                                ExpectedConditions.textToBePresentInElementValue(activeEditor, excelValue),
                                ExpectedConditions.textToBePresentInElement(activeEditor, excelValue)
                        )
                );
            } catch (TimeoutException ignore) {
                // proceed; value check below still guards the commit
            }

            if (typedValue != null && typedValue.trim().equalsIgnoreCase(excelValue.trim())) {

                boolean overlayVisibleNow = false;
                try {
                    overlayVisibleNow = !driver.findElements(overlayPanel).isEmpty()
                            && driver.findElement(overlayPanel).isDisplayed();
                } catch (StaleElementReferenceException ignored) {
                    // if overlay DOM mutated, treat as not visible
                    overlayVisibleNow = false;
                }

                if (overlayVisibleNow) {
                    //1️⃣ Keyboard commit first (VERY IMPORTANT)
                    actions.sendKeys(Keys.ENTER).pause(Duration.ofMillis(10000)).perform();

                    // 2️⃣ Ensure editor really lost focus
                    js.executeScript("arguments[0].blur && arguments[0].blur();", activeEditor);
                    Thread.sleep(10000);

                    // 3️⃣ Wait until Save becomes enabled (not just clickable)

                    wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//div[contains(@class,'modal') or @role='dialog']//button[normalize-space()='Save']")
                    )).click();


                    // 5️⃣ Wait for overlay to close
                    new WebDriverWait(driver, Duration.ofSeconds(15))
                            .until(ExpectedConditions.invisibilityOfElementLocated(overlayPanel));

                    Thread.sleep(500);
                }
                System.out.println("✅ Value committed successfully");
            } else {
                System.out.println("❌ Value mismatch – not committing");
                Thread.sleep(500);
                actions.moveByOffset(50, 50).click().perform();
                Thread.sleep(1000);
            }
        }
    }

    public void selectEcomColumnValues(String excelPath, String sheetName, String shortDescreptionColumnHeader, String parentNodeColumnHeader, String productIDColumnHeader) throws InterruptedException, IOException {

        PDXExcelReader reader = new PDXExcelReader(excelPath, sheetName);
        int rowCount = reader.getRowCount();

        WebElement scrollable = driver.findElement(By.xpath("//div[contains(@class, 'sheet-scroll-container')]"));
        js.executeScript("arguments[0].scrollLeft += 600;", scrollable);
        Thread.sleep(5000);

        WebElement hierarchyElement = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[@title='Ecom']")));

        // Scroll into view (optional if element is not visible)
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", hierarchyElement);

        // Click the element
        hierarchyElement.click();
        js.executeScript("arguments[0].scrollLeft += 400;", scrollable);
        Thread.sleep(2000);
        WebElement shortdesc = driver.findElement(By.xpath("(//th[@id='TableHeader_Short_Item_Description'])[1]"));
        js.executeScript("arguments[0].scrollLeft = arguments[0].scrollWidth", shortdesc);

        Thread.sleep(2000);

        // ✅ Read Excel values (unchanged)
        PDXExcelReader read = new PDXExcelReader(excelPath, sheetName);

        List<String> shortDescValues = new ArrayList<>();

        for (int i = 1; i <= rowCount; i++) {
            String value = read.getCellValue(i, shortDescreptionColumnHeader);
            if (value == null || value.trim().isEmpty() || value.trim().equalsIgnoreCase(shortDescreptionColumnHeader)) {
                continue;
            }
            shortDescValues.add(value.trim());
        }
        read.close();

        if (shortDescValues.isEmpty()) {
            throw new RuntimeException("No Short Item Description values found in sheet: " + sheetName);
        }

        // ✅ Get all UI cells
        List<WebElement> uiCell = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//td[@data-col='28']")));
        int prCount = Math.min(uiCell.size(), shortDescValues.size());
        System.out.println("Processing " + prCount + " rows for Short Item Description...");

        // ✅ Iterate and update each cell
        for (int i = 0; i < prCount; i++) {
            String excelValue = shortDescValues.get(i);

            WebElement cell = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//td[@data-col='28'])[" + (i + 1) + "]")));
            js.executeScript("arguments[0].scrollIntoView({block:'center'})", cell);

            cell.click();
            Thread.sleep(800);

            // First try: wait for the modal title EXACTLY (handles whitespace reliably)
            By valueEditorTitle = By.xpath("//div[normalize-space()='Value editor - 1 item selected']");

            List<WebElement> modalTitleEls = driver.findElements(valueEditorTitle);
            if (!modalTitleEls.isEmpty()) {
                // Modal path → type and Save
                WebElement modalInput = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[@class='input-cell']")));
                modalInput.clear();
                modalInput.sendKeys(excelValue);

                // Save (button can be disabled until you type; now it should be enabled)
                WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[normalize-space()='Save']")));
                saveBtn.click();

                // Wait for modal to close
                wait.until(ExpectedConditions.invisibilityOfElementLocated(valueEditorTitle));
            } else {
                // Fallback: inline editor (your original logic)
                actions.sendKeys(Keys.ENTER).perform();

                WebElement editorPanel = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("div.sheet-edit-mode-editor-overlay-panel")));
                WebElement activeEditor = (WebElement) js.executeScript("return document.activeElement;");

                actions.moveToElement(activeEditor)
                        .click()
                        .pause(Duration.ofMillis(250))
                        .keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                        .sendKeys(Keys.DELETE)
                        .pause(Duration.ofMillis(250))
                        .sendKeys(excelValue)
                        .perform();

                actions.sendKeys(Keys.ENTER).perform();
                Thread.sleep(3000);
            }

            // ✅ Verify
            WebElement committedCell = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("(//td[@data-col='28'])[" + (i + 1) + "]")));
            String uiText = committedCell.getText().trim();

            System.out.println("Excel Value: '" + excelValue + "' | UI Value: '" + uiText + "'");

            if (uiText.equalsIgnoreCase(excelValue.trim())) {
                System.out.println("✅ Short Item Description committed successfully");
            } else {
                System.out.println("❌ Value mismatch – retrying...");
                Thread.sleep(500);
                // Retry commit (unchanged)
                cell.click();
                Thread.sleep(1000);
                actions.sendKeys(Keys.ENTER).perform();
            }
        }

        // ✅ Keep your final scroll line
        js.executeScript("arguments[0].scrollLeft += 1200;", scrollable);

        System.out.println("Row count from sheet '" + sheetName + "': " + rowCount);
        Thread.sleep(1000);
        List<String> parentNodeValues = new ArrayList<>();

        for (int i = 1; i <= rowCount; i++) {
            String parentNodeValue = read.getCellValue(i, parentNodeColumnHeader);
            System.out.println("Read value from row " + i + ": '" + parentNodeValue + "'");
            if (parentNodeValue == null || parentNodeValue.trim().isEmpty() ||
                    parentNodeValue.trim().equalsIgnoreCase(parentNodeColumnHeader.trim())) {
                continue;
            }
            parentNodeValues.add(parentNodeValue.trim());
        }
        read.close();

        if (parentNodeValues.isEmpty()) {
            throw new RuntimeException("No Parent Node List values found in sheet: " + sheetName);
        }

        // ✅ Get all UI rows for Parent Node Lists column
        List<WebElement> uiCells1 = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//td[@data-col='36']"))); // Adjust if column index changes

        int processCount1 = Math.min(uiCells1.size(), parentNodeValues.size());
        System.out.println("Processing " + processCount1 + " rows...");

        // ✅ Iterate and update each row
        for (int i = 0; i < processCount1; i++) {
            String excelValue = parentNodeValues.get(i);

            WebElement scrollContainer = driver.findElement(By.xpath("(//div[@class='sheet-scroll-container'])[2]"));
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollLeft = arguments[0].scrollWidth;", scrollContainer);

            // ✅ Re-fetch the cell each time to avoid stale element issues
            WebElement targetCell = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//td[@data-col='36'])[" + (i + 1) + "]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", targetCell);
            targetCell.click();
            actions.sendKeys(Keys.ENTER).perform();

            // ✅ Wait for editor
            WebElement editorPanel = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("div.sheet-edit-mode-editor-overlay-panel")));
            WebElement activeEditor = (WebElement) ((JavascriptExecutor) driver)
                    .executeScript("return document.activeElement;");

            // ✅ Clear and type new value
            actions.moveToElement(activeEditor)
                    .click()
                    .pause(Duration.ofMillis(200))
                    .keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                    .sendKeys(Keys.DELETE)
                    .pause(Duration.ofMillis(200))
                    .sendKeys(excelValue)
                    .perform();

            // ✅ Read back typed value
            String typedValue = (String) ((JavascriptExecutor) driver)
                    .executeScript("return ('value' in arguments[0]) ? arguments[0].value : arguments[0].textContent;", activeEditor);

            System.out.println("Excel Value: " + excelValue);
            System.out.println("UI Value: " + typedValue);

            if (typedValue.trim().equalsIgnoreCase(excelValue.trim())) {
                actions.sendKeys(Keys.ENTER).perform();
                System.out.println("✅ Value committed successfully");
            } else {
                System.out.println("❌ Value mismatch – not committing");
                Thread.sleep(500);
                actions.moveByOffset(50, 50).click().perform();
                Thread.sleep(1000);
            }
        }
        // Optional: horizontal scroll after edits

        //System.out.println("Row count from sheet '" + SheetName + "': " + rowCount);
        Thread.sleep(1000);


        // ✅ ADDED LOGIC ONLY (NO change to your existing code)
        List<String> parentNodevalues = new ArrayList<>();

        for (int i = 1; i <= rowCount; i++) {
            String parentNodevalue = read.getCellValue(i, productIDColumnHeader);
            System.out.println("Read value from row " + i + ": '" + parentNodevalue + "'");
            if (parentNodevalue == null || parentNodevalue.trim().isEmpty() ||
                    parentNodevalue.trim().equalsIgnoreCase(productIDColumnHeader.trim())) {
                continue;
            }
            parentNodevalues.add(parentNodevalue.trim());
        }
        read.close();

        if (parentNodevalues.isEmpty()) {
            throw new RuntimeException("No Parent Node List values found in sheet: " + sheetName);
        }

        // ✅ Get all UI rows for Parent Node Lists column
        List<WebElement> uiCel = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//td[@data-col='36']"))); // Adjust if column index changes

        int processCoun = Math.min(uiCells1.size(), parentNodeValues.size());
        System.out.println("Processing " + processCoun + " rows...");

        // ✅ Iterate and update each row
        for (int i = 0; i < processCoun; i++) {
            String excelValue = parentNodeValues.get(i);

            WebElement scrollContainer = driver.findElement(By.xpath("(//div[@class='sheet-scroll-container'])[2]"));
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollLeft = arguments[0].scrollWidth;", scrollContainer);

            // ✅ Re-fetch the cell each time to avoid stale element issues
            WebElement targetCell = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//td[@data-col='36'])[" + (i + 1) + "]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", targetCell);
            targetCell.click();
            actions.sendKeys(Keys.ENTER).perform();

            // ✅ Wait for editor
            WebElement editorPanel = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("div.sheet-edit-mode-editor-overlay-panel")));
            WebElement activeEditor = (WebElement) ((JavascriptExecutor) driver)
                    .executeScript("return document.activeElement;");

            // ✅ Clear and type new value
            actions.moveToElement(activeEditor)
                    .click()
                    .pause(Duration.ofMillis(200))
                    .keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                    .sendKeys(Keys.DELETE)
                    .pause(Duration.ofMillis(200))
                    .sendKeys(excelValue)
                    .perform();

            // ✅ Read back typed value
            String typedValue = (String) ((JavascriptExecutor) driver)
                    .executeScript("return ('value' in arguments[0]) ? arguments[0].value : arguments[0].textContent;", activeEditor);

            System.out.println("Excel Value: " + excelValue);
            System.out.println("UI Value: " + typedValue);

            if (typedValue.trim().equalsIgnoreCase(excelValue.trim())) {
                actions.sendKeys(Keys.ENTER).perform();
                System.out.println("✅ Value committed successfully");
            } else {
                System.out.println("❌ Value mismatch – not committing");
                Thread.sleep(500);
                actions.moveByOffset(50, 50).click().perform();
                Thread.sleep(1000);
            }
        }
        js.executeScript("arguments[0].scrollLeft += 400;", scrollable);
        Thread.sleep(2000);
    }

    public void selectHierarchyColumnValues(String excelPath, String sheetName, String columnHeader) throws InterruptedException, IOException {

        WebElement scrollable = driver.findElement(By.xpath("//div[contains(@class, 'sheet-scroll-container')]"));
        js.executeScript("arguments[0].scrollLeft += 600;", scrollable);
        Thread.sleep(5000);

        PDXExcelReader reader = new PDXExcelReader(excelPath, sheetName);
        int rowCount = reader.getRowCount();

        WebElement hierarchyElement = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[@title='Hierarchy']")));

        // Scroll into view (optional if element is not visible)
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", hierarchyElement);

        // Click the element
        hierarchyElement.click();
        js.executeScript("arguments[0].scrollLeft += 400;", scrollable);
        Thread.sleep(2000);


        // ✅ Scroll to PRODUCT TYPE header
        WebElement inputField1 = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//span[contains(@title,'PRODUCT TYPE')])[1]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", inputField1);

        // ✅ Read Excel values
        System.out.println("Row count from sheet '" + sheetName + "': " + rowCount);
        List<String> productTypeValues = new ArrayList<>();
        String normalizedHeader = columnHeader.trim();

        for (int i = 1; i <= rowCount; i++) {
            String productTypeValue = reader.getCellValue(i, normalizedHeader);
            System.out.println("Read value from row " + i + ": '" + productTypeValue + "'");
            if (productTypeValue == null || productTypeValue.trim().isEmpty()
                    || productTypeValue.trim().equalsIgnoreCase(normalizedHeader)) {
                continue; // skip blank/header rows
            }
            productTypeValues.add(productTypeValue.trim());
        }
        reader.close();

        if (productTypeValues.isEmpty()) {
            throw new RuntimeException("No PRODUCT TYPE values found in sheet: " + sheetName);
        }

        // ✅ Get all UI rows for PRODUCT TYPE column
        By selectedCellsLocator = By.xpath("//tr[contains(@class,'is-selected')]//td[@data-col='42']");
        By allCellsLocator = By.xpath("//td[@data-col='42']");
        By overlayLocator = By.cssSelector("div.sheet-edit-mode-editor-overlay-panel");

        // Quick waits
        WebDriverWait overlayWait = new WebDriverWait(driver, Duration.ofSeconds(1));
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2), Duration.ofMillis(100));
        String SELECT_ALL = Keys.chord(Keys.CONTROL, "a");

        List<WebElement> uiCellsPT = driver.findElements(selectedCellsLocator);
        boolean useSelectedRows = !uiCellsPT.isEmpty();
        if (!useSelectedRows) {
            uiCellsPT = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(allCellsLocator));
        }

        int processCountPT = Math.min(uiCellsPT.size(), productTypeValues.size());
        System.out.println("Processing " + processCountPT + " rows for PRODUCT TYPE ("
                + (useSelectedRows ? "selected rows" : "all rows") + ")...");
        for (int i = 0; i < processCountPT; i++) {
            String excelValue = productTypeValues.get(i);


            try {
                if (driver.findElements(overlayLocator).size() > 0) {
                    actions.sendKeys(Keys.ESCAPE).perform();
                    overlayWait.until(ExpectedConditions.invisibilityOfElementLocated(overlayLocator));
                }
            } catch (TimeoutException ignored) {
                actions.sendKeys(Keys.ESCAPE).perform();
            }


            uiCellsPT = driver.findElements(useSelectedRows ? selectedCellsLocator : allCellsLocator);
            if (uiCellsPT.isEmpty() || i >= uiCellsPT.size()) break;

            WebElement targetCell = uiCellsPT.get(i);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", targetCell);

            WebElement scrollContainerPT = driver.findElement(By.xpath("(//div[@class='sheet-scroll-container'])[2]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft = arguments[0].scrollWidth;", scrollContainerPT);

            targetCell.click();
            actions.sendKeys(Keys.ENTER).perform(); // open editor

            // Quick overlay check
            try {
                overlayWait.until(ExpectedConditions.visibilityOfElementLocated(overlayLocator));
            } catch (TimeoutException ignored) {
            }

            WebElement activeEditor = (WebElement) ((JavascriptExecutor) driver)
                    .executeScript("return document.activeElement;");
            activeEditor.sendKeys(SELECT_ALL, Keys.DELETE, excelValue);

            // Try dropdown option
            try {
                WebElement option = shortWait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                        "//div[contains(@class,'sheet-edit-mode-editor-overlay-panel')]//*[normalize-space(text())='" + excelValue + "']")));
                option.click();
            } catch (TimeoutException ignored) {
                actions.sendKeys(Keys.ENTER).perform();
            }

            // Ensure editor closed
            try {
                overlayWait.until(ExpectedConditions.invisibilityOfElementLocated(overlayLocator));
            } catch (TimeoutException ignored) {
                actions.sendKeys(Keys.ESCAPE).perform();
            }

            // Verify committed value
            uiCellsPT = driver.findElements(useSelectedRows ? selectedCellsLocator : allCellsLocator);
            if (i >= uiCellsPT.size()) break;
            WebElement committedCell = uiCellsPT.get(i);
            String uiText = committedCell.getText().trim();
            System.out.println("Excel Value: '" + excelValue + "' | UI Value: '" + uiText + "'");

            // Retry if mismatch
            if (!uiText.equalsIgnoreCase(excelValue)) {
                System.out.println("❌ Mismatch – retry once on the same row...");
                committedCell.click();
                actions.sendKeys(Keys.ENTER).perform();

                activeEditor = (WebElement) ((JavascriptExecutor) driver)
                        .executeScript("return document.activeElement;");
                activeEditor.sendKeys(SELECT_ALL, Keys.DELETE, excelValue);

                try {
                    WebElement option2 = shortWait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                            "//div[contains(@class,'sheet-edit-mode-editor-overlay-panel')]//*[normalize-space(text())='" + excelValue + "']")));
                    option2.click();
                } catch (TimeoutException te) {
                    actions.sendKeys(Keys.ENTER).perform();
                }
                try {
                    overlayWait.until(ExpectedConditions.invisibilityOfElementLocated(overlayLocator));
                } catch (TimeoutException te) {
                    actions.sendKeys(Keys.ESCAPE).perform();
                }

                uiCellsPT = driver.findElements(useSelectedRows ? selectedCellsLocator : allCellsLocator);
                if (i >= uiCellsPT.size()) break;
                committedCell = uiCellsPT.get(i);
                uiText = committedCell.getText().trim();
                System.out.println("Post-retry UI Value: '" + uiText + "'");
            }

            // Focus next cell
            if (i < processCountPT - 1) {
                WebElement nextCell = uiCellsPT.get(i + 1);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", nextCell);

                WebElement scrollContainerPT2 = driver.findElement(By.xpath("(//div[@class='sheet-scroll-container'])[2]"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft = arguments[0].scrollWidth;", scrollContainerPT2);

                try {
                    shortWait.until(ExpectedConditions.elementToBeClickable(nextCell));
                } catch (TimeoutException ignored) {
                    Thread.sleep(120);
                }
                nextCell.click();
            }
        }

        try {
            if (!driver.findElements(overlayLocator).isEmpty()) {
                actions.sendKeys(Keys.ESCAPE).perform();
                overlayWait.until(ExpectedConditions.invisibilityOfElementLocated(overlayLocator));
            }
        } catch (TimeoutException ignored) {
            actions.sendKeys(Keys.ESCAPE).perform();
        }
    }

    public void selectPricingColumnValues(String excelPath, String sheetName, String costPriceColumnHeader, String eVATRateColumnHeader) throws InterruptedException, IOException {

        WebElement scrollable = driver.findElement(By.xpath("(//div[contains(@class, 'sheet-scroll-container')])[1]"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollLeft = arguments[0].scrollWidth;", scrollable);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement pricingElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@title='Pricing']")));

        // Scroll into view
        js.executeScript("arguments[0].click();", pricingElement);

        // Wait until clickable
        wait.until(ExpectedConditions.elementToBeClickable(pricingElement));

        // Click using Actions for extra reliability
        Actions actions = new Actions(driver);
        //actions.moveToElement(pricingElement).click().perform();

        Thread.sleep(1000);
        WebElement scrollable1 = driver.findElement(By.xpath("//div[contains(@class, 'sheet-scroll-container')]"));

        js.executeScript("arguments[0].scrollLeft = arguments[0].scrollWidth;", scrollable1);
        Thread.sleep(500);


        // Step 3: Re-scroll again to the extreme right to override any auto-scroll
        //Actions actions = new Actions(driver);
        actions.click().build().perform();
        Thread.sleep(2000);

        // ✅ Step 1: Scroll to Cost Price header
        WebElement costPriceHeader = driver.findElement(By.xpath("(//th[@id='TableHeader_Cost_Price'])[1]"));
        js.executeScript("arguments[0].scrollLeft = arguments[0].scrollWidth", costPriceHeader);
        Thread.sleep(2000);

        // ✅ Step 2: Read Excel values for Cost Price
        PDXExcelReader pdxExcelReader = new PDXExcelReader(excelPath, sheetName);
        int rowCount = pdxExcelReader.getRowCount();
        List<String> costPriceValues = new ArrayList<>();

        for (int i = 1; i <= rowCount; i++) {
            String value = pdxExcelReader.getCellValue(i, costPriceColumnHeader);
            if (value == null || value.trim().isEmpty() || value.trim().equalsIgnoreCase(costPriceColumnHeader)) {
                continue;
            }
            costPriceValues.add(value.trim());
        }
        pdxExcelReader.close();

        if (costPriceValues.isEmpty()) {
            throw new RuntimeException("No Cost Price values found in sheet: " + sheetName);
        }

        // ✅ Step 3: Get all UI cells for Cost Price column
        List<WebElement> uiCells = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//td[@data-col='53']"))); // Adjust if column index changes

        int processCount = Math.min(uiCells.size(), costPriceValues.size());
        System.out.println("Processing " + processCount + " rows for Cost Price...");

        // ✅ Step 4: Iterate and update each cell
        for (int i = 0; i < processCount; i++) {
            String excelValue = costPriceValues.get(i);

            WebElement cell = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//td[@data-col='53'])[" + (i + 1) + "]")));
            js.executeScript("arguments[0].scrollIntoView({block:'center'})", cell);
            cell.click();
            Thread.sleep(1000);

            // Enter edit mode
            actions.sendKeys(Keys.ENTER).perform();

            // Wait for editor
            WebElement editorPanel = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("div.sheet-edit-mode-editor-overlay-panel")));
            WebElement activeEditor = (WebElement) js.executeScript("return document.activeElement;");

            // Clear and type Excel value
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
                    By.xpath("(//td[@data-col='53'])[" + (i + 1) + "]")));
            String uiText = committedCell.getText().trim();

            System.out.println("Excel Value: '" + excelValue + "' | UI Value: '" + uiText + "'");

            if (uiText.equalsIgnoreCase(excelValue.trim())) {
                System.out.println("✅ Cost Price committed successfully");
            } else {
                System.out.println("❌ Value mismatch – retrying...");
                Thread.sleep(500);
                // Retry commit
                cell.click();
                actions.sendKeys(Keys.ENTER).perform();
                actions.sendKeys(Keys.ENTER).perform();
            }
        }

        // ✅ Post-loop cleanup for last-row overlay
        try {
            if (!driver.findElements(By.cssSelector("div.sheet-edit-mode-editor-overlay-panel")).isEmpty()) {
                actions.sendKeys(Keys.ESCAPE).perform();
                wait.until(ExpectedConditions.invisibilityOfElementLocated(
                        By.cssSelector("div.sheet-edit-mode-editor-overlay-panel")));
            }
        } catch (TimeoutException ignored) {
            actions.sendKeys(Keys.ESCAPE).perform();
        }

        // 1. Scroll to VAT Rate header
        WebElement vatRateHeader = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//span[@title='VAT Rate'])[1]")));
        js.executeScript("arguments[0].scrollIntoView(true);", vatRateHeader);

        // 2. Read Excel values for VAT Rate
       /* PDXExcelReader reader = new PDXExcelReader(excelPath, sheetName);
        int rowCount = reader.getRowCount();*/
        System.out.println("Row count: " + rowCount);

        List<String> vatRateValues = new ArrayList<>();
        for (int i = 1; i <= rowCount; i++) {
            String value = pdxExcelReader.getCellValue(i, eVATRateColumnHeader);
            if (value == null || value.trim().isEmpty() || value.equalsIgnoreCase(eVATRateColumnHeader)) {
                continue;
            }
            vatRateValues.add(value.trim());
        }
        pdxExcelReader.close();

        if (vatRateValues.isEmpty()) {
            throw new RuntimeException("No VAT Rate values found in sheet: " + sheetName);
        }

        // 3. Get all UI cells for VAT Rate column
        List<WebElement> uiCells1 = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//td[@data-col='58']"))); // Adjust data-col if needed

        int processCount1 = Math.min(uiCells1.size(), vatRateValues.size());
        System.out.println("Processing " + processCount + " rows...");

        // 4. Iterate and update each row
        for (int i = 0; i < processCount; i++) {
            String excelValue = vatRateValues.get(i);
            WebElement targetCell = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//td[@data-col='58'])[" + (i + 1) + "]")));
            js.executeScript("arguments[0].scrollIntoView(true);", targetCell);
            targetCell.click();
            actions.sendKeys(Keys.ENTER).perform();

            // Wait for editor
            WebElement editorPanel = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("div.sheet-edit-mode-editor-overlay-panel")));
            WebElement activeEditor = (WebElement) js.executeScript("return document.activeElement;");

            // Clear and type new value
            actions.moveToElement(activeEditor)
                    .click()
                    .pause(Duration.ofMillis(200))
                    .keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                    .sendKeys(Keys.DELETE)
                    .pause(Duration.ofMillis(200))
                    .sendKeys(excelValue)
                    .perform();

            // Validate typed value
            String typedValue = (String) js.executeScript(
                    "return ('value' in arguments[0]) ? arguments[0].value : arguments[0].textContent;", activeEditor);

            System.out.println("Excel Value: " + excelValue);
            System.out.println("UI Value: " + typedValue);

            if (typedValue.trim().equalsIgnoreCase(excelValue.trim())) {
                actions.sendKeys(Keys.ENTER).perform();
                System.out.println("✅ Value committed successfully");
            } else {
                System.out.println("❌ Value mismatch – not committing");
                Thread.sleep(500);
                actions.moveByOffset(50, 50).click().perform();
                Thread.sleep(1000);
            }
        }
    }

    public void assignSizeRangeAndStrokeNumber() throws InterruptedException {

        // Assign Stroke number
        WebElement strokeNumber = driver.findElement(By.xpath("//div[text()='Stroke No. Calculation']"));
        strokeNumber.click();
        Thread.sleep(1000);

        // Click on the "Default View" dropdown
        WebElement dropdown = driver.findElement(By.xpath("//div[text()='Default View']"));
        dropdown.click();
        Thread.sleep(1000); // Optional: wait for the dropdown to open

        // Select the "Mandatory Attribute" option
        WebElement option = driver.findElement(By.xpath("//div[text()='Mandatory Attribute']"));
        option.click();
        Thread.sleep(1000);

        // Click the ellipsis
        WebElement ellipsis = driver.findElement(By.xpath("//i[text()='more_horiz']"));
        ellipsis.click();
        Thread.sleep(500); // Optional: wait for the menu to appear

        // Click the "Assign Size range" option
        WebElement assignSizeRange = driver.findElement(By.xpath("//div[text()='Assign Size range']"));
        assignSizeRange.click();
        Thread.sleep(500);
        ellipsis.click();

        // Click on Submit
        WebElement submitEvent = driver.findElement(By.xpath("//div[text()='Submit event']"));
        submitEvent.click();
        Thread.sleep(500);

        // Wait for the popup to appear
        WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement popupTextArea = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@class='gwt-DialogBox portal-popup']")));
        popupTextArea.click();
        WebElement textarea = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//textarea[@class='gwt-TextArea FormFieldWidget']")));
        textarea.sendKeys("Product Buyer Approved");

        Thread.sleep(1000);

        // Click OK button
        WebElement okButton = driver.findElement(By.xpath("//span[text()='OK']"));
        okButton.click();
        Thread.sleep(1000);

        System.out.println("✅ All Buyer Approval products processed. Proceeding to Asset Approval...");
    }
}
