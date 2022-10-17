Feature: match response

  Background: set base url
    Given url 'https://automationintesting.online'

  Scenario: getting response in JSON format.
    Given path '/room/'
    And header Accept = 'application/json'
    When method get
    Then status 200
    And print response
    And match response ==
    """
    {
  "rooms": [
    {
      "roomid": 1,
      "roomName": "101",
      "type": "single",
      "accessible": true,
      "image": "https://www.mwtestconsultancy.co.uk/img/testim/room2.jpg",
      "description": "Aenean porttitor mauris sit amet lacinia molestie. In posuere accumsan aliquet. Maecenas sit amet nisl massa. Interdum et malesuada fames ac ante.",
      "features": [
        "TV",
        "WiFi",
        "Safe"
      ],
      "roomPrice": 100
    }
  ]
}
    """