package com.rustam.project.service;

import com.rustam.project.TestsBase;
import com.rustam.project.model.entity.Account;
import com.rustam.project.model.entity.Currency;
import com.rustam.project.model.entity.User;
import com.rustam.project.model.exception.NotAllowedException;
import com.rustam.project.model.exception.NotFoundException;
import com.rustam.project.model.request.AddAccountRequest;
import com.rustam.project.model.request.CreateUserRequest;
import com.rustam.project.model.request.RechargeAccountRequest;
import com.rustam.project.model.request.TransferMoneyRequest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static com.rustam.project.service.AccountService.BIG_ZERO;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


/**
 * Created by Rustam Kadyrov on 06.07.2017.
 */
public class AccountServiceTest extends TestsBase {

    private User user;

    @BeforeMethod
    public void beforeMethod() {
        user = getUserService().createUser(CreateUserRequest.builder().name("Test")
                .build());
    }

    @Test
    public void testFindOne() throws Exception {
        Account first = getAccountService().createAccount(AddAccountRequest.builder()
                .accountCurrency(Currency.EUR)
                .userId(user.getId())
                .build());

        Account second = getAccountService().createAccount(AddAccountRequest.builder()
                .accountCurrency(Currency.USD)
                .userId(user.getId())
                .build());

        Account accountFind = getAccountService().findOne(first.getId());

        assertThat(first.getId(), not(second.getId()));
        assertThat(accountFind.getId(), is(first.getId()));
        assertThat(accountFind.getBalance(), equalTo(BIG_ZERO));
        assertThat(accountFind.getCurrency(), is(Currency.EUR));
    }

    @Test
    public void testCreateAccount() throws Exception {
        Account account = getAccountService().createAccount(AddAccountRequest.builder()
                .accountCurrency(Currency.EUR)
                .userId(user.getId())
                .build());

        assertThat(account.getId(), notNullValue());
        assertThat(account.getBalance(), equalTo(BIG_ZERO));
        assertThat(account.getCurrency(), is(Currency.EUR));
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void testCreateAccountNotFound() throws Exception {
        getAccountService().createAccount(AddAccountRequest.builder()
                .accountCurrency(Currency.EUR)
                .userId(100L)
                .build());
    }

    @Test
    public void testTransferMoneyNormal() throws Exception {
        Account first = getAccountService().createAccount(AddAccountRequest.builder()
                .accountCurrency(Currency.EUR)
                .userId(user.getId())
                .build());
        getAccountService().rechargeMoney(RechargeAccountRequest.builder()
                .accountId(first.getId())
                .currency(Currency.EUR)
                .amount(getScaledBigDecimal(10000))
                .build());

        Account second = getAccountService().createAccount(AddAccountRequest.builder()
                .accountCurrency(Currency.EUR)
                .userId(user.getId())
                .build());

        List<Account> accountList = getAccountService().transferMoney(TransferMoneyRequest.builder()
                .accountFrom(first.getId())
                .accountTo(second.getId())
                .amount(getScaledBigDecimal(5555))
                .currency(Currency.EUR)
                .build());

        assertThat(accountList, hasSize(2));
        assertThat(accountList.stream()
                        .filter(account -> account.getId().equals(first.getId())).findFirst()
                        .get().getBalance(),
                is(getScaledBigDecimal(4445)));

        assertThat(accountList.stream()
                        .filter(account -> account.getId().equals(second.getId())).findFirst()
                        .get().getBalance(),
                is(getScaledBigDecimal(5555)));
    }

    @Test(expectedExceptions = NotAllowedException.class)
    public void testTransferMoneyDifferentCurrencies() throws Exception {
        Account first = getAccountService().createAccount(AddAccountRequest.builder()
                .accountCurrency(Currency.EUR)
                .userId(user.getId())
                .build());
        getAccountService().rechargeMoney(RechargeAccountRequest.builder()
                .accountId(first.getId())
                .currency(Currency.EUR)
                .amount(getScaledBigDecimal(10000))
                .build());

        Account second = getAccountService().createAccount(AddAccountRequest.builder()
                .accountCurrency(Currency.EUR)
                .userId(user.getId())
                .build());

        List<Account> accountList = getAccountService().transferMoney(TransferMoneyRequest.builder()
                .accountFrom(first.getId())
                .accountTo(second.getId())
                .amount(getScaledBigDecimal(5555))
                .currency(Currency.USD)
                .build());

        assertThat(accountList, hasSize(2));
    }

    @Test(expectedExceptions = NotAllowedException.class)
    public void testTransferMoneyNotEnoughMoney() throws Exception {
        Account first = getAccountService().createAccount(AddAccountRequest.builder()
                .accountCurrency(Currency.EUR)
                .userId(user.getId())
                .build());

        Account second = getAccountService().createAccount(AddAccountRequest.builder()
                .accountCurrency(Currency.EUR)
                .userId(user.getId())
                .build());

        List<Account> accountList = getAccountService().transferMoney(TransferMoneyRequest.builder()
                .accountFrom(first.getId())
                .accountTo(second.getId())
                .amount(getScaledBigDecimal(5555))
                .currency(Currency.EUR)
                .build());

        assertThat(accountList, hasSize(2));
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void testTransferMoneyNotFound() throws Exception {
        List<Account> accountList = getAccountService().transferMoney(TransferMoneyRequest.builder()
                .accountFrom(10L)
                .accountTo(20L)
                .amount(getScaledBigDecimal(100))
                .currency(Currency.EUR)
                .build());

        assertThat(accountList, hasSize(2));
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void testTransferMoneyNotFoundSecond() throws Exception {
        Account first = getAccountService().createAccount(AddAccountRequest.builder()
                .accountCurrency(Currency.EUR)
                .userId(user.getId())
                .build());

        List<Account> accountList = getAccountService().transferMoney(TransferMoneyRequest.builder()
                .accountFrom(first.getId())
                .accountTo(122L)
                .amount(getScaledBigDecimal(5555))
                .currency(Currency.EUR)
                .build());

        assertThat(accountList, hasSize(2));
    }

    @Test
    public void testRechargeMoney() throws Exception {
        Account account = getAccountService().createAccount(AddAccountRequest.builder()
                .accountCurrency(Currency.EUR)
                .userId(user.getId())
                .build());

        getAccountService().rechargeMoney(RechargeAccountRequest.builder()
                .accountId(account.getId())
                .currency(Currency.EUR)
                .amount(getScaledBigDecimal(8900))
                .build());

        getAccountService().rechargeMoney(RechargeAccountRequest.builder()
                .accountId(account.getId())
                .currency(Currency.EUR)
                .amount(getScaledBigDecimal(560))
                .build());

        assertThat(account.getId(), notNullValue());
        assertThat(account.getBalance(), equalTo(getScaledBigDecimal(8900 + 560)));
        assertThat(account.getCurrency(), is(Currency.EUR));
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void testRechargeMoneyNotFound() throws Exception {
        getAccountService().rechargeMoney(RechargeAccountRequest.builder()
                .accountId(100L)
                .currency(Currency.EUR)
                .amount(getScaledBigDecimal(8900))
                .build());
    }

    @Test(expectedExceptions = NotAllowedException.class)
    public void testRechargeMoneyNotAllowed() throws Exception {
        Account account = getAccountService().createAccount(AddAccountRequest.builder()
                .accountCurrency(Currency.EUR)
                .userId(user.getId())
                .build());

        getAccountService().rechargeMoney(RechargeAccountRequest.builder()
                .accountId(account.getId())
                .currency(Currency.RUR)
                .amount(getScaledBigDecimal(8900))
                .build());
    }
}