package com.challenge.microservices.customer.domain.port.input;

import com.challenge.microservices.customer.domain.model.Customer;

import java.util.List;

public interface CustomerUseCase {

    Customer create(Customer customer);

    Customer findById(Long id);

    List<Customer> findAll();

    Customer update(Long id, Customer customer);

    Customer partialUpdate(Long id, Customer customer);

    void delete(Long id);
}
