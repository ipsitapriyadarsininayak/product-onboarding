package stepDefinitions;

import constants.ElementLocators;
import hooks.Hooks;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.BasePage;
import pages.ReworkInStepPage;
import pages.ReworkPDXHomePage;
import pages.ReworkSize;
import utils.ConfigFileReader;

import java.io.IOException;

public class ReworkSteps extends BasePage {

    ReworkInStepPage reworkInStepPage = new ReworkInStepPage();
    ReworkPDXHomePage reworkPDXHomePage;
    ReworkSize reworkSizePage = new ReworkSize ();

    @When("the user navigates to the Buyer Approval screen")
    public void theUserNavigatesToTheBuyerApprovalScreen() throws InterruptedException, IOException {
        driver.get(ConfigFileReader.get("StepBrandsOnboardingUrl"));
        Thread.sleep(5000);
        reworkInStepPage.clickonBuyerApprovalpage();
    }

    @Then("verify that the user is on the Buyer Approval page")
    public void verifyThatTheUserIsOnTheBuyerApprovalPage() throws InterruptedException {
        reworkInStepPage.useronDateFirstSubmittedByBrand();
    }

    @And("User filter date to descending order")
    public void userFilterDateToDescendingOrder() throws InterruptedException {
        reworkInStepPage.filteronDateToDescendingOrder();
    }

    @And("the user verifies that {string} has been imported into Step")
    public void theUserVerifiesThatHasBeenImportedIntoStep(String SheetName) throws IOException, InterruptedException {
        String excelPath = System.getenv("EXCEL_PATH2");

        int rowNum = 1;
        String columnHeader = "PDX Product ID";
        reworkInStepPage.filterPDXid(excelPath, SheetName, rowNum, columnHeader);
        System.out.println("✅ Test completed successfully. Stopping further steps...");
        Hooks.stopScenario = true; //
        if (Hooks.stopScenario) {
            return; // Skip this step
        }// ✅ Set flag to stop scenario
    }

    @When("user click on proposal status from {string}")
    public void userClickOnProposalStatusFrom(String SheetName) {
        String columnHeader = "Proposal Status";
    }

    @And("user send message to supplier from {string}")
    public void userSendMessageToSuppilerFrom(String SheetName) {
        String  columnHeader1 ="Message To Supplier";
    }

    @And("user click on reason for work from{string}")
    public void userClickOnReasonForWorkFrom(String SheetName) {
        String  columnHeader1 ="Reason for Rework";
    }

    @And("click on menu to submit the event")
    public void clickOnMenuToSubmitTheEvent() {
    }

    @When("User navigate to pdx page")
    public void userNavigateToPdxPage() throws InterruptedException {
        reworkPDXHomePage=new ReworkPDXHomePage();
        driver.get(ConfigFileReader.get("PdxUrl"));
        Thread.sleep(1000);
    }

    @And("User enter valid credential")
    public void userEnterValidCredential() throws IOException, InterruptedException {
        reworkPDXHomePage.pdxlogin();
        waitForElementVisible(ElementLocators.TITLE_HEADER_XPATH);
        Assert.assertEquals(driver.getTitle(), "Dashboard | Product Data Exchange");
        Thread.sleep(5000);

    }

    @Then("User should land on pdx home Page {string}")
    public void userShouldLandOnPdxHomePage(String SheetName) throws InterruptedException, IOException {
        String excelpath = System.getenv("EXCEL_PATH2");
        if (excelpath == null || excelpath.isEmpty()) {
            throw new IllegalArgumentException("Environment variable EXCEL_PATH2 is not set.");
        }
        int rowNum = 1;
        System.out.println("Excel path from config: " + excelpath);
        Thread.sleep(5000);

        String columnHeader = "ID";
        reworkPDXHomePage.clickGridViewIcon(excelpath, SheetName, columnHeader);
    }

    @When("The user navigates to the Buyer Approval screen")
    public void theUserNavigatesToTheBuyerApprovalPage() throws InterruptedException, IOException {
        driver.get(ConfigFileReader.get("StepBrandsOnboardingUrl"));
        Thread.sleep(5000);
        reworkSizePage.clickonBuyerApprovalpage1();
    }
    @Then("Verify that the user is on the Buyer Approval page")
    public void verifyThatTheUserIsOnTheBuyerApprovalPageSTEP() throws InterruptedException {
        reworkSizePage. useronDateFirstSubmittedByBrand1();
    }

    @And("user filter date to descending order")
    public void userFilterDateToDescendingOrderSTEP() throws InterruptedException {
        reworkSizePage.filteronDateToDescendingOrder1();
    }

    @And("The user verifies that {string} has been imported into Step")
    public void theUserVerifiesThatHasBeenImportedIntoSTEP(String SheetName) throws IOException, InterruptedException {
        String excelPath = System.getenv("EXCEL_PATH2");

        int rowNum = 1;
        String columnHeader = "PDX Product ID";
        String columnHeader1 = "Core/Newness";
        String columnHeader2= "Parent Node Lists";
        String columnHeader3="PRODUCT TYPE Â© (External Merch Category)";
        reworkSizePage.filterPDXid1(excelPath, SheetName, rowNum, columnHeader,columnHeader1,columnHeader2,columnHeader3);
        System.out.println("✅ Test completed successfully. Stopping further steps...");
        Hooks.stopScenario = true; //
        if (Hooks.stopScenario) {
            return; // Skip this step
        }// ✅ Set flag to stop scenario

    }
    @And("Remove message to supplier.")
    public void removeMessageToSupplier() {
    }

    @When("I double click on the text field For {string}")
    public void iDoubleClickOnTheTextFieldFor(String SheetName) throws InterruptedException {
        String excelPath = System.getenv("EXCEL_PATH2");
        int rowNum = 1;
        String columnHeader = "Core/Newness";
        //stepApplicationPage.doubleClickTextField(excelPath, SheetName, rowNum, columnHeader);
        Thread.sleep(4000);
    }

    @And("User click on Pricing from {string}")
    public void userClickOnPricingFrom(String SheetName) {
    }
}




