package com.example.healthfitness.integration;

import com.example.healthfitness.model.DailyWorkout;
import com.example.healthfitness.model.DailyWorkoutSet;
import com.example.healthfitness.model.Exercise;
import com.example.healthfitness.model.User;
import com.example.healthfitness.model.WorkoutPlan;
import com.example.healthfitness.model.WorkoutPlanDay;
import com.example.healthfitness.model.WorkoutPlanDayExercise;
import com.example.healthfitness.repository.DailyWorkoutRepository;
import com.example.healthfitness.repository.DailyWorkoutSetRepository;
import com.example.healthfitness.repository.ExerciseRepository;
import com.example.healthfitness.repository.UserRepository;
import com.example.healthfitness.repository.WorkoutPlanDayExerciseRepository;
import com.example.healthfitness.repository.WorkoutPlanDayRepository;
import com.example.healthfitness.repository.WorkoutPlanRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class WorkoutFlowIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private WorkoutPlanRepository workoutPlanRepository;
    @Autowired private WorkoutPlanDayRepository workoutPlanDayRepository;
    @Autowired private WorkoutPlanDayExerciseRepository workoutPlanDayExerciseRepository;
    @Autowired private ExerciseRepository exerciseRepository;
    @Autowired private DailyWorkoutRepository dailyWorkoutRepository;
    @Autowired private DailyWorkoutSetRepository dailyWorkoutSetRepository;

    private User user;
    private Exercise exercise;
    private WorkoutPlanDay planDay;
    private LocalDate date;

    @BeforeEach
    void setUp() {
        user = new User("Test User", "test@example.com", "secret");
        userRepository.save(user);
        exercise = new Exercise();
        exercise.setExerciseName("Bench Press");
        exercise.setMuscleGroup("Chest");
        exercise.setIntensityLevel("Medium");
        exerciseRepository.save(exercise);

        WorkoutPlan plan = new WorkoutPlan();
        plan.setPlanName("Push");
        plan.setUser(user);
        workoutPlanRepository.save(plan);

        planDay = new WorkoutPlanDay();
        planDay.setWorkoutPlan(plan);
        planDay.setName("Day 1");
        planDay.setDayOrder(1);
        workoutPlanDayRepository.save(planDay);

        WorkoutPlanDayExercise pdx = new WorkoutPlanDayExercise();
        pdx.setPlanDay(planDay);
        pdx.setExercise(exercise);
        pdx.setSetsPlanned(2);
        workoutPlanDayExerciseRepository.save(pdx);

        date = LocalDate.of(2026, 1, 2);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        "N/A",
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void workoutLoggingFlow() throws Exception {
        mockMvc.perform(post("/today/{date}/workout/add", date)
                        .param("planDayId", planDay.getPlanDayId().toString())
                        .param("mode", "body")
                        .param("range", "today"))
                .andExpect(status().is3xxRedirection());

        DailyWorkout workout = dailyWorkoutRepository.findByUser_UserIdAndDate(user.getUserId(), date).orElse(null);
        assertThat(workout).isNotNull();
        List<DailyWorkoutSet> sets = dailyWorkoutSetRepository.findByDailyWorkout_DailyWorkoutId(workout.getDailyWorkoutId());
        assertThat(sets).hasSize(2);

        DailyWorkoutSet set = sets.get(0);
        mockMvc.perform(post("/workouts/sets/{setId}/reps", set.getDailyWorkoutSetId())
                        .param("date", date.toString())
                        .param("repsDone", "8"))
                .andExpect(status().is3xxRedirection());
        DailyWorkoutSet updated = dailyWorkoutSetRepository.findById(set.getDailyWorkoutSetId()).orElseThrow();
        assertThat(updated.getRepsDone()).isEqualTo(8);

        mockMvc.perform(post("/workouts/{workoutId}/sets/add", workout.getDailyWorkoutId())
                        .param("date", date.toString())
                        .param("exerciseId", exercise.getExerciseId().toString()))
                .andExpect(status().is3xxRedirection());
        assertThat(dailyWorkoutSetRepository.findByDailyWorkout_DailyWorkoutId(workout.getDailyWorkoutId()).size()).isEqualTo(3);

        DailyWorkoutSet extra = dailyWorkoutSetRepository.findByDailyWorkout_DailyWorkoutId(workout.getDailyWorkoutId()).get(2);
        mockMvc.perform(post("/workouts/sets/{setId}/delete", extra.getDailyWorkoutSetId())
                        .param("date", date.toString()))
                .andExpect(status().is3xxRedirection());
        assertThat(dailyWorkoutSetRepository.findByDailyWorkout_DailyWorkoutId(workout.getDailyWorkoutId()).size()).isEqualTo(2);
    }
}
