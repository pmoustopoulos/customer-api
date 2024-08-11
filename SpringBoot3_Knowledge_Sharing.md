# Spring Boot 3 Knowledge Sharing

This document is designed to help new Spring Boot developers understand the basics of building applications using Spring 
Boot 3. It covers the structure of a sample project, explains the purpose of key annotations, and provides insights into 
best practices. 

Note: **Feedback and contributions are welcome to make this a more comprehensive resource for learners**.

## Table of Contents
1. [Introduction](#1-introduction)
2. [Project Structure Overview](#2-project-structure-overview)
3. [Understanding `pom.xml`](#3-understanding-pomxml)
4. [Configuring `application.yaml`](#4-configuring-applicationyaml)
5. [Detailed Package Breakdown](#5-detailed-package-breakdown)
    - [Entity Layer](#entity-layer)
    - [Repository Layer](#repository-layer)
    - [Service Layer](#service-layer)
    - [DTOs and MapStruct](#dtos-and-mapstruct)
    - [Controller Layer](#controller-layer)
6. [Key Annotations in Spring Boot](#6-key-annotations-in-spring-boot)
7. [Naming Conventions](#7-naming-conventions)
8. [Running the Application Without an IDE](#8-running-the-application-without-an-ide)
9. [Security (Under Construction)](#9-security-under-construction)
10. [Testing (Under Construction)](#10-testing-under-construction)
11. [Best Practices](#11-best-practices)
12. [Conclusion](#12-conclusion)

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




## 3. Understanding `pom.xml`

The `pom.xml` file is the heart of any Maven-based project. It defines the project structure, manages dependencies, 
plugins, and dictates the build process. A well-configured `pom.xml` ensures that your project is easy to build, 
maintain, and deploy.

### Key Sections of `pom.xml`:

- **Project Coordinates**:
    - **`groupId`**: Typically represents the organization or group responsible for the project (e.g., `com.ainigma100`).
    - **`artifactId`**: Represents the name of the project or module. It’s common practice to use your domain name in reverse as the `groupId`, followed by a descriptive name for the `artifactId` (e.g., `com.ainigma100.customer-api`).
    - **`version`**: Specifies the current version of the project. It helps in managing project updates and dependencies (e.g., `1.0.0-SNAPSHOT` for a version in development).

**Example**:
```xml
<groupId>com.ainigma100</groupId>
<artifactId>customer-api</artifactId>
<version>1.0.0-SNAPSHOT</version>
```


## 4. Configuring `application.yaml`

The `application.yaml` file is used to configure your Spring Boot application. It externalizes configuration, making your application easier to manage and adapt to different environments.

### Example `application.yaml` File:

- **Server Configuration**: Sets the port number and base URL path for the application.
- **Spring Configuration**: Configures database connections, JPA settings, and Swagger UI.
- **Flyway Configuration**: Manages database migrations.
- **H2 Database Console**: Enables the H2 database console for development.

## 5. Detailed Package Breakdown

### Entity Layer

- **Purpose**: The entity layer represents the database tables in the form of JPA entities. Each entity class typically maps to a single table in the database.
- **Package**: `entity`
- **Example Classes**:
    - `Customer.java`: Represents the `customers` table.
- **Key Annotations**:
    - `@Entity`: Marks a class as a JPA entity.
    - `@Table(name = "table_name")`: Specifies the name of the table in the database.
    - `@Id`: Indicates the primary key of the entity.
    - `@GeneratedValue(strategy = GenerationType.IDENTITY)`: Defines the strategy for primary key generation.

### Repository Layer

- **Purpose**: The repository layer provides CRUD operations on the entities using Spring Data JPA. It abstracts the data access layer, allowing you to interact with the database without writing SQL queries.
- **Package**: `repository`
- **Example Classes**:
    - `DepartmentRepository.java`: Handles CRUD operations for the `Department` entity.
    - `EmployeeRepository.java`: Handles CRUD operations for the `Employee` entity.
- **Key Annotations**:
    - `@Repository`: Indicates that the class is a Spring Data repository.
    - `extends JpaRepository<T, ID>`: Provides basic CRUD operations and query methods.

### Service Layer

- **Purpose**: The service layer contains business logic, providing an abstraction layer between the controller and repository layers. Services are where most of the application's logic and rules are implemented.
- **Package**: `service`
    - **`impl` Subpackage**: Contains the implementations of the service interfaces.
- **Example Classes**:
    - `DepartmentService.java`: Interface defining business operations related to departments.
    - `EmployeeService.java`: Interface defining business operations related to employees.
    - `DepartmentServiceImpl.java`: Implements `DepartmentService`, containing the actual business logic.
    - `EmployeeServiceImpl.java`: Implements `EmployeeService`, containing the actual business logic.
- **Key Annotations**:
    - `@Service`: Marks a class as a service component.
    - `@Autowired`: Used to inject dependencies into the service.

### DTOs and MapStruct

- **Purpose**: Data Transfer Objects (DTOs) are used to transfer data between the service and controller layers, encapsulating the data and preventing direct exposure of entity objects. MapStruct is a tool used to automatically map between DTOs and entities.
- **Package**: `dto`
- **Example Classes**:
    - `DepartmentDTO.java`: DTO representing department data in API responses.
    - `EmployeeDTO.java`: DTO representing employee data in API responses.
- **Package**: `mapper`
    - Contains mappers for converting between entities and DTOs.
    - **Example Classes**:
        - `DepartmentMapper.java`: Maps between `Department` and `DepartmentDTO`.
        - `EmployeeMapper.java`: Maps between `Employee` and `EmployeeDTO`.
- **Key Annotations**:
    - `@Mapper`: Used by MapStruct to indicate that the interface will be used for mapping.
    - `@Mapping`: Specifies custom mapping rules.

### Controller Layer

- **Purpose**: The controller layer handles incoming HTTP requests and maps them to the appropriate service methods. Controllers are responsible for returning responses to the client.
- **Package**: `controller`
- **Example Classes**:
    - `DepartmentController.java`: Handles requests related to departments (e.g., GET, POST, DELETE).
    - `EmployeeController.java`: Handles requests related to employees (e.g., GET, POST, DELETE).
- **Key Annotations**:
    - `@RestController`: Marks a class as a RESTful web service controller.
    - `@RequestMapping`: Maps HTTP requests to specific handler methods.
    - `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`: Handle specific HTTP methods (GET, POST, PUT, DELETE).
    - `@RequestBody`: Binds the HTTP request body to a DTO.

## 6. Key Annotations in Spring Boot

Spring Boot makes extensive use of annotations to define and manage the behavior of various components. Here’s a quick overview:

- **`@SpringBootApplication`**: Combines `@Configuration`, `@EnableAutoConfiguration`, and `@ComponentScan`. It’s typically placed on the main class to enable auto-configuration and component scanning.
- **`@Entity`**: Used on classes that map to database tables.
- **`@Repository`**: Indicates that the class is a Spring Data repository.
- **`@Service`**: Marks a class as a service component.
- **`@RestController`**: Combines `@Controller` and `@ResponseBody`, making it easier to build RESTful web services.
- **`@Autowired`**: Used to inject dependencies into components, services, or repositories.
- **`@RequestMapping`, `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`**: Map HTTP requests to specific controller methods.
- **`@Mapper`**: Used by MapStruct to generate mappers for converting between entities and DTOs.

## 7. Naming Conventions

### Package Naming
- **Lowercase and Singular**: Package names should be in lowercase and singular (e.g., `com.ainigma100.departmentapi.controller`).
- **No Special Characters**: Avoid using special characters or underscores.

### Class Naming
- **PascalCase**: Class names should follow PascalCase (e.g., `DepartmentService`).
- **Meaningful Names**: Choose descriptive names that clearly indicate the purpose of the class.

### Entity Naming
- **Singular Form**: Entity classes should be named in the singular form (e.g., `Department`).
- **Mapped to Plural Table Names**: Entities often map to plural table names (e.g., `departments`).

### API Endpoint Naming
- **Plural Nouns**: Use plural nouns for API endpoints to represent collections of resources (e.g., `/departments`).
- **Lowercase with Hyphens**: Endpoint paths should be lowercase, with hyphens separating words (e.g., `/employee-reports`).

## 8. Running the Application Without an IDE

If you don't have an IDE, you can still run the Spring Boot application using Maven from the command line.

### Steps:

1. **Navigate to the Project Directory**:
    - Open your terminal or command prompt and navigate to the root directory of your Spring Boot project where the `pom.xml` file is located.

2. **Build the Project**:
    - Run the following command to build the project:
      ```
      mvn clean install
      ```

3. **Run the Application**:
    - Once the build is successful, run the application using:
      ```
      mvn spring-boot:run
      ```

4. **Access the Application**:
    - Open your web browser and go to `http://localhost:8080/` (or the port specified in your `application.yaml`).

## 9. Security (Under Construction)

### Purpose:
- This section will cover how to secure your Spring Boot application using Spring Security.

### Topics to be Covered:
- Basic authentication
- Role-based access control
- Method-level security
- JWT tokens

**Note**: This section is under construction and will be developed further.

## 10. Testing (Under Construction)

### Purpose:
- This section will discuss testing strategies and tools for Spring Boot applications.

### Topics to be Covered:
- Unit testing with JUnit and Mockito
- Integration testing
- Testcontainers for database testing

**Note**: This section is under construction and will be developed further.

## 11. Best Practices

When developing Spring Boot applications, adhering to best practices ensures your code is clean, maintainable, and scalable. Below are some key practices to keep in mind:

### 1. Use DTOs to Abstract Entity Data

- **Purpose**: DTOs (Data Transfer Objects) are used to encapsulate data transferred between the client and server. By using DTOs, you prevent exposing your JPA entities directly to the client, which can mitigate security risks and decouple your API's data model from its internal domain model.
- **Implementation**:
    - Use tools like MapStruct or write custom mappers to convert between entities and DTOs.
    - Ensure that your controllers interact with services using DTOs, not entities, to maintain a clear separation of concerns.

### 2. Leverage Spring’s Dependency Injection

- **Purpose**: Dependency Injection (DI) allows for the automatic management of your application’s dependencies, promoting loose coupling and easier testing.
- **Best Practices**:
    - **Use Constructor Injection**: Prefer constructor injection over field injection as it makes your classes easier to test, clearly indicates the dependencies of your class, and supports immutability.
    - **Avoid Field Injection**: Field injection can lead to issues in unit testing and hides dependencies, making the code harder to understand and maintain.
    - **Use `@Autowired`**: Spring’s `@Autowired` annotation can be used to inject dependencies, but constructor injection is more explicit and recommended.

### 3. Handle Exceptions Globally

- **Purpose**: Centralized exception handling allows you to manage errors consistently across your application, improving the user experience and simplifying error management.
- **Implementation**:
    - Use `@ControllerAdvice` to create a global exception handler that handles exceptions thrown across the application.
    - Use `@ExceptionHandler` within `@ControllerAdvice` to specify custom handling logic for specific exception types.
    - Return structured error responses using a consistent format, which can be encapsulated in a DTO like `APIResponse`.

### 4. Organize Your Code

- **Purpose**: A well-organized codebase makes the project easier to navigate, understand, and maintain, especially as it grows in complexity.
- **Best Practices**:
    - **Separate Concerns**: Maintain a clean and organized project structure by separating concerns into different layers (e.g., controllers, services, repositories).
    - **Keep Methods Small**: Break down large methods into smaller, single-purpose methods to enhance readability and maintainability. Each method should do one thing and do it well.
    - **Avoid Repetition**: Follow the DRY (Don't Repeat Yourself) principle by abstracting common logic into reusable methods or classes.

### 5. Naming Conventions

- **Purpose**: Consistent naming conventions improve the readability and maintainability of your code, making it easier for other developers (and your future self) to understand the purpose of classes, methods, and variables.
- **Best Practices**:
    - **Packages**: Use lowercase and singular names for packages (e.g., `com.ainigma100.departmentapi.controller`).
    - **Classes**: Follow PascalCase for class names (e.g., `DepartmentService`), and ensure names are meaningful and descriptive.
    - **Methods**: Use camelCase for method names (e.g., `getDepartmentById`) and keep method names descriptive to reflect their actions.
    - **Endpoints**: Use lowercase and hyphen-separated words for REST API endpoint paths (e.g., `/api/v1/departments`), and use plural nouns for collections (e.g., `/departments`).

### 6. Use Wrapper Objects for API Responses

- **Purpose**: Wrapping API responses in a standardized object (like `APIResponse`) ensures consistent structure, improves readability, and makes it easier to include additional metadata (e.g., status, errors) along with the actual data.
- **Implementation**:
    - **Standardized Structure**: Define a generic response class that encapsulates the response data, status, and any errors. This approach provides a uniform response format across all endpoints.
    - **Builder Pattern**: Use the Builder pattern to construct response objects, which enhances readability and flexibility by allowing you to add only the fields you need.
    - **Consistency**: Return response objects in all your controller methods to ensure that clients receive a consistent response format, which simplifies client-side parsing and error handling.

### 7. Break Down Complex Logic

- **Purpose**: Breaking down complex logic into smaller, manageable pieces makes your code easier to understand, test, and maintain.
- **Best Practices**:
    - **Refactor Large Methods**: If a method is doing too much, refactor it into smaller methods that each handle a specific part of the logic. This improves readability, makes your code more modular, and simplifies testing.
    - **Single Responsibility Principle (SRP)**: Ensure that each class and method has only one responsibility. This principle helps to make your code more focused, easier to maintain, and less prone to errors.
    - **Avoid Deep Nesting**: Deeply nested code blocks can be hard to follow and maintain. Consider early exits (using return statements) and breaking nested blocks into separate methods to enhance clarity.

### 8. Document Your Code

- **Purpose**: Well-documented code helps new developers understand the application quickly and ensures that the purpose of classes and methods is clear.
- **Best Practices**:
    - **Use Javadoc**: Add Javadoc comments to your classes, methods, and important fields, explaining what they do, what parameters they take, and what they return. This documentation is especially useful for public APIs or libraries.
    - **Inline Comments**: Use inline comments sparingly to explain complex logic or to provide context about why certain decisions were made. Comments should add value by explaining the "why" behind the code, not the "what."
    - **Consistent Style**: Follow a consistent commenting style throughout your codebase. This includes using the same format for Javadoc and inline comments to maintain readability and professionalism.

### 9. Version Control and CI/CD

- **Purpose**: Implementing a robust version control and CI/CD (Continuous Integration/Continuous Deployment) pipeline ensures that your codebase is always in a deployable state and that changes are tracked, reviewed, and integrated systematically.
- **Best Practices**:
    - **Git**: Use Git for version control, and follow a branching strategy (e.g., GitFlow) to manage feature development, bug fixes, and releases. Commit often with meaningful commit messages to document the history of your project.
    - **Code Reviews**: Incorporate code reviews into your development process to catch issues early, share knowledge across the team, and maintain code quality.
    - **CI/CD Pipeline**: Set up a CI/CD pipeline using tools like Jenkins, GitHub Actions, GitLab CI, or CircleCI to automate the building, testing, and deployment of your application. This pipeline should include:
        - **Automated Testing**: Ensure that all tests run automatically on every commit to catch issues early.
        - **Code Quality Checks**: Integrate tools like SonarQube or Checkstyle to enforce coding standards and detect potential issues.
        - **Deployment Automation**: Automate the deployment process to reduce manual errors and speed up delivery.

### 10. Database Migrations with Liquibase or Flyway

- **Purpose**: Database migration tools like Liquibase and Flyway help manage schema changes in a consistent and controlled manner. They are particularly useful in environments where the database schema evolves over time.
- **When to Use**:
    - **Use Case**: If your application requires frequent schema changes, or if you work in a team where multiple developers are modifying the database, using a migration tool is essential. It ensures that all changes are versioned, documented, and applied consistently across different environments (development, testing, production).
    - **When Not Needed**: If your application uses a fixed schema that rarely changes, or if you're using a database with a predefined schema where you don't manage the tables (e.g., a third-party service), you might not need a migration tool. In such cases, focusing on data access rather than schema management is more appropriate.
- **Best Practices**:
    - **Version Control for Migrations**: Always check your migration scripts into version control alongside your application code. This ensures that schema changes are versioned with the corresponding application changes.
    - **Automate Migrations**: Integrate your migration tool into your CI/CD pipeline to ensure that migrations are applied automatically during deployment, reducing the risk of human error.

### 11. Conclusion

By following these best practices, you can build Spring Boot applications that are maintainable, scalable, and easy to understand. These practices not only improve the quality of your code but also make it easier for others to contribute to your project, fostering a collaborative and productive development environment.



## 12. Conclusion

This document aims to provide a foundational understanding of Spring Boot 3, particularly in the context of building a REST API. By following the guidelines and best practices outlined here, you can create a well-structured, maintainable, and scalable application.

### Feedback and Contributions

Feedback and contributions are welcome! If you have suggestions, improvements, or additional insights, please feel free to share. Together, we can make this a valuable resource for anyone learning Spring Boot 3.
