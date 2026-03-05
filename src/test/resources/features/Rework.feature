
Feature: Rework in STEP to PDX


    @consignment_step_approvals_rework
   Scenario Outline:When User opens the Step application
    When the user navigates to the Buyer Approval screen
    Then verify that the user is on the Buyer Approval page
      And User filter date to descending order
    And the user verifies that "<SheetName>" has been imported into Step
    When user click on proposal status from "<SheetName>"
      And user send message to suppiler from "<SheetName>"
      And user click on reason for work from"<SheetName>"
      And click on menu to submit the event
      Examples:
        | SheetName   | CoreNewness |
        | consignment | CORE        |










