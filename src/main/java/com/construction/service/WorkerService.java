package com.construction.service;

import com.construction.model.Worker;
import com.construction.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkerService {

    @Autowired
    private WorkerRepository workerRepository;

    // Save worker without encoding password
    public Worker saveWorker(Worker worker) {
        // Save password as plain text
        return workerRepository.save(worker);
    }

    // Register worker without encoding password
    public Worker registerWorker(Worker worker) throws Exception {
        if(workerRepository.existsByUsername(worker.getUsername())) {
            throw new Exception("Username already exists");
        }
        // Save password as plain text
        return workerRepository.save(worker);
    }

    // Login method: simple plain text password check
    public boolean loginWorker(String workerUsername, String rawPassword) {
        Worker worker = workerRepository.findByUsername(workerUsername).orElse(null);
        return worker != null && rawPassword.equals(worker.getPassword());
    }
}
