# How to Use

## Prerequisites

Before running the application, ensure the following software is installed:

* Java 25
* Maven 3.9+

## Build the Project

Open a terminal in the project root directory and execute:

```bash
mvn -DskipTests clean package
```

If the build completes successfully, the generated JAR file will be available in:

```text
target/library-rental-1.0.0.jar
```

## Run the Application

### Option 1 — Using Maven

```bash
mvn -DskipTests spring-boot:run
```

### Option 2 — Using the Executable JAR

```bash
java -jar target/library-rental-1.0.0.jar
```

## Authentication

### Login

```http
POST /auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin"
}
```

The API returns an Access Token and a Refresh Token.

Include the Access Token in all protected requests:

```http
Authorization: Bearer <accessToken>
```

### Refresh Token

```http
POST /auth/refresh
Content-Type: application/json

{
  "refreshToken": "<refreshToken>"
}
```

## Administrative Operations

### Create Author

```http
POST /admin/authors
```

```json
{
  "name": "Author Name"
}
```

---

### List Authors

```http
GET /admin/authors
```

---

### Create Book

```http
POST /admin/books
```

```json
{
  "title": "Book Title",
  "authorId": 1,
  "isbn": "978-1234567890",
  "copyCount": 5,
  "branchId": 1
}
```

---

### Update Inventory

```http
POST /admin/inventory
```

```json
{
  "bookId": 1,
  "branchId": 1,
  "quantity": 3
}
```

---

### Mark Books as Unavailable

```http
POST /admin/inventory/unusable?bookId=1&branchId=1&quantity=1
```

## Loan Operations

### Create Loan

```http
POST /loans
```

```json
{
  "userId": 1,
  "branchId": 1,
  "items": [
    {
      "bookId": 1,
      "quantity": 1
    }
  ]
}
```

---

### Return Loan

```http
POST /loans/return
```

```json
{
  "loanId": 1
}
```

---

### List Loans

```http
GET /loans
```

## Access Swagger UI

After starting the application, open:

```
http://localhost:8080/swagger-ui/index.html
```

Swagger allows you to explore and test all available API endpoints.

## Notes

* Use Swagger UI to quickly test the REST API.
* The H2 database is recreated every time the application starts.
* If your JWT expires, authenticate again using `/auth/login`.
* Ensure you include the Access Token in the `Authorization` header for protected endpoints.
