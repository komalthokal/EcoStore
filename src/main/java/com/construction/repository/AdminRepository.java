package com.construction.repository;

import com.construction.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

	public interface AdminRepository extends JpaRepository<Admin, Integer> {

	    // Find Admin by username
	    Optional<Admin> findByAdUsername(String adUsername);

	    // Check if username already exists
	    boolean existsByAdUsername(String adUsername);
	}


