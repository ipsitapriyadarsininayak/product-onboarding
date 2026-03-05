
Feature: Consignment/Dropship Create Product

  @consignment_pdx_login
  Scenario: Login with valid credentials
    When User navigate to pdx application
    And User enter valid credentials
    Then User should land on pdx home page

  @consignment_pdx_import_excel

 Scenario Outline: Import data, Add to channel, Validate and submit data
    When User click on import data and upload excel data "<SheetName>"
    And User click Master data
   # And User filter to current date
    And User checks the product IDs imported from "<SheetName>"
    And User Upload Image for products from "<SheetName>"




    Examples:
      | SheetName    | category | Catagory Name      |
      | consignment  |Bra Accessories | Bra Accessories|





  @consignment_step_login
  Scenario: Login with Step and complete Brands onboarding
    When User navigate to Step application
    And User login with valid credentials
    Then User should land on brands onboarding home page

  @consignment_step_approvals
  Scenario Outline: Buyers approval validations
    When user click onto the Buyer approval screen
    Then validate user landed into buyers approval page
    When User click on Date First Submitted by Brand
    And User filter the date to descending order
    And User check the "<SheetName>" imported to step
    And User scroll to seasonality header
#    And User click on the Core Newness
#    And User select "<CoreNewness>" in the dropdown
#    And User enters text key in the search field
    When I double click on the text field for "<SheetName>"
    And the user scrolls the horizontal bar to see pricing in sheet "<SheetName>"
    And User click on Pricing
    When user clicks on Asset Approval
    Then validate user landed into asset approval screen
    When User click on Date First Submitted by Brand in page
    #When User load PDX Product ID from CSV file with sheet name "<SheetName>"
    #When user clicks on Attribute Approval in sheet "<SheetName>"
    Then User click on Date First Submitted by Brand in Attribute approval Page
    #When User load PDX Product ID from CSV file with sheet name "<SheetName>"
    Then the user should be on the Attribute Approval page



    Examples:
      | SheetName  | CoreNewness |
      |consignment  | CORE        |











