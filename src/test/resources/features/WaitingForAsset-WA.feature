
Feature: Verify Waiting for Asset workflow

  @consignment_pdx_login_WA
  Scenario: Login with valid credentials
    When User navigate to pdx application
    And User enter valid credentials
    Then User should land on pdx home page

  @consignment_pdx_import_excel_WA
  Scenario Outline: Import data, Add to channel, Validate and submit data
    When User click on import data and upload excel data "<SheetName>"
    And User click Master data
    And User filters the products imported from "<SheetName>" and submit to Step which doesn't have images
    Examples:
      | SheetName    |
      | consignment  |

  @consignment_step_login_WA
  Scenario: Login Step application and verify user landed on homepage
    When User navigate to Step application
    And User login with valid credentials
    Then User should land on brands onboarding home page

  @consignment_step_waitingForAsset_WA
  Scenario Outline: User perform buyers approval validations and set Waiting for Asset(WA) proposal status
    When user click onto the Buyer approval screen
    Then validate user landed into buyers approval page
    When User click on Date First Submitted by Brand
    And User filter the date to descending order
    And User filter products as per product ID from "<SheetName>"
    When User click on proposal status and set it mentioned as per "<SheetName>" e.g Waiting for Asset(WA)
    And User enter message to supplier as mentioned in "<SheetName>"
    And user select reasons for rework as mentioned in "<SheetName>"
    And User expand Seasonality column and select CoreNewness value as per "<SheetName>"
    And User expand Ecom column and provide required values as per "<SheetName>"
    And User expand Hierarchy column and provide required values as per "<SheetName>"
    And User expand Pricing column and provide required values as per "<SheetName>"
    Examples:
      | SheetName   |
      | consignment |

  @consignment_pdx_upload_image_WA
  Scenario Outline: User navigate to PDX application then upload image and submit to Step
    When User navigate to pdx application
#    And User enter valid credentials
    Then User should land on pdx home page
    When User navigate to channel and filter products as per product ID from "<SheetName>"
    And User Upload Image for products from "<SheetName>" and submit to step
    Examples:
      | SheetName   |
      | consignment |

  @consignment_step_approvals_WA
  Scenario Outline: User navigate to Step application then do Asset approval and Attribute approval
    When User navigate to Step application
    And User login with valid credentials
    Then User should land on brands onboarding home page
    When User click on Asset approval link and verify it navigates to Asset approval page
    And User filter products as per product ID from "<SheetName>" then do Asset approval
    When User click on Attribute approval link and verify it navigates to Attribute approval page
    And User filter products as per product ID from "<SheetName>" then do Attribute approval
    Examples:
      | SheetName   |
      | consignment |