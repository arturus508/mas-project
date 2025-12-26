package com.example.healthfitness.service;

import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final AuthUserService authUserService;

    public CurrentUserService(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    public Long id() {
        return authUserService.currentUser().getUserId();
    }
}
