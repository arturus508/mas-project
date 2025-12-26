package com.example.healthfitness.web;

import com.example.healthfitness.service.CurrentUserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDate;

/**
 * Adds common model attributes that are required on many pages.  By
 * defining them here we avoid repeating the same boilerplate in each
 * controller.  Spring will invoke these methods and add the returned
 * values to the Model of every controller method automatically.
 */
@ControllerAdvice
public class GlobalModelAdvice {

    private final CurrentUserService currentUserService;

    public GlobalModelAdvice(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    /**
     * Adds the current date to the model under the attribute name
     * {@code today}.  Thymeleaf templates can reference this to
     * display the current date without having to compute it in each
     * controller.
     *
     * @return the current date
     */
    @ModelAttribute("today")
    public LocalDate today() {
        return LocalDate.now();
    }

    /**
     * Adds the current user id to the model under the attribute name
     * {@code currentUserId}.  Controllers and views can use this to
     * associate new entities with the logged‑in user.
     *
     * @return the id of the current user
     */
    @ModelAttribute("currentUserId")
    public Long currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return currentUserService.id();
    }
}
