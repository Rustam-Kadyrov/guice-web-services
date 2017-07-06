package com.rustam.project;

import com.rustam.project.service.AccountService;
import com.rustam.project.service.TransferService;
import com.rustam.project.service.UserService;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

/**
 * Created by Rustam Kadyrov on 06.07.2017.
 */
public class TestsBase {
    private Application application;

    @AfterMethod
    public void afterMethod() {
        truncate("Transfer");
        truncate("Account");
        truncate("User");
    }

    @BeforeClass
    public void beforeClass() throws Exception {
        application = new Application();
        application.start();
    }

    @AfterClass
    public void afterClass() throws Exception {
        application.stop();
    }

    private <T> T getInstance(Class<T> type) {
        return application.getInstance(type);
    }

    private void truncate(String entityName) {
        EntityManager entityManager = getInstance(EntityManager.class);
        entityManager.getTransaction().begin();
        entityManager.createQuery("delete from " + entityName + " e").executeUpdate();
        entityManager.getTransaction().commit();
    }

    protected UserService getUserService() {
        return getInstance(UserService.class);
    }

    protected AccountService getAccountService() {
        return getInstance(AccountService.class);
    }

    protected TransferService getTransferService() {
        return getInstance(TransferService.class);
    }

    protected BigDecimal getScaledBigDecimal(long val) {
        return new BigDecimal(val).setScale(3, BigDecimal.ROUND_HALF_DOWN);
    }
}
