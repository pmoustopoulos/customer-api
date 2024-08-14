# Spring Boot 3 Knowledge Sharing

This document is designed to help new Spring Boot developers understand the basics of building applications using Spring
Boot 3. It covers the structure of a sample project, explains the purpose of key annotations, and provides insights
into best practices. **Note**: You have also to check the code example and not only this markdown file because some
parts
are not shown here (custom annotations, Utils class etc).

**Disclaimer**: This guide reflects my personal opinion and approach, based on the knowledge I have gained through my
work as a developer and my studies. It is not necessarily the best or only way to do things, and as time passes,
practices and tools may evolve. I encourage you to explore other perspectives and approaches as well.

**Feedback and Contributions**: I am always open to feedback and contributions. If you have suggestions for improvement
or additional insights, please feel free to share. Together, we can make this a valuable resource for anyone learning
Spring Boot 3.

## Table of Contents

1. [Introduction](#1-introduction)
2. [Project Structure Overview](#2-project-structure-overview)
3. [Introduction to Maven and `pom.xml`](#3-introduction-to-maven-and-pomxml)
4. [Key Annotations in Spring Boot](#4-key-annotations-in-spring-boot)
5. [Naming Conventions](#5-naming-conventions)
6. [Configuring `application.yaml`](#6-configuring-applicationyaml)
7. [Detailed Package Breakdown](#7-detailed-package-breakdown)
    - [Entity Layer](#entity-layer)
    - [Repository Layer](#repository-layer)
    - [Service Layer](#service-layer)
    - [DTOs and MapStruct](#dtos-and-mapstruct)
    - [Controller Layer](#controller-layer)
    - [Exception Handling](#exception-handling)
8. [Helper Classes](#8-helper-classes)
9. [Testing](#9-testing)
10. [Best Practices](#12-best-practices)

## 1. Introduction

### What is Spring Boot?

Spring Boot is an extension of the Spring framework that simplifies the development of Java applications. It provides
tools and conventions that allow developers to get started quickly without needing to manually configure and set up
complex frameworks.

### Why Use Spring Boot?

- **Auto-configuration**: Automatically configures your application based on the dependencies you add to your project.
- **Embedded Server**: You don’t need to set up an external server like Tomcat; Spring Boot applications can run with
  an embedded server.
- **Production-Ready**: Includes features like health checks, metrics, and externalized configuration, making it easy
  to deploy applications in a production environment.

## 2. Project Structure Overview

Understanding the structure of a Spring Boot project is crucial for effective development. Below is the typical
structure
of a sample Spring Boot 3 project. Please note this is something I follow based on the knowledge I gained from other
developers.
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
- **`mapper`**: Classes that map entities to DTOs and vice versa. In this case I will use MapStruct for compile-time
  mapping.
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

- **`groupId`**: Identifies your project’s group, typically the company or organization (e.g., `com.ainigma100`). It is
  a unique identifier that distinguishes your project from others.
- **`artifactId`**: The name of your project or module (e.g., `customer-api`). It represents the artifact, which is
  usually the output of the project, such as a JAR file.
- **`version`**: The current version of your project (e.g., `0.0.1-SNAPSHOT`). It indicates the specific iteration of
  the project, helping in managing releases and dependencies.

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

- **Spring Boot Starter Web**: Provides the necessary components to build a web application, including an embedded
  Tomcat server.
- **Spring Boot Starter Data JPA**: Simplifies database interactions by integrating Spring Data JPA for database
  operations.
- **Spring Boot Starter Actuator**: Adds production-ready features such as monitoring and metrics.
- **Spring Boot Starter Validation**: Facilitates data validation using Hibernate Validator. It allows you to use some
  annotations to validate an object.
- **H2 Database**: A lightweight in-memory database often used for testing and development.
- **Lombok**: Reduces boilerplate code by generating getters, setters, constructors, and other methods at compile time.
- **MapStruct**: A code generator that simplifies the mapping between Java beans.
- **SpringDoc OpenAPI**: Integrates Swagger/OpenAPI support into Spring Boot applications for API documentation.
- **Spring Boot Starter Test**: Provides testing libraries like JUnit, Mockito, and Spring TestContext Framework for
  unit and integration testing.

### Adding More Dependencies

As your project evolves, you might need additional libraries or tools. You can add more dependencies by searching for
them in the [Maven Central Repository](https://mvnrepository.com/) and including them in the `<dependencies>`
section of your `pom.xml`.

### Explanation of Plugin Configuration

In the `<build>` section of the `pom.xml`, we configure the Maven Compiler Plugin with additional settings for Lombok
and MapStruct:

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

By configuring these plugins, we ensure that Lombok, MapStruct, and their integration work seamlessly during the build
process, reducing manual coding effort and improving efficiency.

<br>

## 4. Key Annotations in Spring Boot

In this section, I will walk through the key annotations used across the various classes in the example project,
explaining
their purpose and best practices. These annotations are essential for managing dependencies, handling HTTP requests,
mapping database entities, and more. It is important to note that there are many more annotations available in Spring
Boot,
and I am only covering those that are used in this project. Additionally, when you write tests
(unit tests, integration tests, etc.), you will use additional annotations specific to testing.

### 1. Commonly Used Annotations Across the Application

These annotations are versatile and can be used in different layers of a Spring Boot application, depending on the need.

- **`@Slf4j`**: Creates a `Logger` instance in the class, allowing for easy logging without manually defining a logger.
  This annotation can be used in any class where logging is required, such as service classes, controllers,
  repositories,
  and even configuration classes.


- **`@AllArgsConstructor`, `@NoArgsConstructor`, and `@RequiredArgsConstructor`**:

    - **`@AllArgsConstructor`**: Generates a constructor with parameters for all fields in the class. This is useful
      when
      you want to ensure all fields are initialized at the time of object creation.

    - **`@NoArgsConstructor`**: Generates a no-argument constructor, which is necessary for frameworks like JPA that
      require a default constructor to instantiate entities.

    - **`@RequiredArgsConstructor`**: Generates a constructor with parameters for all final fields and any fields that
      are
      marked as `@NonNull`. This is useful when you want to ensure that mandatory fields are initialized while still
      allowing flexibility in object creation.


- **`@Data`**: Generates getters, setters, `equals()`, `hashCode()`, `toString()`, and other utility methods. While it
  is suitable for DTOs and simple data carrier classes, it is generally not recommended for JPA entities due to
  potential performance issues and complications with `equals()` and `hashCode()` methods. Instead, for JPA entities,
  it's better to use individual annotations like `@Getter`, `@Setter`, and `@ToString`.

### 2. Entity Class Annotations

- **`@Entity`**: Marks the class as a JPA entity, meaning it is mapped to a database table. It is crucial for ORM
  (Object-Relational Mapping) in Spring Boot.


- **`@Table(name = "customers")`**: Specifies the name of the table in the database that this entity maps to. Useful
  when the table name differs from the class name.


- **`@Id`**: Denotes the primary key of the entity. It is a mandatory annotation for JPA entities.


- **`@GeneratedValue(strategy = GenerationType.IDENTITY)`**: Specifies the primary key generation strategy.
  `GenerationType.IDENTITY` indicates that the database will auto-generate the primary key.


- **`@Column`**: Marks a field as a column in the database. It can include additional attributes like `nullable`,
  `unique`, and `length` to define the column's characteristics.


- **`@CreationTimestamp`** and **`@UpdateTimestamp`**: Automatically manage the creation and update timestamps of the
  entity, using the database's current timestamp.


- **Lombok Annotations**:
    - **`@Getter`** and **`@Setter`**: Generate getters and setters for all fields.
    - **`@ToString`**: Generates a `toString()` method.
    - **Note**: The `@Data` annotation is not recommended for JPA entities due to potential performance issues and
      complications with `equals()` and `hashCode()` methods.


- **Best Practice for Associations**: When working with entity relationships (`@OneToMany`, `@ManyToOne`, etc.), it is
  important to understand fetch strategies (`FetchType.LAZY` vs. `FetchType.EAGER`) and cascade options to manage how
  related entities are loaded and persisted.

### 3. DTO Class Annotations

- **`@Data`**: Generates all necessary methods like `equals()`, `hashCode()`, `toString()`, and getters/setters.
  Suitable for DTOs, which are simple data carriers without complex logic.


- **`@JsonInclude(JsonInclude.Include.NON_NULL)`**: Ensures that null fields are not included in the JSON output,
  resulting in cleaner API responses.


- **`@Builder`**: Implements the builder pattern, making it easy to create immutable DTO objects with only the required
  fields set.

### 4. Repository Class Annotations

- **`@Query`**: Defines custom JPQL or native SQL queries within repository interfaces. Useful for complex queries not
  handled by Spring Data JPA's method naming conventions.


- **`@Param`**: Binds method parameters to named parameters in a query, improving readability and maintainability.


- **`@Repository` (Not explicitly required)**: While you don't need to explicitly use this annotation if you extend
  `JpaRepository`, it is good to know that it marks a class as a repository and makes it eligible for exception
  translation into Spring's DataAccessException hierarchy. This annotation can also be used in custom repository
  implementations.

### 5. Service Class Annotations

- **`@Service`**: Marks the class as a service, which is part of the service layer in the application. It is a
  specialization of `@Component`, indicating that the class contains business logic.


- **`@Override`**: Indicates that a method is overriding a method in a superclass. It is a good practice to use this
  annotation to avoid errors, like incorrect method signatures. This annotation can be used in any class where method
  overriding occurs, not just in services.

### 6. Controller Class Annotations

- **`@RestController`**: Combines `@Controller` and `@ResponseBody`, indicating that the class handles HTTP requests and
  that the return value of each method is written directly to the HTTP response body, typically as JSON.


- **`@RequestMapping("/api/v1/customers")`**: Maps HTTP requests to specific methods in the controller, providing a base
  path for all endpoints within the controller.


- **`@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`**: Map HTTP GET, POST, PUT, and DELETE requests,
  respectively, to specific methods in the controller.


- **`@Operation(summary = "Add a new customer")`**: Used in OpenAPI/Swagger documentation to describe the purpose of an
  endpoint, aiding in generating accurate and useful API documentation.


- **`@Valid`**: Used to trigger validation of the request body or path variables based on constraints defined in the
  DTO.


- **`@RequestBody`**: Maps the HTTP request body to a Java object, commonly used in POST and PUT methods.


- **`@PathVariable`**: Binds a method parameter to a URI template variable, allowing extraction of values from the URL.


- **`@RequestParam`**: Binds a method parameter to a query parameter in the URL, useful for passing optional or required
  parameters to an endpoint.

### 7. Additional Annotations

- **`@Configuration`**: Indicates that the class contains Spring bean definitions and can replace traditional XML-based
  configuration. This can be used in any class responsible for defining beans or configuring aspects of the application.


- **`@Value`**: Injects values from properties files into fields, typically used for configuration purposes. It can be
  used in any class where configuration values are needed.


- **`@Bean`**: Marks a method as a bean producer in Spring's application context, with the method's return value being
  registered as a bean and managed by Spring. This is typically used in configuration classes but can be used anywhere
  bean definitions are required.


- **`@EventListener(ApplicationReadyEvent.class)`**: Listens for specific events in the Spring application lifecycle.
  For example, it is used in your `ServerDetails` class to execute code when the application is fully started and ready
  to service requests. This can be applied to any class where event-driven behavior is necessary.


- **`@Mapper(componentModel = "spring")`**: Used in MapStruct to generate a Spring bean for the mapper, allowing it to
  be injected as a dependency where needed. This annotation is essential for integrating MapStruct mappers into your
  Spring Boot application.

### Additional Notes

There are many more annotations in Spring Boot that you might encounter as your application grows or as you write tests
(unit tests, integration tests, etc.). Each layer of the application (controller, service, repository, etc.) and each
use case (security, data validation, testing) has specific annotations that help to streamline development and improve
code quality. This section covers the key annotations used in this project, providing a solid foundation for
understanding how they work together in a Spring Boot application.

## 5. Naming Conventions

Consistent naming conventions in your codebase and API design make your project easier to navigate, maintain, and scale.
Below are some guidelines for naming conventions in a Spring Boot project focused on a customer-related API.

### Package Naming

- **Lowercase and Singular**: Package names should be in lowercase and singular. This enhances readability and
  consistency.
    - **Example**: `com.example.customerapi.controller`, `com.example.customerapi.service`
- **No Special Characters**: Avoid using special characters or underscores in package names. Stick to simple,
  descriptive names.
    - **Example**: `com.example.customerapi.repository` (not `com.example_customerapi.repository`)

### Class Naming

- **PascalCase**: Class names should follow PascalCase, where each word starts with an uppercase letter. This is a
  widely accepted convention in Java.
    - **Example**: `CustomerService`, `CustomerController`, `CustomerRepository`
- **Meaningful Names**: Choose descriptive names that clearly indicate the purpose of the class.
    - **Example**: `CustomerNotificationService` instead of `NotificationHelper`

### Entity Naming

- **Singular Form**: Entity classes should be named in the singular form to represent a single instance of the entity.
    - **Example**: `Customer`, `Address`, `Order`
- **Mapped to Plural Table Names**: Entities often map to plural table names in the database.
    - **Example**: `Customer` class maps to `customers` table, `Order` class maps to `orders` table

### API Endpoint Naming

- **Plural Nouns**: Use plural nouns for API endpoints to represent collections of resources.
    - **Example**: `/customers`, `/orders`, `/addresses`


- **Lowercase with Hyphens**: Endpoint paths should be lowercase, with hyphens separating words for readability.
    - **Example**: `/customers/{customerId}/orders`, `/orders/{orderId}/items`, `/customers/{customerId}/addresses`


- **Avoid Verbs in URIs**: Use nouns to represent resources. The HTTP method (GET, POST, PUT, DELETE) should define the
  action, not the URI.
    - **Bad Examples**: `/getCustomers`, `/createOrder`, `/deleteCustomer`
    - **Good Examples**: `/customers` (GET), `/orders` (POST), `/customers/{id}` (DELETE)


- **Use Forward Slashes (/) for Hierarchy**: Forward slashes are used to indicate a hierarchical relationship between
  resources.
    - **Example**: `/customers/{customerId}/orders`, `/customers/{customerId}/addresses`


- **Do Not Use Trailing Slashes**: Avoid trailing slashes at the end of the URI.
    - **Bad Example**: `/customers/`
    - **Good Example**: `/customers`


- **Use Query Parameters for Filtering**: When filtering collections, use query parameters instead of creating new
  endpoints.
  In some cases you may see filtering and sorting information provided as a payload inside a request body.
    - **Example**: `/customers?status=active`, `/orders?customerId=123&status=pending`

## 6. Configuring `application.yaml`

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

- **In `application.yaml`**: You can specify the active profile directly in the `application.yaml` file using the
  `spring.profiles.active` property. This is useful for setting a default profile that will be used unless another is
  specified at runtime.
- **At Runtime**: You can also activate a profile at runtime by passing the `spring.profiles.active` property as a
  command-line argument or setting it as an environment variable.

### Best Practices

- **Keep It Simple**: Store common settings in the main `application.yaml` and use environment-specific files for
  configuration differences.
- **Use Profiles**: Profiles help manage different environments like development, testing, and production by allowing
  you to specify environment-specific configurations.
- **YAML Advantages**: The YAML format is preferred for its readability and ability to handle complex, hierarchical
  settings more gracefully than traditional properties files.

### Example `application.yaml` and `application-dev.yaml` files for Customer API

In this section, I provide two YAML configuration files to demonstrate some of the settings I use for a Spring Boot
project. These are just examples, and many other configurations are available depending on your project's needs. As I
continue to develop this project, I may add more configurations.

#### application.yaml

This file contains general settings that apply to all environments. Here’s a breakdown of the configurations used:

- **`server.port`**: Specifies the port on which the application will run. In this case, it’s set to `8088`. If you do
  not specify the port, the app will start on a `default port` which is `8080`.
- **`server.servlet.context-path`**: Defines the base URL path for the application. Here, it’s dynamically set to the
  artifact ID of the project.
- **`server.shutdown.graceful`**: Enables graceful shutdown, ensuring that the application completes ongoing requests
  before shutting down.
- **`spring.profiles.active`**: Indicates the active profile to be used. In this case, the development profile (`dev`)
  is active.
- **`spring.application.name`**: Sets the application name, dynamically pulled from the project configuration.
- **`spring.lifecycle.timeout-per-shutdown-phase`**: Configures the timeout for each shutdown phase, here set to
  `25 seconds`.
- **`spring.output.ansi.enabled`**: Controls ANSI output in the console, set to `always` to ensure colored output.
- **`springdoc.swagger-ui.path`**: Sets the path for accessing the Swagger UI, useful for API documentation.
- **`springdoc.title`** and **`springdoc.version`**: Define the title and version of the API documentation, again
  dynamically set based on project properties.
- **`openapi.output.file`**: Specify the file name of the swagger file that will be generated by `OpenApiConfig` class
  on start-up. It will be the documentation of the application.

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

- **`datasource.url`**: Configures the JDBC URL for the H2 database. In this example, the database is stored in a local
  file (`./data/customer-db`) with `AUTO_SERVER=true` to allow remote connections.
- **`datasource.username` and `datasource.password`**: Set the database username and password. The default username (
  `sa`) is used with no password.
- **`datasource.driver-class-name`**: Specifies the JDBC driver class for the H2 database.
- **`jpa.show-sql`**: Enables logging of SQL statements generated by Hibernate.
- **`jpa.properties.hibernate.format_sql`**: Formats SQL output to be more readable.
- **`jpa.properties.hibernate.dialect`**: Specifies the Hibernate dialect to use, which is set to `H2Dialect` for
  compatibility with the H2 database.
- **`jpa.generate-ddl`**: Automatically generates database schema (DDL) from JPA entities.
- **`jpa.hibernate.ddl-auto`**: Controls the behavior of schema generation at runtime, with `update` allowing for
  incremental updates to the schema.

### Notes:

- These configurations are just examples based on what I use in this project. There are many other configurations you
  can apply depending on your project's needs.
- As the project evolves, I may add more configurations to enhance functionality or address specific needs.
- Feel free to explore additional configurations and adjust these examples to fit your project requirements.

**Feedback and Contributions**: If you have suggestions or improvements, please share them. Collaboration is key to
refining this guide and making it a valuable resource for all developers.

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

## 7. Detailed Package Breakdown

### Entity Layer

- **Purpose**: The entity layer represents the database tables in the form of JPA entities. Each entity class typically
  maps to a single table in the database.

- **Package**: `entity`

- **Example Classes**:
    - `Customer.java`: Represents the `customers` table.

- **Key Annotations**:
    - `@Entity`: Marks a class as a JPA entity.
    - `@Table(name = "table_name")`: Specifies the name of the table in the database.
    - `@Id`: Indicates the primary key of the entity.
    - `@GeneratedValue(strategy = GenerationType.IDENTITY)`: Defines the strategy for primary key generation.

<details>
  <summary>View Customer code</summary>

```java
package com.ainigma100.customerapi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phoneNumber;

    private LocalDate dateOfBirth;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy hibernateProxy ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy hibernateProxy ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Customer customer = (Customer) o;
        return getId() != null && Objects.equals(getId(), customer.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy hibernateProxy ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
```

</details>

### Why Not Use `@Data`?

While `@Data` is a convenient annotation provided by Lombok that generates getters, setters, `toString()`, `equals()`,
and `hashCode()` methods, it is not recommended for use with JPA entities (I got this warning from JPA Buddy plugin).
Using `@Data` in JPA entities can lead to severe performance and memory consumption issues. Additionally, `@Data`
generates `equals()` and `hashCode()` methods that might not be suitable for entities, particularly in cases involving
entity relationships and lazy loading.

### Importance of `equals()` and `hashCode()`

- **`equals()`**: This method determines whether two instances are considered equal. For JPA entities, this typically
  means comparing the primary key (ID). Properly implementing `equals()` ensures that the entity behaves correctly when
  compared in collections or when managed by the persistence context.


- **`hashCode()`**: This method provides a hash code for the entity, which is essential for its use in hash-based
  collections like `HashSet` or `HashMap`. Properly implementing `hashCode()` ensures consistency and correctness when
  the entity is stored or retrieved from such collections.

For more details on why these methods are important and best practices for implementing them, you can find  
resources and discussions online.

### Additional Column Specification

You can also specify the name of the column in the database using the `@Column(name = "column_name")` annotation.
This is useful when the field name in the entity class differs from the column name in the database table.

**Example**:

```java

@Column(name = "first_name", nullable = false)
private String firstName;
```

**Note**: If you do not specify the column name and the property is in camelCase, the column name will automatically be
converted to snake_case in most databases. For example, if your entity has a field named `firstName`, it will be mapped
to a column named `first_name` by default.

<br><br>

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

<details>
  <summary>View CustomerRepository code</summary>

```java
package com.ainigma100.customerapi.repository;

import com.ainigma100.customerapi.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByEmail(String email);

}
```

</details>

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

You can write native SQL queries using the @Query annotation and providing an extra parameter `nativeQuery = true`.

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

<br><br>

### DTOs and MapStruct

DTOs (Data Transfer Objects) are used to transfer data between the service layer and the controller layer. They are
simple POJOs (Plain Old Java Objects) that contain only the necessary data and are often used to decouple the internal
entity models from the external API contract.

#### Key Concepts:

- **DTO Usage**:
    - DTOs ensure that only relevant information is exposed to the client. They help in shaping the data according to
      the needs of the client while hiding unnecessary internal details.
    - DTOs can also include validation annotations, ensuring that the data received or sent is valid according to
      business rules.


- **MapStruct for Mapping**:
    - **Automatic Mapping**: MapStruct automatically maps fields with the same name between entity classes and DTOs. For
      fields with different names, you can use the `@Mapping` annotation.
    - **Custom and Complex Mappings**: Allows custom mappings for complex scenarios, including nested objects and
      expression-based mappings.
    - **Performance**: MapStruct is efficient, generating simple, plain Java code for mappings without using reflection,
      making it faster than many other frameworks.
    - **Null Handling and Collection Mapping**: Offers control over how null values are handled and supports mapping
      between collections, such as lists of entities to lists of DTOs.
    - **Flexible Integration**: Easily integrates with Spring or other dependency injection frameworks by customizing
      the component model.

**Note**: You can find more information on MapStruct online.

Below is an example of a DTO and a corresponding MapStruct mapper interface:


<details>
  <summary>View CustomerDTO code</summary>

```java
package com.ainigma100.customerapi.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;

}
```

</details>

<details>
  <summary>View CustomerMapper code</summary>

```java
package com.ainigma100.customerapi.mapper;

import com.ainigma100.customerapi.dto.CustomerDTO;
import com.ainigma100.customerapi.entity.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer toCustomer(CustomerDTO customerDTO);

    CustomerDTO toCustomerDTO(Customer customer);

    List<Customer> toCustomerList(List<CustomerDTO> customerDTOList);

    List<CustomerDTO> toCustomerDTOList(List<Customer> customerList);

}
```

</details>


<br><br>

### Service Layer

The service layer in a Spring Boot application contains the business logic of the application. It acts as an
intermediary
between the controller layer (handling HTTP requests) and the repository layer (interacting with the database).

#### Key Concepts:

- **Interface and Implementation**: It's a good practice to define a service interface and then provide its
  implementation.
  This approach promotes loose coupling and makes your code more modular and easier to test. The interface defines the
  contract for the service, while the implementation class contains the actual business logic.

  **Example**:
    - `CustomerService`: Interface that defines methods for customer-related operations.
    - `CustomerServiceImpl`: Implementation class that provides the logic for methods like retrieving customers,
      updating customer details, etc.


- **Returning DTOs**: The service layer should not return entities directly. Instead, it should return Data Transfer
  Objects (DTOs).
  DTOs are simple objects that carry data between layers. They are particularly useful for exposing only the necessary
  data to the client and for avoiding exposing the internal structure of your entities.

<details>
  <summary>View CustomerService code</summary>

```java
package com.ainigma100.customerapi.service;

import com.ainigma100.customerapi.dto.CustomerDTO;

public interface CustomerService {

    CustomerDTO createCustomer(CustomerDTO customerDTO);

    CustomerDTO getCustomerById(Long id);

    CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO);

    void deleteCustomer(Long id);

}
```

</details>

<details>
  <summary>View CustomerServiceImpl code</summary>

```java
package com.ainigma100.customerapi.service.impl;

import com.ainigma100.customerapi.dto.CustomerDTO;
import com.ainigma100.customerapi.entity.Customer;
import com.ainigma100.customerapi.exception.ResourceAlreadyExistException;
import com.ainigma100.customerapi.exception.ResourceNotFoundException;
import com.ainigma100.customerapi.mapper.CustomerMapper;
import com.ainigma100.customerapi.repository.CustomerRepository;
import com.ainigma100.customerapi.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {

        Customer recordFromDB = customerRepository.findByEmail(customerDTO.getEmail());

        if (recordFromDB != null) {
            throw new ResourceAlreadyExistException("Customer", "email", customerDTO.getEmail());
        }

        Customer recordToBeSaved = customerMapper.toCustomer(customerDTO);

        Customer savedRecord = customerRepository.save(recordToBeSaved);

        return customerMapper.toCustomerDTO(savedRecord);
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {

        Customer recordFromDB = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));

        return customerMapper.toCustomerDTO(recordFromDB);
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {

        Customer recordFromDB = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));

        // just to be safe that the object does not have another id
        customerDTO.setId(recordFromDB.getId());

        Customer recordToBeSaved = customerMapper.toCustomer(customerDTO);

        Customer savedRecord = customerRepository.save(recordToBeSaved);

        return customerMapper.toCustomerDTO(savedRecord);
    }

    @Override
    public void deleteCustomer(Long id) {

        Customer recordFromDB = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));

        customerRepository.delete(recordFromDB);
    }
}
```

</details>


<br><br>

### Controller Layer

The controller layer in a Spring Boot application handles incoming HTTP requests and sends responses back to the client.
It acts as the entry point for the client, interacting with the service layer to process business logic and return the
appropriate data. **Note**: Do not add business logic in this class.

#### Key Concepts:

- **Using Wrapper Objects**:
    - It's a best practice to return a wrapper object from the controller rather than returning entities directly. A
      wrapper object can contain a DTO, along with metadata such as status codes, messages, or other relevant
      information.
    - **Advantages**:
        - **Encapsulation**: The wrapper object encapsulates the DTO and provides a consistent response format, which
          can be useful for clients to process responses reliably.
        - **Security**: By using DTOs inside wrapper objects, you avoid exposing the internal structure of your entities
          directly to the client. This helps in protecting sensitive information and reducing the risk of exposing
          unintended data.
        - **Flexibility**: Wrapper objects allow you to include additional information, such as error messages or
          pagination details, making your API responses more informative and easier to handle on the client side.

    - **Example**:
        - `APIResponse<CustomerDTO>`: A wrapper object that contains the `CustomerDTO` and additional metadata like
          status and messages.

    - **Wrapper Class for API Responses: `APIResponse<T>`**:
        - The `APIResponse<T>` class is a generic wrapper that can be used across different controllers in your
          application. It encapsulates the response data and adds useful metadata like status and error messages.
        - **Key Attributes**:
            - `status`: A string representing the status of the response (e.g., "SUCCESS" or "FAILED").
            - `errors`: A list of `ErrorDTO` objects that contain error details when a request fails.
            - `results`: The actual data (DTO) being returned by the API.

        - **Example**:
      ```java
      @Data
      @AllArgsConstructor
      @NoArgsConstructor
      @JsonInclude(JsonInclude.Include.NON_NULL)
      @Builder
      public class APIResponse<T> {
      
          private String status;
          private List<ErrorDTO> errors;
          private T results;
      
      }
      ```

This structured approach ensures that your application is well-organized, with clear separation of concerns between
different layers. It also makes your API more robust, secure, and easier to maintain.


<details>
  <summary>View CustomerController code</summary>

```java
package com.ainigma100.customerapi.controller;


import com.ainigma100.customerapi.dto.APIResponse;
import com.ainigma100.customerapi.dto.CustomerDTO;
import com.ainigma100.customerapi.dto.CustomerRequestDTO;
import com.ainigma100.customerapi.enums.Status;
import com.ainigma100.customerapi.mapper.CustomerMapper;
import com.ainigma100.customerapi.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
@RestController
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;


    @Operation(summary = "Add a new customer")
    @PostMapping
    public ResponseEntity<APIResponse<CustomerDTO>> createCustomer(
            @Valid @RequestBody CustomerRequestDTO customerRequestDTO) {

        CustomerDTO customerDTO = customerMapper.customerRequestDTOToCustomerDTO(customerRequestDTO);

        CustomerDTO result = customerService.createCustomer(customerDTO);

        // Builder Design pattern
        APIResponse<CustomerDTO> response = APIResponse
                .<CustomerDTO>builder()
                .status(Status.SUCCESS.getValue())
                .results(result)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @Operation(summary = "Find customer by ID",
            description = "Returns a single customer")
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<CustomerDTO>> getCustomerById(@PathVariable("id") Long id) {

        CustomerDTO result = customerService.getCustomerById(id);

        // Builder Design pattern
        APIResponse<CustomerDTO> responseDTO = APIResponse
                .<CustomerDTO>builder()
                .status(Status.SUCCESS.getValue())
                .results(result)
                .build();


        return new ResponseEntity<>(responseDTO, HttpStatus.OK);

    }


    @Operation(summary = "Update an existing customer")
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<CustomerDTO>> updateCustomer(
            @PathVariable("id") Long id,
            @Valid @RequestBody CustomerRequestDTO customerRequestDTO) {

        CustomerDTO customerDTO = customerMapper.customerRequestDTOToCustomerDTO(customerRequestDTO);

        CustomerDTO result = customerService.updateCustomer(id, customerDTO);

        // Builder Design pattern
        APIResponse<CustomerDTO> responseDTO = APIResponse
                .<CustomerDTO>builder()
                .status(Status.SUCCESS.getValue())
                .results(result)
                .build();


        return new ResponseEntity<>(responseDTO, HttpStatus.OK);

    }


    @Operation(summary = "Delete a customer by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<String>> deleteCustomer(@PathVariable("id") Long id) {

        customerService.deleteCustomer(id);

        String result = "Customer deleted successfully";

        // Builder Design pattern
        APIResponse<String> responseDTO = APIResponse
                .<String>builder()
                .status(Status.SUCCESS.getValue())
                .results(result)
                .build();


        return new ResponseEntity<>(responseDTO, HttpStatus.OK);

    }


}
```

</details>

### Exception Handling

In a Spring Boot application, it's important to handle exceptions in a way that provides meaningful feedback to the
client while maintaining a clean and maintainable codebase. In addition, you have to be sure not to expose sensitive
data.
The `GlobalExceptionHandler` class in this project serves this purpose by centralizing exception handling and ensuring
consistent error responses across the entire application.

#### Global Exception Handler

The `GlobalExceptionHandler` class, annotated with `@ControllerAdvice`, intercepts exceptions thrown by any controller
in the application. This class defines several `@ExceptionHandler` methods to handle specific types of exceptions,
ensuring that the application responds with appropriate HTTP status codes and error messages.

**Key Features of `GlobalExceptionHandler`:**

- **Runtime Exceptions**: Handles general runtime exceptions, such as `NullPointerException` and `RuntimeException`,
  returning a 500 Internal Server Error response.
- **Resource Not Found**: Manages `ResourceNotFoundException`, returning a 404 Not Found status with a relevant error
  message.
- **Business Logic and Data Exceptions**: Handles custom exceptions like `ResourceAlreadyExistException`, and
  `DataAccessException`, providing a 400 Bad Request response.
- **Validation Exceptions**: Manages exceptions related to validation, such as `MethodArgumentNotValidException` and
  `ConstraintViolationException`, returning detailed validation error messages.
- **Malformed JSON**: Handles `HttpMessageNotReadableException` to catch and respond to improperly formatted JSON in
  requests.
- **Method Not Supported**: Catches `HttpRequestMethodNotSupportedException`, responding with a 405 Method Not Allowed
  status.

#### Custom Exceptions

The application also defines several custom exceptions to manage specific error scenarios:

- **`BusinessLogicException`**: Thrown when a business rule is violated.
- **`ResourceAlreadyExistException`**: Used when an attempt is made to create a resource that already exists.
- **`ResourceNotFoundException`**: Thrown when a requested resource is not found in the database.

These custom exceptions extend `RuntimeException` and are annotated with `@ResponseStatus` to map them to specific HTTP
status codes.

#### Structured Error Responses

To ensure that error responses are consistent, the `APIResponse` class is used to structure the response body. It
includes:

- **Status**: A string indicating the outcome of the request (e.g., "FAILED").
- **Errors**: A list of `ErrorDTO` objects, each containing a `field` and an `errorMessage` to describe the issue.

This structure ensures that clients receive clear and consistent error messages, which can be easily parsed and handled.

**Note**: This approach to exception handling improves the robustness of the application, making it more maintainable
and user-friendly. For more details on how to implement and extend this global exception handler, you can refer to
additional resources or documentation available online.


<details>
  <summary>View GlobalExceptionHandler code</summary>

```java
package com.ainigma100.customerapi.exception;

import com.ainigma100.customerapi.dto.APIResponse;
import com.ainigma100.customerapi.dto.ErrorDTO;
import com.ainigma100.customerapi.enums.Status;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({RuntimeException.class, NullPointerException.class})
    public ResponseEntity<Object> handleRuntimeExceptions(RuntimeException exception) {

        log.error(exception.getMessage());

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());
        response.setErrors(Collections.singletonList(new ErrorDTO("", "An internal server error occurred")));

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<Object> handleResourceNotFoundExceptions(ResourceNotFoundException exception) {

        log.error(exception.getMessage());

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());
        response.setErrors(Collections.singletonList(new ErrorDTO("", "The requested resource was not found")));

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler({ResourceAlreadyExistException.class, DataAccessException.class})
    public ResponseEntity<Object> handleOtherExceptions(Exception exception) {

        log.error(exception.getMessage());

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());
        response.setErrors(Collections.singletonList(new ErrorDTO("", "An error occurred while processing your request")));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {

        log.error(exception.getMessage());

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());
        response.setErrors(Collections.singletonList(new ErrorDTO("", "The requested URL does not support this method")));

        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }


    @ExceptionHandler({MethodArgumentNotValidException.class, MissingServletRequestParameterException.class, MissingPathVariableException.class})
    public ResponseEntity<Object> handleValidationExceptions(Exception exception) {

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());

        List<ErrorDTO> errors = new ArrayList<>();
        if (exception instanceof MethodArgumentNotValidException ex) {

            ex.getBindingResult().getAllErrors().forEach(error -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.add(new ErrorDTO(fieldName, errorMessage));
            });

        } else if (exception instanceof MissingServletRequestParameterException ex) {

            String parameterName = ex.getParameterName();
            errors.add(new ErrorDTO("", "Required parameter is missing: " + parameterName));

        } else if (exception instanceof MissingPathVariableException ex) {

            String variableName = ex.getVariableName();
            errors.add(new ErrorDTO("", "Missing path variable: " + variableName));
        }

        response.setErrors(errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<APIResponse<ErrorDTO>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

        log.error("Malformed JSON request: {}", ex.getMessage());

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());
        response.setErrors(Collections.singletonList(new ErrorDTO("", "Malformed JSON request")));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<APIResponse<ErrorDTO>> handleConstraintViolationException(ConstraintViolationException ex) {

        List<ErrorDTO> errors = new ArrayList<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(new ErrorDTO(violation.getPropertyPath().toString(), violation.getMessage()));
        }

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());
        response.setErrors(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


}
```

</details>



<br><br>

## 8. Helper Classes

In this section, I provide some helper classes that can be reused in Spring Boot applications. These classes are
designed
to simplify common tasks such as logging, server configuration, and OpenAPI documentation generation.

### OpenApiConfig

This configuration class is responsible for generating OpenAPI documentation using SpringDoc. It fetches the OpenAPI
JSON from the application's endpoints and saves it as a formatted file.

**Key Features**:

- Automatically generates OpenAPI documentation in JSON format.
- Saves the documentation to a specified file in the project root. The output file name is specified in the
  `application.yaml` file.
- Handles both HTTP and HTTPS protocols based on the server configuration.

<details>
  <summary>View OpenApiConfig code</summary>

```java
package com.ainigma100.customerapi.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestClient;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

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
package com.ainigma100.customerapi.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class LoggingFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String clientIP = this.getClientIP(httpServletRequest);

        if (this.shouldLogRequest(httpServletRequest)) {
            log.info("Client IP: {}, Request URL: {}, Method: {}", clientIP, httpServletRequest.getRequestURL(), httpServletRequest.getMethod());
        }

        // pre methods call stamps
        chain.doFilter(request, response);

        // post method calls stamps
        if (this.shouldLogRequest(httpServletRequest)) {
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
package com.ainigma100.customerapi.filter;

import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
        filterBean.setOrder(Integer.MAX_VALUE - 2);

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

<br><br>

## 9. Testing

Testing is essential to ensure your application works as expected. This section covers how to effectively test your
Spring Boot application using Behavior Driven Development (BDD) and useful annotations for different testing scenarios.

### Behavior Driven Development (BDD) Testing

BDD focuses on writing tests that describe the system's behavior from the user's perspective. It helps improve
collaboration between developers, testers, and stakeholders. In Java, you can use JUnit with Mockito to implement BDD.

#### IntelliJ Live Template for BDD

To make writing BDD tests easier in IntelliJ IDEA, you can create a live template that generates a basic structure for
BDD tests. Here’s the template you can use:

```xml

<template name="bdd"
          value="@org.junit.jupiter.api.Test&#10;void given$NAME$_when$NAME2$_then$NAME3$() {&#10;&#10;    // given - precondition or setup&#10;    org.mockito.BDDMockito.given().willReturn();&#10;    &#10;    // when - action or behaviour that we are going to test&#10;    &#10;    &#10;    // then - verify the output&#10;&#10;}"
          description="Behaviour Driven Development (BDD) test template" toReformat="false" toShortenFQNames="true">
    <variable name="NAME" expression="" defaultValue="" alwaysStopAt="true"/>
    <variable name="NAME2" expression="" defaultValue="" alwaysStopAt="true"/>
    <variable name="NAME3" expression="" defaultValue="" alwaysStopAt="true"/>
    <context>
        <option name="JAVA_DECLARATION" value="true"/>
    </context>
</template>
```

With the above template, when you type `bdd` in your test class, IntelliJ IDEA will generate a skeleton for a BDD-style
test, helping you follow the BDD principles consistently.

In your Spring Boot project, BDD can be implemented as follows:

1. **Given**: Set up the initial context or preconditions for the test.
2. **When**: Perform the action or behavior that you want to test.
3. **Then**: Verify the expected outcome or results.

By structuring your tests this way, you ensure they are clear, concise, and focused on the behavior of the application
from the user’s perspective.

### 1. Testing the Repository Layer

The repository layer is responsible for interacting with the database. When testing this layer, focus on ensuring that
your custom query methods behave as expected. If you are only using the provided methods from `JpaRepository` without
any custom queries, you do not need to test this layer.

- **`@DataJpaTest`**: Configures an in-memory database, automatically rolling back transactions after each test. Ensure
  that you have the H2 dependency in your project for this annotation to work correctly. This annotation also limits the
  loaded beans to those required for JPA tests.

- **`@Autowired`**: Used to inject the repository instance into your test class, allowing you to call repository methods
  directly.

- **`@BeforeEach`**: Indicates that the annotated method should be run before each test method in the class. This is
  commonly used for setting up test data or initializing common objects used in multiple tests.

- **`@Test`**: The most common annotation in JUnit, marking a method as a test method that will be executed when running
  the test suite.

<details>
  <summary>View CustomerRepositoryTest code</summary>

```java
package com.ainigma100.customerapi.repository;

import com.ainigma100.customerapi.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


/*
 * @DataJpaTest will automatically configure in-memory database for testing
 * and, it will not load annotated beans into the Application Context.
 * It will only load the repository class. Tests annotated with @DataJpaTest
 * are by default transactional and roll back at the end of each test.
 */
@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer;

    /**
     * This method will be executed before each and every test inside this class
     */
    @BeforeEach
    void setUp() {

        customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Wick");
        customer.setEmail("jwick@tester.com");
        customer.setPhoneNumber("0123456789");
        customer.setDateOfBirth(LocalDate.now().minusYears(18));

    }

    @Test
    void givenValidEmail_whenFindByEmail_thenReturnCustomer() {

        // given - precondition or setup
        String email = "jwick@tester.com";
        customerRepository.save(customer);

        // when - action or behaviour that we are going to test
        Customer customerFromDB = customerRepository.findByEmail(email);

        // then - verify the output
        assertNotNull(customerFromDB);
        assertEquals(customer.getFirstName(), customerFromDB.getFirstName());
        assertEquals(customer.getLastName(), customerFromDB.getLastName());
        assertEquals(customer.getEmail(), customerFromDB.getEmail());
        assertEquals(customer.getPhoneNumber(), customerFromDB.getPhoneNumber());
        assertEquals(customer.getDateOfBirth(), customerFromDB.getDateOfBirth());
    }

    @Test
    void givenInvalidEmail_whenFindByEmail_thenReturnNothing() {

        // given - precondition or setup
        String email = "abc@tester.com";
        customerRepository.save(customer);

        // when - action or behaviour that we are going to test
        Customer customerFromDB = customerRepository.findByEmail(email);

        // then - verify the output
        assertNull(customerFromDB);
    }

    @Test
    void givenNullEmail_whenFindByEmail_thenReturnNothing() {

        // given - precondition or setup
        String email = null;
        customerRepository.save(customer);

        // when - action or behaviour that we are going to test
        Customer customerFromDB = customerRepository.findByEmail(email);

        // then - verify the output
        assertNull(customerFromDB);
    }

    @Test
    void givenEmptyEmail_whenFindByEmail_thenReturnNothing() {

        // given - precondition or setup
        String email = "";
        customerRepository.save(customer);

        // when - action or behaviour that we are going to test
        Customer customerFromDB = customerRepository.findByEmail(email);

        // then - verify the output
        assertNull(customerFromDB);
    }


}
```

</details>


<br><br>

### 2. Testing the Service Layer

The service layer contains your business logic and interacts with the repository layer. Testing this layer typically
involves mocking the repository to isolate the service logic.

- **`@ExtendWith(MockitoExtension.class)`**: Enables Mockito annotations in your test class.
- **`@InjectMocks`**: Injects the mock objects into the service class, allowing you to test the service logic
  independently of the repository layer. This annotation creates an instance of the class under test and injects the
  mock dependencies annotated with `@Mock` into it.
- **`@Mock`**: Used to create mock instances of the repository or other dependencies.
- **`@DisplayName`**: Allows you to provide a custom name for your test methods, making them more descriptive and
  readable in test reports.

<details>
  <summary>View CustomerServiceImplTest code</summary>

```java
package com.ainigma100.customerapi.service.impl;

import com.ainigma100.customerapi.dto.CustomerDTO;
import com.ainigma100.customerapi.entity.Customer;
import com.ainigma100.customerapi.exception.ResourceAlreadyExistException;
import com.ainigma100.customerapi.exception.ResourceNotFoundException;
import com.ainigma100.customerapi.mapper.CustomerMapper;
import com.ainigma100.customerapi.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/*
 * @ExtendWith(MockitoExtension.class) informs Mockito that we are using
 * mockito annotations to mock the dependencies
 */
@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    // @InjectMocks creates the mock object of the class and injects the mocks
    // that are marked with the annotations @Mock into it.
    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    private Customer customer;
    private CustomerDTO customerDTO;

    /**
     * This method will be executed before each and every test inside this class
     */
    @BeforeEach
    void setUp() {

        customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("John");
        customer.setLastName("Wick");
        customer.setEmail("jwick@tester.com");
        customer.setPhoneNumber("0123456789");
        customer.setDateOfBirth(LocalDate.now().minusYears(18));
        customer.setCreatedDate(LocalDateTime.now());
        customer.setUpdatedDate(LocalDateTime.now());

        customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setFirstName("John");
        customerDTO.setLastName("Wick");
        customerDTO.setEmail("jwick@tester.com");
        customerDTO.setPhoneNumber("0123456789");
        customerDTO.setDateOfBirth(LocalDate.now().minusYears(18));
    }


    @Test
    @DisplayName("Test creating a new customer")
    void givenCustomerDTO_whenCreateCustomer_thenReturnCustomerDTO() {

        // given - precondition or setup
        String email = customerDTO.getEmail();
        given(customerRepository.findByEmail(email)).willReturn(null);
        given(customerMapper.customerDTOToCustomer(customerDTO)).willReturn(customer);
        given(customerRepository.save(customer)).willReturn(customer);
        given(customerMapper.customerToCustomerDTO(customer)).willReturn(customerDTO);

        // when - action or behaviour that we are going to test
        CustomerDTO result = customerService.createCustomer(customerDTO);

        // then - verify the output
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(customerDTO.getFirstName());
        assertThat(result.getLastName()).isEqualTo(customerDTO.getLastName());
        assertThat(result.getEmail()).isEqualTo(customerDTO.getEmail());
        assertThat(result.getPhoneNumber()).isEqualTo(customerDTO.getPhoneNumber());

        verify(customerRepository, times(1)).findByEmail(email);
        verify(customerMapper, times(1)).customerDTOToCustomer(customerDTO);
        verify(customerRepository, times(1)).save(customer);
        verify(customerMapper, times(1)).customerToCustomerDTO(customer);

    }

    @Test
    @DisplayName("Test creating a customer with existing email throws ResourceAlreadyExistException")
    void givenExistingEmail_whenCreateCustomer_thenThrowResourceAlreadyExistException() {

        // given - precondition or setup
        String email = customerDTO.getEmail();
        given(customerRepository.findByEmail(email)).willReturn(customer);

        // when/then - verify that the ResourceAlreadyExistException is thrown
        assertThatThrownBy(() -> customerService.createCustomer(customerDTO))
                .isInstanceOf(ResourceAlreadyExistException.class)
                .hasMessageContaining("Resource Customer with email : '" + email + "' already exist");


        verify(customerRepository, times(1)).findByEmail(customerDTO.getEmail());
        verify(customerMapper, never()).customerDTOToCustomer(any(CustomerDTO.class));
        verify(customerRepository, never()).save(any(Customer.class));
        verify(customerMapper, never()).customerToCustomerDTO(any(Customer.class));

    }

    @Test
    @DisplayName("Test retrieving a customer by ID")
    void givenValidId_whenGetCustomerById_thenReturnCustomerDTO() {

        // given - precondition or setup
        Long id = 1L;
        given(customerRepository.findById(id)).willReturn(Optional.of(customer));
        given(customerMapper.customerToCustomerDTO(customer)).willReturn(customerDTO);

        // when - action or behaviour that we are going to test
        CustomerDTO result = customerService.getCustomerById(id);

        // then - verify the output
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(customerDTO.getId());
        assertThat(result.getFirstName()).isEqualTo(customerDTO.getFirstName());
        assertThat(result.getLastName()).isEqualTo(customerDTO.getLastName());
        assertThat(result.getEmail()).isEqualTo(customerDTO.getEmail());
        assertThat(result.getPhoneNumber()).isEqualTo(customerDTO.getPhoneNumber());

        verify(customerRepository, times(1)).findById(id);
        verify(customerMapper, times(1)).customerToCustomerDTO(customer);

    }


    @Test
    @DisplayName("Test retrieving a customer by invalid ID throws ResourceNotFoundException")
    void givenInvalidId_whenGetCustomerById_thenThrowResourceNotFoundException() {

        // given - precondition or setup
        Long id = 100L;
        given(customerRepository.findById(id)).willReturn(Optional.empty());

        // when/then - verify that the ResourceNotFoundException is thrown
        assertThatThrownBy(() -> customerService.getCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id : '" + id + "' not found");


        verify(customerRepository, times(1)).findById(id);
        verify(customerMapper, never()).customerToCustomerDTO(any(Customer.class));

    }


    @Test
    @DisplayName("Test updating a customer by ID")
    void givenValidIdAndCustomerDTO_whenUpdateCustomer_thenReturnUpdatedCustomerDTO() {

        // given - precondition or setup
        Long id = 1L;
        given(customerRepository.findById(id)).willReturn(Optional.of(customer));
        given(customerMapper.customerDTOToCustomer(customerDTO)).willReturn(customer);
        given(customerRepository.save(customer)).willReturn(customer);
        given(customerMapper.customerToCustomerDTO(customer)).willReturn(customerDTO);

        // when - action or behaviour that we are going to test
        CustomerDTO result = customerService.updateCustomer(id, customerDTO);

        // then - verify the output
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(customerDTO.getFirstName());
        assertThat(result.getLastName()).isEqualTo(customerDTO.getLastName());
        assertThat(result.getEmail()).isEqualTo(customerDTO.getEmail());
        assertThat(result.getPhoneNumber()).isEqualTo(customerDTO.getPhoneNumber());

        verify(customerRepository, times(1)).findById(id);
        verify(customerMapper, times(1)).customerDTOToCustomer(customerDTO);
        verify(customerRepository, times(1)).save(customer);
        verify(customerMapper, times(1)).customerToCustomerDTO(customer);

    }

    @Test
    @DisplayName("Test updating a customer by invalid ID throws ResourceNotFoundException")
    void givenInvalidIdAndCustomerDTO_whenUpdateCustomer_thenThrowResourceNotFoundException() {

        // given - precondition or setup
        Long id = 100L;
        given(customerRepository.findById(id)).willReturn(Optional.empty());

        // when/then - verify that the ResourceNotFoundException is thrown
        assertThatThrownBy(() -> customerService.updateCustomer(id, customerDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id : '" + id + "' not found");


        verify(customerRepository, times(1)).findById(id);
        verify(customerMapper, never()).customerDTOToCustomer(any(CustomerDTO.class));
        verify(customerRepository, never()).save(any(Customer.class));
        verify(customerMapper, never()).customerToCustomerDTO(any(Customer.class));

    }


    @Test
    @DisplayName("Test deleting a customer by ID")
    void givenValidId_whenDeleteCustomer_thenDeleteCustomer() {

        // given - precondition or setup
        Long id = 1L;
        given(customerRepository.findById(id)).willReturn(Optional.of(customer));
        doNothing().when(customerRepository).delete(customer);

        // when - action or behaviour that we are going to test
        customerService.deleteCustomer(id);

        // then - verify the output
        verify(customerRepository, times(1)).findById(id);
        verify(customerRepository, times(1)).delete(customer);

    }

    @Test
    @DisplayName("Test deleting a customer by invalid ID throws ResourceNotFoundException")
    void givenInvalidId_whenDeleteCustomer_thenThrowResourceNotFoundException() {

        // given - precondition or setup
        Long id = 1L;
        given(customerRepository.findById(id)).willReturn(Optional.empty());

        // when/then - verify that the ResourceNotFoundException is thrown
        assertThatThrownBy(() -> customerService.deleteCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id : '" + id + "' not found");

        verify(customerRepository, times(1)).findById(id);
        verify(customerRepository, never()).delete(any(Customer.class));

    }

}
```

</details>



<br><br>

### 3. Testing the Controller Layer

The controller layer is responsible for handling HTTP requests and returning appropriate responses. When testing this
layer, the goal is to ensure that the controller behaves correctly in response to various inputs and interactions with
its dependencies, such as services and mappers.

- **`@WebMvcTest`**: This annotation is used to load only the components required for testing the controller layer. It
  configures Spring’s testing support for MVC applications but does not load the full application context, making tests
  faster and more focused.

- **`@MockBean`**: This annotation is used to create and inject mock instances of the service layer or other
  dependencies that the controller interacts with. Mocking these dependencies ensures that the test focuses solely on
  the behavior of the controller without involving actual business logic or database interactions.

- **`@Autowired`**: This annotation is used to inject the `MockMvc` and `ObjectMapper` beans into your test class.
    - `MockMvc` is used to simulate HTTP requests and test the controller’s response without starting the server.
    - `ObjectMapper` is used for serializing and deserializing JSON objects, making it easier to work with request and
      response bodies in tests.

### Key Points:

1. **Mocking Service and Mapper**:
    - Mock the service and mapper beans to isolate the controller’s logic. By controlling the outputs of the service and
      mapper methods, you can focus your tests on the controller's behavior and ensure that it processes requests and
      responses correctly.

2. **Testing HTTP Methods**:
    - Test different HTTP methods (e.g., GET, POST, PUT, DELETE) to ensure that the controller correctly processes
      requests and returns the expected responses for each type of action.

3. **Argument Matchers**:
    - When setting up mock interactions, use `ArgumentMatchers` like `any(Class.class)` to generalize the input
      parameters, especially when you do not care about the specific value. Alternatively, use specific matchers like
      `eq()` when you want to ensure that the method is called with exact values. Choosing the right matcher depends on
      your test scenario. Understanding when to use each will make your tests more reliable.

4. **Validation of Responses**:
    - Validate the status code, headers, and response body using methods like `andExpect()`. Ensure that the response
      structure and content are what you expect. This step is crucial to verify that your API meets its contract.

5. **Use of `ResultActions`**:
   - Capture the result of the `MockMvc` request using `ResultActions`. This allows you to chain further verifications on
     the response, ensuring that all aspects of the response are as expected.

### Note:
  - When writing controller tests, it’s important to decide whether to use `ArgumentMatchers` like `any()` for flexible
  input matching or `eq()` for strict matching based on the context of your test scenario. For more details on using
  argument matchers or `eq()` in Mockito, you can search online resources or refer to Mockito documentation.


<details>
  <summary>View CustomerControllerTest code</summary>

```java
package com.ainigma100.customerapi.controller;

import com.ainigma100.customerapi.dto.CustomerDTO;
import com.ainigma100.customerapi.dto.CustomerRequestDTO;
import com.ainigma100.customerapi.enums.Status;
import com.ainigma100.customerapi.mapper.CustomerMapper;
import com.ainigma100.customerapi.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/*
 * @WebMvcTest annotation will load all the components required
 * to test the Controller layer. It will not load the service or repository layer components
 */
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CustomerService customerService;

  @MockBean
  private CustomerMapper customerMapper;

  private CustomerRequestDTO customerRequestDTO;
  private CustomerDTO customerDTO;

  @BeforeEach
  void setUp() {

    customerRequestDTO = new CustomerRequestDTO();
    customerRequestDTO.setFirstName("John");
    customerRequestDTO.setLastName("Wick");
    customerRequestDTO.setEmail("jwick@tester.com");
    customerRequestDTO.setPhoneNumber("0123456789");
    customerRequestDTO.setDateOfBirth(LocalDate.now().minusYears(18));


    customerDTO = new CustomerDTO();
    customerDTO.setId(1L);
    customerDTO.setFirstName("John");
    customerDTO.setLastName("Wick");
    customerDTO.setEmail("jwick@tester.com");
    customerDTO.setPhoneNumber("0123456789");
    customerDTO.setDateOfBirth(LocalDate.now().minusYears(18));

  }


  @Test
  void givenCustomerDTO_whenCreateCustomer_thenReturnCustomerDTO() throws Exception {

    // given - precondition or setup
    given(customerMapper.customerRequestDTOToCustomerDTO(any(CustomerRequestDTO.class)))
            .willReturn(customerDTO);

    given(customerService.createCustomer(any(CustomerDTO.class))).willReturn(customerDTO);

    // when - action or behaviour that we are going to test
    ResultActions response = mockMvc.perform(post("/api/v1/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customerRequestDTO)));

    // then - verify the output
    response.andDo(print())
            // verify the status code that is returned
            .andExpect(status().isCreated())
            // verify the actual returned value and the expected value
            // $ - root member of a JSON structure whether it is an object or array
            .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())))
            .andExpect(jsonPath("$.results.id", is(1)))
            .andExpect(jsonPath("$.results.firstName", is("John")))
            .andExpect(jsonPath("$.results.lastName", is("Wick")))
            .andExpect(jsonPath("$.results.email", is("jwick@tester.com")))
            .andExpect(jsonPath("$.results.phoneNumber", is("0123456789")))
            .andExpect(jsonPath("$.results.dateOfBirth", is(LocalDate.now().minusYears(18).toString())));
  }


  @Test
  void givenCustomerDTO_whenGetCustomerById_thenReturnCustomerDTO() throws Exception {

    // given - precondition or setup
    given(customerService.getCustomerById(any(Long.class))).willReturn(customerDTO);

    // when - action or behaviour that we are going to test
    ResultActions response = mockMvc.perform(get("/api/v1/customers/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON));

    // then - verify the output
    response.andDo(print())
            // verify the status code that is returned
            .andExpect(status().isOk())
            // verify the actual returned value and the expected value
            // $ - root member of a JSON structure whether it is an object or array
            .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())))
            .andExpect(jsonPath("$.results.id", is(1)))
            .andExpect(jsonPath("$.results.firstName", is("John")))
            .andExpect(jsonPath("$.results.lastName", is("Wick")))
            .andExpect(jsonPath("$.results.email", is("jwick@tester.com")))
            .andExpect(jsonPath("$.results.phoneNumber", is("0123456789")))
            .andExpect(jsonPath("$.results.dateOfBirth", is(LocalDate.now().minusYears(18).toString())));
  }


  @Test
  void givenCustomerDTO_whenUpdateCustomer_thenReturnCustomerDTO() throws Exception {

    // given - precondition or setup
    given(customerMapper.customerRequestDTOToCustomerDTO(any(CustomerRequestDTO.class)))
            .willReturn(customerDTO);

    given(customerService.updateCustomer(any(Long.class), any(CustomerDTO.class))).willReturn(customerDTO);

    // when - action or behaviour that we are going to test
    ResultActions response = mockMvc.perform(put("/api/v1/customers/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customerRequestDTO)));

    // then - verify the output
    response.andDo(print())
            // verify the status code that is returned
            .andExpect(status().isOk())
            // verify the actual returned value and the expected value
            // $ - root member of a JSON structure whether it is an object or array
            .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())))
            .andExpect(jsonPath("$.results.id", is(1)))
            .andExpect(jsonPath("$.results.firstName", is("John")))
            .andExpect(jsonPath("$.results.lastName", is("Wick")))
            .andExpect(jsonPath("$.results.email", is("jwick@tester.com")))
            .andExpect(jsonPath("$.results.phoneNumber", is("0123456789")))
            .andExpect(jsonPath("$.results.dateOfBirth", is(LocalDate.now().minusYears(18).toString())));
  }


  @Test
  void givenCustomerDTO_whenDeleteCustomer_thenReturnCustomerDTO() throws Exception {

    // given - precondition or setup
    willDoNothing().given(customerService).deleteCustomer(any(Long.class));

    // when - action or behaviour that we are going to test
    ResultActions response = mockMvc.perform(delete("/api/v1/customers/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON));

    // then - verify the output
    response.andDo(print())
            // verify the status code that is returned
            .andExpect(status().isOk())
            // verify the actual returned value and the expected value
            // $ - root member of a JSON structure whether it is an object or array
            .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())));
  }


}
```

</details>

<br><br>

## 10. Best Practices

**Disclaimer**: The practices outlined here reflect my personal approach based on what I have learned and observed from
various resources. While I believe these practices can help in building clean, maintainable, and scalable Spring Boot
applications, they are by no means the only way to approach development. I encourage you to explore other perspectives,
adapt these practices to your needs, and continuously evolve your methods as new tools and techniques emerge.

When developing Spring Boot applications, following best practices ensures your code is clean, maintainable, and
scalable. Below are some key practices to keep in mind:

### 1. Use DTOs to Abstract Entity Data

- **Purpose**: DTOs (Data Transfer Objects) are used to encapsulate data transferred between the client and server. By
  using DTOs, you prevent exposing your JPA entities directly to the client, which can mitigate security risks and
  decouple your API's data model from its internal domain model.
- **Implementation**:
    - Use tools like MapStruct or write custom mappers to convert between entities and DTOs.
    - Ensure that your controllers interact with services using DTOs, not entities, to maintain a clear separation of
      concerns.

### 2. Leverage Spring’s Dependency Injection

- **Purpose**: Dependency Injection (DI) allows for the automatic management of your application’s dependencies,
  promoting loose coupling and easier testing.
- **Best Practices**:
    - **Use Constructor Injection**: Prefer constructor injection over field injection as it makes your classes easier
      to test, clearly indicates the dependencies of your class, and supports immutability.
    - **Avoid Field Injection**: Field injection can lead to issues in unit testing and hides dependencies, making the
      code harder to understand and maintain.
    - **Use `@Autowired`**: Spring’s `@Autowired` annotation can be used to inject dependencies, but constructor
      injection is more explicit and recommended.

### 3. Handle Exceptions Globally

- **Purpose**: Centralized exception handling allows you to manage errors consistently across your application,
  improving the user experience and simplifying error management.
- **Implementation**:
    - Use `@ControllerAdvice` to create a global exception handler that handles exceptions thrown across the
      application.
    - Use `@ExceptionHandler` within `@ControllerAdvice` to specify custom handling logic for specific exception types.
    - Return structured error responses using a consistent format, which can be encapsulated in a DTO like
      `APIResponse`.

### 4. Organize Your Code

- **Purpose**: A well-organized codebase makes the project easier to navigate, understand, and maintain, especially as
  it grows in complexity.
- **Best Practices**:
    - **Separate Concerns**: Maintain a clean and organized project structure by separating concerns into different
      layers (e.g., controllers, services, repositories).
    - **Keep Methods Small**: Break down large methods into smaller, single-purpose methods to enhance readability and
      maintainability. Each method should do one thing and do it well.
    - **Avoid Repetition**: Follow the DRY (Don't Repeat Yourself) principle by abstracting common logic into reusable
      methods or classes.

### 5. Naming Conventions

- **Purpose**: Consistent naming conventions improve the readability and maintainability of your code, making it easier
  for other developers (and your future self) to understand the purpose of classes, methods, and variables.
- **Best Practices**:
    - **Packages**: Use lowercase and singular names for packages (e.g., `com.ainigma100.customerapi.controller`).
    - **Classes**: Follow PascalCase for class names (e.g., `CustomerService`), and ensure names are meaningful and
      descriptive.
    - **Methods**: Use camelCase for method names (e.g., `getCustomerById`) and keep method names descriptive to
      reflect their actions.
    - **Endpoints**: Use lowercase and hyphen-separated words for REST API endpoint paths (e.g., `/api/v1/customers`),
      and use plural nouns for collections (e.g., `/customers`).

### 6. Use Wrapper Objects for API Responses

- **Purpose**: Wrapping API responses in a standardized object (like `APIResponse`) ensures consistent structure,
  improves readability, and makes it easier to include additional metadata (e.g., status, errors) along with the actual
  data.
- **Implementation**:
    - **Standardized Structure**: Define a generic response class that encapsulates the response data, status, and any
      errors. This approach provides a uniform response format across all endpoints.
    - **Builder Pattern**: Use the Builder pattern to construct response objects, which enhances readability and
      flexibility by allowing you to add only the fields you need.
    - **Consistency**: Return response objects in all your controller methods to ensure that clients receive a
      consistent response format, which simplifies client-side parsing and error handling.

### 7. Break Down Complex Logic

- **Purpose**: Breaking down complex logic into smaller, manageable pieces makes your code easier to understand, test,
  and maintain.
- **Best Practices**:
    - **Refactor Large Methods**: If a method is doing too much, refactor it into smaller methods that each handle a
      specific part of the logic. This improves readability, makes your code more modular, and simplifies testing.
    - **Single Responsibility Principle (SRP)**: Ensure that each class and method has only one responsibility. This
      principle helps to make your code more focused, easier to maintain, and less prone to errors.
    - **Avoid Deep Nesting**: Deeply nested code blocks can be hard to follow and maintain. Consider early exits (using
      return statements) and breaking nested blocks into separate methods to enhance clarity.

### 8. Document Your Code

- **Purpose**: Well-documented code helps new developers understand the application quickly and ensures that the purpose
  of classes and methods is clear.
- **Best Practices**:
    - **Use Swagger for API Documentation**: Instead of using only the traditional Javadoc comments, leverage Swagger
      annotations to document your APIs. This approach provides interactive documentation that clients can use to
      understand and test your services.
    - **Descriptive Method and Property Names**: Ensure that your method and property names are self-explanatory, making
      the code easier to read and understand without requiring extensive comments.
    - **Generate and Share Swagger Documentation**: Utilize classes to generate Swagger documentation and export it as a
      file. This allows you to share the API documentation easily with others, ensuring they have the necessary
      information to interact with your services. You can reuse the classes I wrote on the current project
    - **Inline Comments**: Use inline comments sparingly to explain complex logic or to provide context about why
      certain decisions were made. Comments should add value by explaining the "why" behind the code, not the "what."
      If your code is clear on what it does, it is not mandatory to add comments.

### 9. Version Control and CI/CD

- **Purpose**: Implementing a robust version control and CI/CD (Continuous Integration/Continuous Deployment) pipeline
  ensures that your codebase is always in a deployable state and that changes are tracked, reviewed, and integrated
  systematically.
- **Best Practices**:
    - **Git**: Use Git for version control, and follow a branching strategy (e.g., GitFlow) to manage feature
      development, bug fixes, and releases. Commit often with meaningful commit messages to document the history of your
      project.
    - **Code Reviews**: Incorporate code reviews into your development process to catch issues early, share knowledge
      across the team, and maintain code quality.
    - **CI/CD Pipeline**: Set up a CI/CD pipeline using tools like Jenkins, GitHub Actions, GitLab CI, or CircleCI to
      automate the building, testing, and deployment of your application. This pipeline should include:
        - **Automated Testing**: Ensure that all tests run automatically on every commit to catch issues early.
        - **Code Quality Checks**: Integrate tools like SonarQube or Checkstyle to enforce coding standards and detect
          potential issues.
        - **Deployment Automation**: Automate the deployment process to reduce manual errors and speed up delivery.

### 10. Database Migrations with Liquibase or Flyway

- **Purpose**: Database migration tools like Liquibase and Flyway help manage schema changes in a consistent and
  controlled manner. They are particularly useful in environments where the database schema evolves over time.
- **When to Use**:
    - **Use Case**: If your application requires frequent schema changes, or if you work in a team where multiple
      developers are modifying the database, using a migration tool is essential. It ensures that all changes are
      versioned, documented, and applied consistently across different environments (development, testing, production).
    - **When Not Needed**: If your application uses a fixed schema that rarely changes, or if you're using a database
      with a predefined schema where you don't manage the tables (e.g., a third-party service), you might not need a
      migration tool. In such cases, focusing on data access rather than schema management is more appropriate.
- **Best Practices**:
    - **Version Control for Migrations**: Always check your migration scripts into version control alongside your
      application code. This ensures that schema changes are versioned with the corresponding application changes.
    - **Automate Migrations**: Integrate your migration tool into your CI/CD pipeline to ensure that migrations are
      applied automatically during deployment, reducing the risk of human error.

### 11. Use Wrapper Classes for API Responses

- **Consistency**: Using a wrapper class like `APIResponse<T>` ensures that all API responses follow a consistent
  structure, which is easier to maintain and extend.
- **Encapsulation**: By encapsulating the DTOs within a wrapper, you can easily add additional metadata like status
  codes, error messages, or pagination details without altering the core response structure.
- **Security**: Wrapping DTOs helps protect the internal structure of your entities and reduces the risk of exposing
  sensitive data.

This practice is not just about standardization but also about making your API responses more predictable and secure.

<br><br>

### Feedback and Contributions

Feedback and contributions are welcome! If you have suggestions, improvements, or additional insights, please feel free
to share. Together, we can make this a valuable resource for anyone learning Spring Boot 3.
