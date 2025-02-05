--liquibase formatted sql
--changeset morik:3

CREATE TABLE users
(
    id UUID NOT NULL PRIMARY KEY default gen_random_uuid(),
    email VARCHAR(255) NOT NULL UNIQUE
)