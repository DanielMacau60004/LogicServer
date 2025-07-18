package com.logic.server.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Logic Tool")
                        .version("1.0")
                        .description("API for managing the Logic Tool")
                        .contact(new Contact()
                                .name("Daniel Macau")
                                .email("dmacau@campus.fct.unl.pt")
                        )
                );
    }
}
