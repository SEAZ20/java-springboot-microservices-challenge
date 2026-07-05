package com.challenge.microservices.customer.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Customer Management Service API")
                        .description("API REST para la gestión de clientes bancarios. Permite crear, consultar, actualizar y eliminar clientes, así como publicar eventos de dominio hacia otros servicios.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("TCS Banking Challenge")))
                .servers(List.of(
                        new Server().url("http://localhost:8081").description("Servidor local")
                ));
    }
}
