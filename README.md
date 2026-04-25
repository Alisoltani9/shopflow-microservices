# Shopflow Microservices

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-green?style=flat-square&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-blue?style=flat-square&logo=postgresql)
![Kafka](https://img.shields.io/badge/Apache_Kafka-black?style=flat-square&logo=apachekafka)
![Docker](https://img.shields.io/badge/Docker-blue?style=flat-square&logo=docker)
![Kubernetes](https://img.shields.io/badge/Kubernetes-326CE5?style=flat-square&logo=kubernetes&logoColor=white)

A production-grade ecommerce backend built as independent microservices with inter-service HTTP communication, event-driven architecture, and Kubernetes deployment.

> **Evolved from:** [Shopflow Monolith](https://github.com/Alisoltani9/java-springboot-shopflow) — same domain, redesigned for scale.

---

## Architecture

```
┌─────────────────────────────────────────────────────┐
│                    Client (Postman)                  │
└──────────┬──────────────┬──────────────┬────────────┘
           │              │              │
           ▼              ▼              ▼
  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐
  │ user-service │ │product-service│ │ order-service│
  │   port 8081  │ │   port 8082  │ │   port 8083  │
  └──────┬───────┘ └──────────────┘ └──────┬───────┘
         │                                  │
         │        HTTP (WebClient)           │
         │◄──────────────────────────────────┘
         │
  ┌──────▼───────┐          ┌──────────────┐
  │  PostgreSQL  │          │    Kafka     │
  │ (users DB)   │          │(order-events)│
  └──────────────┘          └──────────────┘

  ┌──────────────┐          ┌──────────────┐
  │  PostgreSQL  │          │  PostgreSQL  │
  │ (products DB)│          │  (orders DB) │
  └──────────────┘          └──────────────┘
```

Each service owns its own database — no shared data layer.

---

## Services

### user-service (port 8081)
Handles user registration, login, and JWT authentication. Exposes a lookup endpoint used by other services to verify user existence.

- `POST /api/auth/register` — register a new user
- `POST /api/auth/login` — login and receive JWT token
- `GET /api/users/{id}` — internal endpoint for inter-service user lookup

### product-service (port 8082)
Manages the product catalog with seeded data.

- `GET /api/products` — list all products
- `GET /api/products/{id}` — get product by ID

### order-service (port 8083)
Handles order creation. Before saving an order, it calls `user-service` via HTTP to verify the user exists. On success, publishes an event to Kafka.

- `POST /api/orders` — create an order (validates user via user-service)
- `GET /api/orders/my?userId={id}` — get orders for a user

---

## Inter-Service Communication

`order-service` uses Spring WebClient to call `user-service` before creating an order:

```
POST /api/orders
      │
      ▼
UserClient.userExists(userId)
      │
      ▼
GET http://user-service:8081/api/users/{id}
      │
      ├── 200 OK → proceed to save order
      └── 404 Not Found → throw ResourceNotFoundException
```

---

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 + Spring Boot 3.5 | Core framework |
| Spring Security + JWT | Authentication |
| Spring WebClient | Inter-service HTTP calls |
| PostgreSQL | Persistent storage (one DB per service) |
| Apache Kafka | Async order events |
| Flyway | Database migrations |
| Docker | Containerization |
| Kubernetes (Minikube) | Orchestration, scaling, self-healing |
| Lombok | Boilerplate reduction |

---

## Kubernetes

All services are containerized and deployed to Kubernetes. Manifests are in the `/k8s` folder.

```bash
# Start Minikube
minikube start

# Point Docker to Minikube's daemon
eval $(minikube docker-env)

# Build images
docker build -t user-service:latest ./user-service
docker build -t product-service:latest ./product-service
docker build -t order-service:latest ./order-service

# Deploy all services
kubectl apply -f k8s/

# Check running pods (2 replicas per service = 6 pods)
kubectl get pods
```

**Self-healing demo:**
```bash
# Kill a pod
kubectl delete pod <pod-name>

# Kubernetes immediately creates a replacement
kubectl get pods
```

**Scaling:**
```bash
kubectl scale deployment order-service --replicas=4
```

---

## Running Locally

### Prerequisites
- Java 21
- Docker
- Maven

### 1. Start infrastructure

```bash
# Users database
docker run --name users-db \
  -e POSTGRES_DB=shopflow_users \
  -e POSTGRES_USER=ali \
  -e POSTGRES_PASSWORD=yourpassword \
  -p 5433:5432 -d postgres

# Products database
docker run --name products-db \
  -e POSTGRES_DB=shopflow_products \
  -e POSTGRES_USER=ali \
  -e POSTGRES_PASSWORD=yourpassword \
  -p 5434:5432 -d postgres

# Orders database
docker run --name orders-db \
  -e POSTGRES_DB=shopflow_orders \
  -e POSTGRES_USER=ali \
  -e POSTGRES_PASSWORD=yourpassword \
  -p 5435:5432 -d postgres

# Kafka
docker run --name kafka \
  -p 9092:9092 -d apache/kafka
```

### 2. Run each service

```bash
cd user-service && ./mvnw spring-boot:run
cd product-service && ./mvnw spring-boot:run
cd order-service && ./mvnw spring-boot:run
```

### 3. Test the flow

```bash
# Register a user
POST http://localhost:8081/api/auth/register
{"username": "ali", "password": "123456"}

# Login and get JWT
POST http://localhost:8081/api/auth/login
{"username": "ali", "password": "123456"}

# Create an order (validates user via user-service)
POST http://localhost:8083/api/orders
{"userId": 1, "totalPrice": 99.99, "status": "PENDING"}

# Try with fake user — returns 404
POST http://localhost:8083/api/orders
{"userId": 999, "totalPrice": 99.99, "status": "PENDING"}
```

---

## Project Structure

```
shopflow-microservices/
├── user-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── product-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── order-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
└── k8s/
    ├── user-service-deployment.yaml
    ├── user-service-service.yaml
    ├── product-service-deployment.yaml
    ├── product-service-service.yaml
    ├── order-service-deployment.yaml
    └── order-service-service.yaml
```

---

## Author

**Ali Soltani**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Ali_Soltani-blue?style=flat-square&logo=linkedin)](https://www.linkedin.com/in/ali-soltani-841115204/)
[![GitHub](https://img.shields.io/badge/GitHub-Alisoltani9-black?style=flat-square&logo=github)](https://github.com/Alisoltani9)
