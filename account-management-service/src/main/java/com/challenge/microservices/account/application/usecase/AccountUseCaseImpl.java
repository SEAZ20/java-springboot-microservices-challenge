package com.challenge.microservices.account.application.usecase;

import com.challenge.microservices.account.domain.exception.AccountNotFoundException;
import com.challenge.microservices.account.domain.exception.DuplicateAccountNumberException;
import com.challenge.microservices.account.domain.model.Account;
import com.challenge.microservices.account.domain.port.input.AccountUseCase;
import com.challenge.microservices.account.domain.port.output.AccountRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountUseCaseImpl implements AccountUseCase {

    private final AccountRepositoryPort accountRepository;

    @Override
    @Transactional
    public Account create(Account account) {
        log.info("Iniciando creación de cuenta: accountNumber={}, customerId={}",
                account.getAccountNumber(), account.getCustomerId());
        if (accountRepository.existsByAccountNumber(account.getAccountNumber())) {
            log.warn("Numero de cuenta ya registrado: accountNumber={}", account.getAccountNumber());
            throw new DuplicateAccountNumberException(account.getAccountNumber());
        }
        account.setCurrentBalance(account.getInitialBalance());
        Account saved = accountRepository.save(account);
        log.info("Cuenta creada exitosamente: id={}, accountNumber={}", saved.getId(), saved.getAccountNumber());
        return saved;
    }

    @Override
    public Account findById(Long id) {
        log.debug("Buscando cuenta por id={}", id);
        return accountRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cuenta no encontrada: id={}", id);
                    return new AccountNotFoundException(id);
                });
    }

    @Override
    public List<Account> findAll() {
        log.debug("Consultando todas las cuentas");
        List<Account> result = accountRepository.findAll();
        log.debug("Total de cuentas encontradas: {}", result.size());
        return result;
    }

    @Override
    @Transactional
    public Account update(Long id, Account account) {
        log.info("Iniciando actualización completa de cuenta: id={}", id);
        Account existing = findById(id);
        account.setId(existing.getId());
        account.setCurrentBalance(existing.getCurrentBalance());
        Account saved = accountRepository.save(account);
        log.info("Cuenta actualizada exitosamente: id={}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public Account partialUpdate(Long id, Account account) {
        log.info("Iniciando actualización parcial de cuenta: id={}", id);
        Account existing = findById(id);
        if (account.getAccountNumber() != null) existing.setAccountNumber(account.getAccountNumber());
        if (account.getAccountType() != null) existing.setAccountType(account.getAccountType());
        if (account.getStatus() != null) existing.setStatus(account.getStatus());
        Account saved = accountRepository.save(existing);
        log.info("Cuenta actualizada parcialmente: id={}", saved.getId());
        return saved;
    }
}
