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

}