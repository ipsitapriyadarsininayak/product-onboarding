package pages;

import constants.ElementLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.NotepadReader;
import utils.PDXExcelReader;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


public class ReworkInStepPage extends BasePage {

    private PDXExcelReader pdxExcelReader;
    public static boolean stopNow = false;
    String username = System.getenv("PID");
    private static final String FILE_PATH = System.getProperty("user.home") + "/Desktop/StepValidCredentials.txt/";
    private String[] PDXProductIDS;

    public void clickonBuyerApprovalpage() throws InterruptedException, IOException {
        String password = NotepadReader.getDecodePassword(FILE_PATH);

        waitForElementVisible(ElementLocators.STEP_USERNAME_INPUT_XPATH);
        enterText(ElementLocators.STEP_USERNAME_INPUT_XPATH, username);
        waitForElementVisible(ElementLocators.STEP_PASSWORD_INPUT_XPATH);

        enterText(ElementLocators.STEP_PASSWORD_INPUT_XPATH, password);
        Thread.sleep(5000);
        waitForElementVisible(ElementLocators.STEP_LOGIN_BUTTON_XPATH);
        clickElement(ElementLocators.STEP_LOGIN_BUTTON_XPATH);
        Thread.sleep(5000);
        waitForElementVisible(ElementLocators.ALL_USERS);
        clickElement(ElementLocators.ALL_USERS);
        Thread.sleep(6000);
        waitForElementVisible(ElementLocators.BUYERS_APPROVAL_LINK_XPATH);
        clickElement(ElementLocators.BUYERS_APPROVAL_LINK_XPATH);
    }

    public void useronDateFirstSubmittedByBrand() throws InterruptedException {
        Thread.sleep(5000);
        waitForElementVisible(ElementLocators.DATE_FIRST_SUBMITTED_BY_BRAND_XPATH);
        clickElement(ElementLocators.DATE_FIRST_SUBMITTED_BY_BRAND_XPATH);
    }

    public void filteronDateToDescendingOrder() throws InterruptedException {
        waitForElementVisible(ElementLocators.FILTER_DATE_TO_DESCENDING_XPATH);
        clickElement(ElementLocators.FILTER_DATE_TO_DESCENDING_XPATH);
        ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='80%'");
        Thread.sleep(3000);
    }

    public void filterPDXid(String excelPath, String SheetName, int rowNUM, String columnHeader) throws InterruptedException, IOException {

        PDXExcelReader reader = new PDXExcelReader(excelPath, SheetName);
        int rowCount = reader.getRowCount();
        System.out.println("Row count from sheet '" + SheetName + "': " + rowCount);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        Actions actions = new Actions(driver);

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

        // Open filter UI ONCE
        //  WebElement filterHeader = wait.until); // small pause if UI is sluggishWebElement filterHeader = wait.until(ExpectedConditions.elementToBeClickable(
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("waitScreenOverlayGlass")));

        // Open filter UI
        WebElement filterHeader = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//th//span[@title='PDX Product ID']")));
        filterHeader.click();
        Thread.sleep(2000);
        WebElement filter = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//select[contains(@class, 'sortOptionsListBox')]//option[@value='Include only']")));
        filter.click();
        Thread.sleep(2000);
        WebElement textArea = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//textarea[@placeholder='Value or text']")));
        textArea.clear();
        textArea.sendKeys(allIdsText);


        // (Optional) small pause if UI is sluggish
        Thread.sleep(2000);


        // Re-locate Apply button after entering product ID
        WebElement applyBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[.//span[normalize-space()='Apply filter']]")));

        // Wait until it's clickable
        if (applyBtn.isEnabled()) {
            wait.until(ExpectedConditions.elementToBeClickable(applyBtn));
            actions.moveToElement(applyBtn).click().perform();
            System.out.println("Clicked Apply filter for Product ID: " + allIdsText);
            Thread.sleep(10000);
            //((JavascriptExecutor) driver).executeScript("document.body.style.zoom='75%'");
            List<WebElement> rows = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                    By.xpath("//table//tr//td")));
            Thread.sleep(2000);

            if (rows.isEmpty()) {
                System.out.println("No results found after applying PDX Product IDs. Skipping further actions...");

            } else {

                try {
                    WebElement selectAll = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//div[text()='Select All']")));
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
                WebElement navbarLogo = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[@id='stibo-element-navbar-logo']")));
                navbarLogo.click();
                Thread.sleep(2000);
            } catch (Exception e) {
                System.out.println("Failed to click navbar logo: " + e.getMessage());
            }
            System.out.println("Row count from sheet '" + SheetName + "': " + rowCount);
        }

        // Inside the for loop, before scrollToSeasonality()
        if (!driver.findElements(By.xpath("//div[text()='Attribute Approval']")).isEmpty()) {
            System.out.println("✅ Attribute Approval page detected during Buyer Approval processing. Skipping remaining Buyer Approval actions.");
            return; // Exit loop, but continue with Asset Approval and Attribute Approval steps
        }

        try {
            // Scroll to the end first
            scrolltoSeasonality();
            System.out.println("Scrolled successfully!");
            WebElement proposalStatusHeader = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[@title='Proposal Status']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", proposalStatusHeader);

            System.out.println("Proposal Status column is now visible!");

            // Continue with your logic (e.g., read Excel and update cells)
            // ...

        } catch (InterruptedException e) {
            System.err.println("Scroll was interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("Error while scrolling or locating Proposal Status: " + e.getMessage());
        }

        try {
            int rowNum = 1;
            proposalstatus(excelPath, SheetName, 1, "Proposal Status");
        } catch (Exception e) {
            System.out.println("Failed to double-click Proposal Status: " + e.getMessage());
        }

        try{
        // Call your method
        updateMessageToSupplier(excelPath, SheetName);
        System.out.println("✅ Message To Supplier update completed successfully!");
        } catch (Exception e) {
        System.err.println("❌ Error while updating Message To Supplier: " + e.getMessage());
        e.printStackTrace();
        }

        try {
            // Call your method
            updateReasonsForRework(excelPath, SheetName);
            System.out.println("✅ Reasons For Rework updated successfully!");
        } catch (Exception e) {
            System.err.println("❌ Error while updating Reasons For Rework: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            // Call the submitEvent method
            submitEvent("message to supplier");
            System.out.println("Event submitted successfully.");
        } catch (Exception e) {
            System.out.println("Error occurred while submitting event: " + e.getMessage());
            e.printStackTrace(); // Optional: for debugging
        }

    }
    //
    public void scrolltoSeasonality() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        //Thread.sleep(2000);

        WebElement scrollableDiv = driver.findElement(By.xpath("//div[contains(@class, 'sheet-scroll-container')]"));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript("arguments[0].scrollLeft += 200;", scrollableDiv);

        Thread.sleep(2000);
    }

    public static void proposalstatus(String excelPath, String SheetName, int rowNum, String columnHeader) throws InterruptedException, IOException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        Actions ac = new Actions(driver);

        // Scroll to Proposal Status header
        WebElement proposalStatusHeader = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//span[@title='Proposal Status'])[1]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", proposalStatusHeader);

        // Read Excel values
        PDXExcelReader read = new PDXExcelReader(excelPath, SheetName);
        int rowCount = read.getRowCount();
        System.out.println("Row count from sheet '" + SheetName + "': " + rowCount);

        List<String> proposalStatusValues = new ArrayList<>();
        for (int i = 1; i <= rowCount; i++) {
            String proposalStatusValue = read.getCellValue(i, columnHeader);
            System.out.println("Read value from row " + i + ": '" + proposalStatusValue + "'");
            if (proposalStatusValue == null || proposalStatusValue.trim().isEmpty() ||
                    proposalStatusValue.trim().equalsIgnoreCase(columnHeader.trim())) {
                continue;
            }
            proposalStatusValues.add(proposalStatusValue.trim());
        }
        read.close();

        if (proposalStatusValues.isEmpty()) {
            throw new RuntimeException("No Proposal Status values found in sheet: " + SheetName);
        }

        // Get all UI rows for Proposal Status column
        List<WebElement> uiCells = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//td[@data-col='10']")));

        int processCount = Math.min(uiCells.size(), proposalStatusValues.size());
        System.out.println("Processing " + processCount + " rows...");

        // Iterate and update each row
        for (int i = 0; i < processCount; i++) {
            String excelValue = proposalStatusValues.get(i);
            WebElement targetCell = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//td[@data-col='10'])[" + (i + 1) + "]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", targetCell);
            targetCell.click();
            ac.sendKeys(Keys.ENTER).perform();

            // Wait for editor
            WebElement editorPanel = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("div.sheet-edit-mode-editor-overlay-panel")));
            WebElement activeEditor = (WebElement) ((JavascriptExecutor) driver)
                    .executeScript("return document.activeElement;");

            // Clear and type new value
            ac.moveToElement(activeEditor)
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

            if (typedValue.trim().equalsIgnoreCase(excelValue.trim())) {
                ac.sendKeys(Keys.ENTER).perform();
                System.out.println("✅ Value committed successfully");
            } else {
                System.out.println("❌ Value mismatch – not committing");
                Thread.sleep(500);
                ac.moveByOffset(50, 50).click().perform();
                Thread.sleep(1000);
            }
        }
    }

    public void updateMessageToSupplier(String excelPath, String sheetName) throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        Actions actions = new Actions(driver);
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Scroll slightly to bring column into view
        WebElement scrollable = driver.findElement(By.xpath("//div[contains(@class, 'sheet-scroll-container')]"));
        js.executeScript("arguments[0].scrollLeft += 100;", scrollable);
        Thread.sleep(3000);

        // Scroll header into view
        WebElement columnHeader = driver.findElement(By.xpath("(//th[@id='TableHeader_Message_To_Supplier'])[1]"));
        js.executeScript("arguments[0].scrollIntoView({block:'center'})", columnHeader);
        Thread.sleep(2000);

        // Read Excel values
        List<String> messageValues = getStrings(excelPath, sheetName);

        // Get all UI cells for Message To Supplier column
        List<WebElement> uiCells = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//td[@data-col='11']")));

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
                System.out.println("✅ Message To Supplier committed successfully");
            } else {
                System.out.println("❌ Value mismatch – retrying...");
                Thread.sleep(500);
                cell.click();
                actions.sendKeys(Keys.ENTER).perform();
                actions.sendKeys(Keys.ENTER).perform();
            }
        }
    }

    private static List<String> getStrings(String excelPath, String sheetName) throws IOException {
        String messageColumn = "Message To Supplier"; // Excel header
        PDXExcelReader read = new PDXExcelReader(excelPath, sheetName);
        int rowCount = read.getRowCount();
        List<String> messageValues = new ArrayList<>();

        for (int i = 1; i <= rowCount; i++) {
            String value = read.getCellValue(i, messageColumn);
            if (value == null || value.trim().isEmpty() || value.trim().equalsIgnoreCase(messageColumn)) {
                continue;
            }
            messageValues.add(value.trim());
        }
        read.close();

        if (messageValues.isEmpty()) {
            throw new RuntimeException("No Message To Supplier values found in sheet: " + sheetName);
        }
        return messageValues;
    }

    public void updateReasonsForRework(String excelPath, String sheetName) throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Actions actions=new Actions(driver);

        // Scroll slightly to bring column into view
        WebElement scrollable = driver.findElement(By.xpath("//div[contains(@class, 'sheet-scroll-container')]"));
        js.executeScript("arguments[0].scrollLeft += 200;", scrollable);
        Thread.sleep(2000);

        // Scroll header into view
        WebElement columnHeader = driver.findElement(By.xpath("(//span[@title='Reasons For Rework'])[1]"));
        js.executeScript("arguments[0].scrollIntoView({block:'center'})", columnHeader);
        Thread.sleep(2000);

        // Read Excel values
        String reasonColumn = "Reasons For Rework"; // Excel header
        PDXExcelReader read = new PDXExcelReader(excelPath, sheetName);
        int rowCount = read.getRowCount();
        List<String> reasonValues = new ArrayList<>();

        for (int i = 1; i <= rowCount; i++) {
            String value = read.getCellValue(i, reasonColumn);
            if (value == null || value.trim().isEmpty() || value.trim().equalsIgnoreCase(reasonColumn)) {
                continue;
            }
            reasonValues.add(value.trim());
        }
        read.close();

        if (reasonValues.isEmpty()) {
            throw new RuntimeException("No Reasons For Rework values found in sheet: " + sheetName);
        }

        // Get all UI cells for Reasons For Rework column
        List<WebElement> uiCells = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//td[@data-col='12']")));

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

// OR type any alphabet to open dropdown
            // Select option from dropdown


            try {
                // WAIT until Value Editor dropdown becomes VISIBLE
                WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//select[contains(@class,'dropdown')]")));

                // Use Select ONLY after visibility
                Select select = new Select(dropdown);
                select.selectByVisibleText(excelValue);

            } catch (Exception e) {
                // Fallback: keyboard-based selection (React-safe)
                actions.sendKeys(excelValue)
                        .sendKeys(Keys.ENTER)
                        .perform();
            }



            // Click Save button
            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[text()='Save']")));
            saveButton.click();
            Thread.sleep(1000);

            System.out.println("Excel Value: '" + excelValue + "' selected and saved successfully!");
        }
    }

    public void submitEvent(String message) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));

        try {
            WebElement menu = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//i[@class='material-icons'])[6]")));
            menu.click();

            WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[text()='Submit event']")));
            submit.click();

            WebElement textarea = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//textarea[@class='gwt-TextArea FormFieldWidget']")));
            textarea.clear();
            textarea.sendKeys(message);

            WebElement ok = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[text()='OK']")));
            ok.click();

        } catch (Exception e) {
            System.out.println("Error while submitting event: " + e.getMessage());
        }
    }
public void click_masterdata(String excelPath, String SheetName, int rowNUM, String columnHeader) throws InterruptedException, IOException {
    waitForElementVisible(ElementLocators.MASTER_DATA_ICON_XPATH);
    clickElement(ElementLocators.MASTER_DATA_ICON_XPATH);
    System.out.println("MASTER_DATA_ICON clicked");
    Thread.sleep(5000);
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
    reader.close();

    if (excelIds == null || excelIds.isEmpty()) {
        throw new IllegalArgumentException("❌ No IDs found in Excel column");
    }

    // --- Step 2: Click on "List of values" ---
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    WebElement listOfValuesButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//span[text()='List of values']")));
    listOfValuesButton.click();

    // --- Step 3: Wait for the text area and paste all IDs ---
    WebElement textArea = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//input[@placeholder='Enter values']")));

    // Join all IDs with comma
    String allIds = String.join(",", excelIds);

    // Paste into text area
    textArea.sendKeys(allIds);

    // Optional: Submit if needed
    textArea.sendKeys(Keys.ENTER);
    Thread.sleep(2000);
    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript("document.body.style.zoom='80%'");
    WebElement availableinchanel = wait.until(ExpectedConditions.elementToBeClickable
            (By.xpath("//span[text()='Available in channel']")));
    availableinchanel.click();
    Thread.sleep(2000);

    waitForElementVisible(ElementLocators.PRODUCT_LIFECYCLE_DATES_XPATH);
    clickElement(ElementLocators.PRODUCT_LIFECYCLE_DATES_XPATH);

    waitForElementVisible(ElementLocators.START_DATE_XPATH);
    clickElement(ElementLocators.START_DATE_XPATH);

    waitForElementVisible(ElementLocators.CURRENT_DATE_XPATH);
    clickElement(ElementLocators.CURRENT_DATE_XPATH);
    clickElement(ElementLocators.CURRENT_DATE_XPATH);


    WebElement applyButton = driver.findElement(By.xpath(ElementLocators.APPLY_BUTTON_XPATH1));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", applyButton);
    waitForElementVisible(ElementLocators.APPLY_BUTTON_XPATH1);
    clickElement(ElementLocators.APPLY_BUTTON_XPATH1);
    Thread.sleep(10000);
    waitForElementVisible(ElementLocators.FILTER_ICON_XPATH);
    clickElement(ElementLocators.FILTER_ICON_XPATH);
    WebElement clearfilter = wait.until(ExpectedConditions.elementToBeClickable
            (By.xpath("//span[text()='Clear filters']")));
    clearfilter.click();
    Thread.sleep(3000);
    waitForElementVisible(ElementLocators.FILTER_ICON_XPATH);
    clickElement(ElementLocators.FILTER_ICON_XPATH);
    waitForElementVisible(ElementLocators.PRODUCT_LIFECYCLE_DATES_XPATH);
    clickElement(ElementLocators.PRODUCT_LIFECYCLE_DATES_XPATH);

    waitForElementVisible(ElementLocators.START_DATE_XPATH);
    clickElement(ElementLocators.START_DATE_XPATH);

    waitForElementVisible(ElementLocators.CURRENT_DATE_XPATH);
    clickElement(ElementLocators.CURRENT_DATE_XPATH);
    clickElement(ElementLocators.CURRENT_DATE_XPATH);
    WebElement applyButton1 = driver.findElement(By.xpath(ElementLocators.APPLY_BUTTON_XPATH1));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", applyButton1);
    waitForElementVisible(ElementLocators.APPLY_BUTTON_XPATH1);
    clickElement(ElementLocators.APPLY_BUTTON_XPATH1);
    Thread.sleep(10000);
}
}










