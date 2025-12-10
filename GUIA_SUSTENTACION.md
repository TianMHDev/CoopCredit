# 🎓 Guía de Defensa y Sustentación - CoopCredit

Este documento está diseñado para prepararte para tu sustentación. No es solo documentación técnica, es un **guion estratégico** con las preguntas más probables que te hará el jurado y las mejores respuestas para demostrar dominio.

---

## 1. 🎤 El "Elevator Pitch" (Introducción de 2 minutos)
*Usa esto para abrir tu presentación.*

"Buenos días. El proyecto **CoopCredit** es una solución moderna para la gestión de créditos en cooperativas.
El problema que resolvemos es la necesidad de un sistema desacoplado, escalable y seguro para procesar solicitudes de crédito.
Para lograrlo, no construimos un monolito tradicional. Diseñamos una arquitectura basada en **Microservicios** y **Arquitectura Hexagonal**.
Esto nos permite tener un núcleo de negocio puro, independiente de la base de datos o frameworks, y conectarnos con servicios externos (como centrales de riesgo) sin afectar nuestra lógica principal.
Todo el ecosistema está contenerizado con **Docker** para garantizar un despliegue estándar y seguro."

---

## 2. 🔥 Preguntas Frecuentes (Q&A) por Tema

### 🏛️ Sobre Arquitectura Hexagonal

**P: ¿Por qué usaste Arquitectura Hexagonal y no la clásica MVC de 3 capas?**
> **R:** "Porque quería proteger la lógica de negocio. En MVC tradicional, es fácil que la lógica termine dependiendo de la base de datos o del controlador. Con Hexagonal (Puertos y Adaptadores), invierto esa dependencia: mi dominio es el centro y no sabe nada de la base de datos. Esto hace que el sistema sea más mantenible y mucho más fácil de testear, ya que puedo probar el negocio sin levantar la BD."

**P: Explícame qué son los Puertos y los Adaptadores en tu proyecto.**
> **R:** "Los **Puertos** son interfaces (contratos) que define mi dominio.
> *   El *Puerto de Entrada* (`SolicitudServicePort`) define qué puede hacer el usuario.
> *   El *Puerto de Salida* (`PersistencePort`) define qué necesita el dominio guardar.
> Los **Adaptadores** son la implementación real.
> *   El `SolicitudController` es un adaptador que llama al puerto de entrada.
> *   El `JpaRepository` es un adaptador que implementa el puerto de salida para hablar con MySQL."

### 🌐 Sobre Microservicios

**P: ¿Cómo se comunican tus microservicios?**
> **R:** "Usan comunicación síncrona vía **REST**. El servicio de Créditos actúa como cliente usando `RestTemplate` para consultar al servicio Mock de Riesgos. Elegí REST por su simplicidad y estandarización, aunque soy consciente de que genera un acoplamiento temporal (si el mock cae, la solicitud falla)."

**P: ¿Qué pasa si el servicio de Riesgos se cae?**
> **R:** "Actualmente, la operación fallaría controladamente. En una versión futura, implementaría un patrón **Circuit Breaker** (con Resilience4j) para detectar la falla y quizás permitir una aprobación manual o reintentar más tarde, evitando que todo el sistema colapse."

### 🔒 Sobre Seguridad (JWT)

**P: ¿Por qué usaste JWT y no sesiones tradicionales?**
> **R:** "Porque JWT permite una autenticación **Stateless** (sin estado). Como estamos en microservicios, no quiero guardar sesiones en la memoria del servidor, porque si escalo horizontalmente (pongo 5 servidores), la sesión se perdería. Con JWT, el token viaja con el usuario y cualquier microservicio puede validarlo solo verificando su firma criptográfica."

**P: ¿Dónde se valida el Token?**
> **R:** "Tengo un filtro personalizado (`JwtAuthTokenFilter`) que intercepta cada petición HTTP antes de llegar al controlador. Verifica la firma del token y, si es válido, inyecta la identidad del usuario en el contexto de seguridad de Spring."

### 🐳 Sobre Infraestructura (Docker)

**P: ¿Para qué sirve el Dockerfile Multi-stage que mencionaste?**
> **R:** "Sirve para optimizar el tamaño de la imagen y la seguridad. En la primera etapa uso una imagen con Maven para compilar (que es pesada). En la segunda etapa, solo copio el `.jar` resultante a una imagen Alpine muy ligera (JRE). Así, mi contenedor final pesa 100MB en lugar de 600MB y no lleva el código fuente ni herramientas de compilación."

**P: ¿Qué ventaja te da Docker Compose?**
> **R:** "Me permite orquestar todo el entorno con un solo comando. Levanta la base de datos, el mock y el servicio principal, y crea una red interna para que se comuniquen entre ellos por nombre DNS, sin tener que configurar IPs manualmente."

---

## 3. 🧠 Preguntas "Corchadoras" (Nivel Avanzado)

**P: ¿Por qué usaste MySQL y no MongoDB (NoSQL)?**
> **R:** "Porque las transacciones financieras requieren consistencia fuerte (ACID). Una solicitud de crédito es un dato estructurado y relacional (Afiliado -> Solicitud). MySQL garantiza integridad referencial y transaccionalidad, algo crítico para datos bancarios. MongoDB sería mejor para logs o datos no estructurados."

**P: Veo que usas muchos DTOs y Mappers, ¿no es mucho código repetitivo?**
> **R:** "Al contrario, uso **MapStruct** para evitar escribir ese código repetitivo. Y el uso de DTOs es vital para desacoplar. Si cambio una columna en la base de datos, no quiero que eso rompa la API que consumen los clientes. Los DTOs actúan como un contrato estable para el frontend."

**P: ¿Cómo manejarías la concurrencia si 1000 personas piden crédito al tiempo?**
> **R:** "Gracias a que el servicio es Stateless (por JWT y Docker), puedo escalar horizontalmente. Podría usar Kubernetes para levantar 10 instancias de mi servicio de créditos detrás de un balanceador de carga. La base de datos sería el cuello de botella, pero podría mitigarlo con réplicas de lectura."

### 📊 Sobre Observabilidad (Actuator & Prometheus)

**P: ¿Qué es Spring Boot Actuator y para qué lo usas?**
> **R:** "Actuator es una librería de Spring Boot que pone mi aplicación 'lista para producción'. Me permite ver el estado interno del sistema sin tener que entrar al servidor.
> *   Me da endpoints como `/health` para saber si la aplicación y la BD están vivas.
> *   Me da `/metrics` para ver uso de memoria, CPU y hilos.
> En resumen, son los 'signos vitales' de mi microservicio."

**P: ¿Y qué función cumple Prometheus ahí?**
> **R:** "Actuator solo *muestra* los datos en tiempo real, pero no guarda historia. Prometheus es quien **lee y guarda** esos datos.
> *   Actuator expone un endpoint especial (`/actuator/prometheus`) con formato compatible.
> *   Prometheus 'visita' (scrape) ese endpoint cada ciertos segundos y guarda los valores en su base de datos de series de tiempo.
> *   Esto me permitiría, por ejemplo, ver un gráfico de cuántas solicitudes recibí en la última hora o si el consumo de memoria subió ayer."

---

## 4. 💡 Tips para tu Demo en Vivo

1.  **Ten Postman listo**: Ten las pestañas de "Login", "Registrar Afiliado" y "Crear Solicitud" ya abiertas y ordenadas.
2.  **Muestra los Logs**: Cuando hagas clic en "Enviar" en Postman, ten la terminal visible mostrando los logs (`docker compose logs -f`). Ver cómo aparecen los mensajes en tiempo real ("Evaluando riesgo...", "Solicitud Aprobada") da mucha credibilidad.
3.  **Falla a propósito (Opcional)**: Si te sientes confiado, muestra qué pasa si pides un monto gigante. Muestra cómo el sistema lo rechaza correctamente. Eso demuestra que las reglas de negocio funcionan.
4.  **Base de Datos**: Ten abierto DBeaver o Workbench. Muestra la tabla vacía antes de la solicitud y la tabla con el registro después.

---

## 5. 📝 Resumen de Tecnologías (Tu "Cheat Sheet")

*   **Lenguaje**: Java 17 (Moderno, LTS).
*   **Framework**: Spring Boot 2.7 (Estándar de industria).
*   **Arquitectura**: Hexagonal (Desacoplamiento).
*   **BD**: MySQL 8 (Relacional, ACID).
*   **Migraciones**: Flyway (Control de versiones de BD).
*   **Seguridad**: JWT + Spring Security.
*   **Cliente HTTP**: RestTemplate.
*   **Mapeo**: MapStruct + Lombok.
*   **Despliegue**: Docker + Docker Compose.
