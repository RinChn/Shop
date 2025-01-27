--liquibase formatted sql
--changeset morik:4

CREATE TABLE orders
(
    id UUID NOT NULL PRIMARY KEY default gen_random_uuid(),
    user_id UUID NOT NULL,
    price DECIMAL(38, 6) default 0,
    status VARCHAR(255) default 'CREATED',
    FOREIGN KEY (user_id) REFERENCES users(id)
)