package stepDefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import pages.*;
import utils.ConfigFileReader;

import java.io.IOException;

public class WaitingForAssetStep extends BasePage {

    PdxMasterPage pdxMasterPage = new PdxMasterPage();
    PdxChannelPage pdxChannelPage = new PdxChannelPage();
    StepBuyerApprovalPage stepBuyerApprovalPage = new StepBuyerApprovalPage();
    StepAssetApprovalPage stepAssetApprovalPage = new StepAssetApprovalPage();
    StepAttributeApprovalPage stepAttributeApprovalPage = new StepAttributeApprovalPage();
    ReworkSize reworksizePage = new ReworkSize ();

    @And("User filters the products imported from {string} and submit to Step which doesn't have images")
    public void userFiltersTheProductsImportedFromAndSubmitToStepWhichDoesnotHaveImages(String SheetName) throws Exception {
        try {
            String excelPath = ConfigFileReader.get("excelPath");
            if (excelPath == null || excelPath.isEmpty()) {
                throw new IllegalArgumentException("Excel path not found in config.properties.");
            }
            System.out.println("Excel path from config: " + excelPath);

            String columnHeader = "ID";

            // Method to filter product and add to channel
            pdxMasterPage.filterProductsThenAddToChannel(excelPath, SheetName, columnHeader);
            System.out.println("✅ EXIT filterProductsThenAddToChannel (PASS)");

            // Method to filter product and submit to Step
            pdxChannelPage.filterProductThenSubmitToStep(excelPath, SheetName, columnHeader);
            System.out.println("✅ EXIT filterProductThenSubmitToStep (PASS)");
            System.out.println("✅ EXIT userFiltersTheProductsImportedFromAndSubmitToStepWhichDoesnotHaveImages (PASS)");

        } catch (Throwable t) {
            System.out.println("❌ EXIT userFiltersTheProductsImportedFromAndSubmitToStepWhichDoesnotHaveImages (FAIL): " + t);
            // Optional: attach screenshot here
            // If you want the next step to still run, DO NOT rethrow.
            // If you want normal BDD behavior, rethrow:
            throw t;
        }
    }

    @And("User filter products as per product ID from {string}")
    public void userFilterProductsAsPerProductIDFrom(String SheetName) throws Exception {
        try {
            String excelPath = ConfigFileReader.get("excelPath");
            if (excelPath == null || excelPath.isEmpty()) {
                throw new IllegalArgumentException("Excel path not found in config.properties.");
            }
            System.out.println("Excel path from config: " + excelPath);

            String columnHeader = "PDX Product ID";

            // Method to filter product
            stepBuyerApprovalPage.filterProductsByPDXProductID(excelPath, SheetName, columnHeader);
            System.out.println("✅ EXIT filterProductsByPDXProductID (PASS)");

        } catch (Throwable t) {
            System.out.println("❌ EXIT filterProductsByPDXProductID (FAIL): " + t);
            throw t;
        }
    }

    @When("User click on proposal status and set it mentioned as per {string} e.g Waiting for Asset\\(WA)")
    public void userClickOnProposalStatusAndSetItMentionedAsPerEGWaitingForAssetWA(String SheetName) {
        try {
            String excelPath = ConfigFileReader.get("excelPath");
            if (excelPath == null || excelPath.isEmpty()) {
                throw new IllegalArgumentException("Excel path not found in config.properties.");
            }
            System.out.println("Excel path from config: " + excelPath);

            String columnHeader = "Proposal Status";

            // Method to select "Waiting for Asset(WA)" as 'Proposal Status'
            stepBuyerApprovalPage.setWaitingForAssetStatus(excelPath, SheetName, columnHeader);
            System.out.println("✅ Waiting for Asset(WA) updated successfully!");
            System.out.println("✅ EXIT setWaitingForAssetStatus (PASS)");

        } catch (Throwable t) {
            System.out.println("❌ EXIT setWaitingForAssetStatus (FAIL): " + t);
            throw t;
        }
    }

    @And("User enter message to supplier as mentioned in {string}")
    public void userEnterMessageToSupplierAsMentionedIn(String SheetName) throws Exception {
        try {
            String excelPath = ConfigFileReader.get("excelPath");
            if (excelPath == null || excelPath.isEmpty()) {
                throw new IllegalArgumentException("Excel path not found in config.properties.");
            }
            System.out.println("Excel path from config: " + excelPath);

            String columnHeader = "Message To Supplier";

            // Method to update 'Message to Supplier'
            stepBuyerApprovalPage.setMessageToSupplier(excelPath, SheetName, columnHeader);
            System.out.println("✅ EXIT setMessageToSupplier (PASS)");

        } catch (Throwable t) {
            System.out.println("❌ EXIT setMessageToSupplier (FAIL): " + t);
            throw t;
        }
    }

    @And("user select reasons for rework as mentioned in {string}")
    public void userSelectReasonsForReworkAsMentionedIn(String SheetName) throws Exception {
        try {
            String excelPath = ConfigFileReader.get("excelPath");
            if (excelPath == null || excelPath.isEmpty()) {
                throw new IllegalArgumentException("Excel path not found in config.properties.");
            }
            System.out.println("Excel path from config: " + excelPath);

            String columnHeader = "Reasons For Rework";

            // Method to select 'Reasons For Rework'
            stepBuyerApprovalPage.selectReasonsForRework(excelPath, SheetName, columnHeader);
            System.out.println("✅ EXIT selectReasonsForRework (PASS)");

        } catch (Throwable t) {
            System.out.println("❌ EXIT selectReasonsForRework (FAIL): " + t);
            throw t;
        }
    }

    @And("User expand Seasonality column and select CoreNewness value as per {string}")
    public void userExpandSeasonalityColumnAndSelectCoreNewnessValueAsPer(String SheetName) throws Exception {

        String excelPath = ConfigFileReader.get("excelPath");

        int rowNum = 1;
        String columnHeader = "PDX Product ID";
        String columnHeader1 = "Core/Newness";
        String columnHeader2= "Parent Node Lists";
        String columnHeader3="PRODUCT TYPE Â© (External Merch Category)";
        reworksizePage.filterPDXid1(excelPath, SheetName, rowNum, columnHeader,columnHeader1,columnHeader2,columnHeader3);
        System.out.println("✅ Test completed successfully. Stopping further steps...");

        /*
        try {
            String excelPath = ConfigFileReader.get("excelPath");
            if (excelPath == null || excelPath.isEmpty()) {
                throw new IllegalArgumentException("Excel path not found in config.properties.");
            }
            System.out.println("Excel path from config: " + excelPath);

            String columnHeader = "ID";

            // Method to filter product and add to channel
            pdxMasterPage.filterProductsThenAddToChannel(excelPath, SheetName, columnHeader);
            System.out.println("✅ EXIT filterProductsThenAddToChannel (PASS)");

            // Method to filter product and submit to Step
            pdxChannelPage.filterProductThenSubmitToStep(excelPath, SheetName, columnHeader);
            System.out.println("✅ EXIT filterProductThenSubmitToStep (PASS)");
            System.out.println("✅ EXIT userFiltersTheProductsImportedFromAndSubmitToStepWhichDoesnotHaveImages (PASS)");

        } catch (Throwable t) {
            System.out.println("❌ EXIT userFiltersTheProductsImportedFromAndSubmitToStepWhichDoesnotHaveImages (FAIL): " + t);
            throw t;
        } */
    }

    @And("User expand Ecom column and provide required values as per {string}")
    public void userExpandEcomColumnAndProvideRequiredValuesAsPer(String SheetName) throws Exception {
        /*
        try {
            String excelPath = ConfigFileReader.get("excelPath");
            if (excelPath == null || excelPath.isEmpty()) {
                throw new IllegalArgumentException("Excel path not found in config.properties.");
            }
            System.out.println("Excel path from config: " + excelPath);

            String columnHeader = "ID";

            // Method to filter product and add to channel
            pdxMasterPage.filterProductsThenAddToChannel(excelPath, SheetName, columnHeader);
            System.out.println("✅ EXIT filterProductsThenAddToChannel (PASS)");

            // Method to filter product and submit to Step
            pdxChannelPage.filterProductThenSubmitToStep(excelPath, SheetName, columnHeader);
            System.out.println("✅ EXIT filterProductThenSubmitToStep (PASS)");
            System.out.println("✅ EXIT userFiltersTheProductsImportedFromAndSubmitToStepWhichDoesnotHaveImages (PASS)");

        } catch (Throwable t) {
            System.out.println("❌ EXIT userFiltersTheProductsImportedFromAndSubmitToStepWhichDoesnotHaveImages (FAIL): " + t);
            throw t;
        } */
    }

    @And("User expand Hierarchy column and provide required values as per {string}")
    public void userExpandHierarchyColumnAndProvideRequiredValuesAsPer(String SheetName) throws Exception {
        /*
        try {
            String excelPath = ConfigFileReader.get("excelPath");
            if (excelPath == null || excelPath.isEmpty()) {
                throw new IllegalArgumentException("Excel path not found in config.properties.");
            }
            System.out.println("Excel path from config: " + excelPath);

            String columnHeader = "ID";

            // Method to filter product and add to channel
            pdxMasterPage.filterProductsThenAddToChannel(excelPath, SheetName, columnHeader);
            System.out.println("✅ EXIT filterProductsThenAddToChannel (PASS)");

            // Method to filter product and submit to Step
            pdxChannelPage.filterProductThenSubmitToStep(excelPath, SheetName, columnHeader);
            System.out.println("✅ EXIT filterProductThenSubmitToStep (PASS)");
            System.out.println("✅ EXIT userFiltersTheProductsImportedFromAndSubmitToStepWhichDoesnotHaveImages (PASS)");

        } catch (Throwable t) {
            System.out.println("❌ EXIT userFiltersTheProductsImportedFromAndSubmitToStepWhichDoesnotHaveImages (FAIL): " + t);
            throw t;
        } */
    }

    @And("User expand Pricing column and provide required values as per {string}")
    public void userExpandPricingColumnAndProvideRequiredValuesAsPer(String SheetName) throws Exception {
        /*
        try {
            String excelPath = ConfigFileReader.get("excelPath");
            if (excelPath == null || excelPath.isEmpty()) {
                throw new IllegalArgumentException("Excel path not found in config.properties.");
            }
            System.out.println("Excel path from config: " + excelPath);

            String columnHeader = "ID";

            // Method to filter product and add to channel
            pdxMasterPage.filterProductsThenAddToChannel(excelPath, SheetName, columnHeader);
            System.out.println("✅ EXIT filterProductsThenAddToChannel (PASS)");

            // Method to filter product and submit to Step
            pdxChannelPage.filterProductThenSubmitToStep(excelPath, SheetName, columnHeader);
            System.out.println("✅ EXIT filterProductThenSubmitToStep (PASS)");
            System.out.println("✅ EXIT userFiltersTheProductsImportedFromAndSubmitToStepWhichDoesnotHaveImages (PASS)");

        } catch (Throwable t) {
            System.out.println("❌ EXIT userFiltersTheProductsImportedFromAndSubmitToStepWhichDoesnotHaveImages (FAIL): " + t);
            throw t;
        } */
    }

    @When("User click on Asset approval link and verify it navigates to Asset approval page")
    public void userClickOnAssetApprovalLinkAndVerifyItNavigatesToAssetApprovalPage() throws Exception {

        // Method to navigate to 'Asset approval' page
        stepAssetApprovalPage.gotoAssetApprovalAndVerify();
        System.out.println("✅ EXIT gotoAssetApprovalAndVerify (PASS)");
    }

    @And("User filter products as per product ID from {string} then do Asset approval")
    public void userFilterProductsAsPerProductIDFromThenDoAssetApproval(String SheetName) throws Exception {

        try {
            String excelPath = ConfigFileReader.get("excelPath");
            if (excelPath == null || excelPath.isEmpty()) {
                throw new IllegalArgumentException("Excel path not found in config.properties.");
            }
            System.out.println("Excel path from config: " + excelPath);

            String columnHeader = "BrandID";

            // Method to filter product and do Asset approval
            stepAssetApprovalPage.filterProductsThenDoAssetApproval(excelPath, SheetName, columnHeader);
            System.out.println("✅ EXIT filterProductsThenDoAssetApproval (PASS)");

        } catch (Throwable t) {
            System.out.println("❌ EXIT filterProductsThenDoAssetApproval (FAIL): " + t);
            throw t;
        }
    }

    @When("User click on Attribute approval link and verify it navigates to Attribute approval page")
    public void userClickOnAttributeApprovalLinkAndVerifyItNavigatesToAttributeApprovalPage() throws InterruptedException {

        // Method to navigate to 'Attribute approval' page
        stepAttributeApprovalPage.gotoAttributeApprovalAndVerify();
        System.out.println("✅ EXIT gotoAttributeApprovalAndVerify (PASS)");
    }

    @And("User filter products as per product ID from {string} then do Attribute approval")
    public void userFilterProductsAsPerProductIDFromThenDoAttributeApproval(String SheetName) throws IOException, InterruptedException {

        try {
            String excelPath = ConfigFileReader.get("excelPath");
            if (excelPath == null || excelPath.isEmpty()) {
                throw new IllegalArgumentException("Excel path not found in config.properties.");
            }
            System.out.println("Excel path from config: " + excelPath);

            String columnHeader = "PDX Product ID";

            // Method to filter product and do Attribute approval
            stepAttributeApprovalPage.filterProductsThenDoAttributeApproval(excelPath, SheetName, columnHeader);
            System.out.println("✅ EXIT filterProductsThenDoAttributeApproval (PASS)");

        } catch (Throwable t) {
            System.out.println("❌ EXIT filterProductsThenDoAttributeApproval (FAIL): " + t);
            throw t;
        }
    }

    @When("User navigate to channel and filter products as per product ID from {string}")
    public void userNavigateToChannelAndFilterProductsAsPerProductIDFrom(String SheetName) throws IOException, InterruptedException {

        try {
            String excelPath = ConfigFileReader.get("excelPath");
            if (excelPath == null || excelPath.isEmpty()) {
                throw new IllegalArgumentException("Excel path not found in config.properties.");
            }
            System.out.println("Excel path from config: " + excelPath);

            String columnHeader = "ID";

            // Method to filter product in channel grid view
            pdxChannelPage.filterProductsInChannelGridView(excelPath, SheetName, columnHeader);
            System.out.println("✅ EXIT filterProductsInChannelGridView (PASS)");

        } catch (Throwable t) {
            System.out.println("❌ EXIT userNavigateToChannelAndFilterProductsAsPerProductIDFrom (FAIL): " + t);
            throw t;
        }
    }

    @And("User Upload Image for products from {string} and submit to step")
    public void userUploadImageForProductsFromAndSubmitToStep(String SheetName) throws IOException, InterruptedException {

        try {
            String excelPath = ConfigFileReader.get("excelPath");
            if (excelPath == null || excelPath.isEmpty()) {
                throw new IllegalArgumentException("Excel path not found in config.properties.");
            }
            System.out.println("Excel path from config: " + excelPath);

            // Method to upload image as mentioned in Excel
            String imgColumn = "Image Location";
            pdxChannelPage.UploadImageFromAsPerExcel(excelPath, SheetName, imgColumn);
            System.out.println("✅ EXIT UploadImageFromAsPerExcel (PASS)");

        } catch (Throwable t) {
            System.out.println("❌ EXIT userNavigateToChannelAndFilterProductsAsPerProductIDFrom (FAIL): " + t);
            throw t;
        }
    }
}
