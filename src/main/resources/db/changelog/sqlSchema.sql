--liquibase formatted sql
--changeset morik:3

CREATE TABLE product
(
    articleNumber UUID NOT NULL default gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    categories VARCHAR(255),
    price FLOAT NOT NULL,
    quantity INTEGER default 0,
    date_last_changes_quantity TIMESTAMP default CURRENT_TIMESTAMP,
    date_creation TIMESTAMP default CURRENT_TIMESTAMP,
    PRIMARY KEY (articleNumber)
);