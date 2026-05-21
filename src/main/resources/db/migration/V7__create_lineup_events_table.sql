CREATE TABLE IF NOT EXISTS lineup_events (
    id BIGSERIAL PRIMARY KEY,
    artist_id BIGINT NOT NULL REFERENCES artists(id),
    stage_id BIGINT NOT NULL REFERENCES stages(id),
    starts_at TIMESTAMP WITH TIME ZONE NOT NULL,
    ends_at TIMESTAMP WITH TIME ZONE NOT NULL,
    title VARCHAR(255),
    status VARCHAR(50)
);

CREATE INDEX IF NOT EXISTS idx_lineup_events_starts_at ON lineup_events(starts_at);
CREATE INDEX IF NOT EXISTS idx_lineup_events_stage_id ON lineup_events(stage_id);
CREATE INDEX IF NOT EXISTS idx_lineup_events_artist_id ON lineup_events(artist_id);

INSERT INTO lineup_events (id, artist_id, stage_id, starts_at, ends_at, title, status) VALUES
(1, 1, 1, '2026-07-10T18:00:00+03:00', '2026-07-10T19:15:00+03:00', 'Opening Pop Set', 'SCHEDULED'),
(2, 2, 2, '2026-07-10T20:00:00+03:00', '2026-07-10T21:30:00+03:00', 'Rock Night', 'SCHEDULED'),
(3, 3, 3, '2026-07-11T22:00:00+03:00', '2026-07-11T23:45:00+03:00', 'Electronic Pulse', 'SCHEDULED')
ON CONFLICT (id) DO NOTHING;
