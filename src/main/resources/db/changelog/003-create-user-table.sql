--liquibase formatted sql
--changeset morik:3

CREATE TABLE users
(
    id UUID NOT NULL default gen_random_uuid() PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE
)