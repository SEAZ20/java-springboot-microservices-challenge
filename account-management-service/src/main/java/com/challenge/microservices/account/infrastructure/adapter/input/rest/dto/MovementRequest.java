package com.challenge.microservices.account.infrastructure.adapter.input.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de entrada para registrar un movimiento.")
public class MovementRequest {

    @Schema(description = "Fecha del movimiento. Si no se envia, el sistema usa la fecha actual.", example = "2026-07-04")
    private LocalDate date;

    @Schema(description = "Valor del movimiento. Puede ser positivo o negativo.", example = "-50.00")
    @NotNull(message = "El valor del movimiento es obligatorio")
    private BigDecimal value;

    @Schema(description = "Numero de la cuenta asociada al movimiento.", example = "478758")
    @NotBlank(message = "El numero de cuenta es obligatorio")
    private String accountNumber;
}
