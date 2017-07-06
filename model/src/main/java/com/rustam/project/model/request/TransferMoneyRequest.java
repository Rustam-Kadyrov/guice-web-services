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
public class TransferMoneyRequest {
    private Long accountFrom;
    private Long accountTo;
    private BigDecimal amount;
    private Currency currency;

    @Builder
    public TransferMoneyRequest(Long accountFrom, Long accountTo, BigDecimal amount, Currency currency) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
        this.currency = currency;
    }
}
