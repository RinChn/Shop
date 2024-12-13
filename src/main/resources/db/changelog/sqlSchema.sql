--liquibase formatted sql
--changeset morik:1

CREATE TABLE product
(
    id UUID NOT NULL default gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    categories TEXT,
    price FLOAT NOT NULL,
    quantity INTEGER DEFAULT '0',
    articleNumber BIGINT NOT NULL,
    date_last_changes_quantity TIMESTAMP default CURRENT_TIMESTAMP,
    date_creation TIMESTAMP default CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);