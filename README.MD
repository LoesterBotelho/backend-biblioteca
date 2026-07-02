# Library Rental API

Java Spring Boot API for a university library rental system with authentication, roles, and loan management.

---

## Overview

This project is a REST API built with Spring Boot that provides:

- Book rental management
- JWT authentication system
- Role-based access control (ADMIN and USER)
- Loan creation and return system
- Swagger API documentation
- H2 database for development

---

## Tech Stack

- Java 21+
- Spring Boot 4.1.0
- Spring Security
- Spring Data JPA
- Spring Validation
- SpringDoc OpenAPI (Swagger)
- H2 Database
- JWT (JJWT)
- Maven
- Docker / Docker Compose

---

## Project Structure

src/main/java → Source code  
src/main/resources → Configuration files and SQL scripts  
pom.xml → Maven dependencies  

---

## Requirements

- Java 21+
- Maven 3+
- Docker (optional)
- Docker Compose (optional)

---

# PostgreSQL Setup (Docker)

## Option 1 - Docker Run

docker run -d   --name postgres-library   --restart unless-stopped   -e POSTGRES_DB=library   -e POSTGRES_USER=postgres   -e POSTGRES_PASSWORD=postgres   -p 5432:5432   -v postgres-library-data:/var/lib/postgresql/data   postgres:17

---

## Option 2 - Docker Compose

version: "3.9"

services:
  postgres:
    image: postgres:17
    container_name: postgres-library
    restart: unless-stopped

    environment:
      POSTGRES_DB: library
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres

    ports:
      - "5432:5432"

    volumes:
      - postgres-data:/var/lib/postgresql/data

volumes:
  postgres-data:

---

##  How to Run

mvn clean package -DskipTests  
mvn spring-boot:run -DskipTests  
java -jar target/library-rental-1.0.0.jar  

---

## Swagger

http://localhost:8080/swagger-ui/index.html

---

## Security

- /auth/** public
- Swagger public
- Other endpoints require JWT
- /admin/** requires ADMIN role

---

## Default Users

admin / Admin@123  
student1 / Admin@123
