package com.challenge.microservices.account.domain.port.input;

import com.challenge.microservices.account.domain.model.Account;

import java.util.List;

public interface AccountUseCase {

    Account create(Account account);

    Account findById(Long id);

    List<Account> findAll();

    Account update(Long id, Account account);

    Account partialUpdate(Long id, Account account);
}
