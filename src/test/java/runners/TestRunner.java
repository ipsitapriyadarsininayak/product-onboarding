package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features/WaitingForAsset-WA.feature",
        glue = { "stepDefinitions", "hooks", "support" },
        tags = "@consignment_pdx_login_WA or @consignment_pdx_import_excel_WA or @consignment_step_login_WA or " +
                "@consignment_step_waitingForAsset_WA or @consignment_pdx_upload_image_WA or @consignment_step_approvals_WA",

        plugin = {
                "pretty",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",  // ✅ Extent
                "json:target/cucumber-reports/cucumber.json",  // ✅ for Masterthought
        },
        monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests { }