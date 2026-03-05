Feature: Rework in  PDX

@consignment_PDX_Submit_rework
Scenario Outline:  Validate attributes from Master data and submit data
When User navigate to pdx page
And User enter valid credential
Then User should land on pdx home Page "<SheetName>"


  Examples:
    | SheetName   | CoreNewness | term         |
    | consignment | CORE        | Primary size |


