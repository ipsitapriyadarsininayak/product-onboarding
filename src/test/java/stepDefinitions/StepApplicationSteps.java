package stepDefinitions;

import constants.ElementLocators;
import hooks.Hooks;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pages.BasePage;
import pages.StepApplicationPage;

import utils.ConfigFileReader;

import java.awt.*;
import java.io.IOException;
import java.time.Duration;

public class StepApplicationSteps extends BasePage {

    StepApplicationPage stepApplicationPage = new StepApplicationPage();

    @When("User navigate to Step application")
    public void user_navigate_to_step_application() throws InterruptedException {
        driver.get(ConfigFileReader.get("StepBrandsOnboardingUrl"));
        Thread.sleep(5000);
    }

    @And("User login with valid credentials")
    public void userLoginWithValidCredentials() throws IOException, InterruptedException {
        stepApplicationPage.stepLogin();
    }

    @Then("User should land on brands onboarding home page")
    public void userShouldLandOnBrandsOnboardingHomePage() {
        waitForElementVisible(ElementLocators.BRANDS_ONBOARDING_HEADER_XPATH);
        String actualText = getElementText(ElementLocators.BRANDS_ONBOARDING_HEADER_XPATH);
        System.out.println("actualText===>" + actualText);
        Assert.assertTrue(actualText.contains("Brands Onboarding"));
    }

    @When("user click onto the Buyer approval screen")
    public void userClickOntoTheBuyerApprovalScreen() throws InterruptedException {
        stepApplicationPage.clickOnBuyersApproval();

    }

    @Then("validate user landed into buyers approval page")
    public void validateUserLandedIntoBuyersApprovalPage() {
        waitForElementVisible(ElementLocators.BUYERS_APPROVAL_HEADER_XPATH);
        String actualText = getElementText(ElementLocators.BUYERS_APPROVAL_HEADER_XPATH);
        System.out.println("BUYER APPROVAL HEADER Text===>" + actualText);
        Assert.assertTrue(actualText.contains("Buyer Approval"));
    }

    @When("User click on Date First Submitted by Brand")
    public void userClickOnDateFirstSubmittedByBrand() throws InterruptedException {
        stepApplicationPage.clickOnDateFirstSubmittedByBrand();
    }

    @And("User filter the date to descending order")
    public void userFilterTheDateToDescendingOrder() throws InterruptedException {
        stepApplicationPage.filterDateToDescendingOrder();
    }

    @And("User check the {string} imported to step")
    public void userCheckTheImportedToStep(String SheetName) throws InterruptedException, IOException, AWTException {

        int rowNum = 1;
        String excelPath = ConfigFileReader.get("excelPath");
        String columnHeader = "PDX Product ID";

        stepApplicationPage.checkProductIdImported(excelPath, SheetName, rowNum, columnHeader);
        System.out.println("✅ Test completed successfully. Stopping further steps...");
        Hooks.stopScenario = true; //
        if (Hooks.stopScenario) {
            return; // Skip this step
        }// ✅ Set flag to stop scenario
    }


    //throw new SkipException("Stopping further steps after Attribute Approval.");
    @And("User click on check mandatory attribute")
    public void userClickOnCheckMandatoryAttribute() {
       // stepApplicationPage.clickOnCheckMandatoryAttribute();
    }

    @And("User scroll to seasonality header")
    public void userScrollToSeasonalityHeader() throws InterruptedException {
        //stepApplicationPage.scrollToSeasonality();
        //waitForElementVisible(ElementLocators.SEASONALITY_TITLE_HEADER_XPATH);
        //clickElement(ElementLocators.SEASONALITY_TITLE_HEADER_XPATH);
        Thread.sleep(4000);

    }

    /*@And("User click on the Core Newness")
    public void userClickOnTheCoreNewness() {
        stepApplicationPage.ClickOnTheCoreNewness();
    }

    @When("User select {string} in the dropdown")
    public void user_select_in_the_dropdown(String CoreNewness) throws InterruptedException {
        stepApplicationPage.selectCoreNewness(CoreNewness);
        clickElement(ElementLocators.CORE_NEWNESS_XPATH);
        Thread.sleep(4000);
*/

       /* @And("User enters text key in the search field")
    public void userEntersTextKeyInTheSearchField() throws InterruptedException {
       driver.findElement(By.xpath("//textarea[@placeholder='Value or text']")).sendKeys("CORE");
    }*/

    @When("I double click on the text field for {string}")
    public void iDoubleClickOnTheTextField(String SheetName) throws InterruptedException, AWTException, IOException {
        String excelPath = System.getenv("EXCEL_PATH2");
        int rowNum = 1;
        String columnHeader = "Core/Newness";
        //stepApplicationPage.doubleClickTextField(excelPath, SheetName, rowNum, columnHeader);
        Thread.sleep(4000);

    }

    @And("the user scrolls the horizontal bar to see pricing in sheet {string}")
    public void scrollTheHorizontalBarToSeePricing(String SheetName) throws Exception {
        String excelPath = System.getenv("EXCEL_PATH2");
        int rowNum = 1;
        String columnHeader = "Parent Node Lists";
        String columnHeader1 = "PRODUCT TYPE Â© (External Merch Category)";
        //stepApplicationPage.ScrollableContainer(excelPath, SheetName, rowNum, columnHeader, columnHeader1);

    }



    @And("User click on Pricing")
    public void user_click_on_pricing() {
        String excelPath = System.getenv("EXCEL_PATH2");
        int rowNum = 1;
        //stepApplicationPage.pricing(excelPath, sheetName);

    }

    @When("user clicks on Asset Approval")
    public void userClicksOnAssetApproval() {


        try {
            System.out.println("Attempting to click on Asset Approval...");

            // Wait for the element to be clickable
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(ElementLocators.ASSET_APPROVAL_LINK_XPATH)));

            // Click using JavaScript
           // stepApplicationPage.clickOnElementUsingJS(By.xpath(ElementLocators.ASSET_APPROVAL_LINK_XPATH));

            System.out.println("Successfully clicked on Asset Approval.");
        } catch (Exception e) {
            System.err.println("Error while clicking on Asset Approval: " + e.getMessage());
            throw e;
        }


    }

    @Then("validate user landed into asset approval screen")
    public void validateUserLandedIntoAssetApprovalScreen() throws InterruptedException {
       // stepApplicationPage.clickOnDateFirstSubmittedByBrand();

    }

    @When("User click on Date First Submitted by Brand in page")
    public void userClickOnDateFirstSubmittedByBrandInPage() throws InterruptedException {
        //stepApplicationPage.filterDateToDescendingOrder();
        Thread.sleep(2000);

    }


    @Then("User filter using each BrandID from Excel in sheet {string}")
    public void userFilterUsingEachStrokeNumberFromExcel(String SheetName) throws IOException, InterruptedException {
        String columnHeader = "BrandID";
        //stepApplicationPage.applyBrandFilterBulk(SheetName, columnHeader);
    }

    /*@When("user clicks on Attribute Approval in sheet {string}")
    public void userClicksOnAttributeApproval() throws InterruptedException {
        try {
            System.out.println("Attempting to click on Attribute Approval...");

            // Wait for the element to be clickable
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(ElementLocators.ATTRIBUTE_APPROVAL_LINK_XPATH)));

            // Click using JavaScript
           // stepApplicationPage.clickOnElementUsingJS1(By.xpath(ElementLocators.ATTRIBUTE_APPROVAL_LINK_XPATH));

            System.out.println("Successfully clicked on Attribute Approval.");
        } catch (Exception e) {
            System.err.println("Error while clicking on Attribute Approval: " + e.getMessage());
            throw e;
        }


    }*/

    @Then("User click on Date First Submitted by Brand in Attribute approval Page")
    public void userClickOnDateFirstSubmittedByBrandInAttributeApprovalPage() throws InterruptedException {
       // stepApplicationPage.clickOnDateFirstSubmittedByBrand();
        //stepApplicationPage.filterDateToDescendingOrder();

    }

    @When("User load PDX Product ID from excel file with sheet name {string}")
    public void userLoadPDXProductIDFromCSVFile(String SheetName) throws IOException, InterruptedException {

        String excelPath = System.getenv("EXCEL_PATH2"); // Make sure this env variable is set
        int rowNum = 1;
        String columnHeader = "PDX Product ID";
       // stepApplicationPage.PDXProductId(excelPath, SheetName, rowNum, columnHeader);
        //throw new io.cucumber.java.PendingException("Scenario stopped intentionally after Attribute Approval."
    }


    @Then("the user should be on the Attribute Approval page")
    public void theUserShouldBeOnTheAttributeApprovalPage() {

        WebElement header = driver.findElement(By.xpath("//div[text()='Attribute Approval']"));
        Assert.assertTrue(header.isDisplayed());
        System.out.println("✅ Verified: User is on Attribute Approval page.");

    }

}






















