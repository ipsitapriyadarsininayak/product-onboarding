package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features/WaitingForAsset-WA.feature",
        glue = { "stepDefinitions", "hooks", "support" },
        tags = "@consignment_step_login_WA",

        plugin = {
                "pretty",
                "json:target/cucumber-reports/cucumber.json",  // ✅ for Masterthought
                "html:target/cucumber-reports.html",
        },
        monochrome = true
)

public class TestRunner extends AbstractTestNGCucumberTests { }