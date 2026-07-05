package com.challenge.microservices.account.infrastructure.adapter.input.rest;

import com.challenge.microservices.account.domain.model.Account;
import com.challenge.microservices.account.domain.port.input.AccountUseCase;
import com.challenge.microservices.account.infrastructure.adapter.input.rest.dto.AccountRequest;
import com.challenge.microservices.account.infrastructure.adapter.input.rest.dto.AccountResponse;
import com.challenge.microservices.account.infrastructure.adapter.input.rest.mapper.AccountRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Cuentas", description = "Operaciones para la gestión de cuentas bancarias")
@RestController
@RequestMapping("/api/v1/cuentas")
@RequiredArgsConstructor
public class AccountController {

    private final AccountUseCase accountUseCase;
    private final AccountRestMapper mapper;

    @Operation(summary = "Crear cuenta", description = "Registra una nueva cuenta bancaria. El saldo actual se inicializa con el saldo inicial.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cuenta creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody AccountRequest request) {
        Account created = accountUseCase.create(mapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
    }

    @Operation(summary = "Listar cuentas", description = "Retorna todas las cuentas bancarias registradas")
    @ApiResponse(responseCode = "200", description = "Lista de cuentas")
    @GetMapping
    public ResponseEntity<List<AccountResponse>> findAll() {
        List<AccountResponse> accounts = accountUseCase.findAll().stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Obtener cuenta por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cuenta encontrada"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> findById(
            @Parameter(description = "ID de la cuenta") @PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(accountUseCase.findById(id)));
    }

    @Operation(summary = "Actualizar cuenta", description = "Reemplaza todos los campos editables de la cuenta")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cuenta actualizada"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> update(
            @Parameter(description = "ID de la cuenta") @PathVariable Long id,
            @Valid @RequestBody AccountRequest request) {
        Account updated = accountUseCase.update(id, mapper.toDomain(request));
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @Operation(summary = "Actualizar cuenta parcialmente", description = "Actualiza solo los campos proporcionados (número, tipo, estado)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cuenta actualizada"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<AccountResponse> partialUpdate(
            @Parameter(description = "ID de la cuenta") @PathVariable Long id,
            @RequestBody AccountRequest request) {
        Account updated = accountUseCase.partialUpdate(id, mapper.toDomain(request));
        return ResponseEntity.ok(mapper.toResponse(updated));
    }
}
