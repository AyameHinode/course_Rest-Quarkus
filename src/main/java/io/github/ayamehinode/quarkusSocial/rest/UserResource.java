package io.github.ayamehinode.quarkusSocial.rest;


import io.github.ayamehinode.quarkusSocial.rest.dto.CreateUserRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @POST
    public Response createUser(CreateUserRequest userRequest){

        return Response.ok(userRequest).build();

    }

}
