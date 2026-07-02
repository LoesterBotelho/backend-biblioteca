# Library Rental - Usage Guide (Corporate Edition)

## Overview
This document describes how to build, run, and use the Library Rental API in a development environment.

---

## Requirements

Make sure you have installed:

- Java 25 or higher
- Maven 3.9+
- Git (optional but recommended)

---

## Project Build

Run the following command:

```bash
mvn clean package -DskipTests
```

### Expected Output

The generated artifact will be located at:

```
target/library-rental-1.0.0.jar
```

---

## Running the Application

### 1. Using Maven (development)

```bash
mvn spring-boot:run -DskipTests
```

### 2. Using JAR (local/production)

```bash
java -jar target/library-rental-1.0.0.jar
```

---

## Authentication

The API uses JWT-based authentication.

### Login

```http
POST /auth/login
Content-Type: application/json
```

```json
{
  "username": "admin",
  "password": "admin"
}
```

### Using the Token

Include the token in all protected requests:

```http
Authorization: Bearer <accessToken>
```

### Refresh Token

```http
POST /auth/refresh
Content-Type: application/json
```

```json
{
  "refreshToken": "<refreshToken>"
}
```

---

## Admin Module

### Authors

Create author:

```http
POST /admin/authors
```

List authors:

```http
GET /admin/authors
```

---

### Books

Create book:

```http
POST /admin/books
```

```json
{
  "title": "Example Book",
  "authorId": 1,
  "isbn": "978-1234567890",
  "copyCount": 5,
  "branchId": 1
}
```

---

### Inventory

Update inventory:

```http
POST /admin/inventory
```

Mark unusable items:

```http
POST /admin/inventory/unusable?bookId=1&branchId=1&quantity=1
```

---

## Loans

Create loan:

```http
POST /loans
```

```json
{
  "userId": 1,
  "branchId": 1,
  "items": [
    { "bookId": 1, "quantity": 1 }
  ]
}
```

Return loan:

```http
POST /loans/return
```

List loans:

```http
GET /loans
```

---

## Swagger

Open API documentation:

```
http://localhost:8080/swagger-ui/index.html
```

---

## Best Practices

- Use Swagger for fast testing
- Local environment uses in-memory database (H2 or equivalent)
- Always refresh token when expired
