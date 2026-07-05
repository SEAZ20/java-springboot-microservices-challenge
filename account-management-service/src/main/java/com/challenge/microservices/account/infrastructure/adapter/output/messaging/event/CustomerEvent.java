package com.challenge.microservices.account.infrastructure.adapter.output.messaging.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerEvent {

    private String eventType;
    private Long customerId;
    private String name;
    private Boolean status;
}
