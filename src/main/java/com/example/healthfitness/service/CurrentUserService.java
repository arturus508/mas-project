package com.example.healthfitness.service;

import org.springframework.stereotype.Service;

/**
 * Simple service to provide the current user id.  In this phase of
 * development there is no authentication mechanism wired into the
 * controllers, so the application always operates on user id 1.  By
 * centralising this into a service we avoid scattering literals
 * throughout the codebase and make it trivial to plug in a real
 * authentication context later.  The single public method returns
 * the id of the currently authenticated user; initially this always
 * returns 1L.
 */
@Service
public class CurrentUserService {

    /**
     * Return the id of the current user.  At present this is a
     * constant value because the application has not yet integrated
     * with Spring Security or another identity provider.
     *
     * @return user id of the logged‑in user
     */
    public Long id() {
        return 1L;
    }
}