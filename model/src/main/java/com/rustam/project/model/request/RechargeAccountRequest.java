package com.rustam.project.model.request;

import com.rustam.project.model.entity.Currency;

import java.math.BigDecimal;

/**
 * Created by Rustam_Kadyrov on 25.06.2017.
 */
public class RechargeAccountRequest {

    private Long accountId;
    private Currency currency;
    private BigDecimal amount;

    public RechargeAccountRequest() {
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
