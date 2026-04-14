# Plataforma de Gestão de Nutrição Animal

Sistema baseado em microsserviços para gestão de consumo de ração e análise nutricional animal.

---

## Arquitetura

```
Cliente → feed-management-service → RabbitMQ → nutrition-analysis-service → MongoDB
                                                        ↓
                                              feed-cost-service (REST)
```

| Serviço | Porta | Banco | Responsabilidade |
|---------|-------|-------|-----------------|
| feed-management-service | 8081 | MySQL | Registra consumo de ração |
| feed-cost-service | 8082 | MySQL | Fornece custo dos insumos |
| nutrition-analysis-service | 8083 | MongoDB | Processa análises e calcula custo |

---

## Tecnologias

- Java 21
- Spring Boot 3.2.4
- RabbitMQ
- MySQL 8.0
- MongoDB
- Docker

---

## Pré-requisitos

- Java 21+
- Maven
- Docker

---

## Como executar

### 1. Subir a infraestrutura

```bash
docker compose up -d
```

Isso sobe o MySQL, MongoDB e RabbitMQ automaticamente.

### 2. Subir os serviços na ordem

```bash
# 1º
cd feed-cost-service && mvn spring-boot:run

# 2º
cd nutrition-analysis-service && mvn spring-boot:run

# 3º
cd feed-management-service && mvn spring-boot:run
```

---

## Endpoints

### feed-management-service (8081)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | /feeds | Registra consumo de ração |
| GET | /feeds | Lista todos os registros |
| GET | /feeds/{animalId} | Lista registros por animal |

**Exemplo de requisição:**
```json
POST http://localhost:8081/feeds
{
  "animalId": "animal-01",
  "feedType": "MILHO",
  "quantity": 10.5
}
```

### feed-cost-service (8082)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | /cost/{feedType} | Retorna custo por kg do insumo |

**Exemplo:**
```
GET http://localhost:8082/cost/MILHO
```

### nutrition-analysis-service (8083)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | /analysis | Lista todas as análises |
| GET | /analysis/{animalId} | Lista análises por animal |

---

## Tipos de ração disponíveis

`MILHO` `SOJA` `FARELO_SOJA` `SORGO` `TRIGO` `SUPLEMENTO_MINERAL` `NUCLEO_PROTEICO` `SAL_BRANCO` `UREIA` `SILAGEM_MILHO`

---

## Fluxo completo

1. Cliente envia `POST /feeds` com consumo do animal
2. `feed-management-service` salva no MySQL e publica evento no RabbitMQ
3. `nutrition-analysis-service` consome o evento
4. Consulta o custo do insumo no `feed-cost-service` via REST
5. Calcula o custo total (`quantity × costPerKg`)
6. Salva a análise no MongoDB

---

## Painel RabbitMQ

Acesse `http://localhost:15672` com usuário `guest` e senha `guest` para visualizar as filas em tempo real.
