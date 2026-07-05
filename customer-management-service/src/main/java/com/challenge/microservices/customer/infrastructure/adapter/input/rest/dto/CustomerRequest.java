package com.challenge.microservices.customer.infrastructure.adapter.input.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Payload de solicitud para creación o actualización de cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRequest {

    @Schema(description = "Nombre completo del cliente", example = "Jane Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "name is required")
    private String name;

    @Schema(description = "Género del cliente", example = "FEMALE")
    private String gender;

    @Schema(description = "Edad del cliente", example = "30", minimum = "0")
    @Min(value = 0, message = "age must be greater than or equal to 0")
    private Integer age;

    @Schema(description = "Número de identificación del cliente", example = "0102030405", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "identification is required")
    private String identification;

    @Schema(description = "Dirección del cliente", example = "742 Evergreen Terrace")
    private String address;

    @Schema(description = "Número de teléfono del cliente", example = "+1-555-123-4567")
    private String phone;

    @Schema(description = "Contraseña de acceso del cliente", example = "Secret123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "password is required")
    private String password;

    @Schema(description = "Estado del cliente", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "status is required")
    private Boolean status;
}