INSERT INTO exercise (exercise_name, muscle_group, intensity_level, reps, rest_time, sets)
VALUES
  ('Bench Press', 'Chest', 'Medium', 0, 0, 0),
  ('Incline Dumbbell Press', 'Chest', 'Medium', 0, 0, 0),
  ('Pull Up', 'Back', 'Medium', 0, 0, 0),
  ('Barbell Row', 'Back', 'Medium', 0, 0, 0),
  ('Squat', 'Legs', 'Medium', 0, 0, 0),
  ('Deadlift', 'Legs', 'Medium', 0, 0, 0),
  ('Calf Raise', 'Calves', 'Low', 0, 0, 0),
  ('Bicep Curl', 'Biceps', 'Low', 0, 0, 0),
  ('Preacher Curl', 'Biceps', 'Low', 0, 0, 0),
  ('Triceps Extension', 'Triceps', 'Low', 0, 0, 0),
  ('Close Grip Bench Press', 'Triceps', 'Medium', 0, 0, 0)
${exercise_insert_alias}
ON DUPLICATE KEY UPDATE
  exercise_name = ${exercise_name_upsert},
  muscle_group = ${exercise_muscle_group_upsert},
  intensity_level = ${exercise_intensity_level_upsert};
