package com.challenge.microservices.account.application.usecase;

import com.challenge.microservices.account.domain.exception.AccountNotFoundException;
import com.challenge.microservices.account.domain.exception.InsufficientBalanceException;
import com.challenge.microservices.account.domain.model.Account;
import com.challenge.microservices.account.domain.model.Movement;
import com.challenge.microservices.account.domain.model.MovementType;
import com.challenge.microservices.account.domain.port.output.AccountRepositoryPort;
import com.challenge.microservices.account.domain.port.output.MovementRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovementUseCaseImplTest {

    @Mock
    private MovementRepositoryPort movementRepository;

    @Mock
    private AccountRepositoryPort accountRepository;

    @InjectMocks
    private MovementUseCaseImpl useCase;

    @Test
    void create_withDeposit_shouldIncrementBalance() {
        Account account = Account.builder()
                .id(1L).accountNumber("478758")
                .currentBalance(new BigDecimal("2000.00"))
                .build();
        Movement movement = Movement.builder()
                .accountNumber("478758")
                .value(new BigDecimal("500.00"))
                .build();
        Movement saved = Movement.builder().id(1L).movementType(MovementType.DEPOSIT).balance(new BigDecimal("2500.00")).build();

        when(accountRepository.findByAccountNumber("478758")).thenReturn(Optional.of(account));
        when(accountRepository.save(any())).thenReturn(account);
        when(movementRepository.save(any())).thenReturn(saved);

        Movement result = useCase.create(movement);

        assertThat(result.getMovementType()).isEqualTo(MovementType.DEPOSIT);
        assertThat(result.getBalance()).isEqualByComparingTo("2500.00");
    }

    @Test
    void create_withWithdrawal_shouldDecrementBalance() {
        Account account = Account.builder()
                .id(1L).accountNumber("478758")
                .currentBalance(new BigDecimal("2000.00"))
                .build();
        Movement movement = Movement.builder()
                .accountNumber("478758")
                .value(new BigDecimal("-575.00"))
                .build();
        Movement saved = Movement.builder().id(1L).movementType(MovementType.WITHDRAWAL).balance(new BigDecimal("1425.00")).build();

        when(accountRepository.findByAccountNumber("478758")).thenReturn(Optional.of(account));
        when(accountRepository.save(any())).thenReturn(account);
        when(movementRepository.save(any())).thenReturn(saved);

        Movement result = useCase.create(movement);

        assertThat(result.getMovementType()).isEqualTo(MovementType.WITHDRAWAL);
    }

    @Test
    void create_withInsufficientBalance_shouldThrowException() {
        Account account = Account.builder()
                .id(1L).currentBalance(new BigDecimal("100.00"))
                .build();
        Movement movement = Movement.builder()
                .accountNumber("478758")
                .value(new BigDecimal("-99999.00"))
                .build();

        when(accountRepository.findByAccountNumber("478758")).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> useCase.create(movement))
                .isInstanceOf(InsufficientBalanceException.class)
                .hasMessageContaining("Saldo no disponible");
    }

    @Test
    void create_withUnknownAccount_shouldThrowAccountNotFoundException() {
        Movement movement = Movement.builder().accountNumber("000000").value(BigDecimal.TEN).build();
        when(accountRepository.findByAccountNumber("000000")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.create(movement))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("000000");
    }
}
