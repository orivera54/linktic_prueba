CREATE TABLE inventory (
    product_id UUID PRIMARY KEY,
    available INTEGER NOT NULL CHECK (available >= 0),
    reserved INTEGER NOT NULL DEFAULT 0 CHECK (reserved >= 0),
    version BIGINT NOT NULL DEFAULT 0,
    last_updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()
);

-- Idempotency table
CREATE TABLE idempotency_keys (
    key_id VARCHAR(255) PRIMARY KEY,
    response_status INTEGER NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()
);
