package com.challenge.microservices.account.domain.port.output;

import com.challenge.microservices.account.domain.model.CustomerSnapshot;

import java.util.Optional;

public interface CustomerSnapshotRepositoryPort {

    CustomerSnapshot save(CustomerSnapshot snapshot);

    Optional<CustomerSnapshot> findById(Long customerId);

    void deleteById(Long customerId);
}
