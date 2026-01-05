package com.example.healthfitness.integration;

import com.example.healthfitness.model.Habit;
import com.example.healthfitness.model.User;
import com.example.healthfitness.repository.DailyReviewRepository;
import com.example.healthfitness.repository.HabitLogRepository;
import com.example.healthfitness.repository.HabitRepository;
import com.example.healthfitness.repository.TaskRepository;
import com.example.healthfitness.repository.UserRepository;
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
class TodayFlowIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private TaskRepository taskRepository;
    @Autowired private HabitRepository habitRepository;
    @Autowired private HabitLogRepository habitLogRepository;
    @Autowired private DailyReviewRepository dailyReviewRepository;

    private User user;
    private Habit habit;
    private LocalDate date;

    @BeforeEach
    void setUp() {
        user = new User("Test User", "test@example.com", "secret");
        userRepository.save(user);
        habit = new Habit();
        habit.setUser(user);
        habit.setName("Drink water");
        habitRepository.save(habit);
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
    void quickActionsPersist() throws Exception {
        mockMvc.perform(post("/today/{date}/tasks/add", date)
                        .param("title", "Plan day")
                        .param("mode", "mind")
                        .param("range", "today"))
                .andExpect(status().is3xxRedirection());
        assertThat(taskRepository.findByUserAndDateOrderByIdAsc(user, date)).hasSize(1);

        mockMvc.perform(post("/today/{date}/habits/{id}/toggle", date, habit.getId())
                        .param("mode", "mind")
                        .param("range", "today"))
                .andExpect(status().is3xxRedirection());
        assertThat(habitLogRepository.findByHabitAndDate(habit, date)).isPresent();

        mockMvc.perform(post("/today/{date}/review", date)
                        .param("mode", "mind")
                        .param("range", "today")
                        .param("mood", "3")
                        .param("energy", "4")
                        .param("note", "Solid day")
                        .param("resetDone", "true"))
                .andExpect(status().is3xxRedirection());
        assertThat(dailyReviewRepository.findByUser_UserIdAndDate(user.getUserId(), date)).isPresent();
    }
}
