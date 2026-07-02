# 🐳 Executando o PostgreSQL com Docker

Este projeto utiliza o **PostgreSQL 17** como banco de dados.

## Requisitos

* Docker Desktop (ou Docker Engine)
* Docker Compose v2

---

# Opção 1 - Utilizando Docker Run

Inicie um container PostgreSQL:

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

Verifique se o container está em execução:

```bash
docker ps
```

Visualize os logs do container:

```bash
docker logs -f postgres-library
```

Pare o container:

```bash
docker stop postgres-library
```

Inicie o container novamente:

```bash
docker start postgres-library
```

Remova o container:

```bash
docker rm -f postgres-library
```

---

# Opção 2 - Utilizando Docker Compose (Recomendado)

Crie um arquivo `docker-compose.yml` na raiz do projeto.

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

Inicie o banco de dados:

```bash
docker compose up -d
```

Verifique os containers em execução:

```bash
docker compose ps
```

Visualize os logs:

```bash
docker compose logs -f
```

Pare os serviços:

```bash
docker compose stop
```

Reinicie os serviços:

```bash
docker compose restart
```

Pare e remova os containers:

```bash
docker compose down
```

Pare e remova os containers **incluindo todos os dados do banco de dados**:

```bash
docker compose down -v
```

---

# Configuração Padrão do Banco de Dados

| Propriedade    | Valor       |
| -------------- | ----------- |
| Banco de Dados | `library`   |
| Host           | `localhost` |
| Porta          | `5432`      |
| Usuário        | `postgres`  |
| Senha          | `postgres`  |

---

# Documentação do Projeto Library Rental

## Visão geral

API Java Spring Boot para locação de livros em biblioteca universitária.

- Autenticação JWT com login e refresh token
- Perfis de usuário: `ADMIN` e `USER`
- Regras de negócio de empréstimo e devolução
- Banco em memória H2 para demonstração
- Documentação Swagger disponível em execução

## Estrutura principal

- `pom.xml` - dependências e configuração Maven
- `src/main/java` - código-fonte Java
- `src/main/resources/application.properties` - configurações do Spring Boot e JWT
- `src/main/resources/init.sql` / `inserts.sql` - inicialização do banco H2

## Dependências

- Spring Boot 4.1.0
- Spring Security
- Spring Data JPA
- Spring Boot Validation
- SpringDoc OpenAPI UI
- H2 Database
- JJWT

## Execução

1. Build do projeto:

```bash
mvn -DskipTests clean package
```

2. Executar com Maven:

```bash
mvn -DskipTests spring-boot:run
```

3. Alternativa: executar o JAR gerado (após build):

```bash
java -jar target\library-rental-1.0.0.jar
```

> Observação: o jar padrão gerado pelo build é um jar aplicativo. Se quiser executar via `java -jar`, é preciso empacotar como Spring Boot jar ou usar o plugin Spring Boot para repackage.

## Swagger

A interface de documentação é exposta em:

- `http://localhost:8080/swagger-ui/index.html`

## Endpoints principais

### Autenticação

- `POST /auth/login`
  - Body: `{ "username": "admin", "password": "admin" }`
  - Retorna: `accessToken`, `refreshToken`, `tokenType`, etc.

- `POST /auth/refresh`
  - Body: `{ "refreshToken": "..." }`
  - Retorna um novo token JWT.

### Administração (`ADMIN`)

- `POST /admin/authors`
  - Cria um autor
  - Body: `{ "name": "Nome do Autor" }`

- `GET /admin/authors`
  - Lista autores

- `POST /admin/books`
  - Cria um livro
  - Body: `{ "title": "Título", "authorId": 1, "isbn": "...", "copyCount": 3, "branchId": 1 }`

- `GET /admin/books`
  - Lista livros

- `POST /admin/inventory`
  - Ajusta estoque de cópias
  - Body: `{ "bookId": 1, "branchId": 1, "quantity": 5 }`

- `POST /admin/inventory/unusable`
  - Marca livros como inutilizáveis
  - Query: `?bookId=1&branchId=1&quantity=1`

### Empréstimos (`USER`)

- `POST /loans`
  - Cria um empréstimo
  - Body: `{ "userId": 1, "branchId": 1, "items": [ { "bookId": 1, "quantity": 1 } ] }`

- `POST /loans/return`
  - Retorna um empréstimo
  - Body: `{ "loanId": 1 }`

- `GET /loans`
  - Lista empréstimos

## Observações de segurança

- O endpoint `/auth/**` e o Swagger são públicos.
- Todos os demais endpoints exigem JWT.
- Endpoints em `/admin/**` exigem papel `ADMIN`.

## Banco de dados

- H2 em memória
- Dados carregados a partir de `init.sql` e `inserts.sql`

## Configurações de JWT

- `jwt.secret` definido em `application.properties`
- `jwt.expirationMs` controla validade do token
- `jwt.refreshExpirationMs` controla validade do refresh token

---

### users & pass
```
user : admin
pass : Admin@123

$2a$10$7q0v0Jm0qQzqYw8d1rKqUe0Q7mZ7sZfQp1f1oJb0l8QeQm2g0m8x2

user : student1
pass : Student@123
$2a$10$N9k8v2pX9cQw3mR8tY1fQeK0aZpL7sD8vB2nH5xC9mQ1eR6tY7uI0
```