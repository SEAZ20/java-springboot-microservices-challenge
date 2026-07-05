package com.challenge.microservices.account.infrastructure.adapter.input.rest;

import com.challenge.microservices.account.domain.model.Movement;
import com.challenge.microservices.account.domain.port.input.MovementUseCase;
import com.challenge.microservices.account.infrastructure.adapter.input.rest.dto.MovementRequest;
import com.challenge.microservices.account.infrastructure.adapter.input.rest.dto.MovementResponse;
import com.challenge.microservices.account.infrastructure.adapter.input.rest.mapper.MovementRestMapper;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Movimientos", description = "Operaciones para registrar y consultar movimientos bancarios")
@RestController
@RequestMapping("/api/v1/movimientos")
@RequiredArgsConstructor
public class MovementController {

    private final MovementUseCase movementUseCase;
    private final MovementRestMapper mapper;

    @Operation(summary = "Registrar movimiento", description = "Registra un depósito (valor positivo) o retiro (valor negativo) en una cuenta")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Movimiento registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada"),
            @ApiResponse(responseCode = "422", description = "Saldo no disponible")
    })
    @PostMapping
    public ResponseEntity<MovementResponse> create(@Valid @RequestBody MovementRequest request) {
        Movement created = movementUseCase.create(mapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
    }

    @Operation(summary = "Listar movimientos", description = "Retorna todos los movimientos registrados")
    @ApiResponse(responseCode = "200", description = "Lista de movimientos")
    @GetMapping
    public ResponseEntity<List<MovementResponse>> findAll() {
        List<MovementResponse> movements = movementUseCase.findAll().stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(movements);
    }

    @Operation(summary = "Obtener movimiento por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimiento encontrado"),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MovementResponse> findById(
            @Parameter(description = "ID del movimiento") @PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(movementUseCase.findById(id)));
    }

    @Operation(summary = "Actualizar fecha de movimiento")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimiento actualizado"),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MovementResponse> update(
            @Parameter(description = "ID del movimiento") @PathVariable Long id,
            @RequestBody MovementRequest request) {
        Movement updated = movementUseCase.update(id, mapper.toDomain(request));
        return ResponseEntity.ok(mapper.toResponse(updated));
    }
}