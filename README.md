# Money Flow API

Sistema de gerenciamento financeiro pessoal, controle de despesas e receitas.

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=souluanf_personal-finance-tracker&metric=alert_status&token=b6ce94a17d4984b5ba72c334c6e61732ebff4d15)](https://sonarcloud.io/summary/new_code?id=souluanf_personal-finance-tracker)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=souluanf_personal-finance-tracker&metric=coverage&token=b6ce94a17d4984b5ba72c334c6e61732ebff4d15)](https://sonarcloud.io/summary/new_code?id=souluanf_personal-finance-tracker)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=souluanf_personal-finance-tracker&metric=code_smells&token=b6ce94a17d4984b5ba72c334c6e61732ebff4d15)](https://sonarcloud.io/summary/new_code?id=souluanf_personal-finance-tracker)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=souluanf_personal-finance-tracker&metric=ncloc&token=b6ce94a17d4984b5ba72c334c6e61732ebff4d15)](https://sonarcloud.io/summary/new_code?id=souluanf_personal-finance-tracker)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=souluanf_personal-finance-tracker&metric=sqale_index&token=b6ce94a17d4984b5ba72c334c6e61732ebff4d15)](https://sonarcloud.io/summary/new_code?id=souluanf_personal-finance-tracker)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=souluanf_personal-finance-tracker&metric=reliability_rating&token=b6ce94a17d4984b5ba72c334c6e61732ebff4d15)](https://sonarcloud.io/summary/new_code?id=souluanf_personal-finance-tracker)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=souluanf_personal-finance-tracker&metric=duplicated_lines_density&token=b6ce94a17d4984b5ba72c334c6e61732ebff4d15)](https://sonarcloud.io/summary/new_code?id=souluanf_personal-finance-tracker)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=souluanf_personal-finance-tracker&metric=vulnerabilities&token=b6ce94a17d4984b5ba72c334c6e61732ebff4d15)](https://sonarcloud.io/summary/new_code?id=souluanf_personal-finance-tracker)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=souluanf_personal-finance-tracker&metric=bugs&token=b6ce94a17d4984b5ba72c334c6e61732ebff4d15)](https://sonarcloud.io/summary/new_code?id=souluanf_personal-finance-tracker)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=souluanf_personal-finance-tracker&metric=security_rating&token=b6ce94a17d4984b5ba72c334c6e61732ebff4d15)](https://sonarcloud.io/summary/new_code?id=souluanf_personal-finance-tracker)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=souluanf_personal-finance-tracker&metric=sqale_rating&token=b6ce94a17d4984b5ba72c334c6e61732ebff4d15)](https://sonarcloud.io/summary/new_code?id=souluanf_personal-finance-tracker)

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