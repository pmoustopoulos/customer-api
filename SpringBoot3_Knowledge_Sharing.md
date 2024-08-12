# Spring Boot 3 Knowledge Sharing

This document is designed to help new Spring Boot developers understand the basics of building applications using Spring Boot 3. It covers the structure of a sample project, explains the purpose of key annotations, and provides insights into best practices.

**Disclaimer**: This guide reflects my personal opinion and approach, based on the knowledge I have gained through my work as a developer and my studies. It is not necessarily the best or only way to do things, and as time passes, practices and tools may evolve. I encourage you to explore other perspectives and approaches as well.

**Feedback and Contributions**: I am always open to feedback and contributions. If you have suggestions for improvement or additional insights, please feel free to share. Together, we can make this a valuable resource for anyone learning Spring Boot 3.

## Table of Contents
1. [Introduction](#1-introduction)
2. [Project Structure Overview](#2-project-structure-overview)
3. [Introduction to Maven and `pom.xml`](#3-introduction-to-maven-and-pomxml)
4. [Key Annotations in Spring Boot `Under Construction`](#4-key-annotations-in-spring-boot)
5. [Configuring `application.yaml`](#5-configuring-applicationyaml)
6. [Detailed Package Breakdown](#6-detailed-package-breakdown)
    - [Entity Layer `Under Construction`](#entity-layer)
    - [Repository Layer `Under Construction`](#repository-layer)
    - [Service Layer `Under Construction`](#service-layer)
    - [DTOs and MapStruct `Under Construction`](#dtos-and-mapstruct)
    - [Controller Layer `Under Construction`](#controller-layer)
7. [Helper Classes](#7-helper-classes)
8. [Naming Conventions `Under Construction`](#8-naming-conventions)
9. [Running the Application Without an IDE `Under Construction`](#9-running-the-application-without-an-ide)
10. [Security `Under Construction`](#10-security-under-construction)
11. [Testing `Under Construction`](#11-testing-under-construction)
12. [Best Practices](#12-best-practices)
13. [Conclusion](#13-conclusion)

## 1. Introduction

### What is Spring Boot?
Spring Boot is an extension of the Spring framework that simplifies the development of Java applications. It provides
tools and conventions that allow developers to get started quickly without needing to manually configure and set up
complex frameworks.

### Why Use Spring Boot?
- **Auto-configuration**: Automatically configures your application based on the dependencies you add to your project.
- **Embedded Server**: You don’t need to set up an external server like Tomcat; Spring Boot applications can run with an embedded server.
- **Production-Ready**: Includes features like health checks, metrics, and externalized configuration, making it easy to deploy applications in a production environment.

## 2. Project Structure Overview

Understanding the structure of a Spring Boot project is crucial for effective development. Below is the typical structure
of a sample Spring Boot 3 project. Please note this is something I follow based on the knowledge I gained from other developers.
Furthermore, some packages can be skipped in case based on your use case you do not need them.


```
├── config
├── controller
├── dto
├── entity
├── enums
├── exception
├── filter
├── mapper
├── repository
├── service
│   └── impl
└── utils
```

### Packages and Their Purpose

- **`config`**: Contains configuration classes for application-wide settings (e.g., security, open api).
- **`controller`**: REST controllers handling HTTP requests, routing them to services (**note**: do not add logic here).
- **`dto`**: Data Transfer Objects (DTOs) used for transferring data between client and server.
- **`entity`**: JPA entities that represent database tables.
- **`enums`**: Enumerations used across the application.
- **`exception`**: Custom exceptions and a global exception handler.
- **`filter`**: Request filtering and logging logic.
- **`mapper`**: Classes that map entities to DTOs and vice versa. In this case I will use MapStruct for compile-time mapping.
- **`repository`**: Data access layer using Spring Data JPA repositories.
- **`service`**: Business logic layer, including interfaces and their implementations.
- **`utils`**: Utility classes and helpers used across the application.




## 3. Introduction to Maven and `pom.xml`

### What is Maven?
Maven is a powerful build automation and project management tool that is widely used in Java projects. It helps manage
project builds, dependencies, and configurations in a standardized way. Maven centralizes the project’s setup in a file
called the **Project Object Model (POM)**, which is typically located in the `pom.xml` file at the root of your project.

### What is a POM?
The **Project Object Model (POM)** is the core of a Maven project. It’s an XML file that defines the structure,
dependencies, and build configuration of your project. When Maven runs, it reads the `pom.xml` file to determine how to
build, test, and package your application.

### Key Concepts in the POM

#### Project Coordinates:
- **`groupId`**: Identifies your project’s group, typically the company or organization (e.g., `com.ainigma100`). It is a unique identifier that distinguishes your project from others.
- **`artifactId`**: The name of your project or module (e.g., `customer-api`). It represents the artifact, which is usually the output of the project, such as a JAR file.
- **`version`**: The current version of your project (e.g., `0.0.1-SNAPSHOT`). It indicates the specific iteration of the project, helping in managing releases and dependencies.

#### Dependencies:
Dependencies define the external libraries your project needs to function. These are specified in the `<dependencies>`
section of the `pom.xml` and are automatically downloaded and included by Maven.

#### Plugins:
Plugins extend the functionality of Maven and are used to perform various build-related tasks, such as compiling code,
running tests, and packaging the application. They are specified in the `<build>` section of the `pom.xml`.


### Example `pom.xml` file for Customer API

<details>
  <summary>View pom file</summary>

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.3.2</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    <groupId>com.ainigma100</groupId>
    <artifactId>customer-api</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>customer-api</name>
    <description>customer-api</description>

    <properties>
        <java.version>21</java.version>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <springdoc-openapi-starter-webmvc-ui.version>2.6.0</springdoc-openapi-starter-webmvc-ui.version>
        <org.mapstruct.version>1.5.5.Final</org.mapstruct.version>
        <lombok-mapstruct-binding.version>0.2.0</lombok-mapstruct-binding.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
            <version>${org.mapstruct.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>${springdoc-openapi-starter-webmvc-ui.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>${maven-compiler-plugin.version}</version>
                <configuration>
                    <source>${maven.compiler.source}</source>
                    <target>${maven.compiler.target}</target>
                    <annotationProcessorPaths>
                        <path>
                            <groupId>org.mapstruct</groupId>
                            <artifactId>mapstruct-processor</artifactId>
                            <version>${org.mapstruct.version}</version>
                        </path>
                        <path>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </path>
                        <path>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok-mapstruct-binding</artifactId>
                            <version>${lombok-mapstruct-binding.version}</version>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

</details>

### Dependencies Included

This `pom.xml` includes several important dependencies:

- **Spring Boot Starter Web**: Provides the necessary components to build a web application, including an embedded Tomcat server.
- **Spring Boot Starter Data JPA**: Simplifies database interactions by integrating Spring Data JPA for database operations.
- **Spring Boot Starter Actuator**: Adds production-ready features such as monitoring and metrics.
- **Spring Boot Starter Validation**: Facilitates data validation using Hibernate Validator. It allows you to use some annotations to validate an object.
- **H2 Database**: A lightweight in-memory database often used for testing and development.
- **Lombok**: Reduces boilerplate code by generating getters, setters, constructors, and other methods at compile time.
- **MapStruct**: A code generator that simplifies the mapping between Java beans.
- **SpringDoc OpenAPI**: Integrates Swagger/OpenAPI support into Spring Boot applications for API documentation.
- **Spring Boot Starter Test**: Provides testing libraries like JUnit, Mockito, and Spring TestContext Framework for unit and integration testing.

### Adding More Dependencies

As your project evolves, you might need additional libraries or tools. You can add more dependencies by searching for
them in the [Maven Central Repository](https://mvnrepository.com/) and including them in the `<dependencies>`
section of your `pom.xml`.


### Explanation of Plugin Configuration

In the `<build>` section of the `pom.xml`, we configure the Maven Compiler Plugin with additional settings for Lombok and MapStruct:

- **Lombok**: Since Lombok generates code during the compilation phase (e.g., constructors, getters, setters), it
  requires an annotation processor. The plugin configuration ensures that Lombok's annotation processor is included,
  allowing Lombok to function correctly during compilation.


- **MapStruct**: MapStruct is a code generator that automatically creates mappers for converting between different
  Java beans. Like Lombok, MapStruct requires an annotation processor to generate the necessary code during compilation.
  The plugin configuration includes the MapStruct processor, ensuring that the mappings are generated correctly.


- **Lombok-MapStruct Binding**: MapStruct and Lombok both operate during the annotation processing phase, but they
  sometimes need to coordinate to avoid conflicts. The `lombok-mapstruct-binding` dependency ensures that Lombok's
  generated code is compatible with MapStruct, allowing both tools to work together seamlessly. This binding is
  configured in the `annotationProcessorPaths` section of the Maven Compiler Plugin, ensuring that the processors
  are run in the correct order.

By configuring these plugins, we ensure that Lombok, MapStruct, and their integration work seamlessly during the build process, reducing manual coding effort and improving efficiency.

<br>


## 4. Key Annotations in Spring Boot



## 5. Configuring `application.yaml`
The `application.yaml` file is an essential configuration file in a Spring Boot project. It allows you to manage your
application's settings in a clear and structured manner. YAML is preferred over properties files in many cases because
it is easier to read and supports hierarchical data.

### Why `application.yaml`?
When you create a new Spring Boot project, the default configuration file is named `application.properties`. However,
many developers choose to use `application.yaml` instead, as it offers a more concise and readable format, especially
when dealing with complex configurations.

### General Configuration
The main `application.yaml` file typically includes common settings that apply to all environments, such as server
configuration, application name, and basic Spring settings.

### Environment-Specific Configuration
In addition to the main `application.yaml` file, you can create environment-specific YAML files, such as
`application-dev.yaml` for development, `application-uat.yaml` for user acceptance testing, and `application-prod.yaml`
for production. These files contains settings specific to each environment, allowing you to easily switch between
configurations without changing your code.

### Activating Profiles
You can activate a specific profile in several ways:

- **In `application.yaml`**: You can specify the active profile directly in the `application.yaml` file using the `spring.profiles.active` property. This is useful for setting a default profile that will be used unless another is specified at runtime.
- **At Runtime**: You can also activate a profile at runtime by passing the `spring.profiles.active` property as a command-line argument or setting it as an environment variable.

### Best Practices

- **Keep It Simple**: Store common settings in the main `application.yaml` and use environment-specific files for configuration differences.
- **Use Profiles**: Profiles help manage different environments like development, testing, and production by allowing you to specify environment-specific configurations.
- **YAML Advantages**: The YAML format is preferred for its readability and ability to handle complex, hierarchical settings more gracefully than traditional properties files.


### Example `application.yaml` and `application-dev.yaml` files for Customer API

In this section, I provide two YAML configuration files to demonstrate some of the settings I use for a Spring Boot 
project. These are just examples, and many other configurations are available depending on your project's needs. As I 
continue to develop this project, I may add more configurations.

#### application.yaml

This file contains general settings that apply to all environments. Here’s a breakdown of the configurations used:

- **`server.port`**: Specifies the port on which the application will run. In this case, it’s set to `8088`. If you do not specify the port, the app will start on a `default port` which is `8080`.
- **`server.servlet.context-path`**: Defines the base URL path for the application. Here, it’s dynamically set to the artifact ID of the project.
- **`server.shutdown.graceful`**: Enables graceful shutdown, ensuring that the application completes ongoing requests before shutting down.
- **`spring.profiles.active`**: Indicates the active profile to be used. In this case, the development profile (`dev`) is active.
- **`spring.application.name`**: Sets the application name, dynamically pulled from the project configuration.
- **`spring.lifecycle.timeout-per-shutdown-phase`**: Configures the timeout for each shutdown phase, here set to `25 seconds`.
- **`spring.output.ansi.enabled`**: Controls ANSI output in the console, set to `always` to ensure colored output.
- **`springdoc.swagger-ui.path`**: Sets the path for accessing the Swagger UI, useful for API documentation.
- **`springdoc.title`** and **`springdoc.version`**: Define the title and version of the API documentation, again dynamically set based on project properties.
- **`openapi.output.file`**: Specify the file name of the swagger file that will be generated by `OpenApiConfig` class on start-up. It will be the documentation of the application.

```yaml
server:
  port: 8088
  servlet:
    context-path: '/@project.artifactId@'
  shutdown: graceful

spring:
  profiles:
    active: dev # Specify the active profile

  application:
    name: '@project.name@'
  lifecycle:
    timeout-per-shutdown-phase: 25s

  output:
    ansi:
      enabled: always

springdoc:
  swagger-ui:
    path: /ui
  title: 'Customer API'
  version: '@springdoc-openapi-starter-webmvc-ui.version@'
  
openapi:
  output:
    file: 'openapi-@project.artifactId@.json'
```


#### application-dev.yaml
This file contains settings specific to the development environment. Here's what each setting does:

- **`datasource.url`**: Configures the JDBC URL for the H2 database. In this example, the database is stored in a local file (`./data/customer-db`) with `AUTO_SERVER=true` to allow remote connections.
- **`datasource.username` and `datasource.password`**: Set the database username and password. The default username (`sa`) is used with no password.
- **`datasource.driver-class-name`**: Specifies the JDBC driver class for the H2 database.
- **`jpa.show-sql`**: Enables logging of SQL statements generated by Hibernate.
- **`jpa.properties.hibernate.format_sql`**: Formats SQL output to be more readable.
- **`jpa.properties.hibernate.dialect`**: Specifies the Hibernate dialect to use, which is set to `H2Dialect` for compatibility with the H2 database.
- **`jpa.generate-ddl`**: Automatically generates database schema (DDL) from JPA entities.
- **`jpa.hibernate.ddl-auto`**: Controls the behavior of schema generation at runtime, with `update` allowing for incremental updates to the schema.


### Notes:
- These configurations are just examples based on what I use in this project. There are many other configurations you can apply depending on your project's needs.
- As the project evolves, I may add more configurations to enhance functionality or address specific needs.
- Feel free to explore additional configurations and adjust these examples to fit your project requirements.

**Feedback and Contributions**: If you have suggestions or improvements, please share them. Collaboration is key to refining this guide and making it a valuable resource for all developers.


```yaml
datasource:
  url: jdbc:h2:file:./data/notification-manager-db;AUTO_SERVER=true
  username: sa
  password:
  driver-class-name: org.h2.Driver
jpa:
  show-sql: true
  properties:
    hibernate:
      format_sql: true
      dialect: org.hibernate.dialect.H2Dialect
  generate-ddl: true
  hibernate:
    ddl-auto: update
```


<br>

## 6. Detailed Package Breakdown (Under Construction)

### Entity Layer (Under Construction)



<br>

### Repository Layer

- **Purpose**: The repository layer handles CRUD operations for entities using Spring Data JPA, allowing you to
  interact with the database without writing SQL.
- **Package**: `repository`
- **Example Class**:
    - `CustomerRepository.java`: Manages CRUD operations for the `Customer` entity.

- **Key Concepts**:
    - **`extends JpaRepository<Customer, Long>`**: By extending `JpaRepository`, Spring Boot automatically configures a
      repository bean for you. This bean is a proxy implementation of `SimpleJpaRepository`, which provides all the
      necessary CRUD operations and query methods for the `Customer` entity. No need for `@Repository` or `@Component`
      annotations—Spring handles the configuration.

### Query Methods Overview
Spring Data JPA offers several ways to write queries in your repository interfaces, including:

#### 1. Derived Query Methods
You can create simple queries by following naming conventions.

**Example**:
```java
List<Customer> findByLastName(String lastName);
```

<br>

#### 2. Custom Queries with @Query
For more complex queries, you can use the @Query annotation.

**Example**:

```java
import org.springframework.data.repository.query.Param;

@Query("SELECT c FROM Customer c WHERE c.email = :email")
Customer findByEmail(@Param("email") String email);
```

<br>

#### 3. Native Queries
You can write native SQL queries using the @Query annotation.

**Example**:

```java
import org.springframework.data.repository.query.Param;

@Query(value = "SELECT * FROM customers WHERE status = :status", nativeQuery = true)
List<Customer> findByStatus(@Param("status") String status);
```

<br>

### When to Use Native Queries

- **Advantages**: Native queries can be useful when you need to leverage database-specific features, optimize
  performance, or execute complex SQL that might not be easily expressed in JPQL (Java Persistence Query Language).


- **Disadvantages**: However, native queries can reduce the portability of your application across different database
  systems since they are tied to a specific SQL dialect. They also bypass some of the safety checks and optimizations
  provided by JPQL, such as automatic mapping of query results to entities.


- **Best Practice**: Prefer using JPQL or derived query methods for most queries to maintain portability and leverage
  JPA's features. Use native queries only when necessary for performance optimization or when dealing with complex
  queries that JPQL cannot handle efficiently.



### Service Layer


### DTOs and MapStruct


### Controller Layer


## 7. Helper Classes

In this section, I provide some helper classes that can be reused in Spring Boot applications. These classes are designed 
to simplify common tasks such as logging, server configuration, and OpenAPI documentation generation.

### OpenApiConfig

This configuration class is responsible for generating OpenAPI documentation using SpringDoc. It fetches the OpenAPI 
JSON from the application's endpoints and saves it as a formatted file.

**Key Features**:

- Automatically generates OpenAPI documentation in JSON format.
- Saves the documentation to a specified file in the project root. The output file name is specified in the `application.yaml` file.
- Handles both HTTP and HTTPS protocols based on the server configuration.

<details>
  <summary>View OpenApiConfig code</summary>

```java
@Slf4j
@Configuration
@OpenAPIDefinition(info = @Info(
        title = "${springdoc.title}",
        version = "${springdoc.version}",
        description = "Documentation ${spring.application.name} v1.0"
))
public class OpenApiConfig {


    private final Environment environment;

    public OpenApiConfig(Environment environment) {
        this.environment = environment;
    }

    @Value("${server.port:8080}")
    private int serverPort;

    @Value("${openapi.output.file}")
    private String outputFileName;

    private static final String SERVER_SSL_KEY_STORE = "server.ssl.key-store";
    private static final String SERVER_SERVLET_CONTEXT_PATH = "server.servlet.context-path";

    @Bean
    public CommandLineRunner generateOpenApiJson() {
        return args -> {
            String protocol = Optional.ofNullable(environment.getProperty(SERVER_SSL_KEY_STORE)).map(key -> "https").orElse("http");
            String host = getServerIP();
            String contextPath = Optional.ofNullable(environment.getProperty(SERVER_SERVLET_CONTEXT_PATH)).orElse("");

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
}
```
</details>



### LoggingFilter

A servlet filter that logs incoming HTTP requests and outgoing responses. It excludes certain paths, such as those 
related to Actuator and Swagger, from logging to reduce noise.

**Key Features**:

- Logs the client's IP address, request URL, and HTTP method.
- Logs the response status after the request is processed.
- Excludes paths related to Actuator, Swagger, and static resources from logging.

<details>
  <summary>View LoggingFilter code</summary>

```java
@Component
@Slf4j
public class LoggingFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String clientIP = this.getClientIP(httpServletRequest);

        if ( this.shouldLogRequest(httpServletRequest) ) {
            log.info("Client IP: {}, Request URL: {}, Method: {}", clientIP, httpServletRequest.getRequestURL(), httpServletRequest.getMethod());
        }

        // pre methods call stamps
        chain.doFilter(request, response);

        // post method calls stamps
        if ( this.shouldLogRequest(httpServletRequest) ) {
            log.info("Response status: {}", httpServletResponse.getStatus());
        }

    }

    private boolean shouldLogRequest(HttpServletRequest request) {

        // (?i) enables case-insensitive matching, \b matched as whole words
        // reference: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Regular_expressions
        return !request.getServletPath().matches("(?i).*\\b(actuator|swagger|api-docs|favicon|ui)\\b.*");
    }

    private String getClientIP(HttpServletRequest request) {

        String clientIP = request.getHeader("Client-IP");

        if (clientIP == null || clientIP.isEmpty() || "unknown".equalsIgnoreCase(clientIP)) {
            clientIP = request.getHeader("X-Forwarded-For");
        }

        if (clientIP == null || clientIP.isEmpty() || "unknown".equalsIgnoreCase(clientIP)) {
            clientIP = request.getHeader("X-Real-IP");
        }

        if (clientIP == null || clientIP.isEmpty() || "unknown".equalsIgnoreCase(clientIP)) {
            clientIP = request.getRemoteAddr();
        }

        return clientIP != null ? clientIP : "Unknown";
    }

}
```

</details>


### FiltersConfig

This configuration class registers the `LoggingFilter` as a Spring bean and sets its priority in the filter chain.

**Key Features**:

- Registers the `LoggingFilter` with a specified order of execution.
- Ensures that the filter applies to all incoming requests.

<details>
  <summary>View FiltersConfig code</summary>

```java
@AllArgsConstructor
@Configuration
public class FiltersConfig {

    private final LoggingFilter loggingFilter;

    @Bean
    public FilterRegistrationBean<LoggingFilter> loggingFilterBean() {

        final FilterRegistrationBean<LoggingFilter> filterBean = new FilterRegistrationBean<>();
        filterBean.setFilter(loggingFilter);
        filterBean.addUrlPatterns("/*");
        // Lower values have higher priority
        filterBean.setOrder(Integer.MAX_VALUE-2);

        return filterBean;
    }

}
```

</details>


### ServerDetails

This component logs important server details when the application starts, including the server's protocol, host, port, 
context path, and active profiles. It also provides the URL for accessing the Swagger UI.

**Key Features**:

- Logs server details and Swagger UI access URL on application startup.
- Supports both HTTP and HTTPS protocols.
- Displays the active Spring profiles.

<details>
  <summary>View ServerDetails code</summary>

```java
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

    @EventListener(ApplicationReadyEvent.class)
    public void logServerDetails() {

        String protocol = Optional.ofNullable(environment.getProperty(SERVER_SSL_KEY_STORE)).map(key -> "https").orElse("http");
        String host = getServerIP();
        String serverPort = Optional.ofNullable(environment.getProperty(SERVER_PORT)).orElse("8080");
        String contextPath = Optional.ofNullable(environment.getProperty(SERVER_SERVLET_CONTEXT_PATH)).orElse("");
        String[] activeProfiles = Optional.of(environment.getActiveProfiles()).orElse(new String[0]);
        String activeProfile = (activeProfiles.length > 0) ? String.join(",", activeProfiles) : DEFAULT_PROFILE;
        String swaggerUI = Optional.ofNullable(environment.getProperty(SPRINGDOC_SWAGGER_UI_PATH)).orElse("/swagger-ui/index.html");

        log.info(
                """
                
                
                Access Swagger UI URL: {}://{}:{}{}{}
                Active Profile: {}
                """,
                protocol, host, serverPort, contextPath, swaggerUI,
                activeProfile
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
}
```

</details>



## 8. Naming Conventions

### Package Naming
- **Lowercase and Singular**: Package names should be in lowercase and singular (e.g., `com.ainigma100.customerapi.controller`).
- **No Special Characters**: Avoid using special characters or underscores.

### Class Naming
- **PascalCase**: Class names should follow PascalCase (e.g., `CustomerService`).
- **Meaningful Names**: Choose descriptive names that clearly indicate the purpose of the class.

### Entity Naming
- **Singular Form**: Entity classes should be named in the singular form (e.g., `Customer`).
- **Mapped to Plural Table Names**: Entities often map to plural table names (e.g., `customers`).

### API Endpoint Naming
- **Plural Nouns**: Use plural nouns for API endpoints to represent collections of resources (e.g., `/customers`).
- **Lowercase with Hyphens**: Endpoint paths should be lowercase, with hyphens separating words (e.g., `/customer-reports`).

## 9. Running the Application Without an IDE

## 10. Security


## 11. Testing


## 12. Best Practices


## 13. Conclusion



### Feedback and Contributions

Feedback and contributions are welcome! If you have suggestions, improvements, or additional insights, please feel free to share. Together, we can make this a valuable resource for anyone learning Spring Boot 3.

