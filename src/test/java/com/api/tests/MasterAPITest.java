package com.api.tests;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import com.api.constant.Role;
import com.api.utils.AuthTokenProvider;
import com.api.utils.ConfigManager;

import io.restassured.module.jsv.JsonSchemaValidator;

import static io.restassured.RestAssured.*;

public class MasterAPITest {
	
	@Test
	public void masterAPITest() {
		
		given()
		.baseUri(ConfigManager.getProperty("BASE_URI"))
		.and()
		.headers("Authorization", AuthTokenProvider.getToken(Role.FD))
		.and()
		.contentType("")
		.log().all()
		.when()
		.post("master")
		.then()
		.log().all()
		.statusCode(200)
		.time(Matchers.lessThan(1000L))
		.body("message", Matchers.equalTo("Success"))
		.body("data", Matchers.notNullValue())
		.body("data", Matchers.hasKey("mst_oem")) //Key check
		.body("data", Matchers.hasKey("mst_model"))
		.body("$", Matchers.hasKey("message")) //$ means outer json
		.body("$", Matchers.hasKey("data"))
		.body("data.mst_oem.size()", Matchers.equalTo(2)) //check the size of the JSON array with matchers
		.body("data.mst_model.size()", Matchers.greaterThan(0))
		.body("data.mst_oem.id", Matchers.everyItem(Matchers.notNullValue()))
		.body("data.mst_oem.name", Matchers.everyItem(Matchers.notNullValue()))
		.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("response-schema/MasterAPIResponseSchema.json"));
		
		
		
	}
	
	
	@Test
	public void invalidTokenMasterAPITest() {
		
		given()
		.baseUri(ConfigManager.getProperty("BASE_URI"))
		.and()
		.headers("Authorization", "")
		.and()
		.contentType("")
		.log().all()
		.when()
		.post("master")
		.then()
		.log().all()
		.statusCode(401);
		
		
		
	}

}
