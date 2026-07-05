#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE customer_management_db;
    CREATE DATABASE account_management_db;
EOSQL

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname=customer_management_db <<-EOSQL

    CREATE TABLE IF NOT EXISTS person (
        id             BIGSERIAL       NOT NULL,
        name           VARCHAR(255)    NOT NULL,
        gender         VARCHAR(50),
        age            INTEGER,
        identification VARCHAR(255)    NOT NULL,
        address        VARCHAR(255),
        phone          VARCHAR(50),
        CONSTRAINT pk_person PRIMARY KEY (id),
        CONSTRAINT uq_person_identification UNIQUE (identification)
    );

    CREATE TABLE IF NOT EXISTS client (
        id        BIGINT       NOT NULL,
        client_id VARCHAR(255) NOT NULL,
        password  VARCHAR(255) NOT NULL,
        status    BOOLEAN      NOT NULL DEFAULT TRUE,
        CONSTRAINT pk_client         PRIMARY KEY (id),
        CONSTRAINT uq_client_id      UNIQUE (client_id),
        CONSTRAINT fk_client_person  FOREIGN KEY (id)
            REFERENCES person (id) ON DELETE CASCADE
    );

    CREATE INDEX IF NOT EXISTS idx_client_client_id ON client (client_id);

EOSQL

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname=account_management_db <<-EOSQL

    CREATE TABLE IF NOT EXISTS account (
        id              BIGSERIAL      NOT NULL,
        account_number  VARCHAR(20)    NOT NULL,
        account_type    VARCHAR(20)    NOT NULL,
        initial_balance NUMERIC(15,2)  NOT NULL DEFAULT 0.00,
        current_balance NUMERIC(15,2)  NOT NULL DEFAULT 0.00,
        status          BOOLEAN        NOT NULL DEFAULT TRUE,
        customer_id     BIGINT         NOT NULL,
        CONSTRAINT pk_account         PRIMARY KEY (id),
        CONSTRAINT uq_account_number  UNIQUE (account_number),
        CONSTRAINT chk_account_type   CHECK (account_type IN ('SAVINGS', 'CHECKING')),
        CONSTRAINT chk_balance        CHECK (current_balance >= 0)
    );

    CREATE TABLE IF NOT EXISTS movement (
        id            BIGSERIAL      NOT NULL,
        date          DATE           NOT NULL,
        movement_type VARCHAR(20)    NOT NULL,
        value         NUMERIC(15,2)  NOT NULL,
        balance       NUMERIC(15,2)  NOT NULL,
        account_id    BIGINT         NOT NULL,
        CONSTRAINT pk_movement          PRIMARY KEY (id),
        CONSTRAINT fk_movement_account  FOREIGN KEY (account_id)
            REFERENCES account (id) ON UPDATE CASCADE ON DELETE RESTRICT,
        CONSTRAINT chk_movement_type    CHECK (movement_type IN ('DEPOSIT', 'WITHDRAWAL'))
    );

    CREATE TABLE IF NOT EXISTS customer_snapshot (
        customer_id BIGINT       NOT NULL,
        name        VARCHAR(255) NOT NULL,
        status      BOOLEAN      NOT NULL DEFAULT TRUE,
        CONSTRAINT pk_customer_snapshot PRIMARY KEY (customer_id)
    );

    CREATE INDEX IF NOT EXISTS idx_account_customer_id    ON account  (customer_id);
    CREATE INDEX IF NOT EXISTS idx_account_number         ON account  (account_number);
    CREATE INDEX IF NOT EXISTS idx_movement_account_date  ON movement (account_id, date);

EOSQL
