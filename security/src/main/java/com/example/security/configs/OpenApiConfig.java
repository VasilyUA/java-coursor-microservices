package com.example.security.configs;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.responses.*;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.*;

import java.util.ArrayList;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customizeOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .paths(new Paths()
                        .addPathItem("/", unsecuredEndpoint(PathItem.HttpMethod.GET, "200"))
                        .addPathItem("/api", unsecuredEndpoint(PathItem.HttpMethod.GET, "200"))
                        .addPathItem("/api/auth/register", unsecuredEndpoint(PathItem.HttpMethod.POST, "201"))
                        .addPathItem("/api/auth/authenticate", unsecuredEndpoint(PathItem.HttpMethod.POST, "200"))
                        .addPathItem("/api/health-no-sql-endpoint", unsecuredEndpoint(PathItem.HttpMethod.GET, "200"))
                );
    }

    private PathItem unsecuredEndpoint(PathItem.HttpMethod httpMethod, String statusCode) {
        PathItem pathItem = new PathItem();
        Operation operation = new Operation();

        ApiResponses apiResponses = new ApiResponses();
        apiResponses.addApiResponse(statusCode, new ApiResponse().description("OK"));

        operation.setResponses(apiResponses);
        operation.setSecurity(new ArrayList<>());

        switch (httpMethod) {
            case GET -> pathItem.setGet(operation);
            case POST -> pathItem.setPost(operation);
            case PUT -> pathItem.setPut(operation);
            case DELETE -> pathItem.setDelete(operation);
        }

        return pathItem;
    }
}
