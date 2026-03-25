package pages;

import constants.ElementLocators;
import utils.ConfigFileReader;
import utils.NotepadReader;

import java.io.IOException;

public class PdxLoginPage extends BasePage{

//    private static final String FILE_PATH = System.getProperty("user.home") + "/Desktop/PdxValidCredentials.txt";
//    String username = System.getenv("USERNAME");
    String invalidUsername = System.getenv("InvalidUser");
    String username = ConfigFileReader.get("PDX_USERNAME");
    String password = ConfigFileReader.get("PDX_PASSWORD");


    public void pdxLogin() throws IOException, InterruptedException {

        /*String password = ExcelReader.getDecodedPassword();*/
//        String password = NotepadReader.getDecodePassword(FILE_PATH);
        waitForElementVisible(ElementLocators.USERNAME_INPUT_XPATH);
        enterText(ElementLocators.USERNAME_INPUT_XPATH, username);
        clickElement(ElementLocators.NEXT_BUTTON_XPATH);
        waitForElementVisible(ElementLocators.PASSWORD_INPUT_XPATH);
        enterText(ElementLocators.PASSWORD_INPUT_XPATH, password);
        clickElement(ElementLocators.LOGIN_BUTTON_XPATH);

    }

    public void pdxInvalidLogin() throws IOException {
        System.out.println("invalidUsername==>" + invalidUsername);
        String InvalidPassword = NotepadReader.getInvalidPassword();

        waitForElementVisible(ElementLocators.USERNAME_INPUT_XPATH);
        enterText(ElementLocators.USERNAME_INPUT_XPATH, invalidUsername);
        clickElement(ElementLocators.NEXT_BUTTON_XPATH);
        waitForElementVisible(ElementLocators.PASSWORD_INPUT_XPATH);
        enterText(ElementLocators.PASSWORD_INPUT_XPATH, InvalidPassword);
        clickElement(ElementLocators.LOGIN_BUTTON_XPATH);

    }
}
