package io.github.ayamehinode.quarkusSocial.rest;

import io.github.ayamehinode.quarkusSocial.domain.model.User;
import io.github.ayamehinode.quarkusSocial.domain.repository.UserRepository;
import io.github.ayamehinode.quarkusSocial.rest.dto.CreatePostRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.json.bind.JsonbBuilder;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
class PostResourceTest {

    @Inject
    UserRepository userRepository;
    Long userId;

    @BeforeEach
    @Transactional
    public void setup(){
        var user = new User();
        user.setAge(15);
        user.setName("X");
        userRepository.persist(user);
        userId = user.getId();
    }

    @Test
    @DisplayName("Should Create a post for an user")
    public void creatPostTest(){
        var newPost = new CreatePostRequest();
        newPost.setText("Believe in your capacity");

        var userID = 1;

        given()
                .contentType(ContentType.JSON)
                .body(JsonbBuilder.create().toJson(newPost))
                .pathParam("userId", userID)
                .when()
                .post()
                .then()
                .statusCode(201);

    }

    @Test
    @DisplayName("Returns 404 when try to make a post for an inexistent user")
    public void postForInexistentUserTest(){
        var newPost = new CreatePostRequest();
        newPost.setText("Believe in your capacity");

        var inexistentID = 999;

        given()
                .contentType(ContentType.JSON)
                .body(JsonbBuilder.create().toJson(newPost))
                .pathParam("userId", inexistentID)
                .when()
                .post()
                .then()
                .statusCode(404);

    }

    @Test
    @DisplayName("Should return 404 status when an user doesn't exist")
    public void listPostUserNotFoundTest(){
        var inexistentUserId = 900;

        given()
                .pathParam("userId", inexistentUserId)
                .when()
                .get()
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Should return 400 when followerId header isn't present")
    public void listPostFollowerHeaderNotSendTest(){

        given()
                .pathParam("userId", userId)
                .when()
                .get()
                .then()
                .statusCode(400)
                .body(Matchers.is("You need to inform the followerId"));

    }

    @Test
    @DisplayName("Should return 403 when followerId doesn't exist")
    public void listPostFollowerNotFoundTest(){

        var inexistentFollowerId = 1000;

        given()
                .pathParam("userId", userId)
                .header("followerId", inexistentFollowerId)
                .when()
                .get()
                .then()
                .statusCode(400)
                .body(Matchers.is("Follower informed does not exist!"));

    }

    @Test
    @DisplayName("Should return 403 when followerId isn't a follower")
    public void listPostNotFollowerTest(){

    }

    @Test
    @DisplayName("Should list all posts")
    public void listPostTest(){

    }

}