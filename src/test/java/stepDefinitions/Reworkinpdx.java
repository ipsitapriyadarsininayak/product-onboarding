package stepDefinitions;

import constants.ElementLocators;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.BasePage;
import pages.ReworkPDXHomePage;
import utils.ConfigFileReader;

import java.io.IOException;

public class Reworkinpdx extends BasePage
{
    ReworkPDXHomePage reworkPDXHomePage;

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



}





