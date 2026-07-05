package com.challenge.microservices.account.infrastructure.adapter.input.rest.mapper;

import com.challenge.microservices.account.domain.model.Account;
import com.challenge.microservices.account.infrastructure.adapter.input.rest.dto.AccountRequest;
import com.challenge.microservices.account.infrastructure.adapter.input.rest.dto.AccountResponse;
import org.springframework.stereotype.Component;

@Component
public class AccountRestMapper {

    public Account toDomain(AccountRequest request) {
        return Account.builder()
                .accountNumber(request.getAccountNumber())
                .accountType(request.getAccountType())
                .initialBalance(request.getInitialBalance())
                .status(request.getStatus())
                .customerId(request.getCustomerId())
                .build();
    }

    public AccountResponse toResponse(Account account) {
        return AccountResponse.builder()
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
