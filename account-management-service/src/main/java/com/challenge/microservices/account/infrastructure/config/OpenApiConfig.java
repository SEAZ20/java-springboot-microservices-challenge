package com.challenge.microservices.account.infrastructure.config;

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
                        .title("Account Management Service API")
                        .description("API REST para la gestión de cuentas y movimientos bancarios. Permite administrar cuentas, registrar depósitos y retiros, y consultar estados de cuenta por rango de fechas.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("TCS Banking Challenge")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Servidor local")
                ));
    }
}
