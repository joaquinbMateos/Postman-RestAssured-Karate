Feature: post request to booking endpoint

  Background: set base url
    Given url 'https://automationintesting.online'
    * configure ssl = true

  Scenario: get token from 'auth/login/' endpoint.
    * def getRoom = call read("getRequest.feature")
    * def room = getRoom.roomid

    Given path '/auth/login/'
    And request {"username": "admin","password": "password" }
    When method post
    Then status 200
    * def token = responseHeaders['Set-Cookie']
    And print token

  #Scenario: getting rooms from API in JSON format.
    #* def body = read("booking.json")
    #* def getToken = call read("getToken.feature")
    #* def token = getToken.token

    Given path "/booking/"
    And cookie authToken = token
    And request {"bookingdates": {"checkin": "2022-10-27","checkout": "2022-10-28"} ,"depositpaid": true,"firstname": "joaquin","lastname": "mateos","roomid": "#(room)","totalprice":0}
    When method post
    Then status 201
    And print response
    #And match response.firstname == "joaquin"
