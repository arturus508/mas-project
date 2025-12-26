package com.example.healthfitness.controller;

import com.example.healthfitness.service.CurrentUserService;
import com.example.healthfitness.service.DreamService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(DreamViewController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class DreamViewControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private DreamService dreamService;
    @MockBean private CurrentUserService currentUserService;

    @Test
    void getDreamsRendersPage() throws Exception {
        when(currentUserService.id()).thenReturn(1L);
        when(dreamService.search(eq(1L), isNull(), isNull(), isNull())).thenReturn(List.of());

        mockMvc.perform(get("/mind/dreams"))
                .andExpect(status().isOk())
                .andExpect(view().name("dreams"))
                .andExpect(model().attributeExists("dreams"))
                .andExpect(model().attributeExists("dreamForm"));
    }

    @Test
    void postDreamWithValidationErrorsRendersForm() throws Exception {
        when(currentUserService.id()).thenReturn(1L);
        when(dreamService.listAll(1L)).thenReturn(List.of());

        mockMvc.perform(post("/mind/dreams/add")
                        .param("date", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("dreams"))
                .andExpect(model().attributeHasFieldErrors("dreamForm", "title", "content"));
    }

    @Test
    void postDreamValidRedirectsAndCreatesEntry() throws Exception {
        when(currentUserService.id()).thenReturn(1L);

        mockMvc.perform(post("/mind/dreams/add")
                        .param("date", LocalDate.now().toString())
                        .param("title", "Title")
                        .param("content", "Content"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/mind/dreams"));

        verify(dreamService).create(eq(1L), any());
    }
}
