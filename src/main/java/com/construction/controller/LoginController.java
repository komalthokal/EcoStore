package com.construction.controller;

import com.construction.model.Admin;
import com.construction.model.Attendance;
import com.construction.model.Trip;
import com.construction.model.Worker;
import com.construction.repository.AdminRepository;
import com.construction.repository.AttendanceRepository;
import com.construction.repository.SalaryRepository;
import com.construction.repository.TripRepository;
import com.construction.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private WorkerRepository workerRepo;
    
    @Autowired
    private TripRepository tripRepo;
    
    @Autowired
    private AttendanceRepository attendanceRepo;
    
    @Autowired
    private SalaryRepository salaryRepo;

    // ===== SHOW LOGIN PAGE =====
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // return login.html
    }

    // ===== HANDLE LOGIN =====
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        @RequestParam String role,   // added role selection
                        HttpSession session) {

        if ("ADMIN".equalsIgnoreCase(role)) {
            Optional<Admin> adminOpt = adminRepo.findByAdUsername(username);
            if (adminOpt.isPresent()) {
                Admin admin = adminOpt.get();
                if (password.equals(admin.getPassword())) { // plain text compare
                    session.setAttribute("USER", admin);
                    session.setAttribute("ROLE", "ADMIN");
                    return "redirect:/admin/admin-dashboard";
                }
            }
        } else if ("WORKER".equalsIgnoreCase(role)) {
            Optional<Worker> workerOpt = workerRepo.findByUsername(username);
            if (workerOpt.isPresent()) {
                Worker worker = workerOpt.get();
                if (password.equals(worker.getPassword())) { // plain text compare
                    session.setAttribute("USER", worker);
                    session.setAttribute("ROLE", "WORKER");
                    return "redirect:/worker/dashboard";
                }
            }
        }

        // Login failed
        session.setAttribute("LOGIN_ERROR", "Invalid username, password, or role");
        return "redirect:/login?error=true";
    }

    // ===== SHOW REGISTRATION PAGE =====
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register"; // return register.html
    }

 // ===== HANDLE REGISTRATION =====
    @PostMapping("/register")
    public String register(@RequestParam String name,
                           @RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String contact,
                           @RequestParam String address,
                           @RequestParam String password,
                           @RequestParam String role,
                           @RequestParam(required = false) Double baseSalary) {

        if ("ADMIN".equalsIgnoreCase(role)) {

            Admin admin = new Admin();
            admin.setAdName(name);
            admin.setAdUsername(username);
            admin.setAdEmail(email);
            admin.setContact(contact);
            admin.setAddress(address);
            admin.setPassword(password);   // ⚠️ later encrypt
            admin.setRole(Role.ADMIN);

            adminRepo.save(admin);

        } else if ("WORKER".equalsIgnoreCase(role)) {

            Worker worker = new Worker();
            worker.setWorkerName(name);
            worker.setUsername(username);
            worker.setWorkerEmail(email);
            worker.setContact(contact);
            worker.setAddress(address);
            worker.setPassword(password);  // ⚠️ later encrypt
            worker.setRole(Role.WORKER);

            // ✅ IMPORTANT: base_salary must never be null
            worker.setBaseSalary(baseSalary != null ? baseSalary : 0.0);

            workerRepo.save(worker);
        }

        return "redirect:/login";
    }


    // ===== ADMIN DASHBOARD =====
    @GetMapping("/admin/admin-dashboard")
    public String adminDashboard(HttpSession session,Model model) {
        // Optional: check if admin is logged in
        String role = (String) session.getAttribute("ROLE");
        if (!"ADMIN".equals(role)) {
            return "redirect:/login";
        }
        long totalWorkers = workerRepo.count();

        LocalDate today = LocalDate.now();
        long totalTripsToday = tripRepo.findByTripDate(today).size();

        long workersPresentToday =
                attendanceRepo.findByAttendanceDateAndStatus(today, "Present").size();

        long totalSalariesPending =
                salaryRepo.countByStatus("Unpaid");

        model.addAttribute("totalWorkers", totalWorkers);
        model.addAttribute("totalTripsToday", totalTripsToday);
        model.addAttribute("workersPresentToday", workersPresentToday);
        model.addAttribute("totalSalariesPending", totalSalariesPending);

       
        return "admin-dashboard"; // matches admin-dashboard.html
    }


    @GetMapping("/worker/dashboard")
    public String workerDashboard(HttpSession session, Model model) {
        Worker worker = (Worker) session.getAttribute("USER"); // get logged-in worker
        if (worker == null) {
            return "redirect:/login";
        }

        Long workerId = worker.getWorkerId();

        // Fetch all trips for this worker
        List<Trip> trips = tripRepo.findByWorkerId(workerId);

        // Recent trips (last 5)
        List<Trip> recentTrips = trips.stream()
                .sorted((t1, t2) -> t2.getTripDate().compareTo(t1.getTripDate())) // newest first
                .limit(5)
                .toList();

        // Calculate totals
        double totalDistance = trips.stream()
                .mapToDouble(t -> t.getDistanceKm() != null ? t.getDistanceKm() : 0)
                .sum();
        double totalIncome = trips.stream()
                .mapToDouble(t -> t.getTripIncome() != null ? t.getTripIncome() : 0)
                .sum();
        int totalTrips = trips.size();

        // ===== Fetch today's attendance =====
        Attendance todayAttendance = attendanceRepo.findByWorkerIdAndAttendanceDate(
                workerId, java.time.LocalDate.now()
        );
        String attendanceStatus = (todayAttendance != null) ? todayAttendance.getStatus() : "Absent";

        // Add attributes to model
        model.addAttribute("workerName", worker.getWorkerName());
        model.addAttribute("recentTrips", recentTrips);
        model.addAttribute("totalTrips", totalTrips);
        model.addAttribute("totalDistance", totalDistance);
        model.addAttribute("totalIncome", totalIncome);
        model.addAttribute("attendanceStatus", attendanceStatus);

        return "dashboard"; // dashboard.html
    }



    // ===== LOGOUT =====
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
