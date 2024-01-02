package api_testing;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import files.Payload;
import files.ReUsableMethods;

public class Basics {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//given--all input
		//when--submit the api (resource, http method)
		//then--validate the response
		RestAssured.baseURI="https://rahulshettyacademy.com";
		String response = given().log().all().queryParam("key", "qaclick123").headers("content-type","application/json").
		body(Payload.Addplace()).when().post("maps/api/place/add/json").then().assertThat().statusCode(200).
		body("scope", equalTo("APP")).header("server", "Apache/2.4.52 (Ubuntu)").extract().response().asString();
	
		//Add place-> Update Place with New Address -> Get Place to validate if New address is present in response
		
		System.out.println(response);
		
		JsonPath js= new JsonPath(response);
		
		String place_id =js.getString("place_id");
		
		System.out.println(place_id);
	
		
		//update place
		String newAddress = "Summer Walk, Africa";
		
		given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body("{\r\n" + 
				"\"place_id\":\""+place_id+"\",\r\n" + 
				"\"address\":\""+newAddress+"\",\r\n" + 
				"\"key\":\"qaclick123\"\r\n" + 
				"}").
		when().put("maps/api/place/update/json")
		.then().assertThat().log().all().statusCode(200).
		body("msg", equalTo("Address successfully updated"));
		
		
		
		// Get place
		
		String getPlaceResponse = given().log().all().queryParam("key", "qaclick123").
		queryParam("place_id", place_id).when().get("maps/api/place/update/json").
		then().assertThat().log().all().statusCode(200).extract().response().asString();
		
		JsonPath js1=ReUsableMethods.rawToJson(getPlaceResponse);
		String actualaddress = js1.getString(getPlaceResponse);
		System.out.println(actualaddress);
		Assert.assertEquals(actualaddress, newAddress);	
	
	
	}

}
