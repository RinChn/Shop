--liquibase formatted sql
--changeset morik:1

CREATE TABLE product
(
    id UUID NOT NULL default gen_random_uuid(),
    article INTEGER NOT NULL UNIQUE IDENTITY(1, 1),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    categories VARCHAR(255),
    price DECIMAL NOT NULL,
    quantity INTEGER default 0,
    date_last_changes_quantity TIMESTAMP default CURRENT_TIMESTAMP,
    date_creation TIMESTAMP default CURRENT_TIMESTAMP,
    PRIMARY KEY (article_number)
);