package Homework07;

import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class homework07_02 {

    // -------------- GET REQUEST -------------- //


    @Test
    public void getRequest(){
        given()
                .baseUri("https://automationintesting.online")
                .when()
                .get("/room")
                .then()
                .statusCode(200);
    }

    @Test
    public void validateJsonRequest(){
        given()
                .baseUri("https://automationintesting.online")
                .when()
                .get("/room")
                .then()
                .statusCode(200)
                .body("rooms[0].roomName", equalTo("101"));
    }

    @Test
    public void extractRoomID(){
        baseURI = "https://automationintesting.online";
        Response response = given().contentType(ContentType.JSON).get("/room"); //.log().all()
        JsonPath extractor = response.jsonPath();
        int roomID = extractor.get("rooms[0].roomid");
        System.out.println("Room id is: "+roomID);
    }


    // -------------- POST REQUEST -------------- //

    public String getToken(){
        JSONObject body = new JSONObject();
        body.put("username", "admin");
        body.put("password", "password");

        Headers headers = given()
                .baseUri("https://automationintesting.online")
                .contentType(ContentType.JSON)
                .body(body.toString())
                .when()
                .post("/auth/login")
                .then()
                //.log().all()
                .statusCode(200)
                .extract().headers();

        String token = headers.getValue("Set-Cookie");
        System.out.println("token value is: "+token);
        return token;
    }

    @Test
    public void postBooking(){
        File file = new File("src/resources/booking.json");
        String token = getToken();

        given()
                .baseUri("https://automationintesting.online")
                .cookie(token)
                .contentType(ContentType.JSON)
                .body(file)
                .when()
                .post("/booking/")
                .then()
                .log().all()
                .statusCode(201);
    }

    @Test
    public void postBookingParameters(){
        String token = getToken();
        //Do a GET call against https://automationintesting.online/room/ , you should get 1-N rooms returned:
        baseURI = "https://automationintesting.online";
        Response response = given().contentType(ContentType.JSON).get("/room"); //.log().all()
        response.prettyPrint();
        JsonPath extractor = response.jsonPath();

        //Extract a room ID from one of the rooms:
        int roomID = extractor.get("rooms[0].roomid");
        System.out.println("Room id is: "+roomID);

        //vars
        String inDate = "2022-11-01";
        String outDate = "2022-11-03";
        String checkName = "joaquin";
        String checkLastName = "mateos";

        //Json body:
        JSONObject booking_dates = new JSONObject();
        booking_dates.put("checkin", inDate);
        booking_dates.put("checkout", outDate);

        JSONObject body = new JSONObject();
        body.put("bookingdates", booking_dates);
        body.put("despositpaid", true);
        body.put("firstname", checkName);
        body.put("lastname", checkLastName);
        body.put("roomid", roomID);
        body.put("totalprice", 0);

        given()
                .baseUri("https://automationintesting.online")
                .cookie(token)
                .contentType(ContentType.JSON)
                .body(body.toString())
                .when()
                .post("/booking/")
                .then()
                .log().all()
                .statusCode(201);
    }
}
