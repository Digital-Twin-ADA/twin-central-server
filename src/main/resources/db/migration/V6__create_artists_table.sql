CREATE TABLE IF NOT EXISTS artists (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    genre VARCHAR(100),
    bio TEXT,
    country VARCHAR(100),
    image_url VARCHAR(2048)
);

INSERT INTO artists (id, name, genre, bio, country, image_url) VALUES
(1, 'Aria Nova', 'Pop', 'High-energy pop artist known for immersive festival performances.', 'Romania', NULL),
(2, 'Cargo', 'Rock', 'Alternative rock band with anthemic live sets.', 'Romania', NULL),
(3, 'DJ Pulsewave', 'Electronic', 'Electronic producer blending melodic techno with festival bass.', 'Netherlands', NULL)
ON CONFLICT (id) DO NOTHING;
