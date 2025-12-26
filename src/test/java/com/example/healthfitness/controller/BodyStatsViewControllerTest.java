package com.example.healthfitness.controller;

import com.example.healthfitness.service.BodyStatsService;
import com.example.healthfitness.service.CurrentUserService;
import com.example.healthfitness.service.UserService;
import com.example.healthfitness.web.form.BodyStatsForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BodyStatsViewController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class BodyStatsViewControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private BodyStatsService bodyStatsService;
    @MockBean private CurrentUserService currentUserService;
    @MockBean private UserService userService;

    @Test
    void getBodyStatsRendersPage() throws Exception {
        when(currentUserService.id()).thenReturn(1L);
        when(bodyStatsService.getBodyStatsByUser(1L)).thenReturn(List.of());

        mockMvc.perform(get("/body-stats"))
                .andExpect(status().isOk())
                .andExpect(view().name("body-stats"))
                .andExpect(model().attributeExists("bodyStats"))
                .andExpect(model().attributeExists("bodyStatsForm"));
    }

    @Test
    void postBodyStatsWithValidationErrorsRendersForm() throws Exception {
        when(currentUserService.id()).thenReturn(1L);
        when(bodyStatsService.getBodyStatsByUser(1L)).thenReturn(List.of());

        mockMvc.perform(post("/body-stats/add")
                        .param("dateRecorded", "")
                        .param("weight", "-1")
                        .param("bodyFatPercent", "-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("body-stats"))
                .andExpect(model().attributeHasFieldErrors("bodyStatsForm", "dateRecorded", "weight", "bodyFatPercent"));
    }

    @Test
    void postBodyStatsValidRedirectsAndCreatesEntry() throws Exception {
        when(currentUserService.id()).thenReturn(1L);

        mockMvc.perform(post("/body-stats/add")
                        .param("dateRecorded", LocalDate.now().toString())
                        .param("weight", "82.5")
                        .param("bodyFatPercent", "15.2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/body-stats"));

        verify(bodyStatsService).addBodyStatsToUser(eq(1L), any(BodyStatsForm.class));
    }

    @Test
    void deleteBodyStatsRedirects() throws Exception {
        when(currentUserService.id()).thenReturn(1L);

        mockMvc.perform(post("/body-stats/delete/10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/body-stats"));

        verify(bodyStatsService).deleteBodyStats(1L, 10L);
    }
}
