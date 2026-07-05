package com.challenge.microservices.customer.domain.port.output;

import com.challenge.microservices.customer.domain.model.Customer;

public interface CustomerEventPublisherPort {

    void publishCreated(Customer customer);

    void publishUpdated(Customer customer);

    void publishDeleted(Customer customer);
}
