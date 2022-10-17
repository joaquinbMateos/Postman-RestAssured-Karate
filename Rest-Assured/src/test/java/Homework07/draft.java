package Homework07;

import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class draft {

    @Test @Ignore
    public void getRequest(){
        given()
                .baseUri("https://automationintesting.online")
                .when()
                .get("/room")
                .then()
                .statusCode(200);
    }

    @Test @Ignore
    public void validateJsonRequest(){
        given()
                .baseUri("https://automationintesting.online")
                .when()
                .get("/room")
                .then()
                .statusCode(200)
                .body("rooms[0].roomName", equalTo("101"));
    }

    @Test @Ignore
    public void extractResponseData(){
        Response response = given()
                .baseUri("https://automationintesting.online")
                .when()
                .get("/room")
                .then().extract().response();
        response.prettyPrint();
    }

    @Test @Ignore
    public void extractSingleValue(){
        baseURI = "https://automationintesting.online";
        Response response = given().contentType(ContentType.JSON).get("/room"); //.log().all()
        JsonPath extractor = response.jsonPath();
        int roomID = extractor.get("rooms[0].roomid");
        System.out.println("Room id is: "+roomID);
    }

    @Test @Ignore
    public void verifyStatusLine(){
        given()
                .baseUri("https://automationintesting.online")
                .when()
                .get("/room")
                .then()
                .statusLine("HTTP/1.1 200 OK");
    }

    // -------------- POST REQUEST -------------- //

    @Test @Ignore
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

    @Test @Ignore
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
    public void postBooking2(){
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
