package io.github.ayamehinode.quarkusSocial.rest;

import io.github.ayamehinode.quarkusSocial.domain.model.Follower;
import io.github.ayamehinode.quarkusSocial.domain.model.Post;
import io.github.ayamehinode.quarkusSocial.domain.model.User;
import io.github.ayamehinode.quarkusSocial.domain.repository.FollowerRepository;
import io.github.ayamehinode.quarkusSocial.domain.repository.PostRepository;
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
    @Inject
    FollowerRepository followerRepository;
    @Inject
    PostRepository postRepository;

    Long userId;
    Long notFollowerId;
    Long followerId;

    @BeforeEach
    @Transactional
    public void setup(){
        //Default user
        var user = new User();
        user.setName("X");
        user.setAge(16);
        userRepository.persist(user);
        userId = user.getId();

        Post post = new Post();
        post.setText("My name is X");
        post.setUser(user);
        postRepository.persist(post);

        //NotFollower user
        var notFollower = new User();
        notFollower.setAge(19);
        notFollower.setName("Zero");
        userRepository.persist(notFollower);
        notFollowerId = notFollower.getId();

        //Follower user
        var follower = new User();
        follower.setAge(14);
        follower.setName("Axl");
        userRepository.persist(follower);
        followerId = follower.getId();

        Follower XFollower = new Follower();
        XFollower.setUser(user);
        XFollower.setFollower(follower);
        followerRepository.persist(XFollower);

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

        given()
                .pathParam("userId", userId)
                .header("followerId", notFollowerId)
                .when()
                .get()
                .then()
                .statusCode(403)
                .body(Matchers.is("Is not posible to see posts for an user you not follow"));

    }

    @Test
    @DisplayName("Should list all posts")
    public void listPostTest(){

      given()
                .pathParam("userId", userId)
                .header("followerId", followerId)
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("size()", Matchers.is(1));

    }

}