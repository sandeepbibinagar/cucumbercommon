@skip
Feature: Burp Suite Enterprise Application Scan
  In order to obtain a security status for an application
  As a Security tester
  I want to run dynamic scan to a URL

  Scenario: Burp Suite Enterprise scan
    Given I log into Burp Suite Enterprise with valid credentials
    And I create Burp Suite Site with username "adm@example.com" and password "Password123"
    When I run scan
    Then I check for vulnerabilities
    And I delete the site