package com.challenge.microservices.customer.infrastructure.adapter.input.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "CustomerResponse", description = "Carga útil de respuesta del cliente")
public class CustomerResponse {

    @Schema(description = "Identificador del cliente", example = "1")
    private Long id;

    @Schema(description = "Nombre completo del cliente", example = "María Silva")
    private String name;

    @Schema(description = "Género del cliente", example = "FEMENINO")
    private String gender;

    @Schema(description = "Edad del cliente", example = "32")
    private Integer age;

    @Schema(description = "Documento de identificación del cliente", example = "1234567890")
    private String identification;

    @Schema(description = "Dirección del cliente", example = "Calle Principal 123")
    private String address;

    @Schema(description = "Número de teléfono del cliente", example = "+15551234567")
    private String phone;

    @Schema(description = "Estado activo del cliente", example = "true")
    private Boolean status;
}
