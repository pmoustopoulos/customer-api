package com.ainigma100.customerapi.filter;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

/**
 * Component for logging server details upon application startup.
 *
 * <p>This class listens for the ApplicationReadyEvent to log important server details such as
 * the protocol, host, port, context path, active profiles, and the URL for accessing Swagger UI.</p>
 */
@AllArgsConstructor
@Component
public class ServerDetails {

    private static final Logger log = LoggerFactory.getLogger(ServerDetails.class);

    private final Environment environment;
    private static final String SERVER_SSL_KEY_STORE = "server.ssl.key-store";
    private static final String SERVER_PORT = "server.port";
    private static final String SERVER_SERVLET_CONTEXT_PATH = "server.servlet.context-path";
    private static final String SPRINGDOC_SWAGGER_UI_PATH = "springdoc.swagger-ui.path";
    private static final String DEFAULT_PROFILE = "default";
    private static final String SPRING_H2_CONSOLE_ENABLED = "spring.h2.console.enabled";
    private static final String SPRING_H2_CONSOLE_PATH = "spring.h2.console.path";
    private static final String SPRING_DATASOURCE_DRIVER_CLASS_NAME = "spring.datasource.driver-class-name";

    @EventListener(ApplicationReadyEvent.class)
    public void logServerDetails() {

        String protocol = Optional.ofNullable(environment.getProperty(SERVER_SSL_KEY_STORE)).map(key -> "https").orElse("http");
        String host = getServerIP();
        String serverPort = Optional.ofNullable(environment.getProperty(SERVER_PORT)).orElse("8080");
        String contextPath = Optional.ofNullable(environment.getProperty(SERVER_SERVLET_CONTEXT_PATH)).orElse("");
        String[] activeProfiles = Optional.of(environment.getActiveProfiles()).orElse(new String[0]);
        String activeProfile = (activeProfiles.length > 0) ? String.join(",", activeProfiles) : DEFAULT_PROFILE;
        String swaggerUI = Optional.ofNullable(environment.getProperty(SPRINGDOC_SWAGGER_UI_PATH)).orElse("/swagger-ui/index.html");

        // Get H2 Console URL if H2 is configured and the console is enabled
        String h2ConsoleUrl = getH2ConsoleUrlIfEnabled(protocol, host, serverPort, contextPath);

        log.info(
                """
                
                Access Swagger UI URL: {}://{}:{}{}{}
                Active Profile: {}{}
                """,
                protocol, host, serverPort, contextPath, swaggerUI,
                activeProfile,
                h2ConsoleUrl
        );
    }

    private String getServerIP() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("Error resolving host address", e);
            return "unknown";
        }
    }

    private String getH2ConsoleUrlIfEnabled(String protocol, String host, String port, String contextPath) {

        if (isH2DatabaseConfigured() && isH2ConsoleEnabled()) {
            String h2ConsolePath = Optional.ofNullable(environment.getProperty(SPRING_H2_CONSOLE_PATH)).orElse("/h2-console");
            return String.format("%nAccess H2 Console URL: %s://%s:%s%s%s", protocol, host, port, contextPath, h2ConsolePath);
        }

        return "";
    }

    private boolean isH2DatabaseConfigured() {
        // Check if the driver class name is related to H2
        String driverClassName = environment.getProperty(SPRING_DATASOURCE_DRIVER_CLASS_NAME);
        return Optional.ofNullable(driverClassName)
                .map(driver -> driver.equals("org.h2.Driver"))
                .orElse(false);
    }

    private boolean isH2ConsoleEnabled() {
        return Optional.ofNullable(environment.getProperty(SPRING_H2_CONSOLE_ENABLED))
                .map(Boolean::parseBoolean)
                .orElse(false);
    }
}
