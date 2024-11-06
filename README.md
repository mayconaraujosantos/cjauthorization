# Caju - Simplified Credit Card Transaction Payload

Este projeto implementa uma versão simplificada de um payload de transação de cartão de crédito utilizando **Spring Boot 3**, **Kotlin**, e **H2 Database**. O objetivo é criar uma aplicação simples para processar transações de cartão de crédito, com validações e API REST.

## Tecnologias Utilizadas

- **Backend**:
    - Spring Boot 3
    - Kotlin (JVM)
    - H2 Database (para desenvolvimento)
    - JPA (Java Persistence API)
    - Spring Web e Spring Validation
    - Springdoc OpenAPI para documentação da API
    - JUnit 5 para testes

## Pré-requisitos

Antes de rodar a aplicação, você precisa ter as seguintes ferramentas instaladas no seu ambiente de desenvolvimento:

1. **Java 17+**
    - Você pode verificar a versão instalada com o comando:
      ```bash
      java -version
      ```

2. **Kotlin**
    - O projeto usa Kotlin como linguagem de desenvolvimento, que é configurado automaticamente via Gradle.

3. **Gradle**
    - Este projeto utiliza Gradle como sistema de build. Você pode verificar se o Gradle está instalado com:
      ```bash
      gradle -v
      ```

4. **Banco de Dados H2** (embutido)
    - O banco de dados H2 é usado para desenvolvimento, e ele roda em memória por padrão.

## Configuração e Execução

### 1. Clone o repositório

Clone este repositório para sua máquina local:

```bash
git clone https://github.com/mayconaraujosantos/cjauthorization.git
cd cjauthorization
```

## Banco de dados 
No seu navegador pode acessar o endereço
 - http://localhost:8080/h2-console/
usuario e senha sao sa, vc pode encontrar no **application.properties**

## Collections no Postman 
no arquivo postman_dump.json temos os payloads para testes
