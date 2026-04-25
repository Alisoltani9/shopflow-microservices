CREATE TABLE order_items(
        id BIGSERIAL PRIMARY KEY,
        order_id BIGINT NOT NULL REFERENCES orders(id),
        product_id BIGINT NOT NULL,
        quantity INTEGER NOT NULL,
        price NUMERIC(10,2) NOT NULL
);