package com.example.sandbox.getPet;

import com.example.sandbox.Common;
import com.example.sandbox.util.swagger.definitions.Status;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.report.TestListener;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import static com.example.sandbox.util.constans.Tags.SMOKE;

@Listeners(TestListener.class)
public class petDetailTest extends Common {

    @Test(enabled = true,groups = {SMOKE},description ="description")
    public void Test1() {
        Map<String, String> queryParams = new TreeMap<>();
        queryParams.put("status", "available");

        Response response = getUrl(findByStatus, queryParams);
        Assert.assertEquals(response.getStatusCode(), 200, "Invalid response code");

        String id = response.jsonPath().get("find{it.status.equals('available')}.id").toString();

        Response response2 = getUrl(petById.replace("{petId}", id));
        Assert.assertEquals(response2.getStatusCode(), 200, "Invalid response code");

        SoftAssert softAssert = new SoftAssert();
        JsonPath jsonPath = new JsonPath(response2.body().asString());

        softAssert.assertNotNull(jsonPath.get("id"), "id is missing!");
        softAssert.assertTrue(isNumber(jsonPath.get("id")), "id format is not correct!");

        softAssert.assertNotNull(jsonPath.get("category.id"), "category.id is missing!");
        softAssert.assertTrue(jsonPath.get("category.id") instanceof Integer, "category.id format is not correct!");

        softAssert.assertNotNull(jsonPath.get("category.name"), "category.name is missing!");
        softAssert.assertTrue(jsonPath.get("category.name") instanceof String, "category.name format is not correct!");

        softAssert.assertNotNull(jsonPath.get("name"), "name is missing!");
        softAssert.assertTrue(jsonPath.get("name") instanceof String, "name format is not correct!");

        softAssert.assertNotNull(jsonPath.get("photoUrls[0]"), "photoUrls[0] is missing!");
        softAssert.assertTrue(jsonPath.get("photoUrls[0]") instanceof String, "photoUrls[0] format is not correct!");

        softAssert.assertNotNull(jsonPath.get("tags[0].id"), "tags[0].id is missing!");
        softAssert.assertTrue(jsonPath.get("tags[0].id") instanceof Integer, "tags[0].id format is not correct!");

        softAssert.assertNotNull(jsonPath.get("tags[0].name"), "tags[0].name is missing!");
        softAssert.assertTrue(jsonPath.get("tags[0].name") instanceof String, "tags[0].name format is not correct!");

        String status = jsonPath.get("status");
        softAssert.assertNotNull(jsonPath.get("status"), "status is missing!");
        softAssert.assertTrue(jsonPath.get("status") instanceof String, "status is not correct!");
        if(!status.isEmpty()){
            softAssert.assertTrue(isValidStatus(status), "status is not an enum value!");
        }
        softAssert.assertAll();

    }

    /* 1. feladat
    * Jobb szeretem használni a SoftAssert osztályt ha egy teszteseten belül több ellenőrzés is van, mert így minden vizsgálatra kerül.
    * */

    @Test(testName = "Find pets by sold status", enabled = true, groups = {SMOKE}, description = "Get all sold pets")
    public void findPetsBySoldStatus(){
        SoftAssert softAssert = new SoftAssert();
        Map<String, String> queryParams = new TreeMap<>();
        queryParams.put("status", "sold");

        Response response = getUrl(findByStatus, queryParams);
        softAssert.assertTrue(response.timeIn(TimeUnit.MILLISECONDS) < 500, "Response time is more than 500 ms!");
        softAssert.assertEquals(response.getStatusCode(), 200, "Invalid response code");
        softAssert.assertAll();
    }

    @Test(testName = "Find pets by invalid parameter", enabled = true, groups = {SMOKE}, description = "Trying to get pets by invalid parameter - Negative test -")
    public void findPetsByInvalidParameter(){
        SoftAssert softAssert = new SoftAssert();
        Map<String, String> queryParams = new TreeMap<>();
        queryParams.put("invalidParameter", "sold");

        Response response = getUrl(findByStatus, queryParams);
        softAssert.assertTrue(response.timeIn(TimeUnit.MILLISECONDS) < 500, "Response time is more than 500 ms!");
        softAssert.assertEquals(response.getStatusCode(), 400, "Invalid response code");
        softAssert.assertAll();
    }

}
