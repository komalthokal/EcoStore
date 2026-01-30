package com.construction.repository;

import com.construction.model.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WorkerRepository extends JpaRepository<Worker, Long> {

    // Find Worker by username
    Optional<Worker> findByUsername(String username);
    
   
    
    // Check if username already exists
    boolean existsByUsername(String username);



	Optional<Worker> findByWorkerId(Long workerId);
}

