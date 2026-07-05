package com.challenge.microservices.account.infrastructure.adapter.input.rest;

import com.challenge.microservices.account.domain.port.input.ReportUseCase;
import com.challenge.microservices.account.infrastructure.adapter.input.rest.dto.StatementResponse;
import com.challenge.microservices.account.infrastructure.adapter.input.rest.mapper.MovementRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Reportes", description = "Generación de estado de cuenta por cliente y rango de fechas")
@RestController
@RequestMapping("/api/v1/reportes")
@RequiredArgsConstructor
public class ReportController {

    private final ReportUseCase reportUseCase;
    private final MovementRestMapper mapper;

    @Operation(
            summary = "Estado de cuenta",
            description = "Retorna el estado de cuenta de un cliente en un rango de fechas, incluyendo todos sus movimientos y saldos resultantes"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado de cuenta generado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping
    public ResponseEntity<List<StatementResponse>> getStatement(
            @Parameter(description = "ID del cliente", required = true) @RequestParam Long cliente,
            @Parameter(description = "Fecha de inicio (yyyy-MM-dd)", required = true)
                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "Fecha de fin (yyyy-MM-dd)", required = true)
                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<StatementResponse> statement = reportUseCase.getStatement(cliente, startDate, endDate)
                .stream()
                .map(mapper::toStatementResponse)
                .toList();

        return ResponseEntity.ok(statement);
    }
}
