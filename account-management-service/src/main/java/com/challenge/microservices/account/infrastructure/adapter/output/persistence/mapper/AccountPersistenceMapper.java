package com.challenge.microservices.account.infrastructure.adapter.output.persistence.mapper;

import com.challenge.microservices.account.domain.model.Account;
import com.challenge.microservices.account.infrastructure.adapter.output.persistence.entity.AccountEntity;
import org.springframework.stereotype.Component;

@Component
public class AccountPersistenceMapper {

    public Account toDomain(AccountEntity entity) {
        return Account.builder()
                .id(entity.getId())
                .accountNumber(entity.getAccountNumber())
                .accountType(entity.getAccountType())
                .initialBalance(entity.getInitialBalance())
                .currentBalance(entity.getCurrentBalance())
                .status(entity.getStatus())
                .customerId(entity.getCustomerId())
                .build();
    }

    public AccountEntity toEntity(Account account) {
        return AccountEntity.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .initialBalance(account.getInitialBalance())
                .currentBalance(account.getCurrentBalance())
                .status(account.getStatus())
                .customerId(account.getCustomerId())
                .build();
    }
}
