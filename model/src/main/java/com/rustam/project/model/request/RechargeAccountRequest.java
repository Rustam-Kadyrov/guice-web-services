package com.rustam.project.model.request;

import com.rustam.project.model.entity.Currency;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by Rustam_Kadyrov on 25.06.2017.
 */
@Getter
@Setter
@NoArgsConstructor
public class RechargeAccountRequest {

    private Long accountId;
    private Currency currency;
    private BigDecimal amount;

    @Builder
    public RechargeAccountRequest(Long accountId, Currency currency, BigDecimal amount) {
        this.accountId = accountId;
        this.currency = currency;
        this.amount = amount;
    }
}
