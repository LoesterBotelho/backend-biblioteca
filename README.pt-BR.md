# Library Rental API

API Java Spring Boot para um sistema de locação de livros universitário com autenticação, papéis de usuário e gerenciamento de empréstimos.

---

## Visão Geral

Este projeto é uma API REST construída com Spring Boot que fornece:

- Gerenciamento de locação de livros
- Sistema de autenticação JWT
- Controle de acesso baseado em roles (ADMIN e USER)
- Sistema de empréstimos e devoluções
- Documentação da API com Swagger
- Banco de dados H2 para desenvolvimento

---

## Tecnologias

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

## Estrutura do Projeto

src/main/java → Código fonte  
src/main/resources → Configurações e scripts SQL  
pom.xml → Dependências Maven  

---

## Requisitos

- Java 21+
- Maven 3+
- Docker (opcional)
- Docker Compose (opcional)

---

# Configuração PostgreSQL (Docker)

## Opção 1 - Docker Run

docker run -d   --name postgres-library   --restart unless-stopped   -e POSTGRES_DB=library   -e POSTGRES_USER=postgres   -e POSTGRES_PASSWORD=postgres   -p 5432:5432   -v postgres-library-data:/var/lib/postgresql/data   postgres:17

---

## Opção 2 - Docker Compose

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

## Como executar

mvn clean package -DskipTests  
mvn spring-boot:run -DskipTests  
java -jar target/library-rental-1.0.0.jar  

---

## Swagger

http://localhost:8080/swagger-ui/index.html

---

## Segurança

- /auth/** público
- Swagger público
- Demais endpoints exigem JWT
- /admin/** exige role ADMIN

---

## Usuários padrão

admin / Admin@123  
student1 / Admin@123
