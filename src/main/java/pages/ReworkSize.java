package pages;

import constants.ElementLocators;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.NotepadReader;
import utils.PDXExcelReader;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ReworkSize extends BasePage {
    private PDXExcelReader pdxExcelReader;
    public static boolean stopNow = false;
    String username = System.getenv("PID");
    private static final String FILE_PATH = System.getProperty("user.home") + "/Desktop/StepValidCredentials.txt/";
    private String[] PDXProductIDS;

    public void clickonBuyerApprovalpage1() throws InterruptedException, IOException {
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

    public void useronDateFirstSubmittedByBrand1() throws InterruptedException {
        Thread.sleep(5000);
        waitForElementVisible(ElementLocators.DATE_FIRST_SUBMITTED_BY_BRAND_XPATH);
        clickElement(ElementLocators.DATE_FIRST_SUBMITTED_BY_BRAND_XPATH);
    }

    public void filteronDateToDescendingOrder1() throws InterruptedException {
        waitForElementVisible(ElementLocators.FILTER_DATE_TO_DESCENDING_XPATH);
        clickElement(ElementLocators.FILTER_DATE_TO_DESCENDING_XPATH);
        ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='80%'");
        Thread.sleep(3000);
    }

    public void filterPDXid1(String excelPath, String SheetName, int rowNUM, String columnHeader,
                             String columnHeader1,String columnHeader2,String columnHeader3)
            throws InterruptedException, IOException {

        PDXExcelReader reader = new PDXExcelReader(excelPath, SheetName);
        int rowCount = reader.getRowCount();
        System.out.println("Row count from sheet '" + SheetName + "': " + rowCount);

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

        System.out.println("Row count from sheet '" + SheetName + "': " + rowCount);
        List<String> corenewnessValues = new ArrayList<>();
        for (int i = 1; i <= rowCount; i++) {
            String corenewnessValue = reader.getCellValue(i, columnHeader1);
            System.out.println("Read value from row " + i + ": '" + corenewnessValue + "'");
            if (corenewnessValue == null || corenewnessValue.trim().isEmpty()
                    || corenewnessValue.trim().equalsIgnoreCase(columnHeader1.trim())) {
                continue;
            }
            corenewnessValues.add(corenewnessValue.trim());
        }
        reader.close();

        if (corenewnessValues.isEmpty()) {
            throw new RuntimeException("No Core/Newness values found in sheet: " + SheetName);
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

        WebElement scrollable = driver.findElement(By.xpath("//div[contains(@class, 'sheet-scroll-container')]"));
        //JavascriptExecutor js = (JavascriptExecutor) driver;
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
        String shortDescColumn = "Short Item Description"; // Excel header
        PDXExcelReader read = new PDXExcelReader(excelPath, SheetName);

        List<String> shortDescValues = new ArrayList<>();

        for (int i = 1; i <= rowCount; i++) {
            String value = read.getCellValue(i, shortDescColumn);
            if (value == null || value.trim().isEmpty() || value.trim().equalsIgnoreCase(shortDescColumn)) {
                continue;
            }
            shortDescValues.add(value.trim());
        }
        read.close();

        if (shortDescValues.isEmpty()) {
            throw new RuntimeException("No Short Item Description values found in sheet: " + SheetName);
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
                Thread.sleep(1000);
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

        System.out.println("Row count from sheet '" + SheetName + "': " + rowCount);
        Thread.sleep(1000);
        List<String> parentNodeValues = new ArrayList<>();

        for (int i = 1; i <= rowCount; i++) {
            String parentNodeValue = read.getCellValue(i, columnHeader2);
            System.out.println("Read value from row " + i + ": '" + parentNodeValue + "'");
            if (parentNodeValue == null || parentNodeValue.trim().isEmpty() ||
                    parentNodeValue.trim().equalsIgnoreCase(columnHeader2.trim())) {
                continue;
            }
            parentNodeValues.add(parentNodeValue.trim());
        }
        read.close();

        if (parentNodeValues.isEmpty()) {
            throw new RuntimeException("No Parent Node List values found in sheet: " + SheetName);
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
            String parentNodevalue = read.getCellValue(i, columnHeader);
            System.out.println("Read value from row " + i + ": '" + parentNodevalue + "'");
            if (parentNodevalue == null || parentNodevalue.trim().isEmpty() ||
                    parentNodevalue.trim().equalsIgnoreCase(columnHeader.trim())) {
                continue;
            }
            parentNodevalues.add(parentNodevalue.trim());
        }
        read.close();

        if (parentNodevalues.isEmpty()) {
            throw new RuntimeException("No Parent Node List values found in sheet: " + SheetName);
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

        WebElement hierarchyElement1 = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[@title='Hierarchy']")));

        // Scroll into view (optional if element is not visible)
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", hierarchyElement1);

        // Click the element
        hierarchyElement1.click();
        js.executeScript("arguments[0].scrollLeft += 400;", scrollable);
        Thread.sleep(2000);

        //PDXExcelReader reader = new PDXExcelReader(excelPath, "SheetName");


        // ✅ Scroll to PRODUCT TYPE header
        WebElement inputField1 = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//span[contains(@title,'PRODUCT TYPE')])[1]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", inputField1);

        // ✅ Read Excel values
        System.out.println("Row count from sheet '" + SheetName + "': " + rowCount);
        List<String> productTypeValues = new ArrayList<>();
        String normalizedHeader = columnHeader3.trim();

        for (int i = 1; i <= rowCount; i++) {
            String productTypeValue = read.getCellValue(i, normalizedHeader);
            System.out.println("Read value from row " + i + ": '" + productTypeValue + "'");
            if (productTypeValue == null || productTypeValue.trim().isEmpty()
                    || productTypeValue.trim().equalsIgnoreCase(normalizedHeader)) {
                continue; // skip blank/header rows
            }
            productTypeValues.add(productTypeValue.trim());
        }
        read.close();

        if (productTypeValues.isEmpty()) {
            throw new RuntimeException("No PRODUCT TYPE values found in sheet: " + SheetName);
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
        // ✅ Iterate and update each ro

       /* driver.findElement(By.xpath("//div[text()='Unfreeze panes']")).click();
        Thread.sleep(2000);*/

//        WebElement threeDots = driver.findElement(By.xpath("//i[text()='more_horiz']"));
//        threeDots.click();
//        Thread.sleep(1000);
//        WebElement scrollable2 = driver.findElement(By.xpath("//div[contains(@class, 'sheet-scroll-container')]"));
//        js.executeScript("arguments[0].scrollLeft = arguments[0].scrollWidth;", scrollable2);
        WebElement strokeno = driver.findElement(By.xpath("//div[text()='Stroke No. Calculation']"));
        strokeno.click();
        Thread.sleep(1000);

        try {
            pricing1(excelPath, SheetName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        {
            System.out.println("✅ All Buyer Approval products processed. Proceeding to Asset Approval...");
        }

        {
//            System.out.println("⚠️ Some Buyer Approval products were skipped or failed. Proceeding to Asset Approval...");
        }

        // Simulate click anywhere to dismiss overlay
        actions.moveByOffset(10, 10).click().perform();
        Thread.sleep(5000);

        /*
        // Asset approval
        try {
            //WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            WebElement navbar = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@id='stibo-element-navbar-logo']")));
            navbar.click();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            waitForElementVisible(ElementLocators.ALL_USERS);
            clickElement(ElementLocators.ALL_USERS);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            WebElement assetApprovalBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@title='Asset Approval']")));
            assetApprovalBtn.click();


            String columnName = "BrandID";
            applyBrandFilterBulk1
                    (SheetName, columnName);
            Thread.sleep(6000);
            WebElement navbar2 = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@id='stibo-element-navbar-logo']")));
            try {
                navbar2.click();
            } catch (ElementClickInterceptedException e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", navbar2);
            }
            Thread.sleep(3000);

        } catch (Exception e) {
            System.out.println("❌ Asset Approval failed: " + e.getMessage());
        }

        // Attribute approval
        try {
           // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
            waitForElementVisible(ElementLocators.ALL_USERS);
            clickElement(ElementLocators.ALL_USERS);
            Thread.sleep(8000);
            WebElement attributeApprovalBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@title='Attribute Approval']")));
            attributeApprovalBtn.click();
            Thread.sleep(5000);

            PDXProductId1(excelPath, SheetName, rowNUM, columnHeader);

            System.out.println("✅ Test completed successfully. All operations finished.");
            System.out.println("✅ Test completed successfully. All steps passed.");

            //throw new io.cucumber.java.PendingException("test stopped intentionally after successful completion");

        } catch (Exception ignored) {
            System.out.println("Attribute Approval page not found.  not Continuing...");
        } */
    }


public void pricing1(String excelPath, String SheetName) throws Exception {

            WebElement scrollable = driver.findElement(By.xpath("(//div[contains(@class, 'sheet-scroll-container')])[2]"));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollLeft = arguments[0].scrollWidth;", scrollable);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
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
            String costPriceColumn = "Cost Price"; // Excel header
            PDXExcelReader read = new PDXExcelReader(excelPath, SheetName);
            int rowCount = read.getRowCount();
            List<String> costPriceValues = new ArrayList<>();

            for (int i = 1; i <= rowCount; i++) {
                String value = read.getCellValue(i, costPriceColumn);
                if (value == null || value.trim().isEmpty() || value.trim().equalsIgnoreCase(costPriceColumn)) {
                    continue;
                }
                costPriceValues.add(value.trim());
            }
            read.close();

            if (costPriceValues.isEmpty()) {
                throw new RuntimeException("No Cost Price values found in sheet: " + SheetName);
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
                String value = read.getCellValue(i, "VAT Rate");
                if (value == null || value.trim().isEmpty() || value.equalsIgnoreCase("VAT Rate")) {
                    continue;
                }
                vatRateValues.add(value.trim());
            }
            read.close();

            if (vatRateValues.isEmpty()) {
                throw new RuntimeException("No VAT Rate values found in sheet: " + SheetName);
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
            // ✅ Post-loop cleanup for last-row overlay
       /* try {
            if (!driver.findElements(By.cssSelector("div.sheet-edit-mode-editor-overlay-panel")).isEmpty()) {
                actions.sendKeys(Keys.ESCAPE).perform();
                wait.until(ExpectedConditions.invisibilityOfElementLocated(
                        By.cssSelector("div.sheet-edit-mode-editor-overlay-panel")));
            }
        } catch (TimeoutException ignored) {
            actions.sendKeys(Keys.ESCAPE).perform();
        }*/
            // 1. Click on the "Default View" dropdown
            WebElement dropdown = driver.findElement(By.xpath("//div[text()='Default View']"));
            dropdown.click();
            Thread.sleep(1000); // Optional: wait for the dropdown to open

            // 2. Select the "Mandatory Attribute" option
            WebElement option = driver.findElement(By.xpath("//div[text()='Mandatory Attribute']"));
            option.click();
            Thread.sleep(1000);
            // 1. Click the three dots icon
            WebElement threeDots = driver.findElement(By.xpath("//i[text()='more_horiz']"));
            threeDots.click();
            Thread.sleep(500); // Optional: wait for the menu to appear

            // 2. Click the "Assign Size range" option
            WebElement assignSizeRange = driver.findElement(By.xpath("//div[text()='Assign Size range']"));
            assignSizeRange.click();
            Thread.sleep(500);
            threeDots.click();

            WebElement submitEvent = driver.findElement(By.xpath("//div[text()='Submit event']"));
            submitEvent.click();
            Thread.sleep(500);

            // 1. Wait for the popup to appear
            WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement popupTextArea = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[@class='gwt-DialogBox portal-popup']") // Adjust this XPath based on your actual popup structure
            ));
            popupTextArea.click();
            WebElement textarea = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//textarea[@class='gwt-TextArea FormFieldWidget']")));
            textarea.sendKeys("Product Returned - Waiting for Asset(WA)");

            Thread.sleep(1000);

            // 3. Click the OK button
            try {
                WebElement okButton = driver.findElement(By.xpath("//span[text()='OK']")); // Adjust text if needed
                okButton.click();
                Thread.sleep(1000);

                System.out.println("Clicked OK . Stopping further execution.");
                return;


            } catch (Exception e) {
                // ✅ Log the error but do not fail the test
                System.out.println("Handled exception during OK  " + e.getMessage());
                return;
            }
        }

public void clickOnElementUsingJS(By locator) {
    WebElement element = driver.findElement(locator);
    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript("arguments[0].click();", element);

}

//ASSET APPROVAL(StrokeNumber)
public void applyBrandFilterBulk1(String SheetName, String columnHeader) throws InterruptedException, IOException {

    String excelPath = System.getenv("EXCEL_PATH2");
    PDXExcelReader reader = new PDXExcelReader(excelPath, SheetName);
    int rowCount = reader.getRowCount();

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    Actions actions = new Actions(driver);
    JavascriptExecutor js = (JavascriptExecutor) driver;

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
            By.xpath("(//span[@title='Brand Ref ID'])[1]")));
    filterHeader.click();
    Thread.sleep(2000);

    WebElement filterOption = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//select[contains(@class, 'sortOptionsListBox')]//option[@value='Include only']")));
    filterOption.click();
    Thread.sleep(2000);

    WebElement textArea = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//textarea[@placeholder='Value or text']")));
    textArea.clear();
    textArea.sendKeys(allBrandIDs);
    Thread.sleep(3000);

    WebElement applyBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//button[.//span[normalize-space()='Apply filter']]")));

    if (applyBtn.isEnabled()) {
        wait.until(ExpectedConditions.elementToBeClickable(applyBtn));
        actions.moveToElement(applyBtn).click().perform();
        System.out.println("✅ Applied filter for all BrandIDs");
        Thread.sleep(10000);


        // Select All
        try {
            WebElement selectAll = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[text()='Select All']")));
            selectAll.click();
            System.out.println("✅ Selected all filtered items");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("❌ Select All failed: " + e.getMessage());
        }

        // Submit
        WebElement submit1 = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[text()='Submit']")));
        submit1.click();
        Thread.sleep(1000);

        WebElement text1 = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//textarea[@class='gwt-TextArea FormFieldWidget']")));
        text1.click();
        text1.sendKeys("Attribute approval");
        Thread.sleep(1000);

        WebElement ok1 = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[text()='OK']")));
        ok1.click();
        Thread.sleep(5000);

        System.out.println("🎯 Finished bulk processing and returned to main screen.");
    } else {
        // ✅ Apply Filter is disabled → click navbar logo and return to main screen
        System.out.println("⚠ Apply filter is disabled. Clicking navbar logo to return to main screen...");


        try {
            // Click filter header again to close the panel
            WebElement clearfilter = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[.//span[normalize-space()='Clear filter']]")));
            clearfilter.click();
            Thread.sleep(2000);

            System.out.println("✅ Closed filter panel successfully.");
        } catch (Exception e) {
            System.out.println("❌ Failed to close filter panel: " + e.getMessage());
        }

        // Scroll to navbar logo
        WebElement navbarLogo = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[@id='stibo-element-navbar-logo']")));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", navbarLogo);
        Thread.sleep(500);

        // Click using JavaScript to bypass overlay issues
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", navbarLogo);
        System.out.println("✅ Clicked navbar logo successfully.");
        Thread.sleep(2000);


    }
}


public void clickOnElementUsingJS1(By locator) throws InterruptedException {
    WebElement element = driver.findElement(locator);
    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript("arguments[0].click();", element);
    Thread.sleep(2000);
}

//ATTRIBUTE APPROVAL
public void PDXProductId1(String excelPath, String SheetName, int rowNUM, String columnHeader) throws InterruptedException, IOException {
    PDXExcelReader reader = new PDXExcelReader(excelPath, SheetName);
    int rowCount = reader.getRowCount();

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    Actions actions = new Actions(driver);
    JavascriptExecutor js = (JavascriptExecutor) driver;

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
    // Clear previous filter if any
        /*try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement clearFilterBtn = shortWait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(., 'Clear filter')]")));
            clearFilterBtn.click();
            System.out.println("🧹 Cleared previous filter.");
        } catch (Exception e) {
            System.out.println("⚠️ No filter to clear or skipped due to timeout.");
        }*/

    // Apply filter using "Include only"
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
    textArea.sendKeys(allProductIDs);
    Thread.sleep(5000);

    WebElement applyBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//button[.//span[normalize-space()='Apply filter']]")));
    if (applyBtn.isEnabled()) {
        wait.until(ExpectedConditions.elementToBeClickable(applyBtn));
        actions.moveToElement(applyBtn).click().perform();
        System.out.println("✅ Applied filter for all BrandIDs");
        Thread.sleep(10000);
        try {
            WebElement selectAll = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[text()='Select All']")));
            selectAll.click();
            System.out.println("✅ Selected all filtered items");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("❌ Select All failed: " + e.getMessage());
        }

        WebElement submit1 = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[text()='Submit']")));
        submit1.click();
        Thread.sleep(1000);
        WebElement text1 = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//textarea[@class='gwt-TextArea FormFieldWidget']")));
        text1.click();
        text1.sendKeys("Attribute approval");
        Thread.sleep(1000);

        WebElement ok1 = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[text()='OK']")));
        ok1.click();
        Thread.sleep(5000);

        try {
            WebElement closePopup = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//i[@class='material-icons portal-alert-popup-close-box__button']")));
            closePopup.click();
            System.out.println("✅ Popup closed successfully");

            WebElement navbarLogo = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[@id='stibo-element-navbar-logo']")));

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
                WebElement clearfilter = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[.//span[normalize-space()='Clear filter']]")));
                clearfilter.click();
                Thread.sleep(2000);

                System.out.println("✅ Closed filter panel successfully.");
            } catch (Exception e) {
                System.out.println("❌ Failed to close filter panel: " + e.getMessage());
            }

            // Scroll to navbar logo
            WebElement navbarLogo = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[@id='stibo-element-navbar-logo']")));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", navbarLogo);
            Thread.sleep(500);

            // Click using JavaScript to bypass overlay issues
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", navbarLogo);
            System.out.println("✅ Clicked navbar logo successfully.");
            Thread.sleep(2000);
        }
    }
}




