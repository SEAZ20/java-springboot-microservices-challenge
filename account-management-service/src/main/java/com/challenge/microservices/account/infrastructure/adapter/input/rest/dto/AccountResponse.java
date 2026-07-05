package com.challenge.microservices.account.infrastructure.adapter.input.rest.dto;

import com.challenge.microservices.account.domain.model.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de salida de una cuenta.")
public class AccountResponse {

    @Schema(description = "Identificador unico de la cuenta.", example = "1")
    private Long id;

    @Schema(description = "Numero unico de la cuenta.", example = "478758")
    private String accountNumber;

    @Schema(description = "Tipo de cuenta.", example = "AHORROS")
    private AccountType accountType;

    @Schema(description = "Saldo inicial registrado para la cuenta.", example = "1500.00")
    private BigDecimal initialBalance;

    @Schema(description = "Saldo actual disponible en la cuenta.", example = "1325.50")
    private BigDecimal currentBalance;

    @Schema(description = "Estado de la cuenta: true activa, false inactiva.", example = "true")
    private Boolean status;

    @Schema(description = "Identificador del cliente propietario de la cuenta.", example = "1")
    private Long customerId;
}
