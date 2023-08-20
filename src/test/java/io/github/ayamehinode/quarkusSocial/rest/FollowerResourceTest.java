package io.github.ayamehinode.quarkusSocial.rest;

import io.github.ayamehinode.quarkusSocial.domain.model.Follower;
import io.github.ayamehinode.quarkusSocial.domain.model.User;
import io.github.ayamehinode.quarkusSocial.domain.repository.FollowerRepository;
import io.github.ayamehinode.quarkusSocial.domain.repository.UserRepository;
import io.github.ayamehinode.quarkusSocial.rest.dto.FollowerRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(FollowerResource.class)
class FollowerResourceTest {

    @Inject
    UserRepository userRepository;
    @Inject
    FollowerRepository followerRepository;

    Long userXId;
    Long followerAxlId;

    @BeforeEach
    @Transactional
    void setUp() {

        //Default user
        var userX = new User();
        userX.setName("X");
        userX.setAge(16);
        userRepository.persist(userX);
        userXId = userX.getId();

        //Default follower
        var followerAxl = new User();
        followerAxl.setName("Axl");
        followerAxl.setAge(14);
        userRepository.persist(followerAxl);
        followerAxlId = followerAxl.getId();

        //Follower defined
        var XsFollower = new Follower();
        XsFollower.setFollower(followerAxl);
        XsFollower.setUser(userX);
        followerRepository.persist(XsFollower);

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
    @DisplayName("Should returns NotFound 404 status when inexistent user is trying to follow")
    public void inexistentUserTryingToFollowTest(){

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

    @Test
    @DisplayName("Should returns NotFound 404 status on list followers with an inexistent user")
    public void inexistentUserTryingToListTest(){

        var inexistentUserId = 1000;

        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", inexistentUserId)
                .when()
                .get()
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());

    }

    @Test
    @DisplayName("Should follow an user successfully")
    public void followUserTest(){

        var body = new FollowerRequest();
        body.setFollowerId(followerAxlId);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId", userXId)
                .when()
                .put()
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

    }

    @Test
    @DisplayName("Should list followers successfully")
    public void listFollowersTest(){

        var response =
        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", userXId)
                .when()
                .get()
                .then()
                .extract().response();

        var followersCount = response.jsonPath().get("followersCount");
        var followersContent = response.jsonPath().getList("content");

        assertEquals(Response.Status.OK.getStatusCode(), response.statusCode());
        assertEquals(1, followersCount);
        assertEquals(1, followersContent.size());

    }

}