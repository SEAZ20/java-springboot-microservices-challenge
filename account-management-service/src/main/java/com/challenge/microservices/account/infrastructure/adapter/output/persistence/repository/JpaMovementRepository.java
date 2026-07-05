package com.challenge.microservices.account.infrastructure.adapter.output.persistence.repository;

import com.challenge.microservices.account.infrastructure.adapter.output.persistence.entity.MovementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface JpaMovementRepository extends JpaRepository<MovementEntity, Long> {

    @Query("SELECT m FROM MovementEntity m JOIN FETCH m.account a WHERE a.customerId = :customerId AND m.date BETWEEN :startDate AND :endDate")
    List<MovementEntity> findByCustomerIdAndDateBetween(
            @Param("customerId") Long customerId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
