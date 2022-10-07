package com.rntgroup.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "user_accounts")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAccount {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference(value = "user_account")
    User user;

    @Column(name = "amount", nullable = false)
    BigDecimal amount;

}
