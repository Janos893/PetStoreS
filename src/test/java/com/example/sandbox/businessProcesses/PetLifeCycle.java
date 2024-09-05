package com.example.sandbox.businessProcesses;

import com.example.sandbox.Common;
import com.example.sandbox.util.body.pet.PostCreatePet;
import com.example.sandbox.util.swagger.definitions.Item;
import com.example.sandbox.util.swagger.definitions.PetBody;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.openqa.selenium.json.Json;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.report.TestListener;

import java.util.Optional;

import static com.example.sandbox.util.Tools.generateRandomNumber;
import static com.example.sandbox.util.body.pet.JsonBody.createJsonBody;
import static com.example.sandbox.util.constans.Tags.SMOKE;
import static com.example.sandbox.util.constans.TestData.*;

@Listeners(TestListener.class)
public class PetLifeCycle extends Common {
    @Test(enabled = true,groups = {SMOKE},description ="description")
    public void Test1(){

        PostCreatePet body = PostCreatePet.builder()
                .PetBody(PetBody.builder()
                        .id(generateRandomNumber())
                        .category(Item.builder()
                                .id(1)
                                .name("Hydra")
                                .build())
                        .name("Princess")
                        .photoUrl(HYDRAIMAGE)
                        .tag(Item.builder()
                                .id(2)
                                .name("cute")
                                .build())
                        .status("available")
                        .build()
                ).build();


        Response response = postUrl(newPet,createJsonBody(body));
        Assert.assertEquals(response.getStatusCode(),200,"Invalid response code");

        String id = response.jsonPath().get("id").toString();

        Response  response2 = getUrl(petById.replace("{petId}",id));
        Assert.assertEquals(response2.getStatusCode(),200,"Invalid response code");

    }

    @Test(testName = "Post new pet", enabled = true, groups = {SMOKE}, description ="Business process - Post new pet")
    public void createNewPet(ITestContext iTestContext){
        PostCreatePet body = PostCreatePet.builder()
                .PetBody(PetBody.builder()
                        .id(generateRandomNumber())
                        .category(Item.builder()
                                .id(ORIGINAL_CATEGORY_ID)
                                .name(ORIGINAL_CATEGORY_NAME)
                                .build())
                        .name(ORIGINAL_NAME)
                        .photoUrl(ORIGINAL_IMAGE)
                        .tag(Item.builder()
                                .id(ORIGINAL_TAG_ID)
                                .name(ORIGINAL_TAG_NAME)
                                .build())
                        .status(ORIGINAL_STATUS)
                        .build()
                ).build();

        Response response = postUrl(newPet,createJsonBody(body));
        JsonPath jsonPath = new JsonPath(response.body().asString());
        Assert.assertEquals(jsonPath.get("name"), ORIGINAL_NAME);
        iTestContext.setAttribute("id", jsonPath.get("id"));
    }

    @Test(testName = "Update pet", enabled = true, groups = {SMOKE}, description ="Business process - Update new pet", dependsOnMethods = "createNewPet", priority = 1)
    public void updatePet(ITestContext iTestContext){
        SoftAssert softAssert = new SoftAssert();
        PostCreatePet body = PostCreatePet.builder()
                .PetBody(PetBody.builder()
                        .id((Integer) iTestContext.getAttribute("id"))
                        .category(Item.builder()
                                .id(UPDATED_CATEGORY_ID)
                                .name(UPDATED_CATEGORY_NAME)
                                .build())
                        .name(UPDATED_NAME)
                        .photoUrl(UPDATED_IMAGE)
                        .tag(Item.builder()
                                .id(UPDATED_TAG_ID)
                                .name(UPDATED_TAG_NAME)
                                .build())
                        .status(UPDATED_STATUS)
                        .build()
                ).build();
        Response response = putUrl(newPet, createJsonBody(body));
        Assert.assertEquals(response.getStatusCode(), 200, "Invalid response code!");

        Response response2 = getUrl(petById.replace("{petId}", iTestContext.getAttribute("id").toString()));
        JsonPath jsonPath = new JsonPath(response2.body().asString());
        softAssert.assertEquals(jsonPath.get("id"), iTestContext.getAttribute("id"), "Incorrect id!");
        softAssert.assertEquals(Integer.parseInt(jsonPath.get("category.id").toString()),UPDATED_CATEGORY_ID, "Incorrect category.id!");
        softAssert.assertEquals(jsonPath.get("category.name"), UPDATED_CATEGORY_NAME, "Incorrect category.name!");
        softAssert.assertEquals(jsonPath.get("name"), UPDATED_NAME, "Incorrect name!");
        softAssert.assertEquals(jsonPath.get("photoUrls[0]"), UPDATED_IMAGE, "Incorrect photoUrls!");
        softAssert.assertEquals(Integer.parseInt(jsonPath.get("tags[0].id").toString()), UPDATED_TAG_ID, "Incorrect tags[0].id!");
        softAssert.assertEquals(jsonPath.get("tags[0].name"), UPDATED_TAG_NAME, "Incorrect tags[0].name!");
        softAssert.assertEquals(jsonPath.get("status"), UPDATED_STATUS, "Incorrect status!");
        softAssert.assertAll();

    }

    @Test(testName = "Delete pet", enabled = true, groups = {SMOKE}, description ="Business process - Delete new pet", dependsOnMethods = "createNewPet", priority = 2)
    public void deletePet(ITestContext iTestContext){
        SoftAssert softAssert = new SoftAssert();
        Response response = deleteUrl(petById.replace("{petId}", iTestContext.getAttribute("id").toString()));
        Assert.assertEquals(response.getStatusCode(), 200, "Invalid response code!");

        Response response2 = getUrl(petById.replace("{petId}", iTestContext.getAttribute("id").toString()));
        JsonPath jsonPath = new JsonPath(response2.body().asString());
        softAssert.assertEquals(Integer.parseInt(jsonPath.get("code").toString()), 1, "Unknown code!");
        softAssert.assertEquals(jsonPath.get("type"), "error", "Unknown type");
        softAssert.assertEquals(jsonPath.get("message"), "Pet not found", "Incorrect message");
        softAssert.assertAll();
    }

}
