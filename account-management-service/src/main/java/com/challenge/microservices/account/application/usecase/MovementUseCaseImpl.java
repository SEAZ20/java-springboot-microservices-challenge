package com.challenge.microservices.account.application.usecase;

import com.challenge.microservices.account.domain.exception.AccountNotFoundException;
import com.challenge.microservices.account.domain.exception.InsufficientBalanceException;
import com.challenge.microservices.account.domain.exception.MovementNotFoundException;
import com.challenge.microservices.account.domain.model.Account;
import com.challenge.microservices.account.domain.model.Movement;
import com.challenge.microservices.account.domain.model.MovementType;
import com.challenge.microservices.account.domain.port.input.MovementUseCase;
import com.challenge.microservices.account.domain.port.output.AccountRepositoryPort;
import com.challenge.microservices.account.domain.port.output.MovementRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovementUseCaseImpl implements MovementUseCase {

    private final MovementRepositoryPort movementRepository;
    private final AccountRepositoryPort accountRepository;

    @Override
    @Transactional
    public Movement create(Movement movement) {
        log.info("Iniciando registro de movimiento: accountNumber={}, value={}",
                movement.getAccountNumber(), movement.getValue());

        Account account = accountRepository.findByAccountNumber(movement.getAccountNumber())
                .orElseThrow(() -> {
                    log.warn("Cuenta no encontrada para movimiento: accountNumber={}", movement.getAccountNumber());
                    return new AccountNotFoundException(movement.getAccountNumber());
                });

        BigDecimal newBalance = account.getCurrentBalance().add(movement.getValue());
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            log.warn("Saldo insuficiente: accountId={}, currentBalance={}, requestedValue={}",
                    account.getId(), account.getCurrentBalance(), movement.getValue());
            throw new InsufficientBalanceException();
        }

        MovementType type = movement.getValue().compareTo(BigDecimal.ZERO) >= 0
                ? MovementType.DEPOSIT
                : MovementType.WITHDRAWAL;

        account.setCurrentBalance(newBalance);
        accountRepository.save(account);

        movement.setMovementType(type);
        movement.setBalance(newBalance);
        movement.setAccountId(account.getId());
        movement.setAccountNumber(account.getAccountNumber());
        if (movement.getDate() == null) {
            movement.setDate(LocalDate.now());
        }

        Movement saved = movementRepository.save(movement);
        log.info("Movimiento registrado exitosamente: id={}, type={}, newBalance={}",
                saved.getId(), type, newBalance);
        return saved;
    }

    @Override
    public Movement findById(Long id) {
        log.debug("Buscando movimiento por id={}", id);
        return movementRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Movimiento no encontrado: id={}", id);
                    return new MovementNotFoundException(id);
                });
    }

    @Override
    public List<Movement> findAll() {
        log.debug("Consultando todos los movimientos");
        List<Movement> result = movementRepository.findAll();
        log.debug("Total de movimientos encontrados: {}", result.size());
        return result;
    }

    @Override
    @Transactional
    public Movement update(Long id, Movement movement) {
        log.info("Actualizando fecha de movimiento: id={}", id);
        Movement existing = findById(id);
        if (movement.getDate() != null) existing.setDate(movement.getDate());
        Movement saved = movementRepository.save(existing);
        log.info("Movimiento actualizado: id={}", saved.getId());
        return saved;
    }
}
