Feature: post request to booking endpoint with parameters

  Background: set base url
    Given url 'https://automationintesting.online'
    * configure ssl = true

  Scenario: get token from 'auth/login/' endpoint.
    * def getRoom = call read("getRoom.feature")
    * def room = getRoom.roomid

    * def inDate = "2020-12-28"
    * def outDate = "2020-12-29"
    * def firstName = "joaquin"
    * def lastName = "mateos"

    Given path '/auth/login/'
    And request {"username": "admin","password": "password" }
    When method post
    Then status 200
    * def token = responseHeaders['Set-Cookie']
    And print token

    Given path "/booking/"
    And cookie authToken = token
    And request {"bookingdates": {"checkin": "#(inDate)","checkout": "#(outDate)"} ,"depositpaid": true,"firstname": "#(firstName)","lastname": "#(lastName)","roomid": "#(room)","totalprice":0}
    When method post
    Then status 201
    And print response
    #And match response.firstname == "joaquin"