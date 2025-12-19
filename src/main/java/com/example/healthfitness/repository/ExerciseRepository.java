package com.example.healthfitness.repository;

import com.example.healthfitness.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Exercise} entities.  In addition to the standard
 * CRUD operations provided by {@link JpaRepository}, a convenience
 * method is declared to determine whether an exercise with a given
 * name already exists.  This is used during data seeding to avoid
 * inserting duplicate exercises.
 */
@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    /**
     * Returns {@code true} if an exercise with the given name exists.
     *
     * @param exerciseName the name of the exercise to check
     * @return {@code true} if an exercise with that name exists, otherwise {@code false}
     */
    boolean existsByExerciseName(String exerciseName);
}