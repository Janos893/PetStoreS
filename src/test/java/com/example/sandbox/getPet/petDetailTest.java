package com.example.sandbox.getPet;

import com.example.sandbox.Common;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.report.TestListener;

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

        System.out.println(response2.body().asString());
        SoftAssert softAssert = new SoftAssert();
        JsonPath jsonPath = new JsonPath(response2.body().asString());

        softAssert.assertNotNull(jsonPath.get("id"), "id is missing");
        softAssert.assertTrue(jsonPath.get("id") instanceof Long, "id format is not correct");

        softAssert.assertNotNull(jsonPath.get("category.id"), "category.id is missing");
        softAssert.assertTrue(jsonPath.get("category.id") instanceof Integer, "category.id format is not correct");

        softAssert.assertNotNull(jsonPath.get("category.name"), "category.name is missing");
        softAssert.assertTrue(jsonPath.get("category.name") instanceof String, "category.name format is not correct");

        softAssert.assertNotNull(jsonPath.get("name"), "name is missing");
        softAssert.assertTrue(jsonPath.get("name") instanceof String, "name format is not correct");

        softAssert.assertNotNull(jsonPath.get("photoUrls[0]"), "photoUrls is missing");
        softAssert.assertTrue(jsonPath.get("photoUrls[0]") instanceof String, "photoUrls format is not correct");

        softAssert.assertNotNull(jsonPath.get("tags[0].id"), "tags.id is missing");
        softAssert.assertTrue(jsonPath.get("tags[0].id") instanceof Integer, "tags.id format is not correct");

        softAssert.assertNotNull(jsonPath.get("tags[0].name"), "tags.name is missing");
        softAssert.assertTrue(jsonPath.get("tags[0].name") instanceof String, "tags.name format is not correct");

        softAssert.assertNotNull(jsonPath.get("status"), "status is missing");
        softAssert.assertTrue(jsonPath.get("status") instanceof String, "status is not correct");
        System.out.println(jsonPath.get("status").toString());
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
