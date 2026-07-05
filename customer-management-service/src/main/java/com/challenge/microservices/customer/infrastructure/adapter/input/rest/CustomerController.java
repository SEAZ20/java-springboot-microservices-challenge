package com.challenge.microservices.customer.infrastructure.adapter.input.rest;

import com.challenge.microservices.customer.domain.model.Customer;
import com.challenge.microservices.customer.domain.port.input.CustomerUseCase;
import com.challenge.microservices.customer.infrastructure.adapter.input.rest.dto.CustomerRequest;
import com.challenge.microservices.customer.infrastructure.adapter.input.rest.dto.CustomerResponse;
import com.challenge.microservices.customer.infrastructure.adapter.input.rest.mapper.CustomerRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Clientes", description = "Operaciones para la gestión de clientes bancarios")
@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerUseCase customerUseCase;
    private final CustomerRestMapper mapper;

    @Operation(summary = "Crear cliente", description = "Registra un nuevo cliente bancario y publica un evento de creación")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CustomerRequest request) {
        Customer created = customerUseCase.create(mapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
    }

    @Operation(summary = "Listar clientes", description = "Retorna todos los clientes registrados")
    @ApiResponse(responseCode = "200", description = "Lista de clientes")
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> findAll() {
        List<CustomerResponse> customers = customerUseCase.findAll().stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(customers);
    }

    @Operation(summary = "Obtener cliente por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> findById(
            @Parameter(description = "ID del cliente") @PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(customerUseCase.findById(id)));
    }

    @Operation(summary = "Actualizar cliente", description = "Reemplaza todos los campos del cliente y publica un evento de actualización")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(
            @Parameter(description = "ID del cliente") @PathVariable Long id,
            @Valid @RequestBody CustomerRequest request) {
        Customer updated = customerUseCase.update(id, mapper.toDomain(request));
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @Operation(summary = "Actualizar cliente parcialmente", description = "Actualiza solo los campos proporcionados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente actualizado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<CustomerResponse> partialUpdate(
            @Parameter(description = "ID del cliente") @PathVariable Long id,
            @RequestBody CustomerRequest request) {
        Customer updated = customerUseCase.partialUpdate(id, mapper.toDomain(request));
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @Operation(summary = "Eliminar cliente", description = "Elimina el cliente y publica un evento de eliminación")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del cliente") @PathVariable Long id) {
        customerUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}