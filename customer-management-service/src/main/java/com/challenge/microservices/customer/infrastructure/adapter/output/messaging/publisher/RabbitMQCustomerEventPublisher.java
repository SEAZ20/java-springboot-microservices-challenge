package com.challenge.microservices.customer.infrastructure.adapter.output.messaging.publisher;

import com.challenge.microservices.customer.domain.model.Customer;
import com.challenge.microservices.customer.domain.port.output.CustomerEventPublisherPort;
import com.challenge.microservices.customer.infrastructure.adapter.output.messaging.event.CustomerEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQCustomerEventPublisher implements CustomerEventPublisherPort {

    private static final String EXCHANGE = "banking.events";
    private static final String ROUTING_KEY = "customer.event";

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishCreated(Customer customer) {
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, toEvent("CUSTOMER_CREATED", customer));
    }

    @Override
    public void publishUpdated(Customer customer) {
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, toEvent("CUSTOMER_UPDATED", customer));
    }

    @Override
    public void publishDeleted(Customer customer) {
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, toEvent("CUSTOMER_DELETED", customer));
    }

    private CustomerEvent toEvent(String eventType, Customer customer) {
        return CustomerEvent.builder()
                .eventType(eventType)
                .customerId(customer.getId())
                .name(customer.getName())
                .status(customer.getStatus())
                .build();
    }
}
