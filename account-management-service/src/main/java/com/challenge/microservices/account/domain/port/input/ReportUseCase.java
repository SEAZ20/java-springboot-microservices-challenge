package com.challenge.microservices.account.domain.port.input;

import com.challenge.microservices.account.domain.model.StatementEntry;

import java.time.LocalDate;
import java.util.List;

public interface ReportUseCase {

    List<StatementEntry> getStatement(Long customerId, LocalDate startDate, LocalDate endDate);
}
