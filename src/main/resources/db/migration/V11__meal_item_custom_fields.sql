ALTER TABLE `meal_item`
  MODIFY `food_item_id` BIGINT NULL;

ALTER TABLE `meal_item`
  MODIFY `quantity` INT NOT NULL DEFAULT 1;

ALTER TABLE `meal_item`
  ADD COLUMN `name` VARCHAR(255) NULL;

ALTER TABLE `meal_item`
  ADD COLUMN `kcal` INT NULL;

ALTER TABLE `meal_item`
  ADD COLUMN `protein` INT NULL;

ALTER TABLE `meal_item`
  ADD COLUMN `fat` INT NULL;

ALTER TABLE `meal_item`
  ADD COLUMN `carbs` INT NULL;

ALTER TABLE `meal_item`
  ADD COLUMN `source_type` VARCHAR(16) NULL;
