CREATE TABLE IF NOT EXISTS stages (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    capacity INT NOT NULL,
    current_crowd INT NOT NULL DEFAULT 0,
    overcrowded BOOLEAN NOT NULL DEFAULT false,
    zone_code VARCHAR(255)
);
