package com.construction.controller;

import com.construction.model.Attendance;
import com.construction.model.Worker;
import com.construction.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/worker/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    // Show attendance page
    @GetMapping
    public String showAttendancePage(HttpSession session, Model model,
                                     @RequestParam(value="year", required=false) Integer year,
                                     @RequestParam(value="month", required=false) Integer month) {
        Worker worker = (Worker) session.getAttribute("USER");
        if (worker == null) return "redirect:/login";

        LocalDate now = LocalDate.now();
        int displayYear = (year != null) ? year : now.getYear();
        int displayMonth = (month != null) ? month : now.getMonthValue();

        // Monthly attendance
        LocalDate start = LocalDate.of(displayYear, displayMonth, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        List<Attendance> monthlyAttendance = attendanceService.getMonthlyAttendance(worker.getWorkerId(), displayYear, displayMonth);

        // Today attendance
        Attendance todayAttendance = attendanceService.getTodayAttendance(worker.getWorkerId());

        model.addAttribute("workerName", worker.getWorkerName());
        model.addAttribute("todayAttendance", todayAttendance);
        model.addAttribute("monthlyAttendance", monthlyAttendance);
        model.addAttribute("month", displayMonth);
        model.addAttribute("year", displayYear);

        return "attendance";
    }


    // Mark attendance
    @PostMapping("/mark")
    public String markAttendance(HttpSession session,
                                 @RequestParam("status") String status,
                                 @RequestParam(value="remarks", required=false) String remarks) {
        Worker worker = (Worker) session.getAttribute("USER");
        if (worker == null) return "redirect:/login";

        attendanceService.markAttendance(worker.getWorkerId(), status, remarks);
        return "redirect:/worker/attendance";
    }
}
