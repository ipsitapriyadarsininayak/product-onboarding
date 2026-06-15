
Feature: Verify Rework workflow in STEP to PDX

  @consignment_pdx_login
  Scenario: Login with valid credentials
    When User navigate to pdx application
      And User enter valid credentials
    Then User should land on pdx home page

  @consignment_pdx_import_excel
  Scenario Outline: Import data, Add to channel, Validate and submit data
    When User click on import data and upload excel data "<SheetName>"
    And User click Master data
      #And User filter to current date
    And User checks the product IDs imported from "<SheetName>"
    And User Upload Image for products from "<SheetName>"
    Examples:
      | SheetName    |
      | consignment  |

    @consignment_step_approvals_rework
   Scenario Outline:When User opens the Step application
    When the user navigates to the Buyer Approval screen
    Then verify that the user is on the Buyer Approval page
      And User filter date to descending order
      And the user verifies that "<SheetName>" has been imported into Step
    When user click on proposal status from "<SheetName>"
      And user send message to supplier from "<SheetName>"
      And user click on reason for work from"<SheetName>"
      And click on menu to submit the event
      Examples:
        | SheetName   |
        | consignment |

  @consignment_PDX_Submit_rework
  Scenario Outline:  Validate attributes from Master data and submit data
    When User navigate to pdx page
      And User enter valid credential
    Then User should land on pdx home Page "<SheetName>"
      Examples:
        | SheetName   |
        | consignment |

  @consignment_step_approvals_rework1
  Scenario Outline:When User opens the Step application
    When The user navigates to the Buyer Approval screen
    Then Verify that the user is on the Buyer Approval page
      And user filter date to descending order
      And The user verifies that "<SheetName>" has been imported into Step
      #And User check the "<SheetName>" imported to Step
      And Remove message to supplier.
    When I double click on the text field For "<SheetName>"
      And User click on Pricing from "<SheetName>"
      Examples:
        | SheetName   |
        | consignment |










