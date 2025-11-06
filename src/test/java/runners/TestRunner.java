package runners;


/*import io.cucumber.junit.Cucumber;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.junit.runner.RunWith;*/

//import io.cucumber.junit.Cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue ={"stepdefinitions", "hooks"},
        tags ="@wholesale_step_login or @wholesale_step_approvals ",
        plugin = {
                "pretty",
                "json:target/cucumber-reports/cucumber.json",  // ✅ Required for maven-cucumber-reporting
                "html:target/cucumber-reports.html",
                "junit:target/cucumber.xml"
        },

        monochrome = true


)
public class TestRunner extends AbstractTestNGCucumberTests {





}
