package com.ccst.contracttesting;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.lessThan;

public class CountriesContractTest {
    String apiKey = "rc_live_185ee978616845e698291f8841fe3d64";
    @BeforeClass
    public void setup()
    {
        RestAssured.baseURI="https://api.restcountries.com";
    }

    @Test
    public void statusIs200()
    {
        given().header("Authorization","Bearer "+apiKey).when().get("/countries/v5")
                .then().statusCode(200);
    }
    @Test
    public void responseTimeUnder2000s()
    {
        given().header("Authorization","Bearer "+apiKey).when().get("/countries/v5")
                .then().time(lessThan(2000L));
    }
    @Test
    public void matchesContractSchema()
    {
        given().header("Authorization","Bearer "+apiKey).
                when().get("/countries/v5?q=canada")
                .then().body(matchesJsonSchemaInClasspath("countries-schema.json"));
    }
    @Test
    public void noAuthorizationReturns401()
    {
        given().when().get("/countries/v5?q=canada")
                .then().statusCode(401);
    }
    @Test
    public void bearerWrongKeyReturns401()
    {
        given().header("Authorization","Bearer "+apiKey+"abc").
                when().get("/countries/v5?q=canada")
                .then().statusCode(401);
    }
}
