package com.caju.cjauthorization.infrastructure.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SwaggerConfig {

    @Bean
    fun publicApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("transaction-service")
            .pathsToMatch("/transactions/**") // A URL de todos os endpoints que serão documentados
            .build()
    }

    @Bean
    fun customOpenAPI(
        @Value("\${application-description}") appDescription: String?,
        @Value("\${application-version}") appVersion: String?
    ): OpenAPI {
        val contact = Contact()
        contact.email = "suporte@caju.com"
        contact.name = "Equipe Caju"
        contact.url = "https://www.caju.com"

        return OpenAPI()
            .info(
                Info()
                    .title("API de Autorização de Transações")
                    .version(appVersion ?: "1.0.0") // Versão de fallback
                    .description(appDescription ?: "API para autorizar e gerenciar transações.")
                    .termsOfService("http://swagger.io/terms/")
                    .license(License().name("Apache 2.0").url("http://springdoc.org"))
                    .contact(contact)
            )
    }
}