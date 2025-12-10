# Credit Application Service - Affiliate Management Module

## Overview
This microservice implements the **Affiliate Management** module following **Hexagonal Architecture** (Ports and Adapters) and Spring Boot best practices.

## Architecture

### Hexagonal Architecture Layers

```
credit-application-service/
├── domain/                          # Core Business Logic (Pure Domain)
│   ├── model/                       # Domain entities (POJOs)
│   │   ├── Afiliado.java           # Affiliate domain model
│   │   └── EstadoAfiliado.java     # Affiliate status enum
│   ├── exception/                   # Business exceptions
│   │   ├── AfiliadoAlreadyExistsException.java
│   │   ├── AfiliadoNotFoundException.java
│   │   └── InvalidSalaryException.java
│   └── port/                        # Port interfaces
│       ├── in/                      # Input ports (Driving)
│       │   └── AfiliadoServicePort.java
│       └── out/                     # Output ports (Driven)
│           └── AfiliadoPersistencePort.java
│
├── application/                     # Application Layer
│   └── usecase/                     # Use case implementations
│       └── GestionAfiliadosUseCase.java
│
└── infrastructure/                  # Infrastructure Layer
    ├── adapter/
    │   ├── in/                      # Driving adapters
    │   │   └── web/
    │   │       ├── controller/
    │   │       │   └── AfiliadoController.java
    │   │       ├── dto/
    │   │       │   ├── AfiliadoRegisterRequest.java
    │   │       │   ├── AfiliadoUpdateRequest.java
    │   │       │   └── AfiliadoResponse.java
    │   │       ├── mapper/
    │   │       │   └── AfiliadoDtoMapper.java
    │   │       └── exception/
    │   │           ├── ErrorResponse.java
    │   │           └── GlobalExceptionHandler.java
    │   └── out/                     # Driven adapters
    │       └── persistence/
    │           ├── entity/
    │           │   └── AfiliadoEntity.java
    │           ├── repository/
    │           │   └── AfiliadoJpaRepository.java
    │           ├── mapper/
    │           │   └── AfiliadoMapper.java
    │           └── AfiliadoPersistenceAdapter.java
    └── config/
        └── BeanConfiguration.java
```

## Key Principles

### 1. **Pure Domain Model**
- The `Afiliado` class is a **POJO** without any framework dependencies
- No `@Entity`, `@Id`, or other JPA annotations in the domain layer
- Contains only business logic and domain rules

### 2. **Dependency Inversion**
- The domain layer defines **port interfaces** (contracts)
- Infrastructure layer provides **adapter implementations**
- Dependencies point inward (Infrastructure → Application → Domain)

### 3. **Business Validations**
The `GestionAfiliadosUseCase` implements the following validations:

#### Registration (`registrarAfiliado`)
1. **Document Uniqueness**: Validates that the document number doesn't already exist
2. **Salary Validation**: Ensures salary is greater than zero
3. **Default Values**: Sets initial status (ACTIVO) and affiliation date if not provided

#### Update (`editarAfiliado`)
1. **Existence Check**: Validates that the affiliate exists
2. **Salary Validation**: Ensures updated salary is greater than zero
3. **Immutable Fields**: Document and affiliation date cannot be changed

## API Endpoints

### Register Affiliate
```http
POST /api/v1/afiliados
Content-Type: application/json

{
  "documento": "12345678",
  "nombre": "John Doe",
  "salario": 3000.00,
  "fechaAfiliacion": "2024-01-15",
  "estado": "ACTIVO"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "documento": "12345678",
  "nombre": "John Doe",
  "salario": 3000.00,
  "fechaAfiliacion": "2024-01-15",
  "estado": "ACTIVO"
}
```

### Update Affiliate
```http
PUT /api/v1/afiliados/{id}
Content-Type: application/json

{
  "nombre": "John Smith",
  "salario": 3500.00,
  "estado": "ACTIVO"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "documento": "12345678",
  "nombre": "John Smith",
  "salario": 3500.00,
  "fechaAfiliacion": "2024-01-15",
  "estado": "ACTIVO"
}
```

## Error Handling

### Business Exceptions
- **409 Conflict**: `AfiliadoAlreadyExistsException` - Document already exists
- **404 Not Found**: `AfiliadoNotFoundException` - Affiliate not found
- **400 Bad Request**: `InvalidSalaryException` - Invalid salary value
- **400 Bad Request**: Validation errors from `@Valid` annotations

### Error Response Format
```json
{
  "timestamp": "2024-12-09T22:13:00",
  "status": 409,
  "error": "Conflict",
  "message": "Affiliate with document '12345678' already exists",
  "path": "/api/v1/afiliados",
  "validationErrors": null
}
```

## Database Schema

### Table: `afiliados`
```sql
CREATE TABLE afiliados (
    id BIGSERIAL PRIMARY KEY,
    documento VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(200) NOT NULL,
    salario DECIMAL(15,2) NOT NULL,
    fecha_afiliacion DATE NOT NULL,
    estado VARCHAR(20) NOT NULL,
    CONSTRAINT uk_afiliado_documento UNIQUE (documento)
);
```

## Technology Stack
- **Java 11**
- **Spring Boot 2.7.18**
- **Spring Data JPA**
- **PostgreSQL**
- **MapStruct 1.5.5** (for object mapping)
- **Bean Validation** (for input validation)
- **SpringDoc OpenAPI** (for API documentation)

## Setup and Running

### Prerequisites
- Java 11 or higher
- Maven 3.6+
- PostgreSQL 12+

### Database Setup
```sql
CREATE DATABASE coopcredit_db;
CREATE USER postgres WITH PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE coopcredit_db TO postgres;
```

### Build and Run
```bash
# Navigate to the project directory
cd credit-application-service

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### API Documentation
Once running, access the Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

## Testing

### Manual Testing with cURL

**Register an affiliate:**
```bash
curl -X POST http://localhost:8080/api/v1/afiliados \
  -H "Content-Type: application/json" \
  -d '{
    "documento": "12345678",
    "nombre": "John Doe",
    "salario": 3000.00,
    "estado": "ACTIVO"
  }'
```

**Update an affiliate:**
```bash
curl -X PUT http://localhost:8080/api/v1/afiliados/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "John Smith",
    "salario": 3500.00
  }'
```

## Key Checkpoints ✓

- [x] **Domain Model**: `Afiliado` is a pure POJO without framework dependencies
- [x] **Port Interfaces**: `AfiliadoServicePort` (input) and `AfiliadoPersistencePort` (output)
- [x] **Use Case**: `GestionAfiliadosUseCase` depends only on port interfaces
- [x] **Business Validations**: Document uniqueness and salary validation with business exceptions
- [x] **Adapters**: Persistence adapter (JPA) and REST adapter (Controller)
- [x] **Mapping**: MapStruct for Domain ↔ Entity and Domain ↔ DTO conversions
- [x] **Exception Handling**: Global exception handler for business exceptions
- [x] **Configuration**: Spring beans configured for dependency injection

## Next Steps

For the next module (Credit Application), the affiliate validation for **ACTIVO** status will be implemented in the credit application use case, not in the affiliate management module.

## License
MIT License
