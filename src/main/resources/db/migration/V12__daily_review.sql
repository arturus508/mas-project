CREATE TABLE IF NOT EXISTS `daily_review` (
  `daily_review_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `date` DATE NOT NULL,
  `mood` INT,
  `energy` INT,
  `note` VARCHAR(1000),
  `reset_done` BIT NOT NULL DEFAULT 0,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`daily_review_id`),
  UNIQUE KEY `uk_daily_review_user_date` (`user_id`, `date`),
  KEY `idx_daily_review_user_id` (`user_id`),
  CONSTRAINT `fk_daily_review_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB;
