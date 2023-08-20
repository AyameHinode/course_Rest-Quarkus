package io.github.ayamehinode.quarkusSocial.rest;

import io.github.ayamehinode.quarkusSocial.domain.model.User;
import io.github.ayamehinode.quarkusSocial.domain.repository.UserRepository;
import io.github.ayamehinode.quarkusSocial.rest.dto.FollowerRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(FollowerResource.class)
class FollowerResourceTest {

    @Inject
    UserRepository userRepository;

    Long userXId;

    @BeforeEach
    @Transactional
    void setUp() {
        //Default user
        var userX = new User();
        userX.setName("X");
        userX.setAge(16);
        userRepository.persist(userX);
        userXId = userX.getId();
    }

    @Test
    @DisplayName("Should returns Conflict 409 status when user is trying to follow himself")
    public void sameUserAsFollowerTest(){

        var body = new FollowerRequest();
        body.setFollowerId(userXId);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId", userXId)
                .when()
                .put()
                .then()
                .statusCode(Response.Status.CONFLICT.getStatusCode())
                .body(Matchers.is("You can't follow yourself! Try to follow another user."));

    }

    @Test
    @DisplayName("Should returns NotFound 404 status when user doesn't exist")
    public void inexistentUserTest(){

        var inexistentUserId = 1000;

        var body = new FollowerRequest();
        body.setFollowerId(userXId);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId", inexistentUserId)
                .when()
                .put()
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());

    }

}