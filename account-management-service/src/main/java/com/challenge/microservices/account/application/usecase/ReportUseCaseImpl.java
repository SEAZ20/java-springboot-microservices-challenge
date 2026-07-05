package com.challenge.microservices.account.application.usecase;

import com.challenge.microservices.account.domain.exception.CustomerNotFoundException;
import com.challenge.microservices.account.domain.model.CustomerSnapshot;
import com.challenge.microservices.account.domain.model.StatementEntry;
import com.challenge.microservices.account.domain.port.input.ReportUseCase;
import com.challenge.microservices.account.domain.port.output.CustomerSnapshotRepositoryPort;
import com.challenge.microservices.account.domain.port.output.MovementRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportUseCaseImpl implements ReportUseCase {

    private final MovementRepositoryPort movementRepository;
    private final CustomerSnapshotRepositoryPort customerSnapshotRepository;

    @Override
    public List<StatementEntry> getStatement(Long customerId, LocalDate startDate, LocalDate endDate) {
        log.info("Generando estado de cuenta: customerId={}, startDate={}, endDate={}",
                customerId, startDate, endDate);

        CustomerSnapshot customer = customerSnapshotRepository.findById(customerId)
                .orElseThrow(() -> {
                    log.warn("Cliente no encontrado en snapshot: customerId={}", customerId);
                    return new CustomerNotFoundException(customerId);
                });

        List<StatementEntry> entries = movementRepository
                .findByCustomerIdAndDateBetween(customerId, startDate, endDate)
                .stream()
                .map(m -> StatementEntry.builder()
                        .date(m.getDate())
                        .customerName(customer.getName())
                        .accountNumber(m.getAccountNumber())
                        .accountType(m.getAccountType())
                        .initialBalance(m.getAccountInitialBalance())
                        .accountStatus(m.getAccountStatus())
                        .movementValue(m.getValue())
                        .availableBalance(m.getBalance())
                        .build())
                .toList();

        log.info("Estado de cuenta generado: customerId={}, totalMovimientos={}",
                customerId, entries.size());
        return entries;
    }
}
