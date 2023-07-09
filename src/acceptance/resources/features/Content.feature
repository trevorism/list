Feature: CRUD a data list
  Create, read, update, delete a list

  Scenario: Read a data list
    Given the list application is alive
    And a new list container is created
    And contains content with the value "test"
    When the container content is requested
    Then the content includes "test"
    And the list container is deleted successfully

  Scenario: Update a data list
    Given the list application is alive
    And a new list container is created
    And contains content with the value "test"
    When the container content is updated with "test2"
    Then the content includes "test" and "test2"
    And the list container is deleted successfully
