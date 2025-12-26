CREATE TABLE IF NOT EXISTS `workout_plan_day` (
  `plan_day_id` BIGINT NOT NULL AUTO_INCREMENT,
  `workout_plan_id` BIGINT NOT NULL,
  `name` VARCHAR(64) NOT NULL DEFAULT 'Day 1',
  `day_order` INT NOT NULL,
  PRIMARY KEY (`plan_day_id`),
  KEY `idx_wpd_plan_id` (`workout_plan_id`),
  CONSTRAINT `fk_wpd_plan` FOREIGN KEY (`workout_plan_id`) REFERENCES `workout_plan` (`workout_plan_id`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `workout_plan_day_exercise` (
  `plan_day_exercise_id` BIGINT NOT NULL AUTO_INCREMENT,
  `plan_day_id` BIGINT NOT NULL,
  `exercise_id` BIGINT,
  `sets_planned` INT NOT NULL,
  `target_reps` INT,
  `rest_time` INT,
  PRIMARY KEY (`plan_day_exercise_id`),
  KEY `idx_wpde_plan_day_id` (`plan_day_id`),
  KEY `idx_wpde_exercise_id` (`exercise_id`),
  CONSTRAINT `fk_wpde_plan_day` FOREIGN KEY (`plan_day_id`) REFERENCES `workout_plan_day` (`plan_day_id`),
  CONSTRAINT `fk_wpde_exercise` FOREIGN KEY (`exercise_id`) REFERENCES `exercise` (`exercise_id`)
) ENGINE=InnoDB;

INSERT INTO `workout_plan_day` (`workout_plan_id`, `name`, `day_order`)
SELECT wp.`workout_plan_id`, 'Day 1', 1
FROM `workout_plan` wp;

INSERT INTO `workout_plan_day_exercise` (`plan_day_id`, `exercise_id`, `sets_planned`, `target_reps`, `rest_time`)
SELECT wpd.`plan_day_id`, wpe.`exercise_id`, wpe.`sets`, wpe.`reps`, wpe.`rest_time`
FROM `workout_plan_day` wpd
JOIN `workout_plan_exercise` wpe
  ON wpe.`workout_plan_id` = wpd.`workout_plan_id`;
