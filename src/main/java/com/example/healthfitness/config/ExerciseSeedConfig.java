package com.example.healthfitness.config;

import com.example.healthfitness.model.Exercise;
import com.example.healthfitness.repository.ExerciseRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;


@Configuration
public class ExerciseSeedConfig {

    @Bean
    CommandLineRunner seedExercises(ExerciseRepository repo) {
        return args -> {
            if (repo.count() > 0) return;
            // chest
            repo.save(make("Bench Press",           "Chest",   "Medium"));
            repo.save(make("Incline Dumbbell Press","Chest",   "Medium"));
            // back
            repo.save(make("Pull Up",               "Back",    "Medium"));
            repo.save(make("Barbell Row",           "Back",    "Medium"));
            // legs
            repo.save(make("Squat",                 "Legs",    "Medium"));
            repo.save(make("Deadlift",              "Legs",    "Medium"));
            // calves
            repo.save(make("Calf Raise",            "Calves",  "Low"));
            // biceps
            repo.save(make("Bicep Curl",            "Biceps",  "Low"));
            repo.save(make("Preacher Curl",         "Biceps",  "Low"));
            // triceps
            repo.save(make("Triceps Extension",     "Triceps", "Low"));
            repo.save(make("Close Grip Bench Press","Triceps", "Medium"));
        };
    }

    private Exercise make(String name, String muscle, String intensity) {
        Exercise ex = new Exercise();
        ex.setExerciseName(name);
        ex.setMuscleGroup(muscle);
        ex.setIntensityLevel(intensity);
        return ex;
    }
}
