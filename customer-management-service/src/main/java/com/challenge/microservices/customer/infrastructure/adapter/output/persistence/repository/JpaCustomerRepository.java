package com.challenge.microservices.customer.infrastructure.adapter.output.persistence.repository;

import com.challenge.microservices.customer.infrastructure.adapter.output.persistence.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCustomerRepository extends JpaRepository<CustomerEntity, Long> {

    boolean existsByIdentification(String identification);
}
