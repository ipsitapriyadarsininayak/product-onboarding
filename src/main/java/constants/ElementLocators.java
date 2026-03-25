package constants;

public class ElementLocators {

    // --- PDX application ---
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
    public static final String UPLOAD_FILE_BUTTON_XPATH = "//button[text()=' Upload file ']";
    public static final String EXCEL_SHEET_ABS_PATH = "C:\\Users\\2451875\\OneDrive - TCS COM PROD\\BRAND\\PdxStibo\\PdxStibo\\src\\test\\resources\\data\\TestDataEAN.xlsx ";
    public static final String APPLY_BUTTON_XPATH = "//button[.//span[normalize-space()='Apply']] ";
    public static final String OK_BUTTON_XPATH = "//span[text() =' Ok ']";
    public static final String MASTER_DATA_ICON_XPATH = "//span[text() =' Master data ']";
    public static final String MASTER_DATA_CHANNEL_HEADER_XPATH = "//h1[text() = 'Master data - List view']";
    public static final String APPLY_BUTTON_XPATH1="//span[text() ='Apply']";

    //Filter to current date
    public static final String PRODUCT_NAME_XPATH="//input[@placeholder='Search for a product name']";
    public static final String FILTER_ICON_XPATH = "//mat-icon[text()='filter_list']";
    public static final String LIST_OF_VALUES_XPATH = "//span[text()='List of values']";
    public static final String LIST_OF_VALUES_TEXTBOX_XPATH = "//input[@placeholder='Enter values']";
    public static final String AVAILABLE_IN_CHANNEL_XPATH = "//span[text()=' Available in channel ']";
    public static final String PRODUCT_LIFECYCLE_DATES_XPATH = "//span[normalize-space(text())='Product lifecycle dates']";
    public static final String START_DATE_XPATH = "//input[@placeholder='Start date']";
    public static final String CURRENT_DATE_XPATH = "//button[@aria-current='date']";
    public static final String APPLY_BUTTON_IN_FILTER_XPATH = "//span[text() ='Apply']";

    //Check the productID
    public static final String PRODUCT_NAMES_LIST_XPATH = "(//span[@class='product-name'])";
    public static final String SELECT_ALL_CHECKBOX_XPATH = "(//input[@type='checkbox'])[1]";

    //add to channel
    public static final String THREE_DOTS_ICON_XPATH = "//button[.//mat-icon[normalize-space()='more_vert']]";
    public static final String ADD_TO_CHANNEL_XPATH = "button:nth-of-type(5)";

    //channel & category
    public static final String CHANNEL_AND_CATEGORY_RADIO_XPATH = "//mat-icon[text()='arrow_right_alt']";
    public static final String CHANNEL_DROPDOWN_ARROW_XPATH = "(//mat-icon[text()='keyboard_arrow_down'])[2]";
    public static final String BRANDS_AT_M_AND_S_DEV_XPATH = "//mat-option[@title='BRANDS at M&S DEV']";
    public static final String VERTICLE_SCROLL_XPATH = "/html/body/lp-root/lp-default-app-layout" +
            "/lp-application-layout/lp-application-sidebar-deprecated/" +
            "lp-sidebar-menu-deprecated/mat-sidenav-container/mat-sidenav-content" +
            "/div/div/lp-move-products/div/div[1]";

    //select category
    public static final String CATEGORY_DROPDOWN_ARROW_XPATH = "(//mat-icon[text()='keyboard_arrow_down'])[3]";
    public static final String SEARCH_CATEGORY_TEXTBOX_XPATH = "//input[@placeholder='Search...']";
    public static final String SElECT_SEARCHED_CATEGORY_XPATH = "//mat-icon[text()='style']";

    //add products
    public static final String ADD_PRODUCTS_BUTTON_XPATH = "//span[@class='text']";
    public static final String GRID_VIEW_XPATH = "//mat-icon[text()='grid_on']";
    public static final String FAMILY_PRODUCTS_TAB_XPATH = "//span//p[text()=' Family products ']";

    //filter and Image on Channel page
    public static final String PRODUCT_STATUS_AND_FLAG_XPATH = "//span[text()=' Product status and flags ']";
    public static final String PRODUCT_LIFECYCLE_DATES_CHANNEL_XPATH = "//span[text()=' Product lifecycle dates ']";
    public static final String ADD_IMAGE_ICON_XPATH = ".//td[contains(@class,'primaryAsset')]//lp-dynamic-icon[.//mat-icon[text()='add']]";
    public static final String SEARCH_IMAGE_ICON_XPATH = "(//mat-icon[text()='search'])[2]";
    public static final String SEARCH_IMAGE_TEXTBOX_XPATH = "//div[@class='toolbar-items']/lp-asset-search-bar/lp-search-bar[@class='_active']/input";
    public static final String SEARCHED_IMAGE_SPAN_XPATH = "//div[@class='cdk-virtual-scroll-content-wrapper']//img";
    public static final String ADD_ASSET_BUTTON_XPATH = "//span[text()=' Add assets ']";


    //Submit Icon from Ellipsis
    public static final String ELLIPSIS_XPATH = "(//button[@aria-haspopup='menu'])[3]";
    public static final String SUBMIT_ELLIPSIS_XPATH = "button[aria-label=\"Submit\"]";
    public static final String SUBMIT_CHANNEL_ELLIPSIS_XPATH = "(//div[contains(@class,'cdk-overlay-pane')]//button)[7]";

    //Submit button
    public static final String SUBMIT_BUTTON_XPATH = "//span[text()=' Submit ']";

    //status
    public static final String OK_MESSAGE_XPATH = "//div[contains(text(),'OK')]";
    public static final String SUBMITTED_STATUS_XPATH = "//span[contains(text(),'Submitted')]";

    // Channel
    public static final String CHANNEL_MAIN_MENU_XPATH = "(//span[@class='item-name' ])[3]";
    public static final String CHANNELS_OVERVIEW_SUB_MENU_XPATH = "//span[text()=' Channels overview ']";
    public static final String M_AND_S_DEV_CHANNEL_XPATH = "//span[text()=' BRANDS at M&S DEV ']";
    public static final String GO_TO_LIST_VIEW_CHANNEL_XPATH = "//li[.//a[normalize-space()='Go to list view']]";

    // --- STEP application ---
    //Login
    public static final String STEP_USERNAME_INPUT_XPATH = "//input[@placeholder='Username']";
    public static final String STEP_PASSWORD_INPUT_XPATH = "//input[@id='password-input']";
    public static final String STEP_LOGIN_BUTTON_XPATH = "//span[text()='Login']";
    public static final String BRANDS_ONBOARDING_HEADER_XPATH = "(//div[@title='Brands Onboarding'])[1]";
    public static final String M_AND_S_LOGO_HOME_XPATH = "//div[@id='stibo-element-navbar-logo']";

    //Buyer approval elements
    public static final String ALL_USERS="(//div[@title='Show all items assigned to any user'])[1]";
    public static final String BUYERS_APPROVAL_LINK_XPATH = "//div[@title ='Buyer Approval']";
    public static final String BUYERS_APPROVAL_HEADER_XPATH = "//div[text() ='Buyer Approval ']";
    public static final String DATE_FIRST_SUBMITTED_BY_BRAND_XPATH = "//div[@style='max-height: 45px;']//span[@title='Date First Submitted by Brand']";
    public static final String FILTER_DATE_TO_DESCENDING_XPATH = "//div[text()='Descending (Z-A)']";
    public static final String PDX_PRODUCT_ID_HEADER_XPATH = "//th//span[@title='PDX Product ID']";
    public static final String FILTER_BY_INCLUDE_ONLY_XPATH = "//select[contains(@class, 'sortOptionsListBox')]//option[@value='Include only']";
    public static final String FILTER_TEXTBOX_XPATH = "//textarea[@placeholder='Value or text']";
    public static final String APPLY_FILTER_BUTTON_XPATH = "//button[.//span[normalize-space()='Apply filter']]";
    public static final String SELECT_ALL_BUTTON_XPATH = "//div[text()='Select All']";
    public static final String SCROLLBAR_DIV_XPATH = "//div[contains(@class, 'sheet-scroll-container')]";
    public static final String PROPOSAL_STATUS_HEADER_XPATH = "(//span[@title='Proposal Status'])[1]";
    public static final String PROPOSAL_STATUS_COLUMN_XPATH = "//td[@data-col='10']";
    public static final String MESSAGE_TO_SUPPLIER_HEADER_XPATH = "//th[@id='TableHeader_Message_To_Supplier' and @col-header-id='11']";
    public static final String MESSAGE_TO_SUPPLIER_COLUMN_XPATH = "//td[@data-col='11']";
    public static final String REASONS_FOR_REWORK_HEADER_XPATH = "(//span[@title='Reasons For Rework'])[1]";
    public static final String REASONS_FOR_REWORK_COLUMN_XPATH = "//td[@data-col='12']";
    public static final String REASONS_FOR_REWORK_DROPDOWN_XPATH = "//select[contains(@class,'dropdown')]";
    public static final String REASONS_FOR_REWORK_DROPDOWN_SAVE_BUTTON_XPATH = "//span[text()='Save']";
    public static final String CHECK_MANDATORY_ATTRIBUTE_BUTTON_XPATH = "//div[text() ='Check Mandatory Attributes']";
    public static final String SEASONALITY_TITLE_HEADER_XPATH = "//span[@title='Seasonality']";
    public static final String CORE_NEWNESS_XPATH="//th[@class='last-of-group-child'][1]";
    //public static final String TEXT_XPATH=" //td[@class='cell-selected cell-selected-primary selected sheet-coll']";
    //public static final String TEXT_XPATH="//textarea[@placeholder='Value or text']";
    public static final String textFieldLocator="(//tr[@class='even']/td[20])[1])));";
    public static final String Pricing_XPATH ="//span[@title='Pricing']";
    public static final String Cost_Price_XPATH ="(//span[text()='Cost Price'])[1]";
    public static final String scrollableContainer_XPATH="//div[contains(@class, 'sheet-scroll-container')]";
    public static final String ASSET_APPROVAL_LINK_XPATH ="//div[@title='Asset Approval']";
    public static final String BRAND_REF_ID_HEADER_XPATH ="(//span[@title='Brand Ref ID'])[1]";
    public static final String MESSAGE_TO_SUPPLIER_HEADER_ASSET_XPATH = "(//span[@title='Message To Supplier'])[1]";
    public static final String MESSAGE_TO_SUPPLIER_COLUMN_ASSET_XPATH = "//td[@data-col='16']";
    public static final String ELLIPSIS_BUTTON_XPATH ="//i[text()='more_horiz']";
    public static final String APPROVAL_SUBMIT_BUTTON_XPATH ="//div[text()='Submit']";
    public static final String APPROVAL_TEXT_AREA_XPATH ="//textarea[@class='gwt-TextArea FormFieldWidget']";
    public static final String APPROVAL_OK_BUTTON_XPATH ="//span[text()='OK']";
    public static final String CLEAR_FILTER_BUTTON_XPATH ="//button[.//span[normalize-space()='Clear filter']]";
    public static final String ATTRIBUTE_APPROVAL_LINK_XPATH ="//div[@title='Attribute Approval']";
    public static final String ATTRIBUTE_ALERT_POPUP_XPATH ="//i[@class='material-icons portal-alert-popup-close-box__button']";
}




