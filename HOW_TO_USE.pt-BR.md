# Library Rental - Guia de Utilização (Corporate Edition)

## Visão Geral
Este documento descreve como executar, configurar e utilizar a API do sistema Library Rental em ambiente de desenvolvimento.

---

## Pré-requisitos

Antes de iniciar, certifique-se de ter instalado:

- Java 25 ou superior
- Maven 3.9+
- Git (opcional, recomendado)

---

## Build do Projeto

Execute o comando abaixo para compilar o projeto:

```bash
mvn clean package -DskipTests
```

### Resultado esperado
O artefato será gerado em:

```
target/library-rental-1.0.0.jar
```

---

## Execução da Aplicação

### 1. Via Maven (desenvolvimento)

```bash
mvn spring-boot:run -DskipTests
```

### 2. Via JAR (produção/local)

```bash
java -jar target/library-rental-1.0.0.jar
```

---

## Autenticação

A API utiliza autenticação baseada em JWT.

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

### Uso do Token

Inclua o token em todas as requisições protegidas:

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

## Módulo Administrativo

### Autores

Criar autor:

```http
POST /admin/authors
```

Listar autores:

```http
GET /admin/authors
```

---

### Livros

Criar livro:

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

### Estoque

Atualizar estoque:

```http
POST /admin/inventory
```

Marcar itens inutilizáveis:

```http
POST /admin/inventory/unusable?bookId=1&branchId=1&quantity=1
```

---

## Empréstimos

Criar empréstimo:

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

Devolver empréstimo:

```http
POST /loans/return
```

Listar empréstimos:

```http
GET /loans
```

---

## Swagger

Acesse a documentação interativa:

```
http://localhost:8080/swagger-ui/index.html
```

---

## Boas Práticas

- Utilize Swagger para testes rápidos
- Ambiente local utiliza banco em memória (H2 ou equivalente)
- Sempre gere novo token ao expirar autenticação
