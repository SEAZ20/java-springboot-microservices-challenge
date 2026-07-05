package com.challenge.microservices.account.application.usecase;

import com.challenge.microservices.account.domain.model.CustomerSnapshot;
import com.challenge.microservices.account.domain.port.input.CustomerSnapshotUseCase;
import com.challenge.microservices.account.domain.port.output.CustomerSnapshotRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerSnapshotUseCaseImpl implements CustomerSnapshotUseCase {

    private final CustomerSnapshotRepositoryPort snapshotRepository;

    @Override
    public void handleCustomerCreated(CustomerSnapshot snapshot) {
        log.info("Procesando evento CUSTOMER_CREATED: customerId={}, name={}",
                snapshot.getCustomerId(), snapshot.getName());
        snapshotRepository.save(snapshot);
        log.debug("Snapshot de cliente creado: customerId={}", snapshot.getCustomerId());
    }

    @Override
    public void handleCustomerUpdated(CustomerSnapshot snapshot) {
        log.info("Procesando evento CUSTOMER_UPDATED: customerId={}, name={}",
                snapshot.getCustomerId(), snapshot.getName());
        snapshotRepository.save(snapshot);
        log.debug("Snapshot de cliente actualizado: customerId={}", snapshot.getCustomerId());
    }

    @Override
    public void handleCustomerDeleted(Long customerId) {
        log.info("Procesando evento CUSTOMER_DELETED: customerId={}", customerId);
        snapshotRepository.deleteById(customerId);
        log.debug("Snapshot de cliente eliminado: customerId={}", customerId);
    }
}
