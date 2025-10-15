# Money Flow API

Sistema de gerenciamento financeiro pessoal, controle de despesas e receitas.

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=souluanf_luanfernandesapi&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=souluanf_luanfernandesapi)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=souluanf_luanfernandesapi&metric=coverage)](https://sonarcloud.io/summary/new_code?id=souluanf_luanfernandesapi)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=souluanf_luanfernandesapi&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=souluanf_luanfernandesapi)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=souluanf_luanfernandesapi&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=souluanf_luanfernandesapi)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=souluanf_luanfernandesapi&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=souluanf_luanfernandesapi)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=souluanf_luanfernandesapi&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=souluanf_luanfernandesapi)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=souluanf_luanfernandesapi&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=souluanf_luanfernandesapi)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=souluanf_luanfernandesapi&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=souluanf_luanfernandesapi)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=souluanf_luanfernandesapi&metric=bugs)](https://sonarcloud.io/summary/new_code?id=souluanf_luanfernandesapi)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=souluanf_luanfernandesapi&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=souluanf_luanfernandesapi)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=souluanf_luanfernandesapi&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=souluanf_luanfernandesapi)

## Sumário

- [Funcionalidades](#funcionalidades)
- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Requisitos](#requisitos)
- [Configuração](#configuração)
- [Execução](#execução)
- [Swagger](#swagger)
- [Contato](#contato)

## Funcionalidades

- Gestão de usuários (CRUD completo)
- Controle de transações (receitas e despesas)
- Cálculo de balanço mensal
- Dados iniciais pré-carregados

## Tecnologias

### Core

- Java 25
- Spring Boot 3.5.6
- Maven 3.6+

### Documentação

- OpenAPI 3.0 (Swagger)
- Swagger UI

### Testes

- JUnit 5
- Spring Boot Test

## Arquitetura

Projeto segue Clean Architecture com camadas bem definidas:
- **Domain**: Entidades e regras de negócio
- **Repository**: Persistência em memória (ConcurrentHashMap)
- **Service**: Lógica de aplicação
- **Controller**: API REST

## Requisitos

- JDK 25
- Maven 3.6+
- Docker

## Configuração

**Instalação do JDK, Maven e Docker:**

- [Instruções para instalação do JDK 25](https://www.oracle.com/java/technologies/downloads/)
- [Instruções para instalação do Maven](https://maven.apache.org/install.html)

## Execução

### Executar aplicação

```bash
 mvn spring-boot:run
```

### Executar testes
```bash
mvn test
```

### Gerar relatório de cobertura
```bash
mvn clean test jacoco:report
```

## Swagger

- **OpenAPI UI:** [http://localhost:8080/money-flow-api/swagger-ui/index.html](http://localhost:8080/money-flow-api/swagger-ui/index.html)
- **API Docs:** [http://localhost:8080/money-flow-api/v3/api-docs](http://localhost:8080/money-flow-api/v3/api-docs)
- **Postman Collection:** [collection/money-flow-api.postman_collection.json](collection/money-flow-api.postman_collection.json)

[<img src="https://run.pstmn.io/button.svg" alt="Run In Postman" style="width: 128px; height: 32px;">](https://app.getpostman.com/run-collection/26187327-d0600fcf-c880-45c3-8d45-7254e6f4cf75?action=collection%2Ffork&source=rip_markdown&collection-url=entityId%3D26187327-d0600fcf-c880-45c3-8d45-7254e6f4cf75%26entityType%3Dcollection%26workspaceId%3D6464d1b4-1abc-402a-8ad8-a4c5c5d78b90)

## Contato

Para suporte ou feedback:

- **Nome:** Luan Fernandes
- **Email:**  [contact@luanfernandes.dev](mailto:contact@luanfernandes.dev)
- **LinkedIn:** [https://linkedin.com/in/souluanf](https://linkedin.com/in/souluanf)