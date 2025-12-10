# Risk Central Mock Service

Microservicio simulado para evaluación de riesgo crediticio con resultados deterministas.

## Características

- **Evaluación Determinista**: El mismo documento siempre genera el mismo score
- **Sin Base de Datos**: No requiere JPA ni persistencia
- **Sin Seguridad**: Enfocado en simplicidad
- **Algoritmo Consistente**: Usa hash del documento como seed

## Algoritmo de Evaluación

### 1. Generación del Seed
```java
long seed = Math.abs(documento.hashCode() % 1000);
```

### 2. Generación del Score
```java
Random random = new Random(seed);
int score = 300 + random.nextInt(651); // Rango: 300-950
```

### 3. Clasificación de Riesgo

| Score | Nivel de Riesgo | Decisión |
|-------|-----------------|----------|
| 300-500 | ALTO | Rechazado |
| 501-700 | MEDIO | Verificación adicional |
| 701-950 | BAJO | Aprobado |

## Ejecución

### Con Maven
```bash
mvn clean install
mvn spring-boot:run
```

### Con Docker
```bash
docker build -t risk-central-mock .
docker run -p 8081:8081 risk-central-mock
```

## Endpoint

### POST /risk-evaluation

**Request:**
```json
{
  "documento": "12345678",
  "monto": 5000.00,
  "plazo": 12
}
```

**Response:**
```json
{
  "documento": "12345678",
  "score": 650,
  "nivelRiesgo": "MEDIO",
  "detalle": "Medium risk: Score indicates moderate credit risk. Additional verification required."
}
```

## Prueba de Consistencia

El mismo documento siempre retorna el mismo resultado:

```bash
# Primera llamada
curl -X POST http://localhost:8081/risk-evaluation \
  -H "Content-Type: application/json" \
  -d '{"documento":"12345678","monto":5000,"plazo":12}'

# Segunda llamada (mismo resultado)
curl -X POST http://localhost:8081/risk-evaluation \
  -H "Content-Type: application/json" \
  -d '{"documento":"12345678","monto":5000,"plazo":12}'
```

## Puerto

El servicio corre en el puerto **8081** para no conflictuar con credit-application-service (8080).
