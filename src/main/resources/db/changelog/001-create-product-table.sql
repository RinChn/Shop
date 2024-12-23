--liquibase formatted sql
--changeset morik:4

CREATE TABLE product
(
    article_number UUID NOT NULL default gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    categories VARCHAR(255),
    price FLOAT NOT NULL,
    quantity INTEGER default 0,
    date_last_changes_quantity TIMESTAMP default CURRENT_TIMESTAMP,
    date_creation TIMESTAMP default CURRENT_TIMESTAMP,
    PRIMARY KEY (article_number)
);