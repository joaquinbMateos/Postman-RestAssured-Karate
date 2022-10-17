Feature: get request from room endpoint

  Background: set base url
    Given url 'https://automationintesting.online'

  Scenario: getting rooms from API in JSON format.
    Given path '/room/'
    And header Accept = 'application/json'
    When method get
    Then status 200
    And print response
    * def roomid = response['rooms'][0]['roomid']
    And print roomid
