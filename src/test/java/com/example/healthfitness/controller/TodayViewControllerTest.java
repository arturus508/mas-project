package com.example.healthfitness.controller;

import com.example.healthfitness.model.Habit;
import com.example.healthfitness.model.User;
import com.example.healthfitness.service.CurrentUserService;
import com.example.healthfitness.service.DailyReviewService;
import com.example.healthfitness.service.HabitLogService;
import com.example.healthfitness.service.HabitService;
import com.example.healthfitness.service.TaskService;
import com.example.healthfitness.service.TodayViewService;
import com.example.healthfitness.service.UserService;
import com.example.healthfitness.web.view.TodayViewModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(TodayViewController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class TodayViewControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private TodayViewService todayViewService;
    @MockBean private CurrentUserService currentUserService;
    @MockBean private TaskService taskService;
    @MockBean private HabitService habitService;
    @MockBean private HabitLogService habitLogService;
    @MockBean private UserService userService;
    @MockBean private DailyReviewService dailyReviewService;

    @Test
    void todayRendersView() throws Exception {
        when(currentUserService.id()).thenReturn(1L);
        TodayViewModel vm = new TodayViewModel();
        vm.setDate(LocalDate.now());
        when(todayViewService.build(eq(1L), any(LocalDate.class))).thenReturn(vm);

        mockMvc.perform(get("/today"))
                .andExpect(status().isOk())
                .andExpect(view().name("today"))
                .andExpect(model().attributeExists("vm"));
    }

    @Test
    void dateRendersView() throws Exception {
        LocalDate date = LocalDate.of(2025, 1, 5);
        when(currentUserService.id()).thenReturn(1L);
        TodayViewModel vm = new TodayViewModel();
        vm.setDate(date);
        when(todayViewService.build(1L, date)).thenReturn(vm);

        mockMvc.perform(get("/today/" + date))
                .andExpect(status().isOk())
                .andExpect(view().name("today"))
                .andExpect(model().attributeExists("vm"));
    }

    @Test
    void toggleTaskRedirects() throws Exception {
        LocalDate date = LocalDate.of(2025, 1, 5);
        when(currentUserService.id()).thenReturn(1L);

        mockMvc.perform(post("/today/" + date + "/tasks/10/toggle"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/today/" + date));

        verify(taskService).toggle(1L, 10L);
    }

    @Test
    void toggleHabitRedirects() throws Exception {
        LocalDate date = LocalDate.of(2025, 1, 5);
        User user = new User();
        Habit habit = new Habit();
        habit.setId(7L);

        when(currentUserService.id()).thenReturn(1L);
        when(userService.getUserById(1L)).thenReturn(user);
        when(habitService.getForUserOrThrow(1L, 7L)).thenReturn(habit);

        mockMvc.perform(post("/today/" + date + "/habits/7/toggle"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/today/" + date));

        verify(habitLogService).toggle(user, habit, date);
    }
}
