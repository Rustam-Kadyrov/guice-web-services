package com.rustam.project.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
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
@Entity(name = "Transfer")
@Table(name = "transfer")
public class Transfer {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    @NotNull
    private Long accountFrom;

    @Column
    @NotNull
    private Long accountTo;

    @Column(precision = 19, scale = 3)
    @DecimalMin(value = "0", inclusive = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Currency currency;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transfer transfer = (Transfer) o;

        if (!id.equals(transfer.id)) return false;
        if (!accountFrom.equals(transfer.accountFrom)) return false;
        if (!accountTo.equals(transfer.accountTo)) return false;
        if (!amount.equals(transfer.amount)) return false;
        if (currency != transfer.currency) return false;
        return createdAt.equals(transfer.createdAt);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + accountFrom.hashCode();
        result = 31 * result + accountTo.hashCode();
        result = 31 * result + amount.hashCode();
        result = 31 * result + currency.hashCode();
        result = 31 * result + createdAt.hashCode();
        return result;
    }
}
