package com.felixnguyen.dreamshops.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Dream Shops API")
            .description("E-commerce REST API built with Spring Boot 3")
            .version("1.0.0")
            .contact(new Contact()
                .name("Felix Nguyen")
                .email("felix_nguyen@gmail.com")
                .url("https://github.com/felixnguyen"))
            .license(new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0")))
        .servers(List.of(
            new Server().url("http://localhost:9191").description("Local Server"),
            new Server().url("https://api.dreamshops.com").description("Production Server")));
  }
}
