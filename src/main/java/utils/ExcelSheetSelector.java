package utils;
import io.cucumber.java.Scenario;

public class ExcelSheetSelector {


    public static String getSheetNameForScenario(Scenario scenario) {
        if (scenario == null || scenario.getSourceTagNames() == null) {
            throw new IllegalArgumentException("Scenario or tags are null.");
        }

        if (scenario.getSourceTagNames().contains("@consignment")) {
            return "Sheet1";
        } else if (scenario.getSourceTagNames().contains("@wholesale")) {
            return "Sheet2";
        } else {
            throw new IllegalArgumentException("Scenario must be tagged with either @consignment or @wholesale.");
        }
    }
}


