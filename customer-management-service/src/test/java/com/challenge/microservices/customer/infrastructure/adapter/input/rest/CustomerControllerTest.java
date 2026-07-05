package com.challenge.microservices.customer.infrastructure.adapter.input.rest;

import com.challenge.microservices.customer.domain.exception.CustomerNotFoundException;
import com.challenge.microservices.customer.domain.model.Customer;
import com.challenge.microservices.customer.domain.port.input.CustomerUseCase;
import com.challenge.microservices.customer.infrastructure.adapter.input.rest.dto.CustomerRequest;
import com.challenge.microservices.customer.infrastructure.adapter.input.rest.mapper.CustomerRestMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerUseCase customerUseCase;

    @MockitoBean
    private CustomerRestMapper mapper;

    @Test
    void create_withValidRequest_shouldReturn201() throws Exception {
        CustomerRequest request = CustomerRequest.builder()
                .name("Jose Lema")
                .identification("1234567890")
                .password("secret")
                .status(true)
                .build();

        Customer domainCustomer = Customer.builder().id(1L).name("Jose Lema").build();

        when(mapper.toDomain(any())).thenReturn(domainCustomer);
        when(customerUseCase.create(any())).thenReturn(domainCustomer);
        when(mapper.toResponse(any())).thenCallRealMethod();

        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void create_withMissingName_shouldReturn400() throws Exception {
        CustomerRequest request = CustomerRequest.builder()
                .identification("1234567890")
                .password("secret")
                .status(true)
                .build();

        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findById_whenNotFound_shouldReturn404() throws Exception {
        when(customerUseCase.findById(999L)).thenThrow(new CustomerNotFoundException(999L));

        mockMvc.perform(get("/api/v1/clientes/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cliente no encontrado con id: 999"));
    }
}
