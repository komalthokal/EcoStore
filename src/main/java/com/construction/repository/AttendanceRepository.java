package com.construction.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.construction.model.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {


	    // Find all attendance records of a worker in a given month
	    List<Attendance> findByWorkerIdAndAttendanceDateBetween(Long workerId, LocalDate start, LocalDate end);


    List<Attendance> findByWorkerIdOrderByAttendanceDateDesc(Long workerId);

    List<Attendance> findByWorkerIdAndAttendanceDateBetweenOrderByAttendanceDateDesc(
            Long workerId, LocalDate startDate, LocalDate endDate);

    Attendance findByWorkerIdAndAttendanceDate(Long workerId, LocalDate date);


	List<Attendance> findByAttendanceDateAndStatus(LocalDate today, String string);


	List<Attendance> findByWorkerId(Long workerId);
}

