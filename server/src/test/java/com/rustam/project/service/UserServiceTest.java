package com.rustam.project.service;

import com.rustam.project.TestsBase;
import com.rustam.project.model.entity.User;
import com.rustam.project.model.exception.NotFoundException;
import com.rustam.project.model.exception.ValidationException;
import com.rustam.project.model.request.CreateUserRequest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by Rustam Kadyrov on 06.07.2017.
 */
public class UserServiceTest extends TestsBase {

    @Test
    public void testCreateUser() throws Exception {
        User user = getUserService().createUser(CreateUserRequest.builder().name("Test")
                .build());

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), is("Test"));
        assertThat(user.getAccounts(), empty());
    }

    @Test
    public void testFindOne() throws Exception {
        User first = getUserService().createUser(CreateUserRequest.builder().name("Fred")
                .build());

        User second = getUserService().createUser(CreateUserRequest.builder().name("John")
                .build());

        User find = getUserService().findOne(first.getId());

        assertThat(first.getId(), not(second.getId()));
        assertThat(find.getId(), is(first.getId()));
        assertThat(find.getName(), is(first.getName()));
    }

    @Test
    public void testUpdateUser() throws Exception {
        User user = getUserService().createUser(CreateUserRequest.builder().name("Fred")
                .build());
        user.setName("Not Fred");
        User fromDb = getUserService().updateUser(user);

        assertThat(fromDb.getName(), is("Not Fred"));
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testUpdateUserIdNull() throws Exception {
        User user = new User();
        user.setName("Fred");
        User fromDb = getUserService().updateUser(user);

        assertThat(fromDb.getName(), is("Not Fred"));
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void testUpdateUserNotFound() throws Exception {
        User user = new User();
        user.setId(100L);
        user.setName("Fred");
        User fromDb = getUserService().updateUser(user);

        assertThat(fromDb.getName(), is("Not Fred"));
    }

    @Test
    public void testRemove() throws Exception {
        Long userId = getUserService().createUser(CreateUserRequest.builder().name("Fred")
                .build()).getId();

        Long id = getUserService().remove(userId);
        assertThat(id, is(userId));

        User fromDb = getUserService().findOne(id);
        assertThat(fromDb, nullValue());
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void testRemoveNotFound() throws Exception {
        getUserService().remove(100L);
    }

    @Test
    public void testFindAll() throws Exception {
        getUserService().createUser(CreateUserRequest.builder().name("Fred")
                .build());

        getUserService().createUser(CreateUserRequest.builder().name("John")
                .build());

        List<User> users = getUserService().findAll();

        assertThat(users, hasSize(2));
        assertThat(users.stream().map(User::getName).collect(Collectors.toList()),
                containsInAnyOrder("Fred", "John"));
    }
}