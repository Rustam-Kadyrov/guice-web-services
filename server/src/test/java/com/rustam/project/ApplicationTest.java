package com.rustam.project;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.rustam.project.config.jackson.ObjectMapperProvider;
import com.rustam.project.model.entity.Account;
import com.rustam.project.model.entity.Currency;
import com.rustam.project.model.entity.Transfer;
import com.rustam.project.model.entity.User;
import com.rustam.project.model.request.AddAccountRequest;
import com.rustam.project.model.request.CreateUserRequest;
import com.rustam.project.model.request.RechargeAccountRequest;
import com.rustam.project.model.request.TransferMoneyRequest;
import com.rustam.project.model.response.MessageResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created by Rustam Kadyrov on 25.06.2017.
 */
public class ApplicationTest {

    private Logger logger = Logger.getLogger(getClass().getCanonicalName());

    private Application application;
    private Client client;

    @BeforeClass
    public void beforeClass() throws Exception {
        application = new Application();
        application.start();

        ClientConfig clientConfig = new DefaultClientConfig();
        JacksonJaxbJsonProvider jacksonProvider = new JacksonJaxbJsonProvider();
        jacksonProvider.setMapper(new ObjectMapperProvider().get());
        clientConfig.getSingletons().add(jacksonProvider);
        client = Client.create(clientConfig);
    }

    @AfterClass
    public void afterClass() throws Exception {
        application.stop();
    }


    @Test
    public void testApplicationRun() {

        User user = createUser();
        assertEquals(user.getId(), Long.valueOf(1L));

        Account account1 = addAccount(user);
        assertNotNull(account1);
        assertEquals(account1.getId(), Long.valueOf(2L));
        assertEquals(account1.getBalance(), BigDecimal.ZERO);
        assertEquals(account1.getCurrency(), Currency.EUR);

        Account account2 = addAccount(user);
        assertNotNull(account2);
        assertEquals(account2.getId(), Long.valueOf(3L));
        assertEquals(account2.getBalance(), BigDecimal.ZERO);
        assertEquals(account2.getCurrency(), Currency.EUR);

        checkUsersAccounts(user);

        Account account1Recharged = rechargeAccount(account1);
        assertEquals(account1Recharged.getBalance().doubleValue(), 10000.0);

        transferMoney(account1Recharged, account2, BigDecimal.valueOf(6000));

        Account account1FromDb = getAccount(account1Recharged.getId());
        assertEquals(account1FromDb.getBalance().doubleValue(), 4000.0);

        Account account2FromDb = getAccount(account2.getId());
        assertEquals(account2FromDb.getBalance().doubleValue(), 6000.0);

        List<Transfer> transfers = getTransfers();
        assertEquals(transfers.size(), 1);
    }

    private User createUser() {
        logger.info("Creating user");
        WebResource createUserResource = client
                .resource("http://localhost:8081/my-app/user");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setName("Fred");
        MessageResponse<User> response = invokePost(createUserResource, createUserRequest, new GenericType<MessageResponse<User>>() {
        });
        logger.info("result: " + response);
        User user = response.getMessage();
        return user;
    }

    private Account getAccount(Long id) {
        WebResource getAccountResource = client
                .resource("http://localhost:8081/my-app/account?id=" + id);

        MessageResponse<Account> accountResponse = getAccountResource.type(MediaType.APPLICATION_JSON)
                .get(new GenericType<MessageResponse<Account>>() {
                });
        return accountResponse.getMessage();
    }

    private Account addAccount(User user) {
        logger.info("Adding account");
        WebResource addAccountResource = client
                .resource("http://localhost:8081/my-app/account");
        AddAccountRequest addAccountRequest = new AddAccountRequest();
        addAccountRequest.setUserId(user.getId());
        addAccountRequest.setAccountCurrency(Currency.EUR);
        MessageResponse<Account> accountResponse = invokePost(addAccountResource, addAccountRequest, new GenericType<MessageResponse<Account>>() {
        });
        logger.info("result: " + accountResponse);
        return accountResponse.getMessage();
    }

    private void checkUsersAccounts(User user) {
        WebResource getUserResource = client
                .resource("http://localhost:8081/my-app/user?id=" + user.getId());

        MessageResponse<User> userResponse = getUserResource.type(MediaType.APPLICATION_JSON)
                .get(new GenericType<MessageResponse<User>>() {
                });
        assertEquals(userResponse.getMessage().getAccounts().size(), 2);
    }

    private Account rechargeAccount(Account account1) {
        logger.info("Recharge account " + account1);
        WebResource rechargeAccountResource = client
                .resource("http://localhost:8081/my-app/account/recharge");
        RechargeAccountRequest rechargeAccountRequest = new RechargeAccountRequest();
        rechargeAccountRequest.setAccountId(account1.getId());
        rechargeAccountRequest.setCurrency(account1.getCurrency());
        rechargeAccountRequest.setAmount(BigDecimal.valueOf(10000));
        MessageResponse<Account> rechargeResponse = invokePost(rechargeAccountResource, rechargeAccountRequest, new GenericType<MessageResponse<Account>>() {
        });
        logger.info("result: " + rechargeResponse.getMessage());
        return rechargeResponse.getMessage();
    }

    private void transferMoney(Account account1, Account account2, BigDecimal transferAmount) {
        logger.info("Transfer all moneys from one account to another");
        WebResource transfer = client
                .resource("http://localhost:8081/my-app/account/transfer");

        TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest();
        transferMoneyRequest.setAccountFrom(account1.getId());
        transferMoneyRequest.setAccountTo(account2.getId());
        transferMoneyRequest.setCurrency(account1.getCurrency());
        transferMoneyRequest.setAmount(transferAmount);
        MessageResponse<List<Account>> messageAccountsResponse = invokePost(transfer, transferMoneyRequest, new GenericType<MessageResponse<List<Account>>>() {
        });
        assertEquals(messageAccountsResponse.getMessage().size(), 2);
        logger.info("result: " + messageAccountsResponse.getMessage());
    }

    private <T> T invokePost(WebResource resource, Object request, GenericType<T> type) {
        return resource.type(MediaType.APPLICATION_JSON)
                .post(type, request);
    }

    private List<Transfer> getTransfers() {
        logger.info("Getting transfers");
        WebResource transfersResource = client
                .resource("http://localhost:8081/my-app/transfer");

        MessageResponse<List<Transfer>> transfersResponse = transfersResource.type(MediaType.APPLICATION_JSON)
                .get(new GenericType<MessageResponse<List<Transfer>>>() {
                });
        logger.info("result: " + transfersResponse.getMessage());
        return transfersResponse.getMessage();
    }
}
