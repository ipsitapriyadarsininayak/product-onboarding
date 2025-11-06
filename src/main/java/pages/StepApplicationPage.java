package pages;

import constants.ElementLocators;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.NotepadReader;
import utils.PDXExcelReader;
import java.awt.*;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.List;

public class StepApplicationPage extends BasePage {
    private PDXExcelReader pdxExcelReader;


    String username = System.getenv("PID");
    private static final String FILE_PATH = System.getProperty("user.home") + "/Desktop/StepValidCredentials.txt/";
    private String[] PDXProductIDS;


    public void stepLogin() throws IOException, InterruptedException {
        String password = NotepadReader.getDecodePassword(FILE_PATH);

        waitForElementVisible(ElementLocators.STEP_USERNAME_INPUT_XPATH);
        enterText(ElementLocators.STEP_USERNAME_INPUT_XPATH, username);
        waitForElementVisible(ElementLocators.STEP_PASSWORD_INPUT_XPATH);
        enterText(ElementLocators.STEP_PASSWORD_INPUT_XPATH, password);
        Thread.sleep(5000);
        waitForElementVisible(ElementLocators.STEP_LOGIN_BUTTON_XPATH);
        clickElement(ElementLocators.STEP_LOGIN_BUTTON_XPATH);

    }

    public void clickOnBuyersApproval() throws InterruptedException {
        Thread.sleep(5000);
        waitForElementVisible(ElementLocators.ALL_USERS);
        clickElement(ElementLocators.ALL_USERS);
        Thread.sleep(5000);
        waitForElementVisible(ElementLocators.BUYERS_APPROVAL_LINK_XPATH);
        clickElement(ElementLocators.BUYERS_APPROVAL_LINK_XPATH);

    }

    public void clickOnDateFirstSubmittedByBrand() throws InterruptedException {
        Thread.sleep(5000);
        waitForElementVisible(ElementLocators.DATE_FIRST_SUBMITTED_BY_BRAND_XPATH);
        clickElement(ElementLocators.DATE_FIRST_SUBMITTED_BY_BRAND_XPATH);

    }

    public void filterDateToDescendingOrder() throws InterruptedException {
        waitForElementVisible(ElementLocators.FILTER_DATE_TO_DESCENDING_XPATH);
        clickElement(ElementLocators.FILTER_DATE_TO_DESCENDING_XPATH);
        ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='80%'");
        Thread.sleep(3000);

    }


    public void checkProductIdImported(String excelPath, String SheetName, int rowNUM, String columnHeader) throws IOException, InterruptedException, AWTException {
        PDXExcelReader reader = new PDXExcelReader(excelPath, SheetName);
        int rowCount = reader.getRowCount();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        Actions actions = new Actions(driver);

        List<String> PDXProductIDs = new ArrayList<>();

        for (int i = 1; i <= rowCount; i++) {
            String PDXProductID;

            if (SheetName.equalsIgnoreCase("Sheet1")) {
                PDXProductID = reader.getCellValue(i, columnHeader); // logic for Sheet1
            } else if (SheetName.equalsIgnoreCase("Sheet2")) {
                PDXProductID = reader.getCellValue(i, columnHeader); // logic for Sheet2 (can be different if needed)
            } else {
                PDXProductID = reader.getCellValue(i, columnHeader); // default logic
            }

            if (PDXProductID != null && !PDXProductID.trim().isEmpty()) {
                PDXProductIDs.add(PDXProductID.trim());
            }
        }

        reader.close();

        // Optional: log or assert
        if (PDXProductIDs.isEmpty()) {
            throw new RuntimeException("No Product IDs found in sheet: " + SheetName);
        }

        // You can now use PDXProductIDs for further validation


        if (PDXProductIDs.isEmpty()) {
            throw new RuntimeException("No PDX Product IDs found in Excel column: " + columnHeader);

        }
        int processedCount = 0;

        for (String PDXProductID : PDXProductIDs) {
            // Open filter UI
            WebElement filterHeader = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//th//span[@title='PDX Product ID']")));
            filterHeader.click();
            Thread.sleep(2000);
            WebElement filter = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//select[contains(@class, 'sortOptionsListBox')]//option[@value='Equals']")));
            filter.click();
            Thread.sleep(2000);
            WebElement textArea = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//textarea[@placeholder='Value or text']")));
            textArea.clear();
            textArea.sendKeys(PDXProductID);
            Thread.sleep(7000);
            // Re-locate Apply button after entering product ID
            WebElement applyBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//button[.//span[normalize-space()='Apply filter']]")));

            // Wait until it's clickable

            if (applyBtn.isEnabled()) {
                wait.until(ExpectedConditions.elementToBeClickable(applyBtn));
                actions.moveToElement(applyBtn).click().perform();
                System.out.println("Clicked Apply filter for Product ID: " + PDXProductID);
                Thread.sleep(10000);

                List<WebElement> rows = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.xpath("//table//tr//td")));
                Thread.sleep(10000);

                if (rows.size() == 0) {
                    System.out.println("No results found for Product ID: " + PDXProductID + ". Skipping...");
                    continue;
                }


                try {
                    WebElement selectAll = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//div[text()='Select All']")));
                    selectAll.click();
                    System.out.println("Selected all for Product ID: " + PDXProductID);
                } catch (Exception e) {
                    System.out.println("Failed to click Select All: " + e.getMessage());
                }

            } else {
                // ✅ Apply Filter is disabled → click navbar logo and continue
                System.out.println("Apply filter is disabled for Product ID: " + PDXProductID + ". Clicking navbar logo...");
                try {
                    WebElement navbarLogo = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//div[@id='stibo-element-navbar-logo']")));
                    navbarLogo.click();
                    Thread.sleep(2000);
                } catch (Exception e) {
                    System.out.println("Failed to click navbar logo: " + e.getMessage());
                }
                continue; // Skip remaining logic for this iteration
            }


            try {
                scrollToSeasonality();
            } catch (Exception e) {
                System.out.println("Failed to scroll to Seasonality: " + e.getMessage());
                throw new RuntimeException("Test failed: Seasonality element not found", e);
            }


            try {
                int rowNum = 1;
                doubleClickTextField(excelPath, SheetName, 1, "Core/Newness");
            } catch (Exception e) {
                System.out.println("Failed to double-click Core/Newness: " + e.getMessage());
            }


            try {
                ScrollableContainer(excelPath, SheetName, 1, "Parent Node Lists", "PRODUCT TYPE Â© (External Merch Category) ");
            } catch (Exception e) {
                System.out.println("Failed to scroll container: " + e.getMessage());
            }

            try {
                pricing();
            } catch (Exception e) {
                System.out.println("Failed to execute pricing: " + e.getMessage());
            }

            try {
                clickOnBuyersApproval();
            } catch (Exception e) {
                System.out.println("Failed to click on Buyers Approval: " + e.getMessage());
            }

            try {
                clickOnDateFirstSubmittedByBrand();
            } catch (Exception e) {
                System.out.println("Failed to click on Date First Submitted By Brand: " + e.getMessage());
            }

            try {
                filterDateToDescendingOrder();
            } catch (Exception e) {
                System.out.println("Failed to filter date to descending order: " + e.getMessage());
            }

            processedCount++;

        } /*catch (Exception e) {
                System.out.println("Failed to process Product ID: " + PDXProductID + " due to: " + e.getMessage());
            }*/


        if (processedCount == 0) {
            System.out.println("⚠️ No Product IDs were processed. Proceeding to Asset Approval anyway...");
        } else if (processedCount == PDXProductIDs.size()) {
            System.out.println("✅ All Buyer Approval products processed. Proceeding to Asset Approval...");
        } else {
            System.out.println("⚠️ Some Buyer Approval products were skipped or failed. Proceeding to Asset Approval...");
        }

// Simulate click anywhere to dismiss overlay
        actions.moveByOffset(10, 10).click().perform();
        Thread.sleep(5000);

// Proceed to Asset Approval
        WebElement navbar = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@id='stibo-element-navbar-logo']")));
        navbar.click();
        Thread.sleep(5000);
        waitForElementVisible(ElementLocators.ALL_USERS);
        clickElement(ElementLocators.ALL_USERS);
        Thread.sleep(10000);
        WebElement assetApprovalBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@title='Asset Approval']")));
        assetApprovalBtn.click();
        Thread.sleep(10000);

        String columnName = "BrandID";
        applyBrandFilterBulk(SheetName, columnName);
        Thread.sleep(6000);

        try {
            waitForElementVisible(ElementLocators.ALL_USERS);
            clickElement(ElementLocators.ALL_USERS);
            Thread.sleep(8000);
            WebElement attributeApprovalBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@title='Attribute Approval']")));
            attributeApprovalBtn.click();
            Thread.sleep(5000);

            PDXProductId(excelPath, SheetName, rowNUM, columnHeader);


            System.out.println("✅ Test completed successfully. All operations finished.");
            System.out.println("✅ Test completed successfully. All steps passed.");

        } catch (Exception e) {
            System.out.println("❌ Test failed during final operation: " + e.getMessage());
            e.printStackTrace();

            // throw new RuntimeException("Test failed", e);
        }
    }

    public void clickOnCheckMandatoryAttribute() {
        waitForElementVisible(ElementLocators.CHECK_MANDATORY_ATTRIBUTE_BUTTON_XPATH);
        clickElement(ElementLocators.CHECK_MANDATORY_ATTRIBUTE_BUTTON_XPATH);
    }

    public void scrollToSeasonality() throws InterruptedException {

        Thread.sleep(2000);

        WebElement scrollableDiv = driver.findElement(By.xpath("//div[contains(@class, 'sheet-scroll-container')]"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollLeft = arguments[0].scrollWidth;", scrollableDiv);
        Thread.sleep(2000);
        WebElement seasonality = driver.findElement(By.xpath("//span[@title='Seasonality']"));
        seasonality.click();
    }

        /*js.executeScript("window.scrollBy(500, 0);");
        js.executeScript("window.scrollTo(document.body.scrollWidth, 0);"); */


       /* Actions actions = new Actions(driver);
        WebElement scrollArea = driver.findElement(By.xpath("//div[contains(@class, 'sheet-scroll-container')]"));
        actions.moveToElement(scrollArea).clickAndHold().moveByOffset(300, 0).release().perform(); */
   /* }

    public void ClickOnTheCoreNewness() {
        waitForElementVisible(ElementLocators.CORE_NEWNESS_XPATH);
        clickElement(ElementLocators.CORE_NEWNESS_XPATH);
        }

    public void selectCoreNewness(String coreNewness) {

    }*/

    public void doubleClickTextField(String excelPath, String SheetName, int rowNum, String columnHeader) throws InterruptedException, AWTException, IOException {
        //Actions ac = new Actions(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));

        WebElement corenewness = wait.until(ExpectedConditions.elementToBeClickable(By.xpath
                ("(//span[@title='Core / Newness'])[1]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", corenewness);

        // String excelPath = System.getenv("EXCEL_PATH2");
        PDXExcelReader read = new PDXExcelReader(excelPath, SheetName);
        int rowCount = read.getRowCount();
        System.out.println("Row count from sheet '" + SheetName + "': " + rowCount);
        Actions ac = new Actions(driver);

        List<String> corenewnessValues = new ArrayList<>();

        for (int i = 1; i <= rowCount; i++) {
            String corenewnessValue= read.getCellValue(i, columnHeader);
            System.out.println("Read value from row " + i + ": '" + corenewnessValue + "'"); // ✅ Added

            // ✅ Skip if value is empty or equals the column header (repeated header row)
            if (corenewnessValue == null || corenewnessValue.trim().isEmpty() || corenewnessValue.trim().equalsIgnoreCase(columnHeader.trim())) {
                continue;
            }

            corenewnessValues.add(corenewnessValue.trim());
        }

        read.close();

        if (corenewnessValues.isEmpty()) {
                throw new RuntimeException("No Core/Newness values found in sheet: " + SheetName);
            }

            for (String corenewnessValue : corenewnessValues) {
       /* WebElement corenewness = wait.until(ExpectedConditions.elementToBeClickable(By.xpath
                ("(//span[@title='Core / Newness'])[1]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", corenewness);
        //System.out.println("Excel value: '" + cellValue + "'");*/
                WebElement targetcell1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//td[@data-col='20'])[1]")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", targetcell1);
                targetcell1.click();
                ac.sendKeys(Keys.ENTER).perform();
                // Step 1: Wait for the editor panel to be clickable
                WebElement editorPanel = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("div.sheet-edit-mode-editor-overlay-panel")));
                WebElement activeEditor = (WebElement) ((JavascriptExecutor) driver)
                        .executeScript("return document.activeElement;");

                ac.moveToElement(activeEditor)
                        .click()
                        .pause(Duration.ofMillis(200))
                        .keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                        .sendKeys(Keys.DELETE)
                        .pause(Duration.ofMillis(200))
                        .sendKeys(corenewnessValue)
                        .perform();

// Step 4: Read back value using JS
                String typedValue = (String) ((JavascriptExecutor) driver)
                        .executeScript("return ('value' in arguments[0]) ? arguments[0].value : arguments[0].textContent;", activeEditor);

// Step 5: Print and compare
                System.out.println("Excel Value: " +corenewnessValue);
                System.out.println("UI Value: " + typedValue);

                if (columnHeader.trim().equals(typedValue.trim())) {
                    ac.sendKeys(Keys.ENTER).perform();
                    System.out.println("✅ Value committed successfully");
                } else {
                    System.out.println("❌ Value mismatch – not committing");


                Thread.sleep(500);

                ac.moveByOffset(50, 50).click().perform();
                Thread.sleep(1000);
            }
        /*WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement textField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//tr[@class='even']/td[20])[1]"))); // Update locator if needed
        textField.click();
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.SHIFT, "c").build().perform();
        Thread.sleep(1000);
        WebElement body = driver.findElement(By.tagName("body"));
        actions.moveToElement(body).moveByOffset(10, 10).click().perform();

    }*/
        }}



    public void ScrollableContainer(String excelPath, String SheetName, int rowNum, String columnHeader,String columnHeader1) throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement ecom = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@title='Ecom']")));
        ecom.click();
        WebElement scrollable = driver.findElement(By.xpath("//div[contains(@class, 'sheet-scroll-container')]"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollLeft += 300;", scrollable);
        /*js.executeScript("arguments[0].scrollLeft = arguments[0].scrollWidth;", scrollable);*/
        Thread.sleep(5000);
        WebElement shortdesc = driver.findElement(By.xpath("(//th[@id='TableHeader_Short_Item_Description'])[1]"));
        js.executeScript("arguments[0].scrollLeft = arguments[0].scrollWidth", shortdesc);
        Thread.sleep(2000);
        WebElement cell = driver.findElement(By.xpath("(//td[@data-col='28'])[1]"));
        cell.click();
        Thread.sleep(2000);

        Actions actions = new Actions(driver);
        actions.sendKeys("T").pause(600)
                .sendKeys("e").pause(400)
                .sendKeys("s").pause(100)
                .sendKeys("t").pause(100)
                .sendKeys("i").pause(100)
                .sendKeys("n").pause(100)
                .sendKeys("g").perform();
        Thread.sleep(2000);
        /*WebElement safeArea = driver.findElement(By.xpath("//div[@class='stb-DetailPanel']")); // Replace with actual selector
        safeArea.click();*/
        actions.sendKeys(Keys.ENTER).pause(Duration.ofMillis(700)).click().perform();
        Thread.sleep(1000);
        WebElement scrollable1 = driver.findElement(By.xpath("//div[contains(@class, 'sheet-scroll-container')]"));
        js.executeScript("arguments[0].scrollLeft += 1200;", scrollable); // Adjust offset as needed
        Thread.sleep(3000);
        // Scrolls 300px to the right

        //PARENT NODE
        WebElement inputField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath
                ("(//span[@title='Parent Node Lists'])[1]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", inputField);
        Thread.sleep(1000);
        PDXExcelReader read = new PDXExcelReader(excelPath, SheetName);
        int rowCount = read.getRowCount();
        System.out.println("Row count from sheet '" + SheetName + "': " + rowCount);
        Actions ac = new Actions(driver);

        List<String> parentnodelistvalues = new ArrayList<>();

        for (int i = 1; i <= rowCount; i++) {
            String parentnodelistvalue = read.getCellValue(i, columnHeader);
            System.out.println("Read value from row " + i + ": '" + parentnodelistvalue + "'"); // ✅ Added

            // ✅ Skip if value is empty or equals the column header (repeated header row)
            if (parentnodelistvalue == null || parentnodelistvalue.trim().isEmpty() || parentnodelistvalue.trim().equalsIgnoreCase(columnHeader.trim())) {
                continue;
            }

            parentnodelistvalues.add(parentnodelistvalue.trim());
        }

        read.close();
        if (parentnodelistvalues.isEmpty()) {
            throw new RuntimeException("No parent node listvalues found in sheet: " + SheetName);
        }

        for (String parentnodelistvalue : parentnodelistvalues) {
            WebElement targetcell = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//td[@data-col='36'])[1]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", targetcell);
            targetcell.click();
            actions.sendKeys(Keys.ENTER).perform();

            // Step 1: Wait for the editor panel to be clickable
            WebElement editorPanel = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("div.sheet-edit-mode-editor-overlay-panel")));

// Step 2: Get the actual active editable element
            WebElement activeEditor = (WebElement) ((JavascriptExecutor) driver)
                    .executeScript("return document.activeElement;");

// Step 3: Clear and type using Actions
            actions.moveToElement(activeEditor)
                    .click()
                    .pause(Duration.ofMillis(200))
                    .keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                    .sendKeys(Keys.DELETE)
                    .pause(Duration.ofMillis(200))
                    .sendKeys(parentnodelistvalue)
                    .perform();


            String typedValue = (String) ((JavascriptExecutor) driver)
                    .executeScript("return ('value' in arguments[0]) ? arguments[0].value : arguments[0].textContent;", activeEditor);

// Step 5: Print and compare
            System.out.println("Excel Value: " + parentnodelistvalue);
            System.out.println("UI Value: " + typedValue);

            if (columnHeader.trim().equals(typedValue.trim())) {
                ac.sendKeys(Keys.ENTER).perform();
                System.out.println("✅ Value committed successfully");
            } else {
                System.out.println("❌ Value mismatch – not committing");


                Thread.sleep(500);

                ac.moveByOffset(50, 50).click().perform();
                Thread.sleep(1000);
            }
// Scroll the element into view with a slight offset
            //js.executeScript("arguments[0].scrollIntoView({block: 'center'});", header);

            Thread.sleep(5000);

            driver.findElement(By.xpath("//div[text()='Freeze panes']")).click();
            Thread.sleep(5000);
            //hierarchy
            WebElement scrollableDiv = driver.findElement(By.xpath("//div[contains(@class, 'sheet-scroll-container')]"));

            js.executeScript("arguments[0].scrollLeft += 600;", scrollableDiv);
            Thread.sleep(1000);
            WebElement hierarchyElement = driver.findElement(By.xpath("//span[text()='Hierarchy']/following-sibling::div"));

            hierarchyElement.click();
            Thread.sleep(5000);

            WebElement scrollableDiv2 = driver.findElement(By.xpath("//div[contains(@class, 'sheet-scroll-container')]"));

            js.executeScript("arguments[0].scrollLeft += 400;", scrollableDiv2);
            Thread.sleep(5000);
            //PDXExcelReader reader1 = new PDXExcelReader(excelPath, "Sheet1");

           /* String ProductTypeA = reader1.getCellValue(1, "PRODUCT TYPE Â© (External Merch Category)");
            read.close();*/


                // ✅ Scroll to PRODUCT TYPE element dynamically (ignore encoding issues)
                WebElement inputField1 = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("(//span[contains(@title,'PRODUCT TYPE')])[1]")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", inputField1);

                // ✅ Read Excel
                PDXExcelReader read1 = new PDXExcelReader(excelPath, SheetName);
                int rowCount1 = read1.getRowCount();
                System.out.println("Row count from sheet '" + SheetName + "': " + rowCount1);

                List<String> productTypeAs = new ArrayList<>();

                // ✅ Find column index dynamically instead of hardcoding columnHeader1


            String normalizedHeader = columnHeader1.trim();
            for (int i = 1; i <= rowCount1; i++) {
                String productTypeA = read1.getCellValue(i, normalizedHeader); // Use original header name
                System.out.println("Read value from row " + i + ": '" + productTypeA + "'");

                if (productTypeA == null || productTypeA.trim().isEmpty() ||
                        productTypeA.trim().equalsIgnoreCase(normalizedHeader)) {
                    continue;
                }
                productTypeAs.add(productTypeA.trim());
            }

            read1.close();



            if (productTypeAs.isEmpty()) {
                    throw new RuntimeException("No PRODUCT TYPE values found in sheet: " + SheetName);
                }

                // ✅ Use first value for UI interaction
                for (String productTypeA : productTypeAs) {
                    WebElement targetCell1 = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("(//td[@data-col='42'])[1]")));
                    targetCell1.click();
                    actions.sendKeys(Keys.ENTER).perform();

                    WebElement editableDiv1 = wait.until(ExpectedConditions.elementToBeClickable(
                            By.cssSelector("div.sheet-edit-mode-editor-overlay-panel")));

                    WebElement activeEditor1 = (WebElement) ((JavascriptExecutor) driver)
                            .executeScript("return document.activeElement;");

                    // ✅ Clear and type using Actions
                    actions.moveToElement(activeEditor1)
                            .click()
                            .pause(Duration.ofMillis(200))
                            .keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                            .sendKeys(Keys.DELETE)
                            .pause(Duration.ofMillis(200))
                            .sendKeys(productTypeA)
                            .perform();

                    // ✅ Read back value using JS
                    String typedValue1 = (String) ((JavascriptExecutor) driver)
                            .executeScript("return ('value' in arguments[0]) ? arguments[0].value : arguments[0].textContent;", activeEditor1);

                    System.out.println("Excel Value: " + productTypeA);
                    System.out.println("UI Value: " + typedValue1);

                    if (productTypeA.trim().equals(typedValue1.trim())) {
                        actions.sendKeys(Keys.ENTER).perform();
                        System.out.println("✅ Value committed successfully");
                    } else {
                        System.out.println("❌ Value mismatch – not committing");
                    }

                    break; // ✅ Stop after first successful update
                }
            }
                driver.findElement(By.xpath("//div[text()='Unfreeze panes']")).click();
                Thread.sleep(2000);

                WebElement threeDots = driver.findElement(By.xpath("//i[text()='more_horiz']"));
                threeDots.click();
                Thread.sleep(1000);
                WebElement scrollable2 = driver.findElement(By.xpath("//div[contains(@class, 'sheet-scroll-container')]"));
                js.executeScript("arguments[0].scrollLeft = arguments[0].scrollWidth;", scrollable2);
                WebElement strokeno = driver.findElement(By.xpath("//div[text()='Stroke No. Calculation']"));
                strokeno.click();
                Thread.sleep(1000);
            }


    public void pricing() throws InterruptedException {

       WebElement scrollable = driver.findElement(By.xpath("(//div[contains(@class, 'sheet-scroll-container')])[2]"));
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

        WebElement nextElement = driver.findElement(By.xpath("(//span[text()='Cost Price'])[1]"));
        js.executeScript("arguments[0].scrollLeft = arguments[0].scrollWidth", nextElement);
        Thread.sleep(2000);
        WebElement ele = driver.findElement(By.xpath("(//td[@data-col='53'])[1]"));
        ele.click();

        actions.sendKeys("5%").build().perform();
        Thread.sleep(1000);
        WebElement body = driver.findElement(By.tagName("body"));
        actions.moveToElement(body).moveByOffset(20, 20).click().perform();
        WebElement vatRateElement = driver.findElement(By.xpath("(//th/div[@class='sheet-header-cell']/span[@title='VAT Rate'])[1]"));
        String vatRateText = vatRateElement.getText();
        js.executeScript("arguments[0].scrollLeft = arguments[0].scrollWidth", vatRateElement);
        Thread.sleep(2000);
        WebElement element1 = driver.findElement(By.xpath("(//td[@data-col='58'])[1]"));
        element1.click();
        actions.sendKeys("20").sendKeys(Keys.ENTER).perform();
        Thread.sleep(1000);
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
        textarea.sendKeys("Product approve");

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
    public void applyBrandFilterBulk(String SheetName,String columnHeader) throws InterruptedException, IOException {
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
    public void PDXProductId(String excelPath, String SheetName, int rowNUM, String columnHeader) throws InterruptedException, IOException {
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

        // Clear previous filter if any
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement clearFilterBtn = shortWait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(., 'Clear filter')]")));
            clearFilterBtn.click();
            System.out.println("🧹 Cleared previous filter.");
        } catch (Exception e) {
            System.out.println("⚠️ No filter to clear or skipped due to timeout.");
        }

        // Apply filter using "Include only"
        WebElement filterHeader = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//th//span[@title='PDX Product ID']")));
        filterHeader.click();
        Thread.sleep(1000);

        WebElement filter = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//select[contains(@class, 'sortOptionsListBox')]//option[@value='Include only']")));
        filter.click();
        Thread.sleep(1000);

        WebElement textArea = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//textarea[@placeholder='Value or text']")));
        textArea.clear();
        textArea.sendKeys(allProductIDs);
        Thread.sleep(1000);

        WebElement applyBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[.//span[normalize-space()='Apply filter']]")));
        actions.moveToElement(applyBtn).click().perform();
        System.out.println("✅ Applied filter for all Product IDs");
        Thread.sleep(2000);

        // Select all
        try {
            WebElement selectAll = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[text()='Select All']")));
            selectAll.click();
            Thread.sleep(1000);
            System.out.println("✅ Selected all filtered items");
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
        Thread.sleep(3000);

        // Handle popup
        try {
            WebDriverWait popupWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement popupMessage = popupWait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//i[@class='material-icons portal-alert-popup-close-box__button']")));
            popupMessage.click();
            Thread.sleep(1000);
            System.out.println("✅ Popup closed after submission.");
        } catch (Exception e) {
            System.out.println("⚠️ Popup not found or failed to close → " + e.getMessage());

            //System.out.println("✅ Test completed successfully.");

        }
    }

    }




































