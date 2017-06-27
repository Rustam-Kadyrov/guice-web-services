package com.rustam.project.endpoint;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import com.rustam.project.model.entity.User;
import com.rustam.project.model.request.CreateUserRequest;
import com.rustam.project.model.response.MessageResponse;
import com.rustam.project.service.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Manage user
 * Created by Rustam Kadyrov on 25.06.2017.
 */
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Path("/user")
public class UserEndpoint {

    private final UserService userService;

    @Inject
    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }

    /**
     * Create new user
     *
     * @param createUserRequest
     * @return
     */
    @POST
    public MessageResponse<User> createUser(CreateUserRequest createUserRequest) {
        return new MessageResponse<>(userService.createUser(createUserRequest));
    }

    /**
     * Get user by id
     *
     * @param id
     * @return
     */
    @GET
    public MessageResponse<User> getUser(@QueryParam("id") Long id) {
        return new MessageResponse<>(userService.findOne(id));
    }

    /**
     * Update users info
     *
     * @param user
     * @return
     */
    @PUT
    public MessageResponse<User> updateUser(User user) {
        checkNotNull(user.getId());
        return new MessageResponse<>(userService.updateUser(user));
    }

    /**
     * Remove users account
     *
     * @param id
     * @return
     */
    @DELETE
    public MessageResponse<Long> deleteUser(@QueryParam("id") Long id) {
        checkNotNull(id);
        return new MessageResponse<>(userService.remove(id));
    }

    /**
     * Get list of all users
     *
     * @return
     */
    @GET
    @Path("/list")
    public MessageResponse<List<User>> getAllUsers() {
        return new MessageResponse<>(userService.findAll());
    }
}
