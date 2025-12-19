package com.example.healthfitness.config;

import com.example.healthfitness.model.Exercise;
import com.example.healthfitness.repository.ExerciseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration to seed the database with a standard set of exercises
 * on application startup.  Rather than skipping seeding if any
 * exercises already exist, this implementation checks each
 * predefined exercise individually and inserts it only if it does
 * not already exist.  This ensures that all required exercises are
 * available even if a user manually created one or more entries.
 */
@Configuration
public class ExerciseSeedConfig {

    @Bean
    CommandLineRunner seedExercises(ExerciseRepository repo) {
        return args -> {
            seed(repo, "Bench Press",            "Chest",   "Medium");
            seed(repo, "Incline Dumbbell Press", "Chest",   "Medium");
            seed(repo, "Pull Up",                "Back",    "Medium");
            seed(repo, "Barbell Row",            "Back",    "Medium");
            seed(repo, "Squat",                  "Legs",    "Medium");
            seed(repo, "Deadlift",               "Legs",    "Medium");
            seed(repo, "Calf Raise",             "Calves",  "Low");
            seed(repo, "Bicep Curl",             "Biceps",  "Low");
            seed(repo, "Preacher Curl",          "Biceps",  "Low");
            seed(repo, "Triceps Extension",      "Triceps", "Low");
            seed(repo, "Close Grip Bench Press", "Triceps", "Medium");
        };
    }

    /**
     * Helper that inserts a new exercise if one with the same name does
     * not already exist.  The default reps, sets and rest time values
     * defined on the {@link Exercise} entity will be used.
     */
    private void seed(ExerciseRepository repo, String name, String muscle, String intensity) {
        if (!repo.existsByExerciseName(name)) {
            Exercise ex = new Exercise();
            ex.setExerciseName(name);
            ex.setMuscleGroup(muscle);
            ex.setIntensityLevel(intensity);
            repo.save(ex);
        }
    }
}