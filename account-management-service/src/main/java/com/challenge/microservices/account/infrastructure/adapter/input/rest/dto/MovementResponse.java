package com.challenge.microservices.account.infrastructure.adapter.input.rest.dto;

import com.challenge.microservices.account.domain.model.MovementType;
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
@Schema(description = "Datos de salida de un movimiento de cuenta.")
public class MovementResponse {

    @Schema(description = "Identificador unico del movimiento.", example = "10")
    private Long id;

    @Schema(description = "Fecha en la que se registro el movimiento.", example = "2026-07-04")
    private LocalDate date;

    @Schema(description = "Tipo de movimiento.", example = "DEBITO")
    private MovementType movementType;

    @Schema(description = "Valor del movimiento.", example = "-50.00")
    private BigDecimal value;

    @Schema(description = "Saldo de la cuenta despues del movimiento.", example = "1450.00")
    private BigDecimal balance;

    @Schema(description = "Identificador de la cuenta asociada.", example = "1")
    private Long accountId;

    @Schema(description = "Numero de cuenta asociada.", example = "478758")
    private String accountNumber;
}
