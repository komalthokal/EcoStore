package com.construction.controller;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.construction.model.Salary;
import com.construction.model.Worker;
import com.construction.repository.SalaryRepository;
import com.construction.repository.WorkerRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class SalaryController {

    @Autowired
    private SalaryRepository salaryRepo;

    @Autowired
    private WorkerRepository workerRepo;
    

    @GetMapping("/worker/salary")
    public String workerSalaryPage(
            @RequestParam(name = "month", required = false) Integer month,
            @RequestParam(name = "year", required = false) Integer year,
            Model model,
            HttpSession session) {

        // Use current month/year if not provided
        YearMonth current = YearMonth.now();
        int selectedMonth = (month != null) ? month : current.getMonthValue();
        int selectedYear = (year != null) ? year : current.getYear();
        YearMonth selectedMonthYear = YearMonth.of(selectedYear, selectedMonth);

        // Get logged-in worker from session
        Worker worker = (Worker) session.getAttribute("USER");
        if (worker == null) return "redirect:/login";
        Long workerId = worker.getWorkerId();

        // Fetch salaries for the worker for the selected month
        List<Salary> monthlySalary = salaryRepo.findByWorkerId(workerId)
                .stream()
                .filter(s -> s.getSalaryMonth().equals(selectedMonthYear))
                .collect(Collectors.toList()); // for Java 8+

        // Calculate summary
        double baseSalary = monthlySalary.stream().mapToDouble(Salary::getBaseSalary).sum();
        double overtimePay = monthlySalary.stream().mapToDouble(Salary::getOvertimePay).sum();
        double overtimeHours = monthlySalary.stream().mapToDouble(Salary::getOvertimeHours).sum();
        double overtimeRate = (overtimeHours != 0) ? overtimePay / overtimeHours : 0;
        double advance = monthlySalary.stream()
                .map(Salary::getAdvance)
                .filter(Objects::nonNull)
                .reduce(0.0, Double::sum);
        double totalSalary = baseSalary + overtimePay - advance;

        // Add variables to model
        model.addAttribute("monthlySalary", monthlySalary);
        model.addAttribute("baseSalary", baseSalary);
        model.addAttribute("overtimePay", overtimePay);
        model.addAttribute("overtimeHours", overtimeHours);
        model.addAttribute("overtimeRate", overtimeRate);
        model.addAttribute("advance", advance);
        model.addAttribute("totalSalary", totalSalary);
        model.addAttribute("month", selectedMonth);
        model.addAttribute("year", selectedYear);

        return "salary"; // Match Thymeleaf template file name
    }


    @GetMapping("/admin/salary")
    public String adminSalaryPage(Model model) {

        // 1️⃣ All workers
        List<Worker> workers = workerRepo.findAll();

        // 2️⃣ All salaries
        List<Salary> allSalaries = salaryRepo.findAll();

        // 3️⃣ Map of workerId -> workerName for table display
        Map<Long, String> workerNameById = workers.stream()
                .collect(Collectors.toMap(Worker::getWorkerId, Worker::getWorkerName));

        // Pass to Thymeleaf
        model.addAttribute("workers", workers);
        model.addAttribute("allSalaries", allSalaries);
        model.addAttribute("workerNameById", workerNameById);

        return "salary"; // Thymeleaf template
    }
    


}
