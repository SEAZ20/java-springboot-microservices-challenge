package com.challenge.microservices.account.infrastructure.adapter.output.messaging.consumer;

import com.challenge.microservices.account.domain.model.CustomerSnapshot;
import com.challenge.microservices.account.domain.port.input.CustomerSnapshotUseCase;
import com.challenge.microservices.account.infrastructure.adapter.output.messaging.event.CustomerEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerEventConsumer {

    private final CustomerSnapshotUseCase customerSnapshotUseCase;

    @RabbitListener(queues = "customer.events.queue")
    public void handleCustomerEvent(CustomerEvent event) {
        log.debug("Received customer event: type={}, customerId={}", event.getEventType(), event.getCustomerId());

        switch (event.getEventType()) {
            case "CUSTOMER_CREATED" -> customerSnapshotUseCase.handleCustomerCreated(toSnapshot(event));
            case "CUSTOMER_UPDATED" -> customerSnapshotUseCase.handleCustomerUpdated(toSnapshot(event));
            case "CUSTOMER_DELETED" -> customerSnapshotUseCase.handleCustomerDeleted(event.getCustomerId());
            default -> log.warn("Unknown customer event type: {}", event.getEventType());
        }
    }

    private CustomerSnapshot toSnapshot(CustomerEvent event) {
        return CustomerSnapshot.builder()
                .customerId(event.getCustomerId())
                .name(event.getName())
                .status(event.getStatus())
                .build();
    }
}
