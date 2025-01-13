--liquibase formatted sql
--changeset morik:2

CREATE TABLE document
(
    id UUID NOT NULL default gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    content BYTEA,
    upload_date timestamp default CURRENT_TIMESTAMP,
    size BIGINT,
    mime_type VARCHAR(100),
    PRIMARY KEY (id)
);