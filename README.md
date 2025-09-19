# Zoonosys API

The **Zoonosys API** is the technological foundation for a communication and management system that connects the city’s zoonosis control center with the public. Our main goal is to **facilitate access and the exchange of information**, enabling the dissemination of news, alerts, and direct interaction with community interests.

This API serves as the engine for transparency and agility in managing zoonosis data, allowing the registration, consultation, and updating of essential information for public health.

---

## 🚀 Technologies Used

- **Java 17**: Main programming language.  
- **Spring Boot 3.3.0**: Framework for rapid application development.  
- **Spring Security**: For authentication and authorization management (JWT).  
- **Spring Data JPA**: For data persistence and database interaction.  
- **PostgreSQL**: Relational database management system.  
- **JWT (Java JWT)**: For generating and validating security tokens.  
- **Springdoc OpenAPI**: For automatic API documentation and Swagger UI.  
- **Lombok**: To simplify writing model classes (entities, DTOs).  

---

## ⚙️ Prerequisites

To run the project locally, you need to have installed:

- **JDK 17** or higher  
- **Maven** or **Gradle** (the project uses Maven)  
- **PostgreSQL** (or Docker to run a database instance)  

---

## 🔧 Installation and Setup

1. **Clone the Repository**
   ```bash
   git clone https://github.com/danielleBortoleto/ApiZoonosys.git
<<<<<<< HEAD
   cd zoonosys
=======
>>>>>>> 705d35e05f38887a880280ee98654042374b2898

______
# Zoonosys API

A **Zoonosys API** é a base tecnológica para um sistema de comunicação e gerenciamento que conecta o centro de controle de zoonoses do município ao público. Nosso principal objetivo é **facilitar o acesso e a intermediação de informações**, viabilizando a divulgação de notícias, alertas e a interação direta com os interesses da comunidade.

Esta API atua como o motor para a transparência e a agilidade na gestão de dados sobre zoonoses, permitindo o registro, consulta e atualização de informações essenciais para a saúde pública.

---

### 🚀 Tecnologias Utilizadas

* **Java 17**: Linguagem de programação principal.
* **Spring Boot 3.3.0**: Framework para desenvolvimento rápido de aplicações.
* **Spring Security**: Para gerenciamento de autenticação e autorização (JWT).
* **Spring Data JPA**: Para persistência de dados e interação com o banco.
* **PostgreSQL**: Sistema de gerenciamento de banco de dados relacional.
* **JWT (Java JWT)**: Para geração e validação de tokens de segurança.
* **Springdoc OpenAPI**: Para documentação automática da API e Swagger UI.
* **Lombok**: Para simplificar a escrita de classes de modelo (entidades, DTOs).

---

### ⚙️ Pré-requisitos

Para rodar o projeto localmente, você precisa ter instalado:

* **JDK 17** ou superior.
* **Maven** ou **Gradle** (o projeto utiliza Maven).
* **PostgreSQL** (ou Docker para rodar uma instância do banco).

---

### 🔧 Instalação e Configuração

1.  **Clone o Repositório:**
    ```bash
    git clone [https://github.com/danielleBortoleto/ApiZoonosys.git]
    cd zoonosys
    ```

2.  **Configurar o Banco de Dados:**
    A API se conecta a um banco de dados PostgreSQL. Crie um banco com o nome `bd_api_zoonosys`.

3.  **Configurar Variáveis de Ambiente:**
    O projeto utiliza variáveis de ambiente para dados sensíveis. Crie um arquivo `.env` na raiz do projeto e preencha-o com as seguintes informações:
    ```
    SERVER_PORT=8080
    DB_USERNAME=postgres
    DB_PASSWORD=sua_senha
    TOKEN_SECRET_KEY=sua_chave_secreta_jwt
    TOKEN_ISSUER=zoonosys-api
    ```
    * **`SERVER_PORT`**: Porta em que a aplicação irá rodar.
    * **`DB_USERNAME`** e **`DB_PASSWORD`**: Credenciais de acesso ao seu banco de dados PostgreSQL.
    * **`TOKEN_SECRET_KEY`**: Chave secreta para assinar e validar tokens JWT. Escolha uma chave segura e complexa.
    * **`TOKEN_ISSUER`**: O emissor do token, uma string que identifica a sua API.

4.  **Rodar a Aplicação:**
    Use o Maven para compilar e rodar a aplicação:
    ```bash
    mvn spring-boot:run
    ```
    A aplicação estará disponível em `http://localhost:8080/api`.

---

### 📝 Documentação da API

A documentação interativa da API é gerada automaticamente pelo Springdoc OpenAPI e pode ser acessada através do **Swagger UI**.

* **URL da Documentação (Swagger UI):**
    `http://localhost:8080/api/api-docs/index.html`

* **URL da Especificação OpenAPI (JSON):**
    `http://localhost:8080/api/api-docs`

---

### 🔒 Autenticação

A API utiliza tokens **JWT (JSON Web Token)** para autenticação. Para acessar os *endpoints* protegidos, você deve incluir o token no cabeçalho `Authorization` com o prefixo `Bearer`.

**Exemplo de Cabeçalho:**
