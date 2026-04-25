CREATE TABLE orders(
        id BIGSERIAL PRIMARY KEY,
        user_id BIGINT NOT NULL,
        total_price NUMERIC(10,2) NOT NULL,
        status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
        created_at TIMESTAMP NOT NULL DEFAULT NOW()
);