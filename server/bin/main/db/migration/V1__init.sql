CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    parent_id BIGINT REFERENCES categories (id),
    sort_order INT NOT NULL DEFAULT 0
);

CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    category_id BIGINT NOT NULL REFERENCES categories (id),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(12, 2) NOT NULL,
    unit VARCHAR(32) NOT NULL DEFAULT 'шт',
    image_url VARCHAR(1024),
    stock INT NOT NULL DEFAULT 0,
    brand VARCHAR(255)
);

CREATE INDEX idx_products_category ON products (category_id);
CREATE INDEX idx_products_brand ON products (brand);
CREATE INDEX idx_products_price ON products (price);

CREATE TABLE user_addresses (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    label VARCHAR(255),
    address_line TEXT NOT NULL,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    is_default BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_user_addresses_user ON user_addresses (user_id);

CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users (id),
    status VARCHAR(32) NOT NULL,
    total NUMERIC(12, 2) NOT NULL,
    address_snapshot TEXT,
    delivery_address_id BIGINT REFERENCES user_addresses (id),
    payment_method VARCHAR(32) NOT NULL,
    payment_status VARCHAR(32) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_orders_user ON orders (user_id);

CREATE TABLE order_items (
    order_id BIGINT NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL REFERENCES products (id),
    quantity INT NOT NULL,
    price NUMERIC(12, 2) NOT NULL,
    PRIMARY KEY (order_id, product_id)
);

CREATE TABLE favorites (
    user_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL REFERENCES products (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, product_id)
);

CREATE TABLE refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    token_hash VARCHAR(64) NOT NULL UNIQUE,
    expires_at TIMESTAMP NOT NULL,
    revoked BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_refresh_tokens_user ON refresh_tokens (user_id);

CREATE TABLE push_subscriptions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    fcm_token TEXT NOT NULL,
    device_id VARCHAR(255),
    UNIQUE (user_id, fcm_token)
);
