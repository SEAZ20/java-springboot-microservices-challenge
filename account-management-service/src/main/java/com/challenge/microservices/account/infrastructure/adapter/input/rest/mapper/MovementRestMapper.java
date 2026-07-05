package com.challenge.microservices.account.infrastructure.adapter.input.rest.mapper;

import com.challenge.microservices.account.domain.model.Movement;
import com.challenge.microservices.account.domain.model.StatementEntry;
import com.challenge.microservices.account.infrastructure.adapter.input.rest.dto.MovementRequest;
import com.challenge.microservices.account.infrastructure.adapter.input.rest.dto.MovementResponse;
import com.challenge.microservices.account.infrastructure.adapter.input.rest.dto.StatementResponse;
import org.springframework.stereotype.Component;

@Component
public class MovementRestMapper {

    public Movement toDomain(MovementRequest request) {
        return Movement.builder()
                .date(request.getDate())
                .value(request.getValue())
                .accountNumber(request.getAccountNumber())
                .build();
    }

    public MovementResponse toResponse(Movement movement) {
        return MovementResponse.builder()
                .id(movement.getId())
                .date(movement.getDate())
                .movementType(movement.getMovementType())
                .value(movement.getValue())
                .balance(movement.getBalance())
                .accountId(movement.getAccountId())
                .accountNumber(movement.getAccountNumber())
                .build();
    }

    public StatementResponse toStatementResponse(StatementEntry entry) {
        return StatementResponse.builder()
                .date(entry.getDate())
                .customerName(entry.getCustomerName())
                .accountNumber(entry.getAccountNumber())
                .accountType(entry.getAccountType())
                .initialBalance(entry.getInitialBalance())
                .status(entry.getAccountStatus())
                .movementValue(entry.getMovementValue())
                .availableBalance(entry.getAvailableBalance())
                .build();
    }
}
