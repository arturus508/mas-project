INSERT INTO food_item (name, unit, kcal100, protein100, fat100, carbs100)
VALUES
  ('Chicken breast', 'G', 165, 31, 3, 0),
  ('Rice  cooked', 'G', 130, 2, 0, 28),
  ('Oats  dry', 'G', 389, 17, 7, 66),
  ('Egg', 'PIECE', 78, 6, 5, 0),
  ('Banana', 'PIECE', 105, 1, 0, 27),
  ('Olive oil', 'G', 884, 0, 100, 0),
  ('Whey scoop', 'PIECE', 120, 24, 2, 3),
  ('Greek yogurt', 'G', 59, 10, 0, 3),
  ('Apple', 'PIECE', 95, 0, 0, 25)
${food_insert_alias}
ON DUPLICATE KEY UPDATE
  name = ${food_name_upsert},
  unit = ${food_unit_upsert},
  kcal100 = ${food_kcal100_upsert},
  protein100 = ${food_protein100_upsert},
  fat100 = ${food_fat100_upsert},
  carbs100 = ${food_carbs100_upsert};
