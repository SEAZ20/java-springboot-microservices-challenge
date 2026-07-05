package com.challenge.microservices.customer.application.usecase;

import com.challenge.microservices.customer.domain.exception.CustomerNotFoundException;
import com.challenge.microservices.customer.domain.model.Customer;
import com.challenge.microservices.customer.domain.port.output.CustomerEventPublisherPort;
import com.challenge.microservices.customer.domain.port.output.CustomerRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerUseCaseImplTest {

    @Mock
    private CustomerRepositoryPort customerRepository;

    @Mock
    private CustomerEventPublisherPort eventPublisher;

    @InjectMocks
    private CustomerUseCaseImpl useCase;

    @Test
    void create_shouldAssignClientIdAndPublishEvent() {
        Customer request = Customer.builder().name("Jose Lema").identification("1234").status(true).build();
        Customer saved = Customer.builder().id(1L).name("Jose Lema").identification("1234").status(true).clientId("uuid-1").build();
        when(customerRepository.save(any())).thenReturn(saved);

        Customer result = useCase.create(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getClientId()).isEqualTo("uuid-1");
        verify(eventPublisher).publishCreated(saved);
    }

    @Test
    void findById_whenNotFound_shouldThrowCustomerNotFoundException() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.findById(99L))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void findAll_shouldReturnAllCustomers() {
        List<Customer> customers = List.of(
                Customer.builder().id(1L).name("Ana").build(),
                Customer.builder().id(2L).name("Luis").build()
        );
        when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> result = useCase.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void delete_shouldDeleteAndPublishEvent() {
        Customer existing = Customer.builder().id(1L).name("Juan").status(true).build();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(existing));

        useCase.delete(1L);

        verify(customerRepository).delete(existing);
        verify(eventPublisher).publishDeleted(existing);
    }

    @Test
    void partialUpdate_shouldOnlyUpdateProvidedFields() {
        Customer existing = Customer.builder().id(1L).name("Original").phone("111").status(true).build();
        Customer patch = Customer.builder().phone("999").build();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(customerRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Customer result = useCase.partialUpdate(1L, patch);

        assertThat(result.getPhone()).isEqualTo("999");
        assertThat(result.getName()).isEqualTo("Original");
        verify(eventPublisher).publishUpdated(any());
    }
}
