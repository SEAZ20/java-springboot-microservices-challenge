package com.challenge.microservices.account.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movement {

    private Long id;
    private LocalDate date;
    private MovementType movementType;
    private BigDecimal value;
    private BigDecimal balance;
    private Long accountId;
    private String accountNumber;
    private AccountType accountType;
    private BigDecimal accountInitialBalance;
    private Boolean accountStatus;
}
