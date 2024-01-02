Feature: Parse
  Scenario: Song title consists of letters (including commas)
    Given The Song Title <song_title>
    When Parsed
    Then The Song Title should be <result>

    Example:
      | song_title | result |
      | You, Clouds, Rain | You, Clouds, Rain
