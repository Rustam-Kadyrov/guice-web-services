package com.rustam.project.model.request;

import com.rustam.project.model.entity.Currency;

/**
 * Created by Rustam_Kadyrov on 25.06.2017.
 */
public class AddAccountRequest {

    private Long userId;
    private Currency accountCurrency;

    public AddAccountRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Currency getAccountCurrency() {
        return accountCurrency;
    }

    public void setAccountCurrency(Currency accountCurrency) {
        this.accountCurrency = accountCurrency;
    }
}
