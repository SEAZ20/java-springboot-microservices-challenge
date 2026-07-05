package com.challenge.microservices.account.infrastructure.adapter.output.persistence;

import com.challenge.microservices.account.domain.model.Movement;
import com.challenge.microservices.account.domain.port.output.MovementRepositoryPort;
import com.challenge.microservices.account.infrastructure.adapter.output.persistence.entity.AccountEntity;
import com.challenge.microservices.account.infrastructure.adapter.output.persistence.mapper.MovementPersistenceMapper;
import com.challenge.microservices.account.infrastructure.adapter.output.persistence.repository.JpaAccountRepository;
import com.challenge.microservices.account.infrastructure.adapter.output.persistence.repository.JpaMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MovementPersistenceAdapter implements MovementRepositoryPort {

    private final JpaMovementRepository jpaMovementRepository;
    private final JpaAccountRepository jpaAccountRepository;
    private final MovementPersistenceMapper mapper;

    @Override
    public Movement save(Movement movement) {
        AccountEntity account = jpaAccountRepository.getReferenceById(movement.getAccountId());
        return mapper.toDomain(jpaMovementRepository.save(mapper.toEntity(movement, account)));
    }

    @Override
    public Optional<Movement> findById(Long id) {
        return jpaMovementRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Movement> findAll() {
        return jpaMovementRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Movement> findByCustomerIdAndDateBetween(Long customerId, LocalDate startDate, LocalDate endDate) {
        return jpaMovementRepository.findByCustomerIdAndDateBetween(customerId, startDate, endDate)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
