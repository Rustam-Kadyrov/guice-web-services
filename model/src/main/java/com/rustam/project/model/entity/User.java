package com.rustam.project.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rustam Kadyrov on 25.06.2017.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name = "User")
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    @NotNull
    private String name;

    @Column
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime lastModification;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "account_id")
    private List<Account> accounts = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!id.equals(user.id)) return false;
        if (!name.equals(user.name)) return false;
        if (lastModification != null ? !lastModification.equals(user.lastModification) : user.lastModification != null)
            return false;
        return accounts.equals(user.accounts);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (lastModification != null ? lastModification.hashCode() : 0);
        result = 31 * result + accounts.hashCode();
        return result;
    }
}
