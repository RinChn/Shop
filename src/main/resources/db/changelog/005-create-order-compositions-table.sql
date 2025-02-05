--liquibase formatted sql
--changeset morik:5

CREATE TABLE order_compositions
(
    product_id UUID REFERENCES products(id),
    order_id UUID REFERENCES orders(id),
    product_quantity INTEGER default 1,
    price DECIMAL(38, 6),
    primary key (order_id, product_id)
)