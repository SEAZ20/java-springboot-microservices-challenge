package com.challenge.microservices.customer.domain.port.output;

import com.challenge.microservices.customer.domain.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepositoryPort {

    Customer save(Customer customer);

    Optional<Customer> findById(Long id);

    List<Customer> findAll();

    boolean existsByIdentification(String identification);

    void delete(Customer customer);
}
