package com.challenge.microservices.account.infrastructure.adapter.input.rest.dto;

import com.challenge.microservices.account.domain.model.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de entrada para crear o actualizar una cuenta.")
public class AccountRequest {

    @Schema(description = "Numero unico de la cuenta.", example = "478758")
    @NotBlank(message = "El numero de cuenta es obligatorio")
    private String accountNumber;

    @Schema(description = "Tipo de cuenta.", example = "AHORROS")
    @NotNull(message = "El tipo de cuenta es obligatorio")
    private AccountType accountType;

    @Schema(description = "Saldo inicial de la cuenta.", example = "1500.00")
    @NotNull(message = "El saldo inicial es obligatorio")
    @DecimalMin(value = "0.00", message = "El saldo inicial debe ser mayor o igual a 0.00")
    private BigDecimal initialBalance;

    @Schema(description = "Estado de la cuenta: true activa, false inactiva.", example = "true")
    @NotNull(message = "El estado de la cuenta es obligatorio")
    private Boolean status;

    @Schema(description = "Identificador del cliente propietario de la cuenta.", example = "1")
    @NotNull(message = "El identificador del cliente es obligatorio")
    private Long customerId;
}
