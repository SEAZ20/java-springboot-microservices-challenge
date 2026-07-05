package com.challenge.microservices.customer.application.usecase;

import com.challenge.microservices.customer.domain.exception.CustomerAlreadyExistsException;
import com.challenge.microservices.customer.domain.exception.CustomerNotFoundException;
import com.challenge.microservices.customer.domain.model.Customer;
import com.challenge.microservices.customer.domain.port.input.CustomerUseCase;
import com.challenge.microservices.customer.domain.port.output.CustomerEventPublisherPort;
import com.challenge.microservices.customer.domain.port.output.CustomerRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerUseCaseImpl implements CustomerUseCase {

    private final CustomerRepositoryPort customerRepository;
    private final CustomerEventPublisherPort eventPublisher;

    @Override
    @Transactional
    public Customer create(Customer customer) {
        log.info("Iniciando creación de cliente: identification={}", customer.getIdentification());
        if (customerRepository.existsByIdentification(customer.getIdentification())) {
            log.warn("Ya existe un cliente con la identificación: {}", customer.getIdentification());
            throw new CustomerAlreadyExistsException(customer.getIdentification());
        }
        customer.setClientId(UUID.randomUUID().toString());
        Customer saved = customerRepository.save(customer);
        eventPublisher.publishCreated(saved);
        log.info("Cliente creado exitosamente: id={}, clientId={}", saved.getId(), saved.getClientId());
        return saved;
    }

    @Override
    public Customer findById(Long id) {
        log.debug("Buscando cliente por id={}", id);
        return customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cliente no encontrado: id={}", id);
                    return new CustomerNotFoundException(id);
                });
    }

    @Override
    public List<Customer> findAll() {
        log.debug("Consultando todos los clientes");
        List<Customer> result = customerRepository.findAll();
        log.debug("Total de clientes encontrados: {}", result.size());
        return result;
    }

    @Override
    @Transactional
    public Customer update(Long id, Customer customer) {
        log.info("Iniciando actualización completa de cliente: id={}", id);
        Customer existing = findById(id);
        customer.setId(existing.getId());
        customer.setClientId(existing.getClientId());
        Customer saved = customerRepository.save(customer);
        eventPublisher.publishUpdated(saved);
        log.info("Cliente actualizado exitosamente: id={}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public Customer partialUpdate(Long id, Customer customer) {
        log.info("Iniciando actualización parcial de cliente: id={}", id);
        Customer existing = findById(id);
        applyPartialUpdate(existing, customer);
        Customer saved = customerRepository.save(existing);
        eventPublisher.publishUpdated(saved);
        log.info("Cliente actualizado parcialmente: id={}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Iniciando eliminación de cliente: id={}", id);
        Customer existing = findById(id);
        customerRepository.delete(existing);
        eventPublisher.publishDeleted(existing);
        log.info("Cliente eliminado exitosamente: id={}", id);
    }

    private void applyPartialUpdate(Customer target, Customer source) {
        if (source.getName() != null) target.setName(source.getName());
        if (source.getGender() != null) target.setGender(source.getGender());
        if (source.getAge() != null) target.setAge(source.getAge());
        if (source.getIdentification() != null) target.setIdentification(source.getIdentification());
        if (source.getAddress() != null) target.setAddress(source.getAddress());
        if (source.getPhone() != null) target.setPhone(source.getPhone());
        if (source.getPassword() != null) target.setPassword(source.getPassword());
        if (source.getStatus() != null) target.setStatus(source.getStatus());
    }
}
