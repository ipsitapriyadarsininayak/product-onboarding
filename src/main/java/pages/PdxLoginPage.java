package pages;

import constants.ElementLocators;
import utils.Base64Utils;
import utils.ConfigFileReader;
import utils.NotepadReader;

import java.io.IOException;

public class PdxLoginPage extends BasePage{

    String invalidUsername = System.getenv("InvalidUser");
    String encodedPDX_Username = ConfigFileReader.get("PDX_USERNAME");
    String encodedPDX_Password = ConfigFileReader.get("PDX_PASSWORD");

    public void pdxLogin() {

        String PDX_username = Base64Utils.decode(encodedPDX_Username);
        String PDX_password = Base64Utils.decode(encodedPDX_Password);

        waitForElementVisible(ElementLocators.USERNAME_INPUT_XPATH);
        enterText(ElementLocators.USERNAME_INPUT_XPATH, PDX_username);
        clickElement(ElementLocators.NEXT_BUTTON_XPATH);
        waitForElementVisible(ElementLocators.PASSWORD_INPUT_XPATH);
        enterText(ElementLocators.PASSWORD_INPUT_XPATH, PDX_password);
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
