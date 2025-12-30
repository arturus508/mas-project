CREATE TABLE IF NOT EXISTS `user` (
  `user_id` BIGINT NOT NULL AUTO_INCREMENT,
  `dtype` VARCHAR(31) NOT NULL,
  `name` VARCHAR(255),
  `email` VARCHAR(255),
  `password` VARCHAR(255),
  `admin_level` INT,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `exercise` (
  `exercise_id` BIGINT NOT NULL AUTO_INCREMENT,
  `exercise_name` VARCHAR(255),
  `muscle_group` VARCHAR(255),
  `intensity_level` VARCHAR(255),
  `reps` INT NOT NULL DEFAULT 0,
  `rest_time` INT NOT NULL DEFAULT 0,
  `sets` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`exercise_id`),
  UNIQUE KEY `uk_exercise_name` (`exercise_name`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `food_item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(120) NOT NULL,
  `unit` VARCHAR(16) NOT NULL,
  `kcal100` INT NOT NULL,
  `protein100` INT NOT NULL,
  `fat100` INT NOT NULL,
  `carbs100` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_food_item_name` (`name`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `meal_plan` (
  `meal_plan_id` BIGINT NOT NULL AUTO_INCREMENT,
  `plan_name` VARCHAR(255),
  `description` VARCHAR(255),
  `dietary_restriction` VARCHAR(255),
  `start_date` DATE,
  `end_date` DATE,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`meal_plan_id`),
  KEY `idx_meal_plan_user_id` (`user_id`),
  CONSTRAINT `fk_meal_plan_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `meal` (
  `meal_id` BIGINT NOT NULL AUTO_INCREMENT,
  `meal_plan_id` BIGINT,
  `user_id` BIGINT NOT NULL,
  `meal_type` VARCHAR(24) NOT NULL,
  `date` DATE NOT NULL,
  `kcal_manual` INT,
  `protein_manual` INT,
  `fat_manual` INT,
  `carbs_manual` INT,
  `total_calories` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`meal_id`),
  KEY `idx_meal_plan_id` (`meal_plan_id`),
  KEY `idx_meal_user_id` (`user_id`),
  CONSTRAINT `fk_meal_plan` FOREIGN KEY (`meal_plan_id`) REFERENCES `meal_plan` (`meal_plan_id`),
  CONSTRAINT `fk_meal_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `ingredient` (
  `ingredient_id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255),
  `quantity` INT NOT NULL,
  `calories_per_portion` INT NOT NULL,
  `meal_id` BIGINT,
  PRIMARY KEY (`ingredient_id`),
  KEY `idx_ingredient_meal_id` (`meal_id`),
  CONSTRAINT `fk_ingredient_meal` FOREIGN KEY (`meal_id`) REFERENCES `meal` (`meal_id`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `meal_item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `meal_id` BIGINT NOT NULL,
  `food_item_id` BIGINT NOT NULL,
  `quantity` INT NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_meal_item_meal_id` (`meal_id`),
  KEY `idx_meal_item_food_item_id` (`food_item_id`),
  CONSTRAINT `fk_meal_item_meal` FOREIGN KEY (`meal_id`) REFERENCES `meal` (`meal_id`),
  CONSTRAINT `fk_meal_item_food_item` FOREIGN KEY (`food_item_id`) REFERENCES `food_item` (`id`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `workout_plan` (
  `workout_plan_id` BIGINT NOT NULL AUTO_INCREMENT,
  `plan_name` VARCHAR(255),
  `description` VARCHAR(255),
  `status` VARCHAR(255),
  `start_date` DATE,
  `end_date` DATE,
  `days_per_week` INT,
  `user_id` BIGINT,
  PRIMARY KEY (`workout_plan_id`),
  KEY `idx_workout_plan_user_id` (`user_id`),
  CONSTRAINT `fk_workout_plan_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `workout_plan_exercise` (
  `wp_exercise_id` BIGINT NOT NULL AUTO_INCREMENT,
  `sets` INT NOT NULL,
  `reps` INT NOT NULL,
  `rest_time` INT NOT NULL,
  `workout_plan_id` BIGINT,
  `exercise_id` BIGINT,
  PRIMARY KEY (`wp_exercise_id`),
  KEY `idx_wpe_workout_plan_id` (`workout_plan_id`),
  KEY `idx_wpe_exercise_id` (`exercise_id`),
  CONSTRAINT `fk_wpe_workout_plan` FOREIGN KEY (`workout_plan_id`) REFERENCES `workout_plan` (`workout_plan_id`),
  CONSTRAINT `fk_wpe_exercise` FOREIGN KEY (`exercise_id`) REFERENCES `exercise` (`exercise_id`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `body_stats` (
  `body_stats_id` BIGINT NOT NULL AUTO_INCREMENT,
  `date_recorded` DATE,
  `weight` DOUBLE,
  `body_fat_percent` DOUBLE,
  `bmi` DOUBLE,
  `user_id` BIGINT,
  PRIMARY KEY (`body_stats_id`),
  KEY `idx_body_stats_user_id` (`user_id`),
  CONSTRAINT `fk_body_stats_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `sleep_entry` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `date` DATE NOT NULL,
  `sleep_start` DATETIME NOT NULL,
  `sleep_end` DATETIME NOT NULL,
  `quality` INT NOT NULL,
  `note` VARCHAR(1000),
  PRIMARY KEY (`id`),
  KEY `idx_sleep_entry_user_id` (`user_id`),
  CONSTRAINT `fk_sleep_entry_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `dream_entry` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `sleep_entry_id` BIGINT,
  `date` DATE NOT NULL,
  `title` VARCHAR(255) NOT NULL,
  `content` LONGTEXT NOT NULL,
  `tags` VARCHAR(255),
  `mood` INT,
  `lucid` BIT,
  `nightmare` BIT,
  `created_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_dream_entry_user_id` (`user_id`),
  KEY `idx_dream_entry_sleep_entry_id` (`sleep_entry_id`),
  CONSTRAINT `fk_dream_entry_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `fk_dream_entry_sleep_entry` FOREIGN KEY (`sleep_entry_id`) REFERENCES `sleep_entry` (`id`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `habit` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `name` VARCHAR(160) NOT NULL,
  `target_per_day` INT NOT NULL DEFAULT 1,
  `active` BIT NOT NULL DEFAULT 1,
  `created_at` DATETIME NOT NULL,
  `cadence` VARCHAR(16) NOT NULL DEFAULT 'DAILY',
  PRIMARY KEY (`id`),
  KEY `idx_habit_user_id` (`user_id`),
  CONSTRAINT `fk_habit_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `habit_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `habit_id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL,
  `date` DATE NOT NULL,
  `done` BIT NOT NULL,
  `value` INT,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_habit_log_habit_id_date` (`habit_id`, `date`),
  KEY `idx_habit_log_user_id` (`user_id`),
  CONSTRAINT `fk_habit_log_habit` FOREIGN KEY (`habit_id`) REFERENCES `habit` (`id`),
  CONSTRAINT `fk_habit_log_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `task` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `title` VARCHAR(200) NOT NULL,
  `date` DATE NOT NULL,
  `done` BIT NOT NULL DEFAULT 0,
  `completed_date` DATE,
  PRIMARY KEY (`id`),
  KEY `idx_task_user_id` (`user_id`),
  CONSTRAINT `fk_task_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `weekly_goal` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `title` VARCHAR(160) NOT NULL,
  `type` VARCHAR(12) NOT NULL,
  `week_start` DATE NOT NULL,
  `week_end` DATE NOT NULL,
  `target_value` INT NOT NULL,
  `current_value` INT NOT NULL DEFAULT 0,
  `completed` BIT NOT NULL DEFAULT 0,
  `completed_date` DATE,
  PRIMARY KEY (`id`),
  KEY `idx_weekly_goal_user_id` (`user_id`),
  CONSTRAINT `fk_weekly_goal_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB;
