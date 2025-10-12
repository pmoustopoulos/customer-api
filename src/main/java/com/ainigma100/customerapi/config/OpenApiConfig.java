package com.ainigma100.customerapi.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringBootVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestClient;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

/**
 * Configuration class for generating OpenAPI documentation.
 *
 * <p>This class configures and generates the OpenAPI documentation for the application
 * using the springdoc-openapi library. It automatically generates and formats the OpenAPI
 * JSON file based on the application's REST endpoints. The generated JSON file is then
 * stored in the root directory of the project for easy access and reference.</p>
 */
@RequiredArgsConstructor
@Slf4j
@Configuration
public class OpenApiConfig {


    private final Environment environment;


    @Value("${server.port:8080}")
    private int serverPort;

    @Value("${openapi.output.file:openapi-default.json}")
    private String outputFileName;

    @Value("${spring.application.name:@project.name@}")
    private String appName;

    @Value("${springdoc.version:1.0}")
    private String documentationVersion;

    @Value("${springdoc.title:API Documentation}")
    private String appTitle;


    @Bean
    public OpenAPI customOpenAPI() {

        String[] activeProfiles = environment.getActiveProfiles();
        String profileInfo = activeProfiles.length > 0
                ? String.join(", ", activeProfiles).toUpperCase()
                : "DEFAULT";

        String springBootVersion = Optional.of(SpringBootVersion.getVersion()).orElse("unknown");
        String description = String.format("Active Profile: <b>%s</b><br/>Spring Boot: <b>%s</b>", profileInfo, springBootVersion);

        boolean isLocalOrH2 = profileInfo.contains("LOCAL") || profileInfo.contains("H2") || profileInfo.contains("DEV");

        final String securitySchemeName = "bearerAuth";


        return new OpenAPI()
                .info(new Info()
                        .title(appTitle)
                        .version(documentationVersion)
                        .description(description))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description(isLocalOrH2
                                                ? "Paste a test token like `admin-token` or `user-token`"
                                                : null)))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName)
                );

    }


    @Bean
    @Profile("!test & !prod")
    public CommandLineRunner generateOpenApiJson() {

        String serverSslKeyStore = "server.ssl.key-store";
        String serverServletContextPath = "server.servlet.context-path";


        return args -> {
            String protocol = Optional.ofNullable(environment.getProperty(serverSslKeyStore)).map(key -> "https").orElse("http");
            String host = getServerIP();
            String contextPath = Optional.ofNullable(environment.getProperty(serverServletContextPath)).orElse("");

            // Define the API docs URL
            String apiDocsUrl = String.format("%s://%s:%d%s/v3/api-docs", protocol, host, serverPort, contextPath);

            log.info("Attempting to fetch OpenAPI docs from URL: {}", apiDocsUrl);

            try {
                // Create RestClient instance
                RestClient restClient = RestClient.create();

                // Fetch the OpenAPI JSON
                String response = restClient.get()
                        .uri(apiDocsUrl)
                        .retrieve()
                        .body(String.class);

                // Delete all previous Swagger files in the root directory before saving the new one
                deletePreviousSwaggerFiles();

                // Format and save the JSON to a file
                formatAndSaveToFile(response, outputFileName);

                log.info("OpenAPI documentation generated successfully at {}", outputFileName);

            } catch (Exception e) {
                log.error("Failed to generate OpenAPI documentation from URL: {}", apiDocsUrl, e);
            }
        };
    }

    private String getServerIP() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("Error resolving host address", e);
            return "unknown";
        }
    }

    private void formatAndSaveToFile(String content, String fileName) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // Enable pretty-print
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            // Read the JSON content as a JsonNode
            JsonNode jsonNode = objectMapper.readTree(content);

            // Write the formatted JSON to a file
            objectMapper.writeValue(new File(fileName), jsonNode);

        } catch (IOException e) {
            log.error("Error while saving JSON to file", e);
        }
    }

    private void deletePreviousSwaggerFiles() {

        File rootDir = new File(System.getProperty("user.dir"));
        File[] swaggerFiles = rootDir.listFiles((dir, name) -> name.startsWith("openapi-") && name.endsWith(".json"));

        if (swaggerFiles != null) {
            for (File file : swaggerFiles) {
                if (file.getName().contains(appName) && file.delete()) {
                    log.info("Deleted old OpenAPI file: {}", file.getName());
                }
            }
        }
    }

}
