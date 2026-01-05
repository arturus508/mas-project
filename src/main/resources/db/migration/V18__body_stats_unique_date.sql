-- Ensure one body_stats entry per user per date.
DELETE bs1
FROM body_stats bs1
JOIN body_stats bs2
  ON bs1.user_id = bs2.user_id
 AND bs1.date_recorded = bs2.date_recorded
 AND bs1.body_stats_id < bs2.body_stats_id;

DELETE FROM body_stats
WHERE date_recorded IS NULL;

ALTER TABLE body_stats
  MODIFY date_recorded DATE NOT NULL;

ALTER TABLE body_stats
  ADD CONSTRAINT uk_body_stats_user_date UNIQUE (user_id, date_recorded);
