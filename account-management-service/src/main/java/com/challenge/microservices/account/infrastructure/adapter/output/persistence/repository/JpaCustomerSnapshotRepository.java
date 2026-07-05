package com.challenge.microservices.account.infrastructure.adapter.output.persistence.repository;

import com.challenge.microservices.account.infrastructure.adapter.output.persistence.entity.CustomerSnapshotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCustomerSnapshotRepository extends JpaRepository<CustomerSnapshotEntity, Long> {
}
