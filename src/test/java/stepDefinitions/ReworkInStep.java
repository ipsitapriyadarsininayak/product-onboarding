package stepDefinitions;

import hooks.Hooks;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.BasePage;
import pages.ReworkInStepPage;
import utils.ConfigFileReader;

import java.io.IOException;

public class ReworkInStep extends BasePage {
    ReworkInStepPage reworkInStepPage = new ReworkInStepPage();

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

    @And("user send message to suppiler from {string}")
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
}




