# Sistema de Gestão de Espaço Físico - Universidade

Este projeto consiste em um sistema completo para gerenciamento de solicitações de **reserva de espaços físicos**, como salas de aula, laboratórios, auditórios, salas de reunião, vagas de estacionamento, entre outros.

O objetivo é permitir que usuários solicitem reservas de espaços e que gestores avaliem e aprovem ou rejeitem essas solicitações com controle de histórico, conflitos de horário e rastreamento por auditoria.

---

## 📌 Funcionalidades

- Cadastro de espaços físicos com características específicas (metragem, equipamentos, tipo).
- Cadastro de usuários (solicitantes e gestores).
- Solicitação de reserva de espaço com data e hora.
- Aprovação ou rejeição da solicitação pelo gestor, com justificativa.
- Histórico de avaliações de cada solicitação.
- Prevenção de conflitos de reserva para o mesmo espaço, data e hora.
- Auditoria de ações dos usuários no sistema.
- Visualização das reservas aprovadas.

---


## 🛠️ Tecnologias Utilizadas

<br>

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)  
• Linguagem principal utilizada no desenvolvimento da aplicação.

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)  
• Framework que facilita a criação de aplicações Java robustas, com injeção de dependência, API REST, entre outros.

![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)  
• Sistema gerenciador de banco de dados relacional usado para armazenar as informações do sistema.

![JDBC](https://img.shields.io/badge/JDBC-003B57?style=for-the-badge&logo=oracle&logoColor=white)  
• API utilizada para conectar e executar consultas SQL diretamente no banco de dados.

![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)  
• Plataforma utilizada para testar os endpoints da API REST, garantindo o correto funcionamento das rotas, validações e respostas do sistema.

---

Pré-requisitos

Para rodar este projeto, você precisa ter instalado:

Java 17+

Maven 3.8+

PostgreSQL 15+

---
Postman (para testes da API)

Crie o banco de dados no PostgreSQL:

CREATE DATABASE gestao_espaco;


Configure as credenciais no arquivo application.properties:

spring.datasource.url=jdbc:postgresql://localhost:5432/gestao_espaco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true



---

xemplos de Requisições
✅ Criar uma nova reserva:
POST /api/reservas
Content-Type: application/json

{
  "usuarioId": 1,
  "espacoId": 2,
  "data": "2025-09-10",
  "horaInicio": "14:00",
  "horaFim": "16:00"
}

✅ Aprovar ou rejeitar solicitação:
PUT /api/avaliacoes/1
Content-Type: application/json

{
  "gestorId": 3,
  "status": "APROVADO",
  "justificativa": "Horário disponível"
}

✅ Listar reservas aprovadas:
GET /api/reservas/aprovadas
## 🧠 Como Executar o Projeto
---
1. Clone este repositório:

```bash
git clone https://github.com/seu-usuario/seu-repositorio.git


contato - gv524003@gmail.com
