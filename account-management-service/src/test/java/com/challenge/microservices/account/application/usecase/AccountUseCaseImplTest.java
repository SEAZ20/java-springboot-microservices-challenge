package com.challenge.microservices.account.application.usecase;

import com.challenge.microservices.account.domain.exception.DuplicateAccountNumberException;
import com.challenge.microservices.account.domain.model.Account;
import com.challenge.microservices.account.domain.model.AccountType;
import com.challenge.microservices.account.domain.port.output.AccountRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountUseCaseImplTest {

    @Mock
    private AccountRepositoryPort accountRepository;

    @InjectMocks
    private AccountUseCaseImpl useCase;

    @Test
    void create_withUniqueAccountNumber_shouldCreateAccount() {
        Account input = Account.builder()
                .accountNumber("478758")
                .accountType(AccountType.SAVINGS)
                .initialBalance(new BigDecimal("2000.00"))
                .status(true)
                .customerId(1L)
                .build();

        Account saved = Account.builder()
                .id(1L)
                .accountNumber("478758")
                .accountType(AccountType.SAVINGS)
                .initialBalance(new BigDecimal("2000.00"))
                .currentBalance(new BigDecimal("2000.00"))
                .status(true)
                .customerId(1L)
                .build();

        when(accountRepository.existsByAccountNumber("478758")).thenReturn(false);
        when(accountRepository.save(any())).thenReturn(saved);

        Account result = useCase.create(input);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getCurrentBalance()).isEqualByComparingTo("2000.00");
    }

    @Test
    void create_withDuplicateAccountNumber_shouldThrowException() {
        Account input = Account.builder()
                .accountNumber("478758")
                .accountType(AccountType.SAVINGS)
                .initialBalance(new BigDecimal("2000.00"))
                .status(true)
                .customerId(1L)
                .build();

        when(accountRepository.existsByAccountNumber("478758")).thenReturn(true);

        assertThatThrownBy(() -> useCase.create(input))
                .isInstanceOf(DuplicateAccountNumberException.class)
                .hasMessageContaining("478758");
    }
}
