package stepDefinitions;

import constants.ElementLocators;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.BasePage;
import pages.PdxLoginPage;
import utils.ConfigFileReader;

import java.io.IOException;


public class PdxStiboLoginSteps extends BasePage {
    PdxLoginPage pdxloginpage;
    public PdxStiboLoginSteps() {

    }

    @When("User navigate to pdx application")
    public void user_navigate_to_pdx_application() throws InterruptedException {
        pdxloginpage=new PdxLoginPage();
        driver.get(ConfigFileReader.get("PdxUrl"));
        Thread.sleep(1000);
    }

    @And("User enter valid credentials")
    public void user_enter_valid_credentials() throws IOException, InterruptedException {
        pdxloginpage.pdxLogin();
    }

    @Then("User should land on pdx home page")
    public void user_should_land_on_pdx_home_page() throws InterruptedException {
        waitForElementVisible(ElementLocators.TITLE_HEADER_XPATH);
        Assert.assertEquals(driver.getTitle(), "Dashboard | Product Data Exchange");
        Thread.sleep(5000);
    }

    @When("User enters an invalid username and invalid password and login")
    public void userEntersAnInvalidUsernameAndInvalidPasswordAndLogin() throws IOException {
        pdxloginpage.pdxInvalidLogin();
    }

    @Then("Validate the error message {string} is displayed")
    public void validateTheErrorMessageIsDisplayed(String expectedMessage) {
        waitForElementVisible(ElementLocators.INVALID_CREDENTIAL_ERROR_XPATH);
        String actualMessage = getElementText(ElementLocators.INVALID_CREDENTIAL_ERROR_XPATH);
        Assert.assertTrue(actualMessage.contains(expectedMessage));
    }
}



