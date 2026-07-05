package com.challenge.microservices.account.infrastructure.adapter.input.rest.dto;

import com.challenge.microservices.account.domain.model.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de salida para un registro del estado de cuenta.")
public class StatementResponse {

    @Schema(description = "Fecha del movimiento registrado.", example = "2026-07-04")
    private LocalDate date;

    @Schema(description = "Nombre del cliente titular de la cuenta.", example = "Jose Perez")
    private String customerName;

    @Schema(description = "Numero de cuenta.", example = "478758")
    private String accountNumber;

    @Schema(description = "Tipo de cuenta.", example = "AHORROS")
    private AccountType accountType;

    @Schema(description = "Saldo inicial de la cuenta.", example = "1500.00")
    private BigDecimal initialBalance;

    @Schema(description = "Estado de la cuenta: true activa, false inactiva.", example = "true")
    private Boolean status;

    @Schema(description = "Valor del movimiento registrado.", example = "-50.00")
    private BigDecimal movementValue;

    @Schema(description = "Saldo disponible luego del movimiento.", example = "1450.00")
    private BigDecimal availableBalance;
}
