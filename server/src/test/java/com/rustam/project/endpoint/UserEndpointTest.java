package com.rustam.project.endpoint;

import com.rustam.project.WebTestsBase;
import com.rustam.project.model.entity.User;
import com.rustam.project.model.request.CreateUserRequest;
import com.rustam.project.model.response.MessageResponse;
import com.sun.jersey.api.client.GenericType;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by Rustam Kadyrov on 07.07.2017.
 */
public class UserEndpointTest extends WebTestsBase {

    @Test
    public void testCreateUser() throws Exception {
        User user = invokePost("/user", CreateUserRequest.builder()
                .name("Fred").build(), new GenericType<MessageResponse<User>>() {
        }).getMessage();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), is("Fred"));
    }

    @Test
    public void testGetUser() throws Exception {
        User user = invokePost("/user", CreateUserRequest.builder()
                .name("Fred").build(), new GenericType<MessageResponse<User>>() {
        }).getMessage();

        MessageResponse<User> response = invokeGet("/user?id=" + user.getId(), new GenericType<MessageResponse<User>>() {
        });

        User userFromRest = response.getMessage();

        assertThat(user.getId(), is(userFromRest.getId()));
        assertThat(user.getName(), is(userFromRest.getName()));
        assertThat(user.getAccounts(), equalTo(userFromRest.getAccounts()));
    }

    @Test
    public void testUpdateUser() throws Exception {
        User user = invokePost("/user", CreateUserRequest.builder()
                .name("Fred").build(), new GenericType<MessageResponse<User>>() {
        }).getMessage();

        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setName("New name");

        User update = invokePut("/user", updateUser, new GenericType<MessageResponse<User>>() {
        }).getMessage();

        assertThat(user.getId(), is(update.getId()));
        assertThat(update.getName(), is("New name"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        User user = invokePost("/user", CreateUserRequest.builder()
                .name("Fred").build(), new GenericType<MessageResponse<User>>() {
        }).getMessage();

        invokeDelete("/user", user.getId(), new GenericType<MessageResponse<Long>>() {
        });

        MessageResponse<User> response = invokeGet("/user?id=" + user.getId(), new GenericType<MessageResponse<User>>() {
        });

        assertThat(response.getMessage(), nullValue());
    }

    @Test
    public void testGetAllUsers() throws Exception {
        invokePost("/user", CreateUserRequest.builder()
                .name("Fred").build(), new GenericType<MessageResponse<User>>() {
        });

        invokePost("/user", CreateUserRequest.builder()
                .name("John").build(), new GenericType<MessageResponse<User>>() {
        });

        List<User> users = invokeGet("/user/list", new GenericType<MessageResponse<List<User>>>() {
        }).getMessage();
        assertThat(users, hasSize(2));
        assertThat(users.stream().map(User::getName).collect(Collectors.toList()), containsInAnyOrder("Fred", "John"));
    }

}