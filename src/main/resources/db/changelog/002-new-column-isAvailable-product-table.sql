--liquibase formatted sql
--changeset morik:1

ALTER TABLE product ADD is_available BOOLEAN NOT NULL DEFAULT TRUE;