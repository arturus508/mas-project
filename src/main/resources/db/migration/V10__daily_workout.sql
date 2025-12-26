CREATE TABLE IF NOT EXISTS `daily_workout` (
  `daily_workout_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `workout_plan_day_id` BIGINT,
  `date` DATE NOT NULL,
  `title` VARCHAR(64) NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`daily_workout_id`),
  UNIQUE KEY `uk_daily_workout_user_date` (`user_id`, `date`),
  KEY `idx_daily_workout_user_id` (`user_id`),
  KEY `idx_daily_workout_plan_day_id` (`workout_plan_day_id`),
  CONSTRAINT `fk_daily_workout_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `fk_daily_workout_plan_day` FOREIGN KEY (`workout_plan_day_id`) REFERENCES `workout_plan_day` (`plan_day_id`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `daily_workout_set` (
  `daily_workout_set_id` BIGINT NOT NULL AUTO_INCREMENT,
  `daily_workout_id` BIGINT NOT NULL,
  `exercise_id` BIGINT,
  `set_number` INT NOT NULL,
  `reps_done` INT,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`daily_workout_set_id`),
  KEY `idx_daily_workout_set_workout_id` (`daily_workout_id`),
  KEY `idx_daily_workout_set_exercise_id` (`exercise_id`),
  CONSTRAINT `fk_daily_workout_set_workout` FOREIGN KEY (`daily_workout_id`) REFERENCES `daily_workout` (`daily_workout_id`),
  CONSTRAINT `fk_daily_workout_set_exercise` FOREIGN KEY (`exercise_id`) REFERENCES `exercise` (`exercise_id`)
) ENGINE=InnoDB;
