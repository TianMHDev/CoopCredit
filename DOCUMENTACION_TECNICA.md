# 📘 Documentación Técnica Exhaustiva del Sistema CoopCredit

Este documento constituye la referencia técnica definitiva para el sistema **CoopCredit**. Ha sido elaborado para proporcionar una comprensión profunda de las decisiones arquitectónicas, patrones de diseño, flujo de datos y tecnologías empleadas. Está diseñado para servir como base para la sustentación técnica del proyecto.

---

## 1. 🏛 Filosofía Arquitectónica: Arquitectura Hexagonal

El sistema no sigue una arquitectura tradicional de capas (MVC), sino que implementa rigurosamente la **Arquitectura Hexagonal** (Ports and Adapters), propuesta por Alistair Cockburn.

### 1.1. El Problema de las Capas Tradicionales
En una arquitectura de capas tradicional (Controller -> Service -> Repository), la base de datos suele convertirse en el centro del diseño. Si cambiamos la base de datos, a menudo tenemos que cambiar el servicio. Esto genera un **acoplamiento fuerte**.

### 1.2. La Solución Hexagonal
La Arquitectura Hexagonal invierte esta dependencia. **El Dominio (Lógica de Negocio) es el centro del universo**. No sabe nada de bases de datos, ni de APIs REST, ni de frameworks.

#### Principio de Inversión de Dependencias (DIP)
Este es el pilar fundamental.
*   **Regla**: Los módulos de alto nivel (Dominio) no deben depender de detalles de bajo nivel (Infraestructura). Ambos deben depender de abstracciones (Puertos/Interfaces).
*   **En CoopCredit**: El Caso de Uso (`GestionSolicitudesUseCase`) **NO** depende de `JpaSolicitudRepository`. Depende de la interfaz `PersistencePort`. Es la infraestructura la que implementa esa interfaz.

### 1.3. Estructura Detallada de Capas

#### A. El Núcleo (Domain Layer)
Es el código más puro del sistema.
*   **Entidades del Dominio**: Objetos como `Solicitud` y `Afiliado`. Representan el estado y comportamiento del negocio. Son POJOs (Plain Old Java Objects) sin anotaciones de JPA ni Jackson.
*   **Puertos (Ports)**: Interfaces que definen contratos.
    *   *Input Ports (Driver)*: Definen qué puede hacer el usuario con el sistema (ej. `SolicitudServicePort`).
    *   *Output Ports (Driven)*: Definen qué necesita el sistema del mundo exterior (ej. `PersistencePort`, `RiskCentralPort`).

#### B. La Capa de Aplicación (Application Layer)
Es la capa de orquestación.
*   **Casos de Uso (Use Cases)**: Implementan los *Input Ports*. Contienen la lógica específica de un flujo de negocio (ej. "Procesar una solicitud de crédito").
*   **Responsabilidad**: Recibir datos del controlador, validarlos, llamar a las entidades del dominio, consultar puertos de salida y devolver una respuesta. **No toma decisiones técnicas**, solo de negocio.
x
#### C. La Capa de Infraestructura (Infrastructure Layer)
Aquí viven los detalles técnicos. Es la única capa que conoce a Spring Boot.
*   **Adaptadores Primarios (Driving)**: Inician la conversación.
    *   `SolicitudController`: Convierte una petición HTTP JSON en una llamada al puerto de entrada.
*   **Adaptadores Secundarios (Driven)**: Responden a lasx necesidades del dominio.
    *   `JpaSolicitudRepository`: Implementa `PersistencePort`. Sabe cómo hablar SQL con MySQL.
    *   `RiskCentralAdapter`: Implementa `RiskCentralPort`. Sabe cómo hacer peticiones HTTP a otro servicio.

---

## 2. 🧩 Patrones de Diseño Implementados

El código no es solo una secuencia de instrucciones; sigue patrones probados para resolver problemas recurrentes.

### 2.1. Inyección de Dependencias (Dependency Injection)
*   **Concepto**: Un objeto no debe crear sus propias dependencias. Se le deben suministrar desde fuera.
*   **Implementación**: Usamos el contenedor de Spring (IoC Container).
*   **Ejemplo**: El `SolicitudController` no hace `new GestionSolicitudesUseCase()`. En su lugar, declara que *necesita* un `SolicitudServicePort` en su constructor, y Spring se lo inyecta. Esto hace que el código sea extremadamente fácil de probar (podemos inyectar un Mock en los tests).
x
### 2.2. Patrón Adaptador (Adapter Pattern)
*   **Concepto**: Permite que interfaces incompatibles trabajen juntas.
*   **Implementación**: `RiskCentralAdapter`.
    *   El Dominio habla el lenguaje de `RiskCentralPort` (método `evaluateRisk`).
    *   La API externa habla HTTP/JSON.
    *   El Adaptador traduce la llamada del dominio a una petición HTTP, y la respuesta JSON a un objeto de dominio.

### 2.3. Patrón Repositorio (Repository Pattern)
*   **Concepto**: Abstrae el acceso a datos como si fuera una colección en memoria.
*   **Implementación**: Spring Data JPA nos da esto "gratis", pero nosotros lo desacoplamos aún más usando el puerto de persistencia. Esto nos permitiría cambiar MySQL por MongoDB sin tocar una sola línea del Caso de Uso.x

### 2.4. Data Transfer Object (DTO)
*   **Concepto**: Objetos simples para transferir datos entre procesos.
*   **Implementación**:
    *   `SolicitudRequest`: Lo que llega del frontend.
    *   `Solicitud`: La entidad de negocio.
    *   `SolicitudEntity`: La tabla en base de datos.
    *   **MapStruct**: Usamos esta librería para mapear automáticamente entre estos objetos, evitando el código repetitivo ("boilerplate") de getters y setters manuales.
x
---

## 3. 🌐 Comunicación entre Microservicios

El sistema es distribuido. No es un monolito.

### 3.1. Comunicación Síncrona (REST)
La comunicación entre `Credit Service` y `Risk Mock` es síncrona.
*   **Protocolo**: HTTP/1.1.
*   **Cliente**: `RestTemplate` (Cliente HTTP síncrono y bloqueante de Spring).
*   **Flujo**: Cuando se pide un crédito, el hilo de ejecución se "bloquea" esperando la respuesta del servicio de riesgos.
*   **Ventaja**: Simplicidad. Es fácil de entender y depurar.
*   **Desventaja**: Si el servicio de riesgos cae, el servicio de créditos no puede completar la operación (Acoplamiento temporal).

### 3.2. Manejo de Fallos
Aunque es un Mock, en un entorno real, el `RiskCentralAdapter` debería implementar patrones como **Circuit Breaker** (usando Resilience4j) para que, si el servicio externo falla, el sistema no colapse y pueda dar una respuesta por defecto o un error controlado.

---

## 4. 🔒 Seguridad Avanzada con JWT

La seguridad no es un añadido, es parte fundamental. Usamos **Stateless Authentication**.

### 4.1. ¿Qué es JWT (JSON Web Token)?
Es un estándar (RFC 7519) para transmitir información de forma segura. Un token tiene 3 partes separadas por puntos:
1.  **Header**: Algoritmo de encriptación (HS256).
2.  **Payload**: Datos del usuario (Claims: `sub`=username, `iat`=issued at, `exp`=expiration).
3.  **Signature**: Una firma criptográfica generada con una clave secreta (`app.jwtSecret`) que garantiza que el token no ha sido modificado.

### 4.2. El Filtro de Seguridad (`JwtAuthTokenFilter`)
Spring Security funciona con una cadena de filtros. Nosotros inyectamos nuestro filtro personalizado:
1.  **Intercepción**: Atrapa cada petición HTTP antes de que llegue al Controller.
2.  **Extracción**: Busca el header `Authorization: Bearer eyJhbG...`.
3.  **Validación**: Usa la librería `jjwt` para verificar la firma y la fecha de expiración.
4.  **Autenticación**: Si es válido, crea un objeto `UsernamePasswordAuthenticationToken` y lo pone en el contexto de seguridad. Spring ahora sabe quién es el usuario.

---

## 5. 📦 Infraestructura como Código: Docker

Docker nos permite empaquetar la aplicación con todas sus dependencias (Java, librerías, variables de entorno) en una unidad estandarizada.

### 5.1. Dockerfile Multi-Stage
Esta es una técnica profesional para optimizar imágenes.
*   **Stage 1 (Builder)**: Usa una imagen base con Maven y JDK completo. Copia el código fuente y ejecuta `mvn package`. El resultado es un archivo `.jar`. Esta imagen es pesada (>500MB).
*   **Stage 2 (Runner)**: Usa una imagen base `alpine-jre` (muy ligera, ~100MB). Solo copia el `.jar` del Stage 1.
*   **Resultado**: Una imagen final pequeña, segura y rápida de desplegar.

### 5.2. Docker Compose y Redes
`docker-compose` crea un entorno virtual completo.
*   **Redes (Networks)**: Crea una red interna donde los contenedores se ven por nombre. `credit-service` puede hacer ping a `db` o `risk-central-mock-service`.
*   **Volúmenes (Volumes)**: `db_data:/var/lib/mysql`. Esto es crucial. Si borras el contenedor de la base de datos, los datos **persisten** en el volumen de Docker. Sin esto, perderías todos los datos al reiniciar.

---

## 6. 🛠 Gestión de Datos y Dependencias

### 6.1. Flyway (Versionamiento de Base de Datos)
No creamos tablas manualmente. Usamos "Database as Code".
*   Tenemos scripts SQL en `src/main/resources/db/migration`.
*   Al iniciar, Flyway revisa una tabla especial (`flyway_schema_history`).
*   Si ve que falta aplicar el script `V1__init.sql`, lo ejecuta.
*   Esto garantiza que la estructura de la BD en producción sea idéntica a la de desarrollo.

### 6.2. Lombok
Es una librería de pre-procesamiento. Durante la compilación, inyecta automáticamente código repetitivo (getters, setters, constructores, toString) basándose en anotaciones (`@Data`, `@Builder`). Mantiene el código fuente limpio y legible.

---
x
## 7. 🔄 Trazabilidad del Flujo de Datos (Paso a Paso)

Para la sustentación, este es el camino que recorre un dato:

1.  **Petición**: El usuario envía JSON a `POST /solicitudes`.
2.  **Filtro JWT**: Valida identidad.
3.  **Controller**: Recibe `SolicitudRequest`. Usa `MapStruct` para convertirlo a `Solicitud` (Dominio).
4.  **UseCase**:
    *   Recibe `Solicitud`.
    *   Invoca `RiskCentralPort.evaluateRisk(afiliado)`.
5.  **RiskAdapter**:
    *   Serializa la petición a JSON.
    *   Envía HTTP POST al puerto 8081.
    *   Recibe respuesta (Score: 850).
    *   Retorna objeto de valor `EvaluacionRiesgo`.
6.  **UseCase (Lógica)**:
    *   Verifica: `if (score < 500) throw new CreditRejectedException()`.
    *   Si pasa, llama a `PersistencePort.save(solicitud)`.
7.  **JpaAdapter**:
    *   Recibe `Solicitud` (Dominio).
    *   Usa `MapStruct` para convertirlo a `SolicitudEntity` (JPA).
    *   Usa `JpaRepository.save(entity)`. Hibernate genera el SQL `INSERT INTO solicitudes...`.
8.  **Retorno**: El ID generado viaja de vuelta hacia arriba (Adapter -> UseCase -> Controller) y se devuelve al usuario.
