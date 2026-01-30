package com.construction.service;

import com.construction.model.Admin;
import com.construction.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    // Save admin WITHOUT encoding password
    public Admin saveAdmin(Admin admin) {
        // Save password as is (plain text)
        return adminRepository.save(admin);
    }

    // Register admin with username check
    public Admin registerAdmin(Admin admin) throws Exception {
        if(adminRepository.existsByAdUsername(admin.getAdUsername())) {
            throw new Exception("Username already exists");
        }
        // Save password as is (plain text)
        return adminRepository.save(admin);
    }

    // Login method using plain text comparison
    public boolean loginAdmin(String adUsername, String rawPassword) {
        Admin admin = adminRepository.findByAdUsername(adUsername).orElse(null);
        return admin != null && rawPassword.equals(admin.getPassword());
    }
}
