# 🐳 Running PostgreSQL with Docker

This project uses **PostgreSQL 17** as its database.

## Requirements

* Docker Desktop (or Docker Engine)
* Docker Compose v2

---

# Option 1 - Using Docker Run

Start a PostgreSQL container:

```bash
docker run -d \
  --name postgres-library \
  --restart unless-stopped \
  -e POSTGRES_DB=library \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  -v postgres-library-data:/var/lib/postgresql/data \
  postgres:17
```

Verify the container is running:

```bash
docker ps
```

View container logs:

```bash
docker logs -f postgres-library
```

Stop the container:

```bash
docker stop postgres-library
```

Start the container again:

```bash
docker start postgres-library
```

Remove the container:

```bash
docker rm -f postgres-library
```

---

# Option 2 - Using Docker Compose (Recommended)

Create a `docker-compose.yml` file in the project root.

```yaml
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

    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres -d library"]
      interval: 10s
      timeout: 5s
      retries: 5
      start_period: 15s

volumes:
  postgres-data:
```

Start the database:

```bash
docker compose up -d
```

Check the running containers:

```bash
docker compose ps
```

View logs:

```bash
docker compose logs -f
```

Stop the services:

```bash
docker compose stop
```

Restart the services:

```bash
docker compose restart
```

Stop and remove the containers:

```bash
docker compose down
```

Stop and remove the containers **including all database data**:

```bash
docker compose down -v
```

---

# Default Database Configuration

| Property | Value       |
| -------- | ----------- |
| Database | `library`   |
| Host     | `localhost` |
| Port     | `5432`      |
| Username | `postgres`  |
| Password | `postgres`  |

---

# Spring Boot Configuration

Configure your `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/library
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver
```

---

# Project Structure

```text
library-api/
│
├── docker-compose.yml
├── README.md
├── init.sql
├── inserts.sql
│
├── src/
├── pom.xml
└── application.properties
```



---

# Library Rental Management System

A RESTful API built with Java and Spring Boot for managing book rentals in university libraries.

## Overview

This project provides a complete library rental management system with secure authentication, role-based authorization, inventory management, and loan processing.

### Features

* JWT Authentication (Access Token & Refresh Token)
* Role-based authorization (`ADMIN` and `USER`)
* Book and author management
* Inventory management
* Book loan and return operations
* RESTful API
* Swagger/OpenAPI documentation
* In-memory H2 database for demonstration

## Technologies

* Java 25
* Spring Boot 4.1.0
* Spring Security
* Spring Data JPA
* Spring Validation
* SpringDoc OpenAPI
* H2 Database
* JJWT
* Maven

## Project Structure

```text
pom.xml
src/
 ├── main/
 │   ├── java/
 │   └── resources/
 │       ├── application.properties
 │       ├── init.sql
 │       └── inserts.sql
```

## Prerequisites

* Java 25
* Maven 3.9+

## Getting Started

Build the project:

```bash
mvn -DskipTests clean package
```

Run with Maven:

```bash
mvn -DskipTests spring-boot:run
```

Or run the generated JAR:

```bash
java -jar target/library-rental-1.0.0.jar
```

> **Note**
>
> To execute the application using `java -jar`, ensure the project is packaged as an executable Spring Boot JAR.

## API Documentation

Swagger UI is available at:

```
http://localhost:8080/swagger-ui/index.html
```

## API Endpoints

### Authentication

| Method | Endpoint        | Description                 |
| ------ | --------------- | --------------------------- |
| POST   | `/auth/login`   | Authenticate user           |
| POST   | `/auth/refresh` | Generate a new access token |

### Administrative Endpoints (ADMIN)

| Method | Endpoint                    | Description               |
| ------ | --------------------------- | ------------------------- |
| POST   | `/admin/authors`            | Create author             |
| GET    | `/admin/authors`            | List authors              |
| POST   | `/admin/books`              | Create book               |
| GET    | `/admin/books`              | List books                |
| POST   | `/admin/inventory`          | Update inventory          |
| POST   | `/admin/inventory/unusable` | Mark books as unavailable |

### Loan Endpoints (USER)

| Method | Endpoint        | Description |
| ------ | --------------- | ----------- |
| POST   | `/loans`        | Create loan |
| POST   | `/loans/return` | Return loan |
| GET    | `/loans`        | List loans  |

## Security

* `/auth/**` endpoints are publicly accessible.
* Swagger UI is publicly accessible.
* All other endpoints require JWT authentication.
* `/admin/**` endpoints require the `ADMIN` role.

## Database

The application uses an in-memory H2 database.

The database is automatically initialized using:

* `init.sql`
* `inserts.sql`

## JWT Configuration

JWT settings are configured in `application.properties`.

Available properties:

* `jwt.secret`
* `jwt.expirationMs`
* `jwt.refreshExpirationMs`

## Demo Credentials

| Username | Password    | Role  |
| -------- | ----------- | ----- |
| admin    | Admin@123   | ADMIN |
| student1 | Student@123 | USER  |

## License

This project is intended for educational and portfolio purposes.
