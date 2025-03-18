# Todo List - Back

## Descrição
API para gerenciamento de tarefas, desenvolvida com Java e Spring Boot. Permite o cadastro de usuários e a criação, edição, exclusão, listagem, filtragem e ordenação de tarefas.

## Tecnologias Utilizadas
- **Java 17**
- **Spring Boot 3.4.3:** Framework para criar aplicações Java.
- **Spring Data JPA:** Facilita operações de banco de dados com JPA, reduzindo código boilerplate.
- **Lombok:** Gera automaticamente getters, setters e construtores, reduzindo verbosidade.
- **MapStruct:** Mapeia objetos entre DTOs e entidades de forma eficiente.
- **JUnit 5:** Framework para testes unitários em Java.
- **Mockito:** Cria mocks para testes unitários, isolando dependências.

## Endpoints

### Usuário

#### Criar Usuário
**POST** `/user/create`

**Corpo da Requisição:**
```json
{
  "name": "Guilherme",
  "email": "gui@gmail.com",
  "password": "123456"
}
```

**Resposta:**
```json
{
  "id": 1,
  "name": "Guilherme",
  "email": "gui@gmail.com",
  "password": "123456"
}
```

---

#### Login de Usuário
**GET** `/user/login`

**Parâmetros da Query:**
- `email`: string (ex.: `gui@gmail.com`)
- `password`: string (ex.: `123456`)

**Resposta:**
```json
{
  "id": 1
}
```

---

### Tarefa

#### Criar Tarefa
**POST** `/task/create/userId/{userId}`

**Corpo da Requisição:**
```json
{
  "title": "Criar entidade Task",
  "description": "Seguir boas práticas",
  "status": "PENDENTE",
  "dueDate": "2025-03-20"
}
```

**Resposta:**
```json
{
  "id": 1,
  "title": "Criar entidade Task",
  "description": "Seguir boas práticas",
  "status": "PENDENTE",
  "dueDate": "2025-03-20"
  "userId": 1
}
```

---

#### Listar Todas as Tarefas
**GET** `/task/findAll`

**Resposta:**
```json
[
  {
    "id": 1,
    "title": "Criar entidade Task",
    "description": "Seguir boas práticas",
    "status": "PENDENTE",
    "dueDate": "2025-03-20"
    "userId": 1
  }
]
```

---

#### Excluir Tarefa
**DELETE** `/task/deleteTask/taskId/{taskId}/userId/{userId}`

**Resposta:**
```json
200 OK
```

---

#### Filtrar Tarefas por Status
**GET** `/task/findByStatus/{status}`

**Parâmetros:**
- `status`: string (ex.: `PENDENTE`, `EM_ANDAMENTO`, `CONCLUIDO`)
- A lista de tarefas retornada será automaticamente ordenada pela data de vencimento em ordem crescente.

**Resposta:**
```json
[
  {
    "id": 1,
    "title": "Criar entidade Task",
    "description": "Seguir boas práticas",
    "status": "PENDENTE",
    "dueDate": "2025-03-20"
    "userId": 1
  },
  {
    "id": 1,
    "title": "Criar entidade Task",
    "description": "Seguir boas práticas",
    "status": "PENDENTE",
    "dueDate": "2025-03-20"
    "userId": 1
  }
]
```

---

#### Ordenar Tarefas por Data de Vencimento em ordem crescente
**GET** `/task/orderByDueDate`

**Resposta:**
```json
[
  {
    "id": 1,
    "title": "Criar entidade Task",
    "description": "Seguir boas práticas",
    "status": "PENDENTE",
    "dueDate": "2025-03-20"
    "userId": 1
  },
  {
    "id": 2,
    "title": "Criar entidade User",
    "description": "Seguir boas práticas",
    "status": "PENDENTE",
    "dueDate": "2025-03-28"
    "userId": 1
  },
]
```

---

#### Editar Tarefa
**PATCH** `/task/update/taskId/{taskId}/userId/{userId}`

- É necessário enviar apenas os campos que deseja atualizar.

**Corpo da Requisição:**
```json
{
  "status": "EM_ANDAMENTO"
}
```

**Resposta:**
```json
{
  "id": 2,
  "title": "Criar entidade User",
  "description": "Seguir boas práticas",
  "status": "EM_ANDAMENTO",
  "dueDate": "2025-03-28"
  "userId": 1
}
```

---

## Camada de Testes

Foi implementada uma camada de testes para o `TaskService`, cobrindo as principais funcionalidades. Os seguintes métodos de teste foram desenvolvidos:

- **`testCreateTask_Success`:** Valida a criação de uma tarefa com sucesso.
- **`testFindAllTasks_Success`:** Verifica a listagem de todas as tarefas.
- **`testDeleteTaskById_Success`:** Testa a exclusão de uma tarefa por ID.
- **`testFilterTaskByStatus_Success`:** Valida a filtragem de tarefas por status.
- **`testOrderByDueDate_Success`:** Testa a ordenação de tarefas por data de vencimento.
- **`testUpdateTask_Success`:** Verifica a atualização de uma tarefa.

Esses testes garantem a confiabilidade e funcionalidade do sistema, validando os cenários principais do gerenciamento de tarefas.

---

## Requisitos Implementados
- **Cadastro de Usuários**
  - Criar uma conta (registro de usuário com nome, e-mail e senha)
  - Login com email e senha (útil para o frontend, pois será necessário verificar o usuário que deseja atualizar ou deletar uma tarefa.)
- **Gerenciamento de Tarefas**
  - Criar, editar, excluir e listar tarefas
  - Cada tarefa contém título, descrição, status (Pendente, Em Andamento, Concluído) e data de vencimento
  - Apenas o criador da tarefa pode editá-la ou excluí-la
- **Filtragem e Ordenação**
  - Filtrar tarefas por status
  - Ordenar por data de vencimento

## Como Rodar o Projeto

### Pré-requisitos
Certifique-se de ter instalado:
- Java 17
- Utilize o Maven Wrapper ou instale o Maven

### Passos para execução

1. Clone este repositório:
   ```sh
   git clone https://github.com/guidias2002/todolist-back.git
   ```
2. Acesse o diretório do projeto:
   ```sh
   cd todolist-back
   ```
3.Compile e execute a aplicação:
```sh
   bash
    ./mvn install
    ./mvn spring-boot:run
   ```
   
