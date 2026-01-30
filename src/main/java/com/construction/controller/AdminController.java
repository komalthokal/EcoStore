package com.construction.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.construction.model.Salary;
import com.construction.model.Worker;
import com.construction.repository.AttendanceRepository;
import com.construction.repository.SalaryRepository;
import com.construction.repository.TripRepository;
import com.construction.repository.WorkerRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private WorkerRepository workerRepo;

    @Autowired
    private TripRepository tripRepo;

    @Autowired
    private AttendanceRepository attendanceRepo;

    @Autowired
    private SalaryRepository salaryRepo;

    // ===== DASHBOARD =====
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        long totalWorkers = workerRepo.count();
        LocalDate today = LocalDate.now();
        long totalTripsToday = tripRepo.findByTripDate(today).size();
        long workersPresentToday = attendanceRepo.findByAttendanceDateAndStatus(today, "Present").size();
        long totalSalariesPending = salaryRepo.countByStatus("Unpaid");

        model.addAttribute("totalWorkers", totalWorkers);
        model.addAttribute("totalTripsToday", totalTripsToday);
        model.addAttribute("workersPresentToday", workersPresentToday);
        model.addAttribute("totalSalariesPending", totalSalariesPending);

        return "admin-dashboard";
    }
    
    @GetMapping("/workers")
    public String workersList(Model model) {
        model.addAttribute("workers", workerRepo.findAll());
        return "admin-workers"; // your Thymeleaf template
    }
    
 // ===== INDIVIDUAL WORKER DETAILS =====
    @GetMapping("/worker/{id}")
    public String workerDetails(@PathVariable Long id, Model model) {

        Worker worker = workerRepo.findById(id).orElse(null);
        if (worker == null) {
            return "redirect:/admin/workers"; // worker not found
        }

        // Fetch related data
        model.addAttribute("worker", worker);
        model.addAttribute("trips", tripRepo.findByWorkerId(id));
        model.addAttribute("attendanceList", attendanceRepo.findByWorkerId(id));
        model.addAttribute("salaryList", salaryRepo.findByWorkerId(id));
        
        

        return "admin-worker-details"; // Thymeleaf template
    }



    // ===== VIEW SALARIES =====
    @GetMapping("/salaries")
    public String viewSalaries(Model model) {
        List<Salary> allSalaries = salaryRepo.findAll();
        List<Worker> workers = workerRepo.findAll();

        // Map for displaying worker names in table
        Map<Long, String> workerNameById = workers.stream()
                .collect(Collectors.toMap(Worker::getWorkerId, Worker::getWorkerName));

        model.addAttribute("allSalaries", allSalaries);
        model.addAttribute("workers", workers);
        model.addAttribute("workerNameById", workerNameById);

        // Default month for new entries
        model.addAttribute("currentMonth", YearMonth.now().toString());

        return "admin-salaries";
    }

    // ===== PAY SALARY =====
    @PostMapping("/salary/pay/{id}")
    public String paySalary(@PathVariable Long id) {
        salaryRepo.findById(id).ifPresent(salary -> {
            salary.setStatus("Paid");
            salaryRepo.save(salary);
        });
        return "redirect:/admin/salaries";
    }

    // ===== CREATE / UPDATE SALARY =====
    @PostMapping("/salary/create")
    public String createOrUpdateSalary(
            @RequestParam Long workerId,
            @RequestParam(required = false) Double overtimeHours,
            @RequestParam(required = false) Double advance,
            @RequestParam String salaryMonth
    ) {
        YearMonth month = YearMonth.parse(salaryMonth);

        // Default base salary and OT rate
        double baseSalary = 21000.0;
        double otRate = 100.0;

        // Fetch existing salary for this worker & month
        Salary salary = salaryRepo.findByWorkerIdAndSalaryMonth(workerId, month);
        if (salary == null) {
            salary = new Salary();
            salary.setWorkerId(workerId);
            salary.setSalaryMonth(month);
            salary.setStatus("Unpaid");
            salary.setOvertimeHours(0.0);
            salary.setOvertimePay(0.0);
            salary.setAdvance(0.0);
            salary.setBaseSalary(baseSalary);
            salary.setOvertimeRate(otRate);
        }

        // ===== CUMULATIVE UPDATES =====
        if (overtimeHours != null) {
            // Add new OT hours to existing
            double newOtHours = salary.getOvertimeHours() + overtimeHours;
            salary.setOvertimeHours(newOtHours);

            // Recalculate OT pay
            salary.setOvertimePay(newOtHours * otRate);
        }

        if (advance != null) {
            // Add new Advance to existing
            double newAdvance = salary.getAdvance() + advance;
            salary.setAdvance(newAdvance);
        }

        // Recalculate total = base + OT pay - advance
        double totalAmount = salary.getBaseSalary() + salary.getOvertimePay() - salary.getAdvance();
        salary.setAmount(totalAmount);

        salaryRepo.save(salary);

        return "redirect:/admin/salaries";
    }

    @GetMapping("/salary/fetch")
    @ResponseBody
    public Salary fetchSalary(
            @RequestParam Long workerId,
            @RequestParam String salaryMonth
    ) {
        YearMonth month = YearMonth.parse(salaryMonth);
        Salary salary = salaryRepo.findByWorkerIdAndSalaryMonth(workerId, month);

        if (salary == null) {
            salary = new Salary();
            salary.setOvertimeHours(0.0);
            salary.setOvertimeRate(100.0);
            salary.setOvertimePay(0.0);
            salary.setAdvance(0.0);
        }

        return salary;
    }


    // ===== GENERATE MONTHLY SALARY =====
    @PostMapping("/salary/generate-monthly")
    public String generateMonthlySalary() {
        YearMonth currentMonth = YearMonth.now();
        List<Worker> workers = workerRepo.findAll();

        for (Worker worker : workers) {
            if (!salaryRepo.existsByWorkerIdAndSalaryMonth(worker.getWorkerId(), currentMonth)) {
                Salary salary = new Salary();
                salary.setWorkerId(worker.getWorkerId());
                salary.setSalaryMonth(currentMonth);
                salary.setBaseSalary(21000.0);
                salary.setOvertimeHours(0.0);
                salary.setOvertimeRate(100.0);
                salary.setOvertimePay(0.0);
                salary.setAdvance(0.0);
                salary.setAmount(21000.0);
                salary.setStatus("Unpaid");
                salaryRepo.save(salary);
            }
        }
        return "redirect:/admin/salaries";
    }
}
