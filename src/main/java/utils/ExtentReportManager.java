package utils;
import com.aventstack.extentreports.*;
import com.aventstack.extentreports.gherkin.model.Feature;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.cucumber.java.Scenario;


import com.aventstack.extentreports.ExtentReports;

public class ExtentReportManager {
    private static ExtentReports extent;
    private static ExtentTest feature;
    private static ExtentTest scenario;

    public static ExtentReports getInstance() {
        if (extent == null) {
            ExtentSparkReporter reporter = new ExtentSparkReporter("target/ExtentReport.html");
            reporter.config().setReportName("BDD Test Execution Report");
            reporter.config().setDocumentTitle("Automation Report");

            extent = new ExtentReports();
            extent.attachReporter(reporter);
        }
        return extent;
    }

    public static void createFeature(String featureName) {
        feature = getInstance().createTest(Feature.class, featureName);
    }

    public static void createScenario(String scenarioName) {
        scenario = feature.createNode(String.valueOf(Scenario.class), scenarioName);
    }

    public static void logStep(String step, Status status) {
        scenario.log(status, step);
    }

    public static void flush() {
        getInstance().flush();
    }

    public static void logSuccess(Scenario scenario) {
    }

    public static void logFailure(Scenario scenario) {
    }
}
