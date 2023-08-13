package io.github.ayamehinode.quarkusSocial.rest;

import io.github.ayamehinode.quarkusSocial.domain.repository.FollowerRepository;
import io.github.ayamehinode.quarkusSocial.domain.repository.UserRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResource {

    private FollowerRepository followerRepository;
    private UserRepository userRepository;

    @Inject
    public FollowerResource(FollowerRepository followerRepository, UserRepository userRepository){
        this.userRepository = userRepository;
        this.followerRepository = followerRepository;
    }

}
