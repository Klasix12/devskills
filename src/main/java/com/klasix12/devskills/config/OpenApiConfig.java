package com.klasix12.devskills.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Devskills api",
                version = "1.0",
                description = "Devskills REST Api description",
                contact = @Contact(name = "Artem", email = "sunlightpattern@yandex.ru")
        )
)
@Configuration
public class OpenApiConfig {
}
