package com.api.tests;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static com.api.constant.Role.*;
import static com.api.utils.AuthTokenProvider.*;

import static io.restassured.module.jsv.JsonSchemaValidator.*;

import static com.api.utils.ConfigManager.*;

import static io.restassured.RestAssured.*;

public class CountAPITest {
	
	@Test
	public void verifyCountAPIResponse() {
		
		given()
		.baseUri(getProperty("BASE_URI"))
		.and()
		.header("Authorization", getToken(FD))
		.log().uri()
		.log().method()
		.log().headers()
		.when()
		.get("/dashboard/count")
		.then()
		.log().all()
		.statusCode(200)
		.body("message", Matchers.equalTo("Success"))
		.time(Matchers.lessThan(1000L))
		.body("data", Matchers.notNullValue())
		.body("data.size()", Matchers.equalTo(3)) //data size check
		.body("data.count", Matchers.everyItem(Matchers.greaterThanOrEqualTo(0)))
		.body("data.label", Matchers.everyItem(Matchers.not(Matchers.blankOrNullString())))
		.body("data.key", Matchers.containsInAnyOrder("pending_fst_assignment","pending_for_delivery","created_today"))
		.body(matchesJsonSchemaInClasspath("response-schema/CountAPIResponseSchema-FD.json"));
		
	}
	
	@Test
	public void countAPITest_MissingAuthToken() {
		
		given()
		.baseUri(getProperty("BASE_URI"))
		.log().uri()
		.log().method()
		.log().headers()
		.when()
		.get("/dashboard/count")
		.then()
		.log().all()
		.statusCode(401);
	}
}
