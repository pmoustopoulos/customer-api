# Spring Boot 3 Knowledge Sharing

This document is designed to help new Spring Boot developers understand the basics of building applications using Spring
Boot 3. It covers the structure of a sample project [Customer API](https://github.com/pmoustopoulos/customer-api),
explains the purpose of key annotations, and provides insights
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
5. [Dependency Injection in Spring Boot](#5-dependency-injection-in-spring-boot)
6. [Design Patterns: RESTful API vs. MVC](#6-design-patterns-restful-api-vs-mvc)
7. [Naming Conventions](#7-naming-conventions)
8. [Configuring `application.yaml`](#8-configuring-applicationyaml)
9. [Detailed Package Breakdown](#9-detailed-package-breakdown)
    - [Entity Layer](#entity-layer)
    - [Repository Layer](#repository-layer)
    - [Service Layer](#service-layer)
    - [DTOs and MapStruct](#dtos-and-mapstruct)
    - [Controller Layer](#controller-layer)
    - [Exception Handling](#exception-handling)
10. [Helper Classes](#10-helper-classes)
11. [Testing](#11-testing)
12. [Best Practices](#12-best-practices)
13. [Enhanced Pagination Example](#13-enhanced-pagination-example)
14. [Feedback and Contributions](#14-feedback-and-contributions)

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

- **`groupId`**: Identifies your project’s group, typically following the reverse domain name pattern of your company or
  organization. For example, if your company’s domain is `ainigma100.com`, your `groupId` might be `com.ainigma100`.
  This convention helps to ensure uniqueness across all projects globally by using a domain that the organization owns
  or controls.
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

In this section, we’ll walk through the key annotations used across the various classes in the example project,
explaining their purpose and best practices. These annotations are essential for managing dependencies, handling HTTP
requests, mapping database entities, and more. Note that we will cover annotations specific to Spring Boot separately
from those provided by Lombok.

### 1. Spring Boot Annotations

These annotations are core to Spring Boot and are used throughout the application to define its structure and behavior.

#### 1.1 Dependency Injection and Component Scanning

- **`@Autowired`**: This annotation is used for automatic dependency injection. It can be applied to constructors,
  methods, fields, and even parameters to inject dependencies wherever they are needed, not just in service classes.
  When used on a constructor, it can be omitted if the class has only one constructor.

- **`@Component`**, **`@Service`**, **`@Repository`**, **`@RestController`**: These annotations are specializations of
  `@Component` and are used to define Spring beans. They indicate that a class is a Spring-managed component and can be
  automatically detected during component scanning. `@RestController` is a specialization of `@Controller` used for
  handling web requests and returning data directly as a response (usually JSON), combining the behavior of
  `@Controller` and `@ResponseBody`.

#### 1.2 Entity Class Annotations

- **`@Entity`**: Marks the class as a JPA entity, meaning it is mapped to a database table.
- **`@Table(name = "customers")`**: Specifies the table name in the database that this entity maps to.
- **`@Id`**: Denotes the primary key of the entity.
- **`@GeneratedValue(strategy = GenerationType.IDENTITY)`**: Specifies the primary key generation strategy.
- **`@Column`**: Marks a field as a column in the database. It can include attributes like `nullable`, `unique`, and
  `length`.
- **`@CreationTimestamp`** and **`@UpdateTimestamp`**: Automatically manage the creation and update timestamps of the
  entity.

#### 1.3 Controller Class Annotations

- **`@RestController`**: Combines `@Controller` and `@ResponseBody`, used to handle HTTP requests and return responses
  directly, usually as JSON. It is the preferred annotation for RESTful web services.

- **`@RequestMapping("/api/v1/customers")`**: Maps HTTP requests to specific methods in the controller, providing a base
  path for all endpoints within the controller.

- **`@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`**: These annotations map HTTP GET, POST, PUT, and
  DELETE requests to specific methods in the controller, respectively.

- **`@Valid`**: Used to trigger validation of the request body or path variables based on constraints defined in the
  DTO.

- **`@RequestBody`**: Maps the HTTP request body to a Java object, commonly used in POST and PUT methods.

- **`@PathVariable`**: Binds a method parameter to a URI template variable, allowing extraction of values from the URL.

- **`@RequestParam`**: Binds a method parameter to a query parameter in the URL, useful for passing optional or required
  parameters to an endpoint.

#### 1.4 Configuration and Bean Management

- **`@Configuration`**: Indicates that the class contains Spring bean definitions and configuration settings.
- **`@Bean`**: Marks a method as a bean producer in Spring’s application context. This method’s return value is
  registered as a Spring bean.

#### 1.5. Event Handling

- **`@EventListener`**: This annotation is used to mark a method as an event listener, which listens for specific events
  within the Spring application lifecycle. For example, `@EventListener(ApplicationReadyEvent.class)` triggers when the
  application is ready to service requests.

### 2. Lombok Annotations

Lombok is a Java library that helps reduce boilerplate code by automatically generating common code like constructors,
getters, setters, etc. It is not part of Spring Boot but is often used alongside it for convenience.

- **`@Slf4j`**: Creates a `Logger` instance in the class, allowing for easy logging without manually defining a logger.

- **`@AllArgsConstructor`, `@NoArgsConstructor`, and `@RequiredArgsConstructor`**:
    - **`@AllArgsConstructor`**: Generates a constructor with parameters for all fields in the class.
    - **`@NoArgsConstructor`**: Generates a no-argument constructor.
    - **`@RequiredArgsConstructor`**: Generates a constructor for all final fields and any fields marked as `@NonNull`.

- **`@Data`**: Generates getters, setters, `equals()`, `hashCode()`, `toString()`, and other utility methods. While it
  is suitable for DTOs and simple data carrier classes, it is generally not recommended for JPA entities due to
  potential performance issues and complications with `equals()` and `hashCode()` methods.

- **`@Builder`**: Implements the builder pattern, making it easy to create immutable objects with only the required
  fields set.

### Additional Notes

There are many more annotations in Spring Boot that you might encounter as your application grows or as you write
tests (unit tests, integration tests, etc.). Each layer of the application (controller, service, repository, etc.) and
each use case (security, data validation, testing) has specific annotations that help to streamline development and
improve code quality. This section covers the key annotations used in this project, providing a solid foundation for
understanding how they work together in a Spring Boot application.

<br><br>

## 5. Dependency Injection in Spring Boot

Dependency Injection (DI) is a core concept in Spring that allows your classes to be loosely coupled. This means that
instead of your classes creating their dependencies, Spring will provide the dependencies they need. This makes your
code easier to manage, test, and maintain.

### Why Use Dependency Injection?

- **Simplifies Code**: You don’t need to create objects manually.
- **Easier Testing**: You can easily swap out dependencies with mock objects during testing.
- **Loose Coupling**: Your classes depend on abstractions (interfaces) rather than concrete implementations.

### Constructor Injection

Constructor injection is the preferred way to inject dependencies in Spring Boot. This means that you pass the
dependencies into a class through its constructor. This makes your classes more straightforward and ensures all
necessary dependencies are available when the object is created.

Here’s how it looks:

```java

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    // Constructor Injection
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
}
```

In the example above, CustomerService depends on CustomerRepository. Spring automatically provides CustomerRepository
when creating a CustomerService instance.

### Using Lombok to Simplify Constructor Injection

In this project, I am using Lombok annotations to reduce boilerplate code. With Lombok, you can avoid writing
constructors manually by using the @RequiredArgsConstructor annotation. This automatically creates a constructor for all
final fields and autowires the dependencies.

```java

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    // Lombok automatically creates the constructor for you!
}
```

### The @Autowired Annotation

The `@Autowired` annotation tells Spring to automatically inject the required dependencies. In constructor injection, if
you have only one constructor, Spring will automatically inject dependencies without needing @Autowired.

#### Without using Lombok annotation

If you are not using Lombok, you can still use constructor injection like this:

```java

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired  // Optional with a single constructor
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
}
```

### Why Constructor Injection is Better

- **Clearer Dependencies**: It’s obvious which dependencies your class needs.
- **Immutable Fields**: Dependencies can be marked as `final`, ensuring they aren’t changed after they’re set.
- **Easier to Test**: You can easily provide mock dependencies when testing.

**Note:** Please search online for more details and try to understand this topic because it is important.



<br><br>

## 6. Design Patterns: RESTful API vs. MVC

When building applications with Spring Boot, it's essential to understand the different design patterns that can be used
to structure your application. The two most common patterns are **RESTful API** and **Model-View-Controller (MVC)**.
Each serves a different purpose and is chosen based on the needs of the application.

### 1. RESTful API (Service-Oriented Architecture)

- **Pattern Name:** **RESTful API** or **Service-Oriented Architecture (SOA)**
- **Annotation:** `@RestController`
- **Purpose:** The RESTful API pattern is designed to expose data and services over HTTP. In this architecture, the
  server does not concern itself with the presentation layer (UI). Instead, it focuses on delivering data, usually in
  JSON or XML format, which is consumed by a client (like a frontend framework such as Angular, React, or Vue.js).
- **Use Case:** This approach is ideal for applications where the frontend is developed separately from the backend. It
  allows for flexibility, as the frontend can be updated independently of the backend, and the same backend can serve
  multiple clients (web, mobile, etc.).

### 2. Model-View-Controller (MVC)

- **Pattern Name:** **Model-View-Controller (MVC)**
- **Annotation:** `@Controller`
- **Purpose:** The MVC pattern is a traditional approach where the server is responsible for both processing data and
  rendering the user interface. The `@Controller` annotation is used to handle HTTP requests and return views (typically
  HTML) that are rendered on the server side using templating engines like Thymeleaf.
- **Use Case:** MVC is suitable for monolithic applications where the server-side code manages both the business logic
  and the presentation logic. It’s often used in applications where the frontend is tightly coupled with the backend,
  and there's less need for a separate API layer.

### Differences Between RESTful API and MVC

- **Separation of Concerns:**
    - **RESTful API:** Decouples the backend from the frontend. The server provides data, while the client handles the
      presentation.
    - **MVC:** The server manages both data processing and presentation, offering a tightly integrated solution.

- **Flexibility:**
    - **RESTful API:** Offers greater flexibility as the backend can serve multiple types of clients, and the frontend
      can be updated independently.
    - **MVC:** Less flexible because the frontend and backend are tightly coupled, making it harder to update one
      without impacting the other.

- **Scalability:**
    - **RESTful API:** Easier to scale horizontally as the server's responsibilities are limited to providing data.
    - **MVC:** Can be more challenging to scale because the server handles both the data and the presentation logic.

- **Development Speed:**
    - **RESTful API:** Faster for teams working on large applications with separate frontend and backend teams.
    - **MVC:** Faster for small to medium-sized projects where one team handles both frontend and backend, and where
      integrating the two layers is straightforward.

### Choosing the Right Pattern

The choice between RESTful API and MVC depends on your project requirements:

- **Use RESTful API** if:
    - Your frontend is built with modern JavaScript frameworks.
    - You need to serve multiple types of clients.
    - You prefer a decoupled architecture that allows for more flexibility and easier maintenance.

- **Use MVC** if:
    - Your application is relatively simple, and the frontend is tightly coupled with the backend.
    - You want to leverage server-side rendering for better SEO or faster initial page loads.
    - You’re building a monolithic application where integrating UI and backend logic is straightforward and beneficial.

## 7. Naming Conventions

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

## 8. Configuring `application.yaml`

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

## 9. Detailed Package Breakdown

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

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());
        response.setErrors(Collections.singletonList(new ErrorDTO("", "An internal server error occurred")));

        log.error("RuntimeException or NullPointerException occurred {}", exception.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<Object> handleResourceNotFoundExceptions(ResourceNotFoundException exception) {

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());
        response.setErrors(Collections.singletonList(new ErrorDTO("", "The requested resource was not found")));

        log.error("ResourceNotFoundException occurred {}", exception.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler({ResourceAlreadyExistException.class, DataAccessException.class})
    public ResponseEntity<Object> handleOtherExceptions(Exception exception) {

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());
        response.setErrors(Collections.singletonList(new ErrorDTO("", "An error occurred while processing your request")));

        log.error("ResourceAlreadyExistException or DataAccessException occurred {}", exception.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());
        response.setErrors(Collections.singletonList(new ErrorDTO("", "The requested URL does not support this method")));

        log.error("HttpRequestMethodNotSupportedException occurred {}", exception.getMessage());

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

        log.error("Validation errors: {}", errors);

        response.setErrors(errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<APIResponse<ErrorDTO>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());
        response.setErrors(Collections.singletonList(new ErrorDTO("", "Malformed JSON request")));

        log.error("Malformed JSON request: {}", ex.getMessage());

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

        log.error("Constraint violation errors: {}", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


}
```

</details>



<br><br>

## 10. Helper Classes

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

## 11. Testing

Testing is essential to ensure your application works as expected. This section will cover how to effectively test your
Spring Boot application using both unit testing and integration testing strategies, including Behavior Driven
Development (BDD).

### Unit Testing

Unit testing is a method of testing individual units or components of the software in isolation. The main goal is to
validate that each unit of the software performs as expected. A "unit" is typically the smallest piece of code that can
be logically isolated, such as a function, method, or class.

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

### Testing the Repository Layer

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

### Testing the Service Layer

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

### Testing the Controller Layer

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
    - Capture the result of the `MockMvc` request using `ResultActions`. This allows you to chain further verifications
      on
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

### Integration Testing

Integration Testing is the phase in software testing where individual units or components of an application are combined
and tested as a group. The main goal of integration testing is to verify the interactions between different modules and
to ensure that they work together as expected. In a Spring Boot application, this typically involves testing the full
stack, including the controller, service, repository layers, and the actual database.

### Using Docker with Testcontainers

In this project, we use [Testcontainers](https://testcontainers.com/) to facilitate integration testing with a real
PostgreSQL database running in a
Docker container. This ensures that our tests run in an environment that closely mirrors production.

#### Note

To run these integration tests, Docker needs to be running on your machine. However, if Docker is not available or
running, the tests will automatically be skipped. This is managed by the following annotation in the test class:

```java
@Testcontainers(disabledWithoutDocker = true)
```

**What This Means:**

- **If Docker is running:** The integration tests will run as usual.
- **If Docker is not running:** The tests won't run, so you won't get any errors related to Docker not being available.

This setup ensures that you won't face issues with integration tests if you don’t have Docker running. It’s a way to
avoid unnecessary errors and make sure you can keep working without interruptions, especially if you're new and still
setting up your environment.

If you need to run the integration tests, just make sure Docker is installed and running on your machine.

<br><br>

## 12. Best Practices

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
    - **Methods**: Use camelCase for method names (e.g., `getCustomerById`) and keep method names descriptive to reflect
      their actions.
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
      information to interact with your services. You can reuse the classes I wrote in the current project.
    - **Inline Comments**: Use inline comments sparingly to explain complex logic or to provide context about why
      certain decisions were made. Comments should add value by explaining the "why" behind the code, not the "what." If
      your code is clear on what it does, it is not mandatory to add comments.

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

### 11. Static Code Analysis with SonarQube

- **Purpose**: SonarQube helps improve code quality by automatically detecting issues like bugs, security
  vulnerabilities, and code smells.

- **Best Practices**:
    - **Integrate into CI/CD**: Add SonarQube to your CI/CD pipeline so that your code is checked for quality every time
      you commit or create a pull request.
    - **Set Quality Gates**: Define thresholds for things like code coverage and bugs in SonarQube to ensure your code
      meets quality standards before it’s merged.
    - **Act on Reports**: Regularly review SonarQube reports and fix any issues it finds, focusing on critical problems
      first.
    - **Team Awareness**: Make sure your team understands how to use SonarQube reports to improve their code.
    - **Manage Technical Debt**: Use SonarQube to track and reduce technical debt by identifying areas that need
      refactoring.

<br><br>

## 13. Enhanced Pagination Example

Pagination is an essential feature when dealing with large datasets in any application. It helps in breaking down large
amounts of data into manageable chunks, improving both performance and user experience. In this section, I will walk
through an enhanced pagination implementation using Spring Boot that you can adapt for your own projects.

This example will demonstrate how to retrieve paginated and sorted customer data from the database, leveraging custom
search criteria.

### CustomerSearchCriteriaDTO

The `CustomerSearchCriteriaDTO` class is used to encapsulate the search criteria that the client sends to the server.
This DTO not only includes pagination parameters like page and size but also allows for sorting and filtering based on
various fields like firstName, lastName, email, etc.

It’s important to note that all the search fields `firstName`, `lastName`, `email`, `phoneNumber`, and `dateOfBirth` are
optional. You can use them to filter records based on the criteria you need. If no filtering is needed, you can simply
pass the pagination and sorting details.

<details>
  <summary>View CustomerSearchCriteriaDTO code</summary>

```java
package com.ainigma100.customerapi.dto;

import com.ainigma100.customerapi.utils.SortItem;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
public class CustomerSearchCriteriaDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;

    @NotNull(message = "page cannot be null")
    @PositiveOrZero(message = "page must be a zero or a positive number")
    private Integer page;

    @Schema(example = "10")
    @NotNull(message = "size cannot be null")
    @Positive(message = "size must be a positive number")
    private Integer size;

    private List<SortItem> sortList;

}
```

</details>



For example, you can send a request like this:

```json
{
  "page": 0,
  "size": 10,
  "sortList": [
    {
      "field": "id",
      "direction": "ASC"
    }
  ]
}
```

Assuming you have this record in your database:

```json
{
  "id": 2,
  "firstName": "marco",
  "lastName": "polo",
  "email": "mpolo@gmail.com",
  "phoneNumber": "1234567891",
  "dateOfBirth": "2004-08-13"
}
```

If you need to filter only by firstName, you can send a request like this:

```json
{
  "firstName": "marco",
  "page": 0,
  "size": 10,
  "sortList": [
    {
      "field": "id",
      "direction": "ASC"
    }
  ]
}
```

**Note**: You can experiment with different combinations of filters and pagination parameters based on your needs.

### Repository Query for Pagination and Filtering

The following query is responsible for handling pagination and filtering.

```java

@Query(value = """
        select cus from Customer cus
        where ( :#{#criteria.firstName} IS NULL OR LOWER(cus.firstName) LIKE LOWER( CONCAT(:#{#criteria.firstName}, '%') ) )
        and ( :#{#criteria.lastName} IS NULL OR LOWER(cus.lastName) LIKE LOWER( CONCAT(:#{#criteria.lastName}, '%') ) )
        and ( :#{#criteria.email} IS NULL OR LOWER(cus.email) LIKE LOWER( CONCAT('%', :#{#criteria.email}, '%') ) )
        and ( :#{#criteria.phoneNumber} IS NULL OR LOWER(cus.phoneNumber) LIKE LOWER( CONCAT('%', :#{#criteria.phoneNumber}, '%') ) )
        and ( :#{#criteria.dateOfBirth} IS NULL OR cus.dateOfBirth = :#{#criteria.dateOfBirth} )
        """)
Page<Customer> getAllCustomersUsingPagination(
        @Param("criteria") CustomerSearchCriteriaDTO customerSearchCriteriaDTO,
        Pageable pageable);
```

### Query Analysis

The query allows you to filter customer data based on the criteria you provide. Here’s how it works:

- **Flexible Filters**: Each field, like `firstName` or `email`, can be used to filter results. If you don’t need a
  specific filter, you can leave it out, and the query will ignore that field.
- **Examples**:
    - **Filtering by First Name**: If you provide a `firstName`, the query looks for customers whose names start with
      that value.
    - **Flexible Email Search**: The query can find customers even if you provide only part of their email address.

This approach makes the query easy to use and flexible for different search needs.

### Utils Class

To facilitate pagination and sorting, a utility class is often needed. Below is an example of a utility class that helps
in creating pageable objects based on the SortItem list provided in the DTO.

<details>
  <summary>View Utils code</summary>

```java
package com.ainigma100.customerapi.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
public class Utils {

    // Private constructor to prevent instantiation
    private Utils() {
        throw new IllegalStateException("Utility class");
    }


    /**
     * Retrieves a value from a Supplier or sets a default value if a NullPointerException occurs.
     * Usage example:
     *
     * <pre>{@code
     * // Example 1: Retrieve a list or provide an empty list if null
     * List<Employee> employeeList = Utils.retrieveValueOrSetDefault(() -> someSupplierMethod(), new ArrayList<>());
     *
     * // Example 2: Retrieve an Employee object or provide a default object if null
     * Employee emp = Utils.retrieveValueOrSetDefault(() -> anotherSupplierMethod(), new Employee());
     * }</pre>
     *
     * @param supplier     the Supplier providing the value to retrieve
     * @param defaultValue the default value to return if a NullPointerException occurs
     * @return the retrieved value or the default value if a NullPointerException occurs
     * @param <T>          the type of the value
     */
    public static <T> T retrieveValueOrSetDefault(Supplier<T> supplier, T defaultValue) {

        try {
            return supplier.get();

        } catch (NullPointerException ex) {

            log.error("Error while retrieveValueOrSetDefault {}", ex.getMessage());

            return defaultValue;
        }
    }


    public static Pageable createPageableBasedOnPageAndSizeAndSorting(List<SortItem> sortList, Integer page, Integer size) {

        List<Sort.Order> orders = new ArrayList<>();

        if (sortList != null) {
            // iterate the SortList to see based on which attributes we are going to Order By the results.
            for (SortItem sortValue : sortList) {
                orders.add(new Sort.Order(sortValue.getDirection(), sortValue.getField()));
            }
        }


        return PageRequest.of(
                Optional.ofNullable(page).orElse(0),
                Optional.ofNullable(size).orElse(10),
                Sort.by(orders));
    }

}
```

</details>

### SortItem

The SortItem class encapsulates the sorting criteria, including the field to be sorted and the direction (ascending or
descending).

<details>
  <summary>View SortItem code</summary>

```java
package com.ainigma100.customerapi.utils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

@Getter
public class SortItem implements Serializable {


    @Schema(example = "id") // set a default sorting property for swagger
    private String field;
    private Sort.Direction direction;

}
```

</details>


**Note**: You can check the implementation and the testing of this feature by reading the code.

By implementing enhanced pagination, you ensure that your application can efficiently handle large datasets while
providing users with a responsive experience. This approach is versatile and can be adapted to various other scenarios
in your application.

<br><br>

## 14. Feedback and Contributions

Feedback and contributions are welcome! If you have suggestions, improvements, or additional insights, please feel free
to share. Together, we can make this a valuable resource for anyone learning Spring Boot 3.

