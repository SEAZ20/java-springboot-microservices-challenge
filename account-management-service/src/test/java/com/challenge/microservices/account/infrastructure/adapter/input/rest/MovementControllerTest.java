package com.challenge.microservices.account.infrastructure.adapter.input.rest;

import com.challenge.microservices.account.domain.exception.AccountNotFoundException;
import com.challenge.microservices.account.domain.exception.InsufficientBalanceException;
import com.challenge.microservices.account.domain.model.Movement;
import com.challenge.microservices.account.domain.model.MovementType;
import com.challenge.microservices.account.domain.port.input.MovementUseCase;
import com.challenge.microservices.account.infrastructure.adapter.input.rest.dto.MovementRequest;
import com.challenge.microservices.account.infrastructure.adapter.input.rest.mapper.MovementRestMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovementController.class)
class MovementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MovementUseCase movementUseCase;

    @MockitoBean
    private MovementRestMapper mapper;

    @Test
    void create_withValidRequest_shouldReturn201() throws Exception {
        MovementRequest request = MovementRequest.builder()
                .value(new BigDecimal("500.00"))
                .accountNumber("478758")
                .build();
        Movement created = Movement.builder().id(1L).movementType(MovementType.DEPOSIT).build();

        when(mapper.toDomain(any())).thenReturn(Movement.builder().build());
        when(movementUseCase.create(any())).thenReturn(created);
        when(mapper.toResponse(any())).thenCallRealMethod();

        mockMvc.perform(post("/api/v1/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void create_withInsufficientBalance_shouldReturn422() throws Exception {
        MovementRequest request = MovementRequest.builder()
                .value(new BigDecimal("-99999.00"))
                                .accountNumber("478758")
                .build();

        when(mapper.toDomain(any())).thenReturn(Movement.builder().build());
        when(movementUseCase.create(any())).thenThrow(new InsufficientBalanceException());

        mockMvc.perform(post("/api/v1/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.message").value("Saldo no disponible"));
    }

    @Test
    void create_withInvalidAccountNumber_shouldReturn404() throws Exception {
        MovementRequest request = MovementRequest.builder()
                .value(new BigDecimal("100.00"))
                .accountNumber("999999")
                .build();

        when(mapper.toDomain(any())).thenReturn(Movement.builder().build());
        when(movementUseCase.create(any())).thenThrow(new AccountNotFoundException("999999"));

        mockMvc.perform(post("/api/v1/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}
