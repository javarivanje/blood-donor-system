package com.bds.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "mlsbace",
                        email = "mls.bctc@gmail.com"
                ),
                version = "0.0.1",
                title = "OpenApi Blood Donor System - mlsbace",
                description = "OpenApi documentation for Blood Donor system Application " +
                        "used for tracking blood donations, " +
                        "available blood units  and blood donation events",
                license = @License(
                        name = "Blood Donor System License",
                        url = "mls.bctc@gmail.com"
                ),
                termsOfService = "Under my terms ONLY"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8081"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "OAuth2 - username/password")
        }
)
@SecurityScheme(
        name = "OAuth2 - username/password",
        description = "OAuth2 auth with username/password and jwt auth",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
