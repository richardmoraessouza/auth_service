# Auth-Service

## Descrição do Projeto

O **Auth-Service** é um microsserviço de autenticação desenvolvido em Spring Boot, projetado para fornecer funcionalidades de registro e login de usuários. Ele utiliza JWT (JSON Web Tokens) para autenticação stateless, garantindo segurança e escalabilidade. O serviço integra-se com um banco de dados PostgreSQL para persistência de dados e emprega Spring Security para controle de acesso.

Este projeto é ideal para aplicações que necessitam de um sistema de autenticação robusto e seguro, seguindo as melhores práticas de desenvolvimento com Spring Boot.

## Tecnologias Utilizadas

- **Java 21**: Linguagem de programação principal.
- **Spring Boot 4.0.5**: Framework para desenvolvimento de aplicações Java.
- **Spring Security**: Para configuração de segurança e autenticação.
- **Spring Data JPA**: Para interação com o banco de dados.
- **PostgreSQL**: Banco de dados relacional.
- **JWT (JJWT 0.11.5)**: Para geração e validação de tokens de autenticação.
- **BCrypt**: Para hashing de senhas.
- **Maven**: Gerenciamento de dependências e build.
- **Docker**: Containerização da aplicação.
- **Lombok**: Para redução de boilerplate code.

## Pré-requisitos

Antes de executar o projeto, certifique-se de ter os seguintes itens instalados:

- **Java 21** ou superior.
- **Maven 3.6+** para gerenciamento de dependências.
- **PostgreSQL** (ou um container Docker com PostgreSQL).
- **Docker** e **Docker Compose** (opcional, para execução via container).

## Instalação e Configuração

### 1. Clonagem do Repositório

Clone o repositório para sua máquina local:

```bash
git clone https://github.com/seu-usuario/auth-service.git
cd auth-service
```

### 2. Configuração do Banco de Dados

O projeto utiliza PostgreSQL. Você pode configurar um banco local ou usar Docker.

#### Opção 1: PostgreSQL Local

Certifique-se de que o PostgreSQL esteja rodando e crie um banco de dados chamado `auth_service` (ou conforme configurado).

#### Opção 2: Usando Docker

Execute o seguinte comando para iniciar um container PostgreSQL:

```bash
docker run --name postgres-auth -e POSTGRES_DB=auth_service -e POSTGRES_USER=your_user -e POSTGRES_PASSWORD=your_password -p 5432:5432 -d postgres:13
```

### 3. Configuração das Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto com as seguintes variáveis:

```env
DB_HOST=jdbc:postgresql://localhost:5432/auth_service
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
CHAVE_SECRETA=your_secret_key_for_jwt
```

- `DB_HOST`: URL de conexão com o banco de dados.
- `DB_USERNAME`: Usuário do banco.
- `DB_PASSWORD`: Senha do banco.
- `CHAVE_SECRETA`: Chave secreta para assinatura dos tokens JWT (deve ser uma string segura e única).

**Nota**: A chave secreta deve ser mantida em segredo e não versionada no repositório.

## Executando a Aplicação

### Opção 1: Executar Localmente

Compile e execute a aplicação usando Maven:

```bash
mvn clean install
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`.

### Opção 2: Usando Docker Compose

O projeto inclui um `docker-compose.yml` para facilitar a execução.

Atualize o `docker-compose.yml` com suas variáveis de ambiente ou passe-as via linha de comando:

```bash
docker-compose up --build
```

Isso construirá a imagem Docker e iniciará o container.

## Documentação da API

O serviço expõe os seguintes endpoints:

### 1. Registro de Usuário
- **Endpoint**: `POST /auth_service/cadastrar`
- **Descrição**: Registra um novo usuário no sistema.
- **Corpo da Requisição** (JSON):
  ```json
  {
    "name": "Nome do Usuário",
    "email": "usuario@example.com",
    "password": "senha_segura"
  }
  ```
- **Resposta de Sucesso** (200):
  ```json
  {
    "id": 1,
    "name": "Nome do Usuário",
    "email": "usuario@example.com",
    "password": "hashed_password"
  }
  ```
- **Notas**: A senha é automaticamente hasheada com BCrypt.

### 2. Login
- **Endpoint**: `POST /auth_service/login`
- **Descrição**: Autentica um usuário e retorna um token JWT.
- **Corpo da Requisição** (JSON):
  ```json
  {
    "email": "usuario@example.com",
    "password": "senha_segura"
  }
  ```
- **Resposta de Sucesso** (200): Token JWT como string.
- **Resposta de Erro** (401): "E-mail ou senha inválidos"

### 3. Listar Usuários
- **Endpoint**: `GET /auth_service`
- **Descrição**: Retorna uma lista de todos os usuários (requer autenticação).
- **Cabeçalhos**: `Authorization: Bearer <token_jwt>`
- **Resposta de Sucesso** (200): Lista de usuários em JSON.

**Notas Gerais**:
- Os endpoints `/auth_service/cadastrar` e `/auth_service/login` são públicos.
- Outros endpoints requerem um token JWT válido no cabeçalho `Authorization`.
- O token JWT expira em 1 hora.

## Esquema do Banco de Dados

O projeto utiliza uma tabela `user` no schema `auth_service`:

```sql
CREATE SCHEMA IF NOT EXISTS auth_service;

CREATE TABLE auth_service.user (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL
);
```

- **id**: Chave primária, auto-incrementada.
- **email**: E-mail único do usuário.
- **password**: Senha hasheada.
- **name**: Nome do usuário.

O Hibernate está configurado para atualizar o schema automaticamente (`spring.jpa.hibernate.ddl-auto=update`).

## Segurança

- **Autenticação**: Baseada em JWT. Tokens são gerados no login e devem ser incluídos em requisições subsequentes.
- **Hashing de Senhas**: Utiliza BCrypt para armazenamento seguro de senhas.
- **Configuração de Segurança**: Desabilita CSRF, CORS, e formulários de login para uma API stateless.
- **Sessões**: Stateless, sem gerenciamento de sessão no servidor.

## Testes

Para executar os testes:

```bash
mvn test
```

O projeto inclui testes para os componentes principais, utilizando Spring Boot Test.

## Implantação

### Usando Docker

Para implantar em produção, construa a imagem Docker:

```bash
docker build -t auth-service .
docker run -p 8080:8080 --env-file .env auth-service
```

### Variáveis de Ambiente em Produção

Certifique-se de definir as variáveis de ambiente adequadamente no ambiente de produção, evitando hardcode de credenciais.

## Contribuição

1. Fork o projeto.
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`).
3. Commit suas mudanças (`git commit -am 'Adiciona nova feature'`).
4. Push para a branch (`git push origin feature/nova-feature`).
5. Abra um Pull Request.

## Licença

Este projeto está licenciado sob a [MIT License](LICENSE).

## Contato

Para dúvidas ou sugestões, entre em contato com o desenvolvedor: [seu-email@example.com](mailto:seu-email@example.com).

---

Este README foi elaborado para fornecer uma visão completa e detalhada do projeto, facilitando a compreensão e adoção por desenvolvedores e arquitetos de sistemas.</content>
<parameter name="filePath">C:\Users\Pichau\Desktop\programacao\Auth-Service\README.md
