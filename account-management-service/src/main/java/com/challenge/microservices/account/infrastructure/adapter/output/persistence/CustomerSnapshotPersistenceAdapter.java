package com.challenge.microservices.account.infrastructure.adapter.output.persistence;

import com.challenge.microservices.account.domain.model.CustomerSnapshot;
import com.challenge.microservices.account.domain.port.output.CustomerSnapshotRepositoryPort;
import com.challenge.microservices.account.infrastructure.adapter.output.persistence.entity.CustomerSnapshotEntity;
import com.challenge.microservices.account.infrastructure.adapter.output.persistence.repository.JpaCustomerSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerSnapshotPersistenceAdapter implements CustomerSnapshotRepositoryPort {

    private final JpaCustomerSnapshotRepository jpaRepository;

    @Override
    public CustomerSnapshot save(CustomerSnapshot snapshot) {
        CustomerSnapshotEntity saved = jpaRepository.save(toEntity(snapshot));
        return toDomain(saved);
    }

    @Override
    public Optional<CustomerSnapshot> findById(Long customerId) {
        return jpaRepository.findById(customerId).map(this::toDomain);
    }

    @Override
    public void deleteById(Long customerId) {
        jpaRepository.deleteById(customerId);
    }

    private CustomerSnapshotEntity toEntity(CustomerSnapshot snapshot) {
        return CustomerSnapshotEntity.builder()
                .customerId(snapshot.getCustomerId())
                .name(snapshot.getName())
                .status(snapshot.getStatus())
                .build();
    }

    private CustomerSnapshot toDomain(CustomerSnapshotEntity entity) {
        return CustomerSnapshot.builder()
                .customerId(entity.getCustomerId())
                .name(entity.getName())
                .status(entity.getStatus())
                .build();
    }
}
