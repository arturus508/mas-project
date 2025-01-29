package com.example.healthfitness.service;

import com.example.healthfitness.model.Admin;
import com.example.healthfitness.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;


    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }


    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }
}

