package com.challenge.microservices.account.infrastructure.adapter.output.persistence.mapper;

import com.challenge.microservices.account.domain.model.Movement;
import com.challenge.microservices.account.infrastructure.adapter.output.persistence.entity.AccountEntity;
import com.challenge.microservices.account.infrastructure.adapter.output.persistence.entity.MovementEntity;
import org.springframework.stereotype.Component;

@Component
public class MovementPersistenceMapper {

    public Movement toDomain(MovementEntity entity) {
        AccountEntity account = entity.getAccount();
        return Movement.builder()
                .id(entity.getId())
                .date(entity.getDate())
                .movementType(entity.getMovementType())
                .value(entity.getValue())
                .balance(entity.getBalance())
                .accountId(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .accountInitialBalance(account.getInitialBalance())
                .accountStatus(account.getStatus())
                .build();
    }

    public MovementEntity toEntity(Movement movement, AccountEntity account) {
        return MovementEntity.builder()
                .id(movement.getId())
                .date(movement.getDate())
                .movementType(movement.getMovementType())
                .value(movement.getValue())
                .balance(movement.getBalance())
                .account(account)
                .build();
    }
}
