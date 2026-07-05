package com.challenge.microservices.customer.infrastructure.adapter.output.persistence;

import com.challenge.microservices.customer.domain.model.Customer;
import com.challenge.microservices.customer.domain.port.output.CustomerRepositoryPort;
import com.challenge.microservices.customer.infrastructure.adapter.output.persistence.mapper.CustomerPersistenceMapper;
import com.challenge.microservices.customer.infrastructure.adapter.output.persistence.repository.JpaCustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerPersistenceAdapter implements CustomerRepositoryPort {

    private final JpaCustomerRepository jpaRepository;
    private final CustomerPersistenceMapper mapper;

    @Override
    public Customer save(Customer customer) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(customer)));
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Customer> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByIdentification(String identification) {
        return jpaRepository.existsByIdentification(identification);
    }

    @Override
    public void delete(Customer customer) {
        jpaRepository.deleteById(customer.getId());
    }
}
