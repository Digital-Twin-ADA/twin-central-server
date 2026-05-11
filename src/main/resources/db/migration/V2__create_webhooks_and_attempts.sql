CREATE TABLE IF NOT EXISTS webhook_subscribers (
  id BIGSERIAL PRIMARY KEY,
  url VARCHAR(2048) NOT NULL,
  secret VARCHAR(512),
  active BOOLEAN DEFAULT true,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE IF NOT EXISTS notification_attempts (
  id BIGSERIAL PRIMARY KEY,
  alert_id BIGINT REFERENCES alerts(id),
  subscriber_id BIGINT REFERENCES webhook_subscribers(id) ON DELETE CASCADE,
  status VARCHAR(50),
  attempts INT DEFAULT 0,
  last_response TEXT,
  last_attempt_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX IF NOT EXISTS idx_webhook_active ON webhook_subscribers(active);
CREATE INDEX IF NOT EXISTS idx_notification_alert ON notification_attempts(alert_id);
CREATE INDEX IF NOT EXISTS idx_notification_subscriber ON notification_attempts(subscriber_id);
