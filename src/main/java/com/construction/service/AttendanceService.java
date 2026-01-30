package com.construction.service;

import com.construction.model.Attendance;
import com.construction.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    // Mark today's attendance
    public Attendance markAttendance(Long workerId, String status, String remarks) {
        LocalDate today = LocalDate.now();
        Attendance existing = attendanceRepository.findByWorkerIdAndAttendanceDate(workerId, today);
        if (existing != null) {
            existing.setStatus(status);
            existing.setRemarks(remarks);
            return attendanceRepository.save(existing);
        }
        Attendance attendance = new Attendance();
        attendance.setWorkerId(workerId);
        attendance.setAttendanceDate(today);
        attendance.setStatus(status);
        attendance.setRemarks(remarks);
        return attendanceRepository.save(attendance);
    }

    // Get monthly attendance
    public List<Attendance> getMonthlyAttendance(Long workerId, int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        return attendanceRepository.findByWorkerIdAndAttendanceDateBetween(workerId, start, end);
    }
    
    public Attendance getTodayAttendance(Long workerId) {
        return attendanceRepository.findByWorkerIdAndAttendanceDate(workerId, LocalDate.now());
    }

}
