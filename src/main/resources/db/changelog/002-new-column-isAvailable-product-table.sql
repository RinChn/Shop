--liquibase formatted sql
--changeset morik:2

ALTER TABLE products ADD is_available BOOLEAN NOT NULL DEFAULT TRUE;