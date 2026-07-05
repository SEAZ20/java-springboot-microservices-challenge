-- ============================================================
-- BANKING MICROSERVICES — ESQUEMA POSTGRESQL
-- ============================================================

-- Bases de datos creadas:
--   • customer_management_db  - customer-management-service
--   • account_management_db   - account-management-service

-- CREAR BASES DE DATOS

CREATE DATABASE customer_management_db
    ENCODING    'UTF8'
    LC_COLLATE  'en_US.UTF-8'
    LC_CTYPE    'en_US.UTF-8'
    TEMPLATE    template0;

CREATE DATABASE account_management_db
    ENCODING    'UTF8'
    LC_COLLATE  'en_US.UTF-8'
    LC_CTYPE    'en_US.UTF-8'
    TEMPLATE    template0;


-- ============================================================
-- MÓDULO: customer_management_db
-- Gestión de personas y clientes bancarios
-- ============================================================

\connect customer_management_db

CREATE TABLE IF NOT EXISTS person (
    id             BIGSERIAL       NOT NULL,
    name           VARCHAR(255)    NOT NULL,
    gender         VARCHAR(50),
    age            INTEGER,
    identification VARCHAR(255)    NOT NULL,
    address        VARCHAR(255),
    phone          VARCHAR(50),

    CONSTRAINT pk_person                   PRIMARY KEY (id),
    CONSTRAINT uq_person_identification    UNIQUE      (identification),
    CONSTRAINT chk_person_age              CHECK       (age IS NULL OR age >= 0)
);

COMMENT ON TABLE  person                IS 'Entidad base para personas. Las subclases se unen mediante el id.';
COMMENT ON COLUMN person.identification IS 'Número de cédula o pasaporte — debe ser único.';

-- ------------------------------------------------------------
-- Tabla: client
-- Extiende person mediante herencia JOINED
-- Mapeada en: CustomerEntity.java
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS client (
    id        BIGINT       NOT NULL,
    client_id VARCHAR(255) NOT NULL,
    password  VARCHAR(255) NOT NULL,
    status    BOOLEAN      NOT NULL DEFAULT TRUE,

    CONSTRAINT pk_client        PRIMARY KEY (id),
    CONSTRAINT uq_client_id     UNIQUE      (client_id),
    CONSTRAINT fk_client_person FOREIGN KEY (id)
        REFERENCES person (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

COMMENT ON TABLE  client          IS 'Cliente bancario — extiende person mediante herencia JOINED.';
COMMENT ON COLUMN client.client_id IS 'UUID de negocio asignado en la creación.';
COMMENT ON COLUMN client.status    IS 'TRUE = activo, FALSE = inactivo.';

-- ============================================================
-- ÍNDICES — customer_management_db
-- ============================================================

CREATE INDEX IF NOT EXISTS idx_client_client_id   ON client (client_id);
CREATE INDEX IF NOT EXISTS idx_person_identification ON person (identification);


-- ============================================================
-- MÓDULO: account_management_db
-- Gestión de cuentas, movimientos y snapshots de clientes
-- ============================================================

\connect account_management_db

-- ------------------------------------------------------------
-- Tabla: account
-- Mapeada en: AccountEntity.java
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS account (
    id              BIGSERIAL       NOT NULL,
    account_number  VARCHAR(20)     NOT NULL,
    account_type    VARCHAR(20)     NOT NULL,
    initial_balance NUMERIC(15, 2)  NOT NULL DEFAULT 0.00,
    current_balance NUMERIC(15, 2)  NOT NULL DEFAULT 0.00,
    status          BOOLEAN         NOT NULL DEFAULT TRUE,
    customer_id     BIGINT          NOT NULL,

    CONSTRAINT pk_account           PRIMARY KEY (id),
    CONSTRAINT uq_account_number    UNIQUE      (account_number),
    CONSTRAINT chk_account_type     CHECK       (account_type IN ('SAVINGS', 'CHECKING')),
    CONSTRAINT chk_current_balance  CHECK       (current_balance >= 0),
    CONSTRAINT chk_initial_balance  CHECK       (initial_balance >= 0)
);

COMMENT ON TABLE  account               IS 'Cuenta bancaria. Referencia al cliente mediante FK lógica (entre bases de datos).';
COMMENT ON COLUMN account.account_type  IS 'SAVINGS (ahorros) | CHECKING (corriente)';
COMMENT ON COLUMN account.customer_id   IS 'Referencia lógica a client.id en customer_management_db.';

-- ------------------------------------------------------------
-- Tabla: movement
-- Mapeada en: MovementEntity.java
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS movement (
    id            BIGSERIAL       NOT NULL,
    date          DATE            NOT NULL,
    movement_type VARCHAR(20)     NOT NULL,
    value         NUMERIC(15, 2)  NOT NULL,
    balance       NUMERIC(15, 2)  NOT NULL,
    account_id    BIGINT          NOT NULL,

    CONSTRAINT pk_movement          PRIMARY KEY (id),
    CONSTRAINT fk_movement_account  FOREIGN KEY (account_id)
        REFERENCES account (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT chk_movement_type    CHECK (movement_type IN ('DEPOSIT', 'WITHDRAWAL'))
);

COMMENT ON TABLE  movement               IS 'Movimiento financiero asociado a una cuenta.';
COMMENT ON COLUMN movement.value         IS 'Positivo = depósito, negativo = retiro.';
COMMENT ON COLUMN movement.balance       IS 'Saldo resultante de la cuenta tras el movimiento.';
COMMENT ON COLUMN movement.movement_type IS 'DEPOSIT (depósito) | WITHDRAWAL (retiro)';

-- ------------------------------------------------------------
-- Tabla: customer_snapshot
-- Modelo de lectura local sincronizado mediante eventos RabbitMQ
-- Mapeada en: CustomerSnapshotEntity.java
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS customer_snapshot (
    customer_id BIGINT       NOT NULL,
    name        VARCHAR(255) NOT NULL,
    status      BOOLEAN      NOT NULL DEFAULT TRUE,

    CONSTRAINT pk_customer_snapshot PRIMARY KEY (customer_id)
);

COMMENT ON TABLE  customer_snapshot            IS 'Copia local desnormalizada de datos del cliente, actualizada mediante eventos de dominio.';
COMMENT ON COLUMN customer_snapshot.customer_id IS 'Espejo de client.id de customer_management_db.';

-- ============================================================
-- ÍNDICES — account_management_db
-- ============================================================

-- Usado en consultas JOIN para reportes (getStatement)
CREATE INDEX IF NOT EXISTS idx_account_customer_id   ON account  (customer_id);
CREATE INDEX IF NOT EXISTS idx_account_number        ON account  (account_number);

-- Usado en findByCustomerIdAndDateBetween (JpaMovementRepository)
CREATE INDEX IF NOT EXISTS idx_movement_account_date ON movement (account_id, date);
CREATE INDEX IF NOT EXISTS idx_movement_date         ON movement (date);
