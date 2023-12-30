Feature: Parse
  Scenario: Song title consists of letters (including commas)
    Given Title with commas
    When Parsed
    Then Title stays the same