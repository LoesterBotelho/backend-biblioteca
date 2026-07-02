# Como Utilizar o Library Rental

## Pré-requisitos

- Java 25 instalado
- Maven 3.9+ instalado

## Preparar o projeto

1. Abra o terminal
2. Execute o build:

```bash
mvn -DskipTests clean package
```

3. Se o build for bem-sucedido, o jar será gerado em `target\library-rental-1.0.0.jar`.

## Iniciar a aplicação

### Via Maven

```bash
mvn -DskipTests spring-boot:run
```

### Via JAR

```bash
java -jar target\library-rental-1.0.0.jar
```

## Usando a API

### Autenticação

1. Fazer login:

```http
POST /auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin"
}
```

2. Usar o token retornado no cabeçalho:

```http
Authorization: Bearer <accessToken>
```

3. Atualizar token:

```http
POST /auth/refresh
Content-Type: application/json

{
  "refreshToken": "<refreshToken>"
}
```

### Chamadas administrativas

- Criar autor:

```http
POST /admin/authors
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Nome do Autor"
}
```

- Listar autores:

```http
GET /admin/authors
Authorization: Bearer <token>
```

- Criar livro:

```http
POST /admin/books
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "Título do Livro",
  "authorId": 1,
  "isbn": "978-1234567890",
  "copyCount": 5,
  "branchId": 1
}
```

- Ajustar estoque:

```http
POST /admin/inventory
Authorization: Bearer <token>
Content-Type: application/json

{
  "bookId": 1,
  "branchId": 1,
  "quantity": 3
}
```

- Marcar livros inutilizáveis:

```http
POST /admin/inventory/unusable?bookId=1&branchId=1&quantity=1
Authorization: Bearer <token>
```

### Empréstimos de usuário

- Criar empréstimo:

```http
POST /loans
Authorization: Bearer <token>
Content-Type: application/json

{
  "userId": 1,
  "branchId": 1,
  "items": [
    { "bookId": 1, "quantity": 1 }
  ]
}
```

- Devolver empréstimo:

```http
POST /loans/return
Authorization: Bearer <token>
Content-Type: application/json

{
  "loanId": 1
}
```

- Listar empréstimos:

```http
GET /loans
Authorization: Bearer <token>
```

## Verificar Swagger

Após iniciar a aplicação, abra:

- `http://localhost:8080/swagger-ui/index.html`

## Dicas

- Use o Swagger para testar endpoints rapidamente.
- Em ambiente de desenvolvimento, o banco H2 é recriado a cada inicialização.
- Se ocorrer erro de token, gere novo login em `/auth/login`.
