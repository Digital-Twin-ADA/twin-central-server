CREATE TABLE IF NOT EXISTS alerts (
    id BIGSERIAL PRIMARY KEY,
    stage_id BIGINT REFERENCES stages(id),
    type VARCHAR(100) NOT NULL,
    message TEXT,
    severity VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    resolved BOOLEAN DEFAULT false,
    resolved_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX IF NOT EXISTS idx_alerts_stage_id ON alerts(stage_id);
