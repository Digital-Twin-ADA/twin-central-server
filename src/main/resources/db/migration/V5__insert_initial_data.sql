-- =====================================
-- 1. INSERT STAGES
-- =====================================
INSERT INTO stages (id, name, capacity, latitude, longitude) VALUES
(1, 'Main Stage', 5000, 45.7489, 21.2087),
(2, 'Rock Stage', 3000, 45.7492, 21.2091),
(3, 'Electronic Stage', 4000, 45.7485, 21.2095),
(4, 'Acoustic Stage', 800, 45.7490, 21.2080);

-- =====================================
-- 2. INSERT FESTIVAL INFO (FIXED)
-- =====================================
INSERT INTO festival_info (id, name, latitude, longitude, description) VALUES
(1, 'UNTOLD Digital Twin Festival', 45.7489, 21.2087, 'Main festival area');

-- =====================================
-- 3. INSERT WEBHOOK SUBSCRIBERS
-- =====================================
INSERT INTO webhook_subscribers (id, url) VALUES
(1, 'http://localhost:3000/webhook'),
(2, 'http://example.com/api/alerts');

-- =====================================
-- 4. INSERT ALERTS
-- =====================================
INSERT INTO alerts (id, type, message, stage_id) VALUES
(1, 'CROWD', 'Overcrowding detected', 1),
(2, 'TECHNICAL', 'Sound system issue', 2),
(3, 'WEATHER', 'Temperature too high', 3);

-- =====================================
-- 5. INSERT NOTIFICATION ATTEMPTS
-- =====================================
INSERT INTO notification_attempts (id, alert_id, subscriber_id, status) VALUES
(1, 1, 1, 'SUCCESS'),
(2, 2, 1, 'FAILED'),
(3, 3, 2, 'SUCCESS');