### 🥷 NinjaList API

Backend da aplicação NinjaList, um sistema de gerenciamento de tarefas com autenticação JWT, controle de usuários e operações CRUD.

Projeto desenvolvido com foco em boas práticas, arquitetura limpa e segurança utilizando Spring Boot + Spring Security + JWT.

## 🚀 Tecnologias Utilizadas

Java 17+

Spring Boot

Spring Security

JWT (JSON Web Token)

JPA / Hibernate

Maven

H2 / PostgreSQL

JUnit 5

Mockito

MockMvc

##📌 Funcionalidades
#🔐 Autenticação

Registro de usuário

Login com geração de token JWT

Proteção de rotas com Spring Security

Filtro customizado para validação do token

##👤 Perfil do Usuário

Buscar perfil autenticado

Atualizar nome e email

Atualização de foto (quando implementado)

##📋 Tarefas

Criar tarefa

Listar tarefas

Atualizar tarefa

Deletar tarefa

Ordenação por prioridade

Controle por usuário autenticado

## 🏗️ Arquitetura

O projeto segue separação em camadas:

```bash
controller → service → repository → database
```

Boas práticas aplicadas:

DTOs para requisição e resposta

Filtro JWT customizado (SecurityFilter)

Classe dedicada para geração e validação do token (TokenConfig)

Uso de @AuthenticationPrincipal

Separação clara de responsabilidades

🔑 Autenticação JWT

Fluxo de autenticação:

Usuário realiza login

API gera token JWT

Cliente envia no header:

Authorization: Bearer {token}

Filtro valida token e injeta usuário no contexto de segurança


## ▶️ Como Executar

### 1️⃣ Clonar o repositório

```bash
git clone https://github.com/ThiagoRicch/ninjalist-backend.git
```

### 2️⃣ Entrar na pasta

```bash
cd ninjalist-backend
```

### 3️⃣ Rodar aplicação

```bash
mvn spring-boot:run
```
