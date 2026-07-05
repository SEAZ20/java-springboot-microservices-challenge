package com.challenge.microservices.customer.domain.exception;

public class CustomerAlreadyExistsException extends RuntimeException {

    public CustomerAlreadyExistsException(String identification) {
        super("Ya existe un cliente con la identificación: " + identification);
    }
}
