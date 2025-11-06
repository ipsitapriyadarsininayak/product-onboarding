Feature: Negative Scenario for login

  @negativeLogin
  Scenario: Login using Invalid credentials
    When User enters an invalid username and invalid password and login
    Then Validate the error message "Invalid username or password" is displayed
