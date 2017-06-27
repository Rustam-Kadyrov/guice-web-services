package com.rustam.project.endpoint;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import com.rustam.project.model.entity.Account;
import com.rustam.project.model.request.AddAccountRequest;
import com.rustam.project.model.request.RechargeAccountRequest;
import com.rustam.project.model.request.TransferMoneyRequest;
import com.rustam.project.model.response.MessageResponse;
import com.rustam.project.service.AccountService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Working with account
 * Created by Rustam_Kadyrov on 25.06.2017.
 */
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Path("/account")
public class AccountEndpoint {

    private final AccountService accountService;

    @Inject
    public AccountEndpoint(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Create account
     *
     * @param addAccountRequest
     * @return
     */
    @POST
    public MessageResponse<Account> createAcoount(AddAccountRequest addAccountRequest) {
        checkNotNull(addAccountRequest.getUserId());
        checkNotNull(addAccountRequest.getAccountCurrency());
        return new MessageResponse<>(accountService.createAccount(addAccountRequest));
    }

    /**
     * Transfer money between accounts
     *
     * @param transferMoneyRequest
     * @return
     */
    @POST
    @Path("/transfer")
    public MessageResponse<List<Account>> transferMoney(TransferMoneyRequest transferMoneyRequest) {
        checkNotNull(transferMoneyRequest.getAccountFrom());
        checkNotNull(transferMoneyRequest.getAccountTo());
        checkNotNull(transferMoneyRequest.getAmount());
        checkNotNull(transferMoneyRequest.getCurrency());
        return new MessageResponse<>(accountService.transferMoney(transferMoneyRequest));
    }


    /**
     * Recharge account with money
     *
     * @param rechargeAccountRequest
     * @return
     */
    @POST
    @Path("/recharge")
    public MessageResponse<Account> rechargeMoney(RechargeAccountRequest rechargeAccountRequest) {
        checkNotNull(rechargeAccountRequest.getAccountId());
        checkNotNull(rechargeAccountRequest.getAmount());
        checkNotNull(rechargeAccountRequest.getCurrency());
        return new MessageResponse<>(accountService.rechargeMoney(rechargeAccountRequest));
    }

    /**
     * Get accounts info
     *
     * @param id
     * @return
     */
    @GET
    public MessageResponse<Account> getAccountInfo(@QueryParam("id") Long id) {
        return new MessageResponse<>(accountService.findOne(id));
    }
}
