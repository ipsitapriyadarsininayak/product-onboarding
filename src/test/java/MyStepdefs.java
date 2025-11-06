import com.opencsv.CSVReader;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
//import org.junit.Assert;
import org.testng.Assert;
import pages.PDXPage;

import java.util.List;

public class MyStepdefs {

    PDXPage pdxPage = new PDXPage(); // Your Page Object class
    String CSVFilePath = "src/test/resources/core_newness.csv";

    @Given("User checking the {string} imported to step from PDX")
    public void userCheckingTheImportedToStepFromPDX(String arg0) {
        String productID = "";
        Assert.assertTrue(pdxPage.isProductImported(productID));


    }

    @And("User scrollling to seasonality")
    public void userScrolllingToSeasonality() {
        pdxPage.scrollToSeasonality();

    }

    @And("User clicking on the Core")
    public void userClickingOnTheCore() {
        pdxPage.clickCore();

    }

    @And("User selecting Core/Newness value from CSV")
    public void userSelectingCoreNewnessValueFromCSV() {

        List<String> values = CSVReaderValues(CSVFilePath);

        pdxPage.selectCoreNewness(values.get(0));

    }

    private List<String> CSVReaderValues(String csvFilePath) {
        List<String> strings = new java.util.ArrayList<>();
        return strings;
    }


}
    



