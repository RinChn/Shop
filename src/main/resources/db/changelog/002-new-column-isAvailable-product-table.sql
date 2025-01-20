--liquibase formatted sql
--changeset morik:3

ALTER TABLE product ADD is_available BOOLEAN NOT NULL DEFAULT TRUE;