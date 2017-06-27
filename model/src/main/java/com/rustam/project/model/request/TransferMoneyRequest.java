package com.rustam.project.model.request;

import com.rustam.project.model.entity.Currency;

import java.math.BigDecimal;

/**
 * Created by Rustam_Kadyrov on 25.06.2017.
 */
public class TransferMoneyRequest {
    private Long accountFrom;
    private Long accountTo;
    private BigDecimal amount;
    private Currency currency;

    public TransferMoneyRequest() {
    }

    public Long getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Long accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Long getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Long accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
