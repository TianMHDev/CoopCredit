CREATE TABLE IF NOT EXISTS afiliados (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    documento VARCHAR(20) NOT NULL,
    nombre VARCHAR(200) NOT NULL,
    salario DECIMAL(15,2) NOT NULL,
    fecha_afiliacion DATE NOT NULL,
    estado VARCHAR(20) NOT NULL,
    CONSTRAINT uk_afiliado_documento UNIQUE (documento)
);

CREATE TABLE IF NOT EXISTS evaluaciones_riesgo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    score INT NOT NULL,
    nivel_riesgo VARCHAR(255) NOT NULL,
    detalle VARCHAR(255) NOT NULL,
    fecha_evaluacion DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS solicitudes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    afiliado_id BIGINT NOT NULL,
    monto_solicitado DECIMAL(19,2) NOT NULL,
    plazo INT NOT NULL,
    tasa_propuesta DECIMAL(19,2) NOT NULL,
    fecha_solicitud DATE NOT NULL,
    estado VARCHAR(255) NOT NULL,
    evaluacion_id BIGINT
);

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    CONSTRAINT uk_users_username UNIQUE (username)
);
