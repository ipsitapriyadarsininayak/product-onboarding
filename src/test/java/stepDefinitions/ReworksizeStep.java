package stepDefinitions;

import hooks.Hooks;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.BasePage;
import pages.ReworkSize;
import utils.ConfigFileReader;

import java.io.IOException;

public class ReworksizeStep extends BasePage {
    ReworkSize reworksizePage = new ReworkSize ();

    @When("The user navigates to the Buyer Approval screen")
    public void theUserNavigatesToTheBuyerApprovalScreen() throws InterruptedException, IOException {
        driver.get(ConfigFileReader.get("StepBrandsOnboardingUrl"));
        Thread.sleep(5000);
        reworksizePage.clickonBuyerApprovalpage1();
    }
    @Then("Verify that the user is on the Buyer Approval page")
    public void verifyThatTheUserIsOnTheBuyerApprovalPage() throws InterruptedException {
        reworksizePage. useronDateFirstSubmittedByBrand1();

    }

    @And("user filter date to descending order")
    public void userFilterDateToDescendingOrder() throws InterruptedException {
        reworksizePage.filteronDateToDescendingOrder1();


    }

    @And("The user verifies that {string} has been imported into Step")
    public void theUserVerifiesThatHasBeenImportedIntoStep(String SheetName) throws IOException, InterruptedException {
        String excelPath = System.getenv("EXCEL_PATH2");

        int rowNum = 1;
        String columnHeader = "PDX Product ID";
        String columnHeader1 = "Core/Newness";
        String columnHeader2= "Parent Node Lists";
        String columnHeader3="PRODUCT TYPE Â© (External Merch Category)";
        reworksizePage.filterPDXid1(excelPath, SheetName, rowNum, columnHeader,columnHeader1,columnHeader2,columnHeader3);
        System.out.println("✅ Test completed successfully. Stopping further steps...");
        Hooks.stopScenario = true; //
        if (Hooks.stopScenario) {
            return; // Skip this step
        }// ✅ Set flag to stop scenario

    }
    @And("Remove message to suplier.")
    public void removeMessageToSuplier() {

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



