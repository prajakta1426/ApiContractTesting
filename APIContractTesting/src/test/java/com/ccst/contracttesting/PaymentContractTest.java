package com.ccst.contracttesting;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.authentication;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;


public class PaymentContractTest {
    @BeforeClass
    public void setup(){
        RestAssured.baseURI="http://localhost/";
        RestAssured.port=8080;
    }

    @Test
    public void statusIs200()
    {
        given().when().get("/payments/2")
                .then().statusCode(200);
    }

    @Test
    public void responseTimeUnder1000s()
    {
        given().when().get("/payments/2")
                .then().time(lessThan(1000L));
    }

    @Test
    public void hasIdAndStatus()
    {
        given().when().get("/payments/2")
                .then().body("id",notNullValue()).body("status",notNullValue());
    }

    @Test
    public void matchesContractSchema()
    {
        given().when().get("/payments/2")
                .then().body(matchesJsonSchemaInClasspath("payment-schema.json"));
    }

    @Test
    public void unknownPaymentReturns404()
    {
        given().when().get("/payments/999")
                .then().statusCode(404);
    }
}
