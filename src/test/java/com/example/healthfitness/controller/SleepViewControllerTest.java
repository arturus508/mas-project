package com.example.healthfitness.controller;

import com.example.healthfitness.model.SleepEntry;
import com.example.healthfitness.service.CurrentUserService;
import com.example.healthfitness.service.SleepService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(SleepViewController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class SleepViewControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private SleepService sleepService;
    @MockBean private CurrentUserService currentUserService;

    @Test
    void getSleepRendersPage() throws Exception {
        when(currentUserService.id()).thenReturn(1L);
        when(sleepService.listForMonth(eq(1L), any(YearMonth.class))).thenReturn(List.of());

        mockMvc.perform(get("/mind/sleep"))
                .andExpect(status().isOk())
                .andExpect(view().name("sleep"))
                .andExpect(model().attributeExists("entries"))
                .andExpect(model().attributeExists("sleepForm"));
    }

    @Test
    void postSleepWithValidationErrorsRendersForm() throws Exception {
        when(currentUserService.id()).thenReturn(1L);
        when(sleepService.listForMonth(eq(1L), any(YearMonth.class))).thenReturn(List.of());

        mockMvc.perform(post("/mind/sleep/add")
                        .param("hours", "0")
                        .param("minutes", "0")
                        .param("quality", "3"))
                .andExpect(status().isOk())
                .andExpect(view().name("sleep"))
                .andExpect(model().attributeHasFieldErrors("sleepForm", "date", "durationValid"));
    }

    @Test
    void postSleepValidRedirectsAndCreatesEntry() throws Exception {
        when(currentUserService.id()).thenReturn(1L);

        mockMvc.perform(post("/mind/sleep/add")
                        .param("date", LocalDate.now().toString())
                        .param("hours", "7")
                        .param("minutes", "30")
                        .param("quality", "4")
                        .param("note", "ok"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/mind/sleep"));

        verify(sleepService).create(eq(1L), ArgumentMatchers.any());
        verifyNoMoreInteractions(sleepService);
    }
}
