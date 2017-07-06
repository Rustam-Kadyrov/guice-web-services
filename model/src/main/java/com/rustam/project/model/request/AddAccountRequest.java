package com.rustam.project.model.request;

import com.rustam.project.model.entity.Currency;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Rustam_Kadyrov on 25.06.2017.
 */
@Getter
@Setter
@NoArgsConstructor
public class AddAccountRequest {

    private Long userId;
    private Currency accountCurrency;

    @Builder
    public AddAccountRequest(Long userId, Currency accountCurrency) {
        this.userId = userId;
        this.accountCurrency = accountCurrency;
    }
}
