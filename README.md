# Hotel Availability Search

Arquitectura hexagonal + Kafka + Oracle

## Requisitos previos

- Docker
- JDK 21
- Maven

## Puesta en marcha

### 1. Levantar infraestructura (Kafka + Oracle)

```bash
docker compose up -d
```

### 2. Esperar que Oracle esté healthy (2-3 min)

```bash
docker ps
```

### 3. Resetear password de SYS

> **Nota:** la imagen `container-registry.oracle.com/database/free:26ai` no aplica el `ORACLE_PASSWORD` del compose al usuario SYS en esta versión.

```bash
echo 'ALTER USER sys IDENTIFIED BY oracle;' | docker exec -i technicaltest-oracle-1 sqlplus -S / as sysdba
```

### 4. Crear el usuario de aplicación

```bash
docker exec -i technicaltest-oracle-1 sqlplus -S sys/oracle@//localhost:1521/FREEPDB1 as sysdba <<'SQL'
CREATE USER hotel IDENTIFIED BY hotel;
GRANT CONNECT, RESOURCE TO hotel;
ALTER USER hotel QUOTA UNLIMITED ON USERS;
SQL
```

### 5. Ejecutar la aplicación

```bash
./mvnw spring-boot:run
```

## Probar endpoints

### POST /search → 202

```bash
curl -X POST http://localhost:8080/search \
  -H "Content-Type: application/json" \
  -d '{"hotelId":"1234aBc","checkIn":"29/12/2023","checkOut":"31/12/2023","ages":[30,29,1,3]}'
```

### GET /count?searchId=<UUID>

```bash
curl http://localhost:8080/count?searchId=<UUID>
```

## Credenciales Oracle

| Rol   | Usuario | Password | Service  |
|-------|---------|----------|----------|
| Admin | sys     | oracle   | FREEPDB1 |
| App   | hotel   | hotel    | FREEPDB1 |

## Arquitectura

```
com.gsuarez.technicaltest
├── domain
│   ├── Search.java
│   └── SearchId.java
├── application
│   ├── port
│   │   ├── in
│   │   │   └── HotelSearchUseCase.java
│   │   └── out
│   │       ├── SearchEventPublisher.java
│   │       └── SearchRepository.java
│   └── service
│       └── HotelSearchService.java
├── adapter
│   ├── in
│   │   └── web
│   │       ├── HotelController.java
│   │       ├── SearchRequest.java
│   │       ├── SearchIdResponse.java
│   │       ├── CountResponse.java
│   │       ├── SearchMapper.java
│   │       ├── ValidationExceptionHandler.java
│   │       ├── DateFormat.java
│   │       └── DateFormatValidator.java
│   └── out
│       ├── kafka
│       │   ├── SearchKafkaProducer.java
│       │   └── SearchKafkaConsumer.java
│       └── persistence
│           ├── SearchEntity.java
│           ├── SearchJpaRepository.java
│           └── SearchPersistenceAdapter.java
└── config
    └── JacksonConfig.java
```

## Cobertura

JaCoCo ≥ 80% en líneas, branches, sentencias y métodos.
