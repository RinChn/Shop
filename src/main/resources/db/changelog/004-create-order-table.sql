--liquibase formatted sql
--changeset morik:4

CREATE TABLE orders
(
    id UUID NOT NULL PRIMARY KEY default gen_random_uuid(),
    number INTEGER NOT NULL,
    customer_id UUID NOT NULL,
    price DECIMAL(38, 6) default 0,
    status VARCHAR(255) default 'CREATED',
    FOREIGN KEY (customer_id) REFERENCES users(id)
)