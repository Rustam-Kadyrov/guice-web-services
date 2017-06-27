package com.rustam.project.service;

import com.google.inject.Inject;
import com.rustam.project.model.entity.Transfer;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by Rustam_Kadyrov on 25.06.2017.
 */
public class TransferService {
    private final EntityManager em;

    @Inject
    public TransferService(EntityManager em) {
        this.em = em;
    }

    public List<Transfer> findAll() {
        return em.createQuery("select t from Transfer t").getResultList();
    }
}
