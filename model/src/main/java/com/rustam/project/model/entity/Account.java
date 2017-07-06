package com.rustam.project.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * Created by Rustam_Kadyrov on 25.06.2017.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name = "Account")
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Currency currency;

    @Column(precision = 19, scale = 3)
    @NotNull
    private BigDecimal balance;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @NotNull
    private ZonedDateTime createdAt;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @NotNull
    private ZonedDateTime modifiedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (!id.equals(account.id)) return false;
        if (currency != account.currency) return false;
        if (!balance.equals(account.balance)) return false;
        if (!createdAt.equals(account.createdAt)) return false;
        return modifiedAt != null ? modifiedAt.equals(account.modifiedAt) : account.modifiedAt == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + currency.hashCode();
        result = 31 * result + balance.hashCode();
        result = 31 * result + createdAt.hashCode();
        result = 31 * result + (modifiedAt != null ? modifiedAt.hashCode() : 0);
        return result;
    }
}
