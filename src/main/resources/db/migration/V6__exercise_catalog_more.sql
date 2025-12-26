INSERT INTO exercise (exercise_name, muscle_group, intensity_level, reps, rest_time, sets)
VALUES
  ('Overhead Press', 'Shoulders', 'Medium', 0, 0, 0),
  ('Lateral Raise', 'Shoulders', 'Low', 0, 0, 0),
  ('Front Raise', 'Shoulders', 'Low', 0, 0, 0),
  ('Face Pull', 'Shoulders', 'Low', 0, 0, 0),
  ('Lat Pulldown', 'Back', 'Medium', 0, 0, 0),
  ('Seated Cable Row', 'Back', 'Medium', 0, 0, 0),
  ('Chest Fly', 'Chest', 'Low', 0, 0, 0),
  ('Push Up', 'Chest', 'Low', 0, 0, 0),
  ('Leg Press', 'Legs', 'Medium', 0, 0, 0),
  ('Romanian Deadlift', 'Legs', 'Medium', 0, 0, 0),
  ('Leg Curl', 'Legs', 'Low', 0, 0, 0),
  ('Leg Extension', 'Legs', 'Low', 0, 0, 0),
  ('Glute Bridge', 'Glutes', 'Low', 0, 0, 0),
  ('Hip Thrust', 'Glutes', 'Medium', 0, 0, 0),
  ('Plank', 'Core', 'Low', 0, 0, 0),
  ('Crunch', 'Core', 'Low', 0, 0, 0),
  ('Hanging Knee Raise', 'Core', 'Medium', 0, 0, 0),
  ('Burpee', 'Full Body', 'High', 0, 0, 0),
  ('Mountain Climber', 'Full Body', 'High', 0, 0, 0),
  ('Jump Rope', 'Cardio', 'High', 0, 0, 0)
${exercise_insert_alias}
ON DUPLICATE KEY UPDATE
  exercise_name = ${exercise_name_upsert},
  muscle_group = ${exercise_muscle_group_upsert},
  intensity_level = ${exercise_intensity_level_upsert};
