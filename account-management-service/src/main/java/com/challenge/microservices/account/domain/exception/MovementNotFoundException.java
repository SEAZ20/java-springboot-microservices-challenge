package com.challenge.microservices.account.domain.exception;

public class MovementNotFoundException extends RuntimeException {

    public MovementNotFoundException(Long id) {
        super("Movimiento no encontrado con id: " + id);
    }
}
