Feature: get token from 'auth/login/' endpoint.

  Background: set base url
    Given url 'https://automationintesting.online'

  Scenario: get token from 'auth/login/' endpoint.
    Given path '/auth/login/'
    And request {"username": "admin","password": "password" }
    When method post
    Then status 200
    * def token = responseHeaders['Set-Cookie']
    And print token