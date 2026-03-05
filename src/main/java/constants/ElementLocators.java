package constants;

public class ElementLocators {

    // PDX
    //login
    public static final String USERNAME_INPUT_XPATH = "//input[@autocomplete='username']";
    public static final String PASSWORD_INPUT_XPATH = "//input[@autocomplete='current-password']";
    public static final String NEXT_BUTTON_XPATH = "//span[text()=' Next ']";
    public static final String LOGIN_BUTTON_XPATH = "//span[text()=' Login ']";
    public static final String TITLE_HEADER_XPATH = "//h1[text()='Dashboard']";

    //Invalid login error message
    public static final String INVALID_CREDENTIAL_ERROR_XPATH = " //span[text()='Invalid username or password.']";

    //Import Data
    public static final String IMPORT_DATA_ICON_XPATH = "//span[text()=' Import data ']";
    public static final String UPLOAD_FILE_BUTTON_XPATH = "//button[text() = ' Upload file ']";
    public static final String EXCEL_SHEET_ABS_PATH = "C:\\Users\\2451875\\OneDrive - TCS COM PROD\\BRAND\\PdxStibo\\PdxStibo\\src\\test\\resources\\data\\TestDataEAN.xlsx ";
    public static final String APPLY_BUTTON_XPATH = "//button[.//span[normalize-space()='Apply']] ";
    public static final String OK_BUTTON_XPATH = "//span[text() =' Ok ']";
    public static final String MASTER_DATA_ICON_XPATH = "//span[text() =' Master data ']";
    public static final String MASTER_DATA_CHANNEL_HEADER_XPATH = "//h1[text() = 'Master data - List view']";
    public static final String APPLY_BUTTON_XPATH1="//span[text() ='Apply']";
    //Filter to current date
    public static final String PRODUCT_NAME_XPATH="//input[@placeholder='Search for a product name']";
    public static final String FILTER_BUTTON_XPATH = "//mat-icon[text() = 'filter_list']";
    public static final String PRODUCT_LIFECYCLE_DATES_DROPDOWN_XPATH = "//span[normalize-space(text())='Product lifecycle dates']";
    public static final String START_DATE_XPATH = "//input[@placeholder='Start date']";
    public static final String CURRENT_DATE_XPATH = "//button[@aria-current='date']";

    //Check the productID
    public static final String PRODUCT_NAMES_LIST_XPATH = "(//span[@class='product-name'])";
    public static final String PRODUCT_CHECKBOX_LIST_XPATH = "//input[@type='checkbox']";

    //add to channel
    public static final String THREE_DOTS_ICON_XPATH = "//button[.//mat-icon[normalize-space()='more_vert']]";
    public static final String ADD_TO_CHANNEL_XPATH = "button:nth-of-type(5)";

    //channel & category
    //public static final String CHANNEL_AND_CATEGORY_XPATH = "-items-//button[@id='lp-pds_sidebarchannels']";
    public static final String CHANNEL_AND_CATEGORY_XPATH="//mat-icon[text()='call_split']";
    //select channel
    public static final String CHANNEL_DROPDOWN_XPATH = "//input[@aria-autocomplete ='list']";
    public static final String BRANDS_AT_M_AND_S_DEV_XPATH = "//span[text() ='BRANDS at M&S DEV']";

    //select category
    public static final String SELECT_A_CATEGORY_DROPDOWN_XPATH = "//span[text() ='Select a category']";
    public static final String SEARCH_INPUT_FIELD_XPATH = "//input[@class ='ng-untouched ng-pristine ng-valid']";
    public static final String SElECT_CATEGORY_XPATH = "//lp-category-list-item[@class = 'ng-star-inserted']";

    //add products
    public static final String ADD_PRODUCTS_BUTTON_XPATH = "//span[text() =' Add products ']";

    //Submit Icon from Menu from 3dots dropdown
    public static final String SUBMIT_MENU_ICON_XPATH = "button:nth-of-type(7) mat-icon";

    //Submit button
    public static final String SUBMIT_BUTTON_XPATH = "//span[text()=' Submit ']";

    //status
    public static final String SUBMITTED_TEXT__XPATH = "//span[text()='Submitted']";
    public static final String STATUS_TEXT__XPATH = "//span[@class='text']";

    //STEP
    //Login
    public static final String STEP_USERNAME_INPUT_XPATH = "//input[@placeholder='Username']";
    public static final String STEP_PASSWORD_INPUT_XPATH = "//input[@id='password-input']";
    public static final String STEP_LOGIN_BUTTON_XPATH = "//span[text()='Login']";
    public static final String BRANDS_ONBOARDING_HEADER_XPATH = "//div[@title='Brands Onboarding']";

    //Buyer approval selectors
    public static final String ALL_USERS="(//div[@title='Show all items assigned to any user'])[1]";
    public static final String BUYERS_APPROVAL_LINK_XPATH = "//div[@title ='Buyer Approval']";
    public static final String BUYERS_APPROVAL_HEADER_XPATH = "//div[text() ='Buyer Approval ']";
    public static final String DATE_FIRST_SUBMITTED_BY_BRAND_XPATH = "//div[@style='max-height: 45px;']//span[@title='Date First Submitted by Brand']";
    public static final String FILTER_DATE_TO_DESCENDING_XPATH = "//div[text()='Descending (Z-A)']";

    public static final String MARKS_AND_SPENCER_PRODUCT_NAMES_LIST_XPATH = "//span[contains(@title,'PDX Product ID')]";
    public static final String MARKS_AND_SPENCER_CHECKBOX_LIST_XPATH = ".//label[@alt='checkbox']";
    public static final String CHECK_MANDATORY_ATTRIBUTE_BUTTON_XPATH = "//div[text() ='Check Mandatory Attributes']";
    public static final String SEASONALITY_TITLE_HEADER_XPATH = "//span[@title='Seasonality']";
    public static final String CORE_NEWNESS_XPATH="//th[@class='last-of-group-child'][1]";
    //public static final String TEXT_XPATH=" //td[@class='cell-selected cell-selected-primary selected sheet-coll']";
    //public static final String TEXT_XPATH="//textarea[@placeholder='Value or text']";
    public static final String textFieldLocator="(//tr[@class='even']/td[20])[1])));";
    public static final String Pricing_XPATH ="//span[@title='Pricing']";
    public static final String Cost_Price_XPATH ="(//span[text()='Cost Price'])[1]";
    public static final String scrollableContainer_XPATH="//div[contains(@class, 'sheet-scroll-container')]";
    public static final String Assetapproval_XPATH="//div[@title='Asset Approval']";
    public static final String AttributeApproval_XPATH="//div[@title='Attribute Approval']";
}




