package com.rustam.project;

import com.rustam.project.model.entity.Account;
import com.rustam.project.model.entity.Currency;
import com.rustam.project.model.entity.Transfer;
import com.rustam.project.model.entity.User;
import com.rustam.project.model.request.AddAccountRequest;
import com.rustam.project.model.request.CreateUserRequest;
import com.rustam.project.model.request.RechargeAccountRequest;
import com.rustam.project.model.request.TransferMoneyRequest;
import com.rustam.project.model.response.MessageResponse;
import com.sun.jersey.api.client.GenericType;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

import static com.rustam.project.service.AccountService.BIG_ZERO;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created by Rustam Kadyrov on 25.06.2017.
 */
public class ApplicationTest extends WebTestsBase {

    private Logger logger = Logger.getLogger(getClass().getCanonicalName());

    @Test
    public void testApplicationRun() {

        User user = createUser();
        assertEquals(user.getId(), Long.valueOf(1L));

        Account account1 = addAccount(user);
        assertNotNull(account1);
        assertEquals(account1.getId(), Long.valueOf(2L));
        assertEquals(account1.getBalance(), BIG_ZERO);
        assertEquals(account1.getCurrency(), Currency.EUR);

        Account account2 = addAccount(user);
        assertNotNull(account2);
        assertEquals(account2.getId(), Long.valueOf(3L));
        assertEquals(account2.getBalance(), BIG_ZERO);
        assertEquals(account2.getCurrency(), Currency.EUR);

        checkUsersAccounts(user);

        Account account1Recharged = rechargeAccount(account1);
        assertEquals(account1Recharged.getBalance().doubleValue(), 10000.0);

        transferMoney(account1Recharged, account2, getScaledBigDecimal(6000));

        Account account1FromDb = getAccount(account1Recharged.getId());
        assertEquals(account1FromDb.getBalance().doubleValue(), 4000.0);

        Account account2FromDb = getAccount(account2.getId());
        assertEquals(account2FromDb.getBalance().doubleValue(), 6000.0);

        List<Transfer> transfers = getTransfers();
        assertEquals(transfers.size(), 1);

        user = changeName(user);
        deleteUser(user.getId());
    }

    private void deleteUser(Long id) {
        logger.info("Delete user");
        MessageResponse<Long> response = invokeDelete("/user", id, new GenericType<MessageResponse<Long>>() {
        });
        logger.info("result: " + response);
    }

    private User changeName(User user) {
        logger.info("Change name");
        user.setName("New name");
        MessageResponse<User> response = invokePut("/user", user, new GenericType<MessageResponse<User>>() {
        });
        logger.info("result: " + response);
        return response.getMessage();
    }

    private User createUser() {
        logger.info("Creating user");
        MessageResponse<User> response = invokePost("/user", CreateUserRequest.builder()
                .name("Fred").build(), new GenericType<MessageResponse<User>>() {
        });
        logger.info("result: " + response);
        User user = response.getMessage();
        return user;
    }

    private Account getAccount(Long id) {
        MessageResponse<Account> accountResponse = invokeGet("/account?id=" + id, new GenericType<MessageResponse<Account>>() {
        });
        return accountResponse.getMessage();
    }

    private Account addAccount(User user) {
        logger.info("Adding account");
        MessageResponse<Account> accountResponse = invokePost("/account", AddAccountRequest.builder()
                .userId(user.getId())
                .accountCurrency(Currency.EUR)
                .build(), new GenericType<MessageResponse<Account>>() {
        });
        logger.info("result: " + accountResponse);
        return accountResponse.getMessage();
    }

    private void checkUsersAccounts(User user) {
        MessageResponse<User> userResponse = invokeGet("/user?id=" + user.getId(), new GenericType<MessageResponse<User>>() {
        });
        assertEquals(userResponse.getMessage().getAccounts().size(), 2);
    }

    private Account rechargeAccount(Account account1) {
        logger.info("Recharge account " + account1);
        RechargeAccountRequest rechargeAccountRequest = new RechargeAccountRequest();
        rechargeAccountRequest.setAccountId(account1.getId());
        rechargeAccountRequest.setCurrency(account1.getCurrency());
        rechargeAccountRequest.setAmount(getScaledBigDecimal(10000));
        MessageResponse<Account> rechargeResponse = invokePost("/account/recharge", rechargeAccountRequest, new GenericType<MessageResponse<Account>>() {
        });
        logger.info("result: " + rechargeResponse.getMessage());
        return rechargeResponse.getMessage();
    }

    private void transferMoney(Account account1, Account account2, BigDecimal transferAmount) {
        logger.info("Transfer all moneys from one account to another");
        TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest();
        transferMoneyRequest.setAccountFrom(account1.getId());
        transferMoneyRequest.setAccountTo(account2.getId());
        transferMoneyRequest.setCurrency(account1.getCurrency());
        transferMoneyRequest.setAmount(transferAmount);
        MessageResponse<List<Account>> messageAccountsResponse = invokePost("/account/transfer", transferMoneyRequest, new GenericType<MessageResponse<List<Account>>>() {
        });
        assertEquals(messageAccountsResponse.getMessage().size(), 2);
        logger.info("result: " + messageAccountsResponse.getMessage());
    }

    private List<Transfer> getTransfers() {
        logger.info("Getting transfers");
        MessageResponse<List<Transfer>> transfersResponse = invokeGet("/transfer",
                new GenericType<MessageResponse<List<Transfer>>>() {
                });
        logger.info("result: " + transfersResponse.getMessage());
        return transfersResponse.getMessage();
    }
}
