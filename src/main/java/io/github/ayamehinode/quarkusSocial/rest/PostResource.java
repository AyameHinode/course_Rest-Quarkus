package io.github.ayamehinode.quarkusSocial.rest;

import io.github.ayamehinode.quarkusSocial.domain.model.Post;
import io.github.ayamehinode.quarkusSocial.domain.model.User;
import io.github.ayamehinode.quarkusSocial.domain.repository.PostRepository;
import io.github.ayamehinode.quarkusSocial.domain.repository.UserRepository;
import io.github.ayamehinode.quarkusSocial.rest.dto.CreatePostRequest;
import io.github.ayamehinode.quarkusSocial.rest.dto.PostResponse;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    private UserRepository userRepository;
    private PostRepository postRepository;

    @Inject
    public PostResource(
            UserRepository userRepository,
            PostRepository postRepository){
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @POST
    @Transactional
    public Response savePost(@PathParam("userId") Long userId, CreatePostRequest postRequest){
        User user = userRepository.findById(userId);
        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Post post = new Post();
        post.setText(postRequest.getText());
        post.setUser(user);
        postRepository.persist(post);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response listPosts(@PathParam("userId") Long userId){
        User user = userRepository.findById(userId);
        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        PanacheQuery<Post> query = postRepository.find("user", Sort.by("dateTime", Sort.Direction.Descending), user);
        List<Post> list = query.list();
        var postResponseList = list.stream()
                .map(post -> PostResponse.fromEntity(post))
                .collect(Collectors.toList());
        return Response.ok(postResponseList).build();
    }

}
