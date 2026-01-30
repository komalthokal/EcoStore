package com.construction.repository;

import com.construction.model.Salary;
import com.construction.model.Worker;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface SalaryRepository extends JpaRepository<Salary, Long> {
    List<Salary> findByWorkerIdOrderBySalaryMonthDesc(Long workerId);
    Salary findByWorkerIdAndSalaryMonth(Long workerId, YearMonth salaryMonth);
	long countByStatus(String string);

	    List<Salary> findByStatus(String status); // e.g., "Unpaid", "Paid"

	    List<Salary> findByWorkerId(Long workerId);

	    // Optional: find salaries for a specific month
	    List<Salary> findBySalaryMonth(java.time.YearMonth month);
		boolean existsByWorkerIdAndSalaryMonth(Long workerId, YearMonth month);
		//Optional<Salary> findByWorkerIdAndSalaryMonth(Long workerId, YearMonth salaryMonth);
		
}



