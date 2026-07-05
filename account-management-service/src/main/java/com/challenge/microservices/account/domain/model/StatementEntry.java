package com.challenge.microservices.account.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatementEntry {

    private LocalDate date;
    private String customerName;
    private String accountNumber;
    private AccountType accountType;
    private BigDecimal initialBalance;
    private Boolean accountStatus;
    private BigDecimal movementValue;
    private BigDecimal availableBalance;
}
