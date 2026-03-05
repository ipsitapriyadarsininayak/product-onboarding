Feature: ReworkSize in STEP to PDX


  @consignment_step_approvals_rework1
  Scenario Outline:When User opens the Step application
    When The user navigates to the Buyer Approval screen
    Then Verify that the user is on the Buyer Approval page
    And user filter date to descending order
    And The user verifies that "<SheetName>" has been imported into Step
    #And User check the "<SheetName>" imported to Step
    And Remove message to suplier.

    When I double click on the text field For "<SheetName>"
    And User click on Pricing from "<SheetName>"

    Examples:
      | SheetName   | CoreNewness |
      | consignment | CORE        |