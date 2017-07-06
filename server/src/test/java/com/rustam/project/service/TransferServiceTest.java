package com.rustam.project.service;

import com.rustam.project.TestsBase;
import com.rustam.project.model.entity.Account;
import com.rustam.project.model.entity.Currency;
import com.rustam.project.model.entity.Transfer;
import com.rustam.project.model.entity.User;
import com.rustam.project.model.request.AddAccountRequest;
import com.rustam.project.model.request.CreateUserRequest;
import com.rustam.project.model.request.RechargeAccountRequest;
import com.rustam.project.model.request.TransferMoneyRequest;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


/**
 * Created by Rustam Kadyrov on 06.07.2017.
 */
public class TransferServiceTest extends TestsBase {

    @Test
    public void testFindAll() throws Exception {
        User user = getUserService().createUser(CreateUserRequest.builder().name("Test")
                .build());

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

        getAccountService().transferMoney(TransferMoneyRequest.builder()
                .accountFrom(first.getId())
                .accountTo(second.getId())
                .amount(getScaledBigDecimal(5555))
                .currency(Currency.EUR)
                .build());

        List<Transfer> transfers = getTransferService().findAll();
        assertThat(transfers, hasSize(1));
        assertThat(transfers.get(0).getAccountFrom(), is(first.getId()));
        assertThat(transfers.get(0).getAccountTo(), is(second.getId()));
        assertThat(transfers.get(0).getAmount(), is(getScaledBigDecimal(5555)));
        assertThat(transfers.get(0).getCurrency(), is(Currency.EUR));
        assertThat(transfers.get(0).getCreatedAt(), notNullValue());
    }

}