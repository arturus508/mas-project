INSERT INTO food_item (name, unit, kcal100, protein100, fat100, carbs100)
VALUES
  ('Salmon', 'G', 208, 20, 13, 0),
  ('Tuna canned', 'G', 132, 29, 1, 0),
  ('Turkey breast', 'G', 135, 30, 1, 0),
  ('Beef mince 10%', 'G', 217, 26, 11, 0),
  ('Potato', 'G', 77, 2, 0, 17),
  ('Sweet potato', 'G', 86, 2, 0, 20),
  ('Pasta cooked', 'G', 131, 5, 1, 25),
  ('Quinoa cooked', 'G', 120, 4, 2, 21),
  ('Broccoli', 'G', 34, 3, 0, 7),
  ('Spinach', 'G', 23, 3, 0, 4),
  ('Tomato', 'G', 18, 1, 0, 4),
  ('Cucumber', 'G', 15, 1, 0, 4),
  ('Avocado', 'G', 160, 2, 15, 9),
  ('Almonds', 'G', 579, 21, 50, 22),
  ('Peanut butter', 'G', 588, 25, 50, 20),
  ('Cottage cheese', 'G', 98, 11, 4, 3),
  ('Milk 2%', 'G', 50, 3, 2, 5),
  ('Cheese cheddar', 'G', 402, 25, 33, 1),
  ('Bread slice', 'PIECE', 79, 3, 1, 15),
  ('Orange', 'PIECE', 62, 1, 0, 15)
${food_insert_alias}
ON DUPLICATE KEY UPDATE
  name = ${food_name_upsert},
  unit = ${food_unit_upsert},
  kcal100 = ${food_kcal100_upsert},
  protein100 = ${food_protein100_upsert},
  fat100 = ${food_fat100_upsert},
  carbs100 = ${food_carbs100_upsert};
