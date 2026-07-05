package com.challenge.microservices.account.domain.port.output;

import com.challenge.microservices.account.domain.model.Movement;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MovementRepositoryPort {

    Movement save(Movement movement);

    Optional<Movement> findById(Long id);

    List<Movement> findAll();

    List<Movement> findByCustomerIdAndDateBetween(Long customerId, LocalDate startDate, LocalDate endDate);
}
