package com.challenge.microservices.customer.infrastructure.adapter.input.rest.mapper;

import com.challenge.microservices.customer.domain.model.Customer;
import com.challenge.microservices.customer.infrastructure.adapter.input.rest.dto.CustomerRequest;
import com.challenge.microservices.customer.infrastructure.adapter.input.rest.dto.CustomerResponse;
import org.springframework.stereotype.Component;

@Component
public class CustomerRestMapper {

    public Customer toDomain(CustomerRequest request) {
        return Customer.builder()
                .name(request.getName())
                .gender(request.getGender())
                .age(request.getAge())
                .identification(request.getIdentification())
                .address(request.getAddress())
                .phone(request.getPhone())
                .password(request.getPassword())
                .status(request.getStatus())
                .build();
    }

    public CustomerResponse toResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .gender(customer.getGender())
                .age(customer.getAge())
                .identification(customer.getIdentification())
                .address(customer.getAddress())
                .phone(customer.getPhone())
                .status(customer.getStatus())
                .build();
    }
}
