package com.rustam.project.endpoint;

import com.rustam.project.WebTestsBase;
import com.rustam.project.model.entity.Account;
import com.rustam.project.model.entity.Currency;
import com.rustam.project.model.entity.User;
import com.rustam.project.model.request.AddAccountRequest;
import com.rustam.project.model.request.CreateUserRequest;
import com.rustam.project.model.request.RechargeAccountRequest;
import com.rustam.project.model.request.TransferMoneyRequest;
import com.rustam.project.model.response.MessageResponse;
import com.sun.jersey.api.client.GenericType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static com.rustam.project.service.AccountService.BIG_ZERO;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by Rustam Kadyrov on 07.07.2017.
 */
public class AccountEndpointTest extends WebTestsBase {

    private User user;

    @BeforeMethod
    public void beforeMethod() {
        user = invokePost("/user", CreateUserRequest.builder()
                .name("Fred").build(), new GenericType<MessageResponse<User>>() {
        }).getMessage();
    }

    @Test
    public void testCreateAccount() throws Exception {
        Account account = invokePost("/account", AddAccountRequest.builder()
                .userId(user.getId())
                .accountCurrency(Currency.EUR)
                .build(), new GenericType<MessageResponse<Account>>() {
        }).getMessage();

        assertThat(account.getId(), notNullValue());
        assertThat(account.getBalance(), equalTo(BIG_ZERO));
        assertThat(account.getCurrency(), is(Currency.EUR));
    }

    @Test
    public void testTransferMoney() throws Exception {
        Account account = invokePost("/account", AddAccountRequest.builder()
                .userId(user.getId())
                .accountCurrency(Currency.EUR)
                .build(), new GenericType<MessageResponse<Account>>() {
        }).getMessage();

        Account account2 = invokePost("/account", AddAccountRequest.builder()
                .userId(user.getId())
                .accountCurrency(Currency.EUR)
                .build(), new GenericType<MessageResponse<Account>>() {
        }).getMessage();

        Account accountRecharged = invokePost("/account/recharge", RechargeAccountRequest.builder()
                .accountId(account.getId())
                .amount(getScaledBigDecimal(2000))
                .currency(Currency.EUR).build(), new GenericType<MessageResponse<Account>>() {
        }).getMessage();

        List<Account> accounts = invokePost("/account/transfer", TransferMoneyRequest.builder()
                .accountFrom(accountRecharged.getId())
                .accountTo(account2.getId())
                .amount(getScaledBigDecimal(100))
                .currency(Currency.EUR)
                .build(), new GenericType<MessageResponse<List<Account>>>() {
        }).getMessage();
        assertThat(accounts, hasSize(2));
        assertThat(accounts.stream().filter(a -> a.getId().equals(account.getId()))
                .findFirst().get().getBalance(), equalTo(getScaledBigDecimal(1900)));
        assertThat(accounts.stream().filter(a -> a.getId().equals(account2.getId()))
                .findFirst().get().getBalance(), equalTo(getScaledBigDecimal(100)));
    }

    @Test
    public void testRechargeMoney() throws Exception {
        Account account = invokePost("/account", AddAccountRequest.builder()
                .userId(user.getId())
                .accountCurrency(Currency.EUR)
                .build(), new GenericType<MessageResponse<Account>>() {
        }).getMessage();

        Account accountRecharged = invokePost("/account/recharge", RechargeAccountRequest.builder()
                .accountId(account.getId())
                .amount(getScaledBigDecimal(2000))
                .currency(Currency.EUR).build(), new GenericType<MessageResponse<Account>>() {
        }).getMessage();

        assertThat(accountRecharged.getId(), is(account.getId()));
        assertThat(accountRecharged.getBalance(), equalTo(getScaledBigDecimal(2000)));
        assertThat(accountRecharged.getCurrency(), is(Currency.EUR));
    }

    @Test
    public void testGetAccountInfo() throws Exception {
        Account account = invokePost("/account", AddAccountRequest.builder()
                .userId(user.getId())
                .accountCurrency(Currency.EUR)
                .build(), new GenericType<MessageResponse<Account>>() {
        }).getMessage();

        Account accountFromRest = invokeGet("/account?id=" + account.getId(), new GenericType<MessageResponse<Account>>() {
        }).getMessage();

        assertThat(account, equalTo(accountFromRest));
    }
}