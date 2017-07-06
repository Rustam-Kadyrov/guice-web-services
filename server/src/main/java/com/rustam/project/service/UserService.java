package com.rustam.project.service;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.rustam.project.model.entity.User;
import com.rustam.project.model.exception.NotFoundException;
import com.rustam.project.model.exception.ValidationException;
import com.rustam.project.model.request.CreateUserRequest;

import javax.persistence.EntityManager;
import java.time.ZonedDateTime;
import java.util.List;

import static java.util.Objects.isNull;


/**
 * Created by Rustam_Kadyrov on 25.06.2017.
 */
public class UserService {

    private final EntityManager em;

    @Inject
    public UserService(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public User createUser(CreateUserRequest createUserRequest) {
        User user = new User();
        user.setName(createUserRequest.getName());
        user.setLastModification(ZonedDateTime.now());
        em.persist(user);
        return user;
    }

    public User findOne(Long id) {
        return em.find(User.class, id);
    }

    @Transactional
    public User updateUser(User user) {
        if (isNull(user.getId())) {
            throw new ValidationException("id is null");
        }
        if (findOne(user.getId()) == null) {
            throw new NotFoundException("User not found");
        }
        user.setLastModification(ZonedDateTime.now());
        return em.merge(user);

    }

    @Transactional
    public Long remove(Long id) {
        User user = findOne(id);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        em.remove(user);
        return id;
    }

    public List<User> findAll() {
        return em.createQuery("select u from User u").getResultList();
    }
}
