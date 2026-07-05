package com.challenge.microservices.account.domain.port.input;

import com.challenge.microservices.account.domain.model.Movement;

import java.util.List;

public interface MovementUseCase {

    Movement create(Movement movement);

    Movement findById(Long id);

    List<Movement> findAll();

    Movement update(Long id, Movement movement);
}
