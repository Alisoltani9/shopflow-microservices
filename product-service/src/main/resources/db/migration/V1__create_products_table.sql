CREATE TABLE products (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  price numeric(10,2) NOT NULL,
  stock INTEGER NOT NULL
);