package com.daimainardi.desafiovotacao.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfigurations {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Voting challenge ")
                        .description("Voting challenge  application, responsible for the management of agendas, sessions, votes and voteResults")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Developer Daiane Mainardi")
                                .email("daimainardi@gmail.com")));
    }
}
