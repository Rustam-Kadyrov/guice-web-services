package com.rustam.project.service;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.rustam.project.model.entity.Account;
import com.rustam.project.model.entity.Transfer;
import com.rustam.project.model.entity.User;
import com.rustam.project.model.exception.NotAllowedException;
import com.rustam.project.model.exception.NotFoundException;
import com.rustam.project.model.request.AddAccountRequest;
import com.rustam.project.model.request.RechargeAccountRequest;
import com.rustam.project.model.request.TransferMoneyRequest;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Rustam_Kadyrov on 25.06.2017.
 */
public class AccountService {

    public static final BigDecimal BIG_ZERO = new BigDecimal(BigInteger.ZERO, 3);
    private final EntityManager em;

    @Inject
    public AccountService(EntityManager em) {
        this.em = em;
    }

    public Account findOne(Long id) {
        return em.find(Account.class, id);
    }

    @Transactional
    public Account createAccount(AddAccountRequest addAccountRequest) {
        User user = em.find(User.class, addAccountRequest.getUserId());
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        Account account = new Account();
        account.setBalance(BIG_ZERO);
        account.setCurrency(addAccountRequest.getAccountCurrency());
        account.setCreatedAt(ZonedDateTime.now());

        account = em.merge(account);
        user.getAccounts().add(account);
        em.merge(user);
        em.flush();
        return account;
    }

    @Transactional
    public List<Account> transferMoney(TransferMoneyRequest transferMoneyRequest) {
        Account from = findOne(transferMoneyRequest.getAccountFrom());
        if (from == null) {
            throw new NotFoundException("Account from not found");
        }
        Account to = findOne(transferMoneyRequest.getAccountTo());
        if (to == null) {
            throw new NotFoundException("Account to not found");
        }

        if (from.getCurrency() != to.getCurrency() || from.getCurrency() != transferMoneyRequest.getCurrency()) {
            throw new NotAllowedException("Cross transfers aren't allowed at the moment");
        }

        if (from.getBalance().compareTo(transferMoneyRequest.getAmount()) == -1) {
            throw new NotAllowedException("Not enough money to transfer");
        }

        Transfer transfer = new Transfer();
        transfer.setAccountFrom(from.getId());
        transfer.setAccountTo(to.getId());
        transfer.setCurrency(transferMoneyRequest.getCurrency());
        transfer.setAmount(transferMoneyRequest.getAmount());
        transfer.setCreatedAt(ZonedDateTime.now());
        em.persist(transfer);

        from.setBalance(from.getBalance().subtract(transferMoneyRequest.getAmount()));
        from.setModifiedAt(ZonedDateTime.now());
        to.setBalance(to.getBalance().add(transferMoneyRequest.getAmount()));
        to.setModifiedAt(ZonedDateTime.now());
        em.merge(from);
        em.merge(to);
        em.flush();
        return Arrays.asList(from, to);
    }

    @Transactional
    public Account rechargeMoney(RechargeAccountRequest rechargeAccountRequest) {
        Account account = findOne(rechargeAccountRequest.getAccountId());
        if (account == null) {
            throw new NotFoundException("Account not found");
        }

        if (account.getCurrency() != rechargeAccountRequest.getCurrency()) {
            throw new NotAllowedException("Cross transfers aren't allowed at the moment");
        }

        account.setBalance(account.getBalance().add(rechargeAccountRequest.getAmount()));
        account.setModifiedAt(ZonedDateTime.now());
        return em.merge(account);
    }
}
