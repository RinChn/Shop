--liquibase formatted sql
--changeset morik:1

CREATE TABLE products
(
    id UUID NOT NULL default gen_random_uuid(),
    article INTEGER NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    categories VARCHAR(255),
    price DECIMAL(38, 6) NOT NULL,
    quantity INTEGER default 0 CHECK (quantity >= 0),
    date_last_changes_quantity TIMESTAMP default CURRENT_TIMESTAMP,
    date_creation TIMESTAMP default CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);