package com.challenge.microservices.customer.infrastructure.adapter.output.persistence.mapper;

import com.challenge.microservices.customer.domain.model.Customer;
import com.challenge.microservices.customer.infrastructure.adapter.output.persistence.entity.CustomerEntity;
import org.springframework.stereotype.Component;

@Component
public class CustomerPersistenceMapper {

    public Customer toDomain(CustomerEntity entity) {
        return Customer.builder()
                .id(entity.getId())
                .name(entity.getName())
                .gender(entity.getGender())
                .age(entity.getAge())
                .identification(entity.getIdentification())
                .address(entity.getAddress())
                .phone(entity.getPhone())
                .clientId(entity.getClientId())
                .password(entity.getPassword())
                .status(entity.getStatus())
                .build();
    }

    public CustomerEntity toEntity(Customer customer) {
        return CustomerEntity.builder()
                .id(customer.getId())
                .name(customer.getName())
                .gender(customer.getGender())
                .age(customer.getAge())
                .identification(customer.getIdentification())
                .address(customer.getAddress())
                .phone(customer.getPhone())
                .clientId(customer.getClientId())
                .password(customer.getPassword())
                .status(customer.getStatus())
                .build();
    }
}
