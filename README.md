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
- Springdoc OpenAPI (Swagger)

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

### 2. Configurar o IP do ambiente (WSL2 ou Linux)

> **Importante:** se a IDE estiver rodando no Windows com Docker no WSL2, a aplicação não consegue acessar os containers via `localhost`. É necessário usar o IP da interface de rede do WSL2.

Para descobrir o IP, rode no terminal do WSL2:

```bash
ip addr show eth0 | grep "inet "
```

O retorno será algo como:
```
inet 172.28.153.162/20
```

Atualize o `application.properties` de cada serviço substituindo `localhost` pelo IP encontrado:

**feed-management-service e feed-cost-service:**
```properties
spring.datasource.url=jdbc:mysql://SEU_IP:3306/nome_do_banco?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
spring.rabbitmq.host=SEU_IP
```

**nutrition-analysis-service:**
```properties
spring.data.mongodb.uri=mongodb://SEU_IP:27017/nutrition_db
spring.rabbitmq.host=SEU_IP
feedcost.service.url=http://localhost:8082
```

> **Nota:** o `feedcost.service.url` permanece como `localhost` pois o `nutrition-analysis-service` acessa o `feed-cost-service` diretamente pelo Windows, não pelo Docker.

---

### 3. Subir os serviços na ordem

```bash
# 1º
cd feed-cost-service && mvn spring-boot:run

# 2º
cd nutrition-analysis-service && mvn spring-boot:run

# 3º
cd feed-management-service && mvn spring-boot:run
```

---

## Documentação com Swagger

Cada serviço expõe sua documentação interativa via Swagger UI. Com os serviços no ar, acesse:

| Serviço | URL |
|---------|-----|
| feed-management-service | http://localhost:8081/swagger-ui.html |
| feed-cost-service | http://localhost:8082/swagger-ui.html |
| nutrition-analysis-service | http://localhost:8083/swagger-ui.html |

O Swagger permite visualizar todos os endpoints, seus parâmetros, exemplos de requisição e resposta, e testar as chamadas diretamente pelo navegador.

---

## Configuração do docker-compose

O MySQL 8 por padrão restringe o usuário `root` para conectar apenas internamente. Para permitir conexões externas (necessário quando a aplicação roda fora do Docker), o `MYSQL_ROOT_HOST: '%'` deve estar configurado no serviço:

```yaml
mysql:
  image: mysql:8.0
  environment:
    MYSQL_ROOT_PASSWORD: root
    MYSQL_ROOT_HOST: '%'
```

> **Atenção:** o `MYSQL_ROOT_HOST` só é aplicado na **primeira inicialização** do container. Se o volume já existia anteriormente, é necessário recriar tudo do zero:
> ```bash
> docker compose down -v
> docker system prune -af
> docker compose up -d
> ```

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

**Exemplo de validação do fluxo completo:**
```
POST http://localhost:8081/feeds
{ "animalId": "animal-01", "feedType": "MILHO", "quantity": 10.5 }

GET http://localhost:8083/analysis/animal-01
→ totalCost: 26.25 (10.5 × 2.50)
```

---

## Painel RabbitMQ

Acesse `http://localhost:15672` com usuário `guest` e senha `guest` para visualizar as filas em tempo real.
