package com.challenge.microservices.account.domain.port.input;

import com.challenge.microservices.account.domain.model.CustomerSnapshot;

public interface CustomerSnapshotUseCase {

    void handleCustomerCreated(CustomerSnapshot snapshot);

    void handleCustomerUpdated(CustomerSnapshot snapshot);

    void handleCustomerDeleted(Long customerId);
}
