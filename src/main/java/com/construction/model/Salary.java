package com.construction.model;

import java.time.LocalDate;
import java.time.YearMonth;

import jakarta.persistence.*;

@Entity
@Table(name = "salary")
public class Salary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salaryId;

    @Column(nullable = false)
    private Long workerId;

    @Column(nullable = false)
    private YearMonth salaryMonth; // store month and year

    @Column(nullable = false)
    private Double baseSalary; // fixed salary

    @Column(nullable = true)
    private Double overtimeHours; // extra hours worked

    @Column(nullable = true)
    private Double overtimeRate; // per hour rate

    @Column(nullable = true)
    private Double overtimePay; // calculated overtime pay

    @Column(nullable = false)
    private Double amount; // total = baseSalary + overtimePay

    @Column(nullable = false)
    private String status; // Paid / Unpaid / Partially Paid

    private String remarks;
    
    @Column(nullable = false)
    private Double advance;
    
    

    public Salary() {
		super();
		// TODO Auto-generated constructor stub
	}
	// Getters and Setters
    public Long getSalaryId() { return salaryId; }
    public void setSalaryId(Long salaryId) { this.salaryId = salaryId; }

    public Long getWorkerId() { return workerId; }
    public void setWorkerId(Long workerId) { this.workerId = workerId; }

    public YearMonth getSalaryMonth() { return salaryMonth; }
    public void setSalaryMonth(YearMonth salaryMonth) { this.salaryMonth = salaryMonth; }

    public Double getBaseSalary() { return baseSalary; }
    public void setBaseSalary(Double baseSalary) { this.baseSalary = baseSalary; }

    public Double getOvertimeHours() { return overtimeHours; }
    public void setOvertimeHours(Double overtimeHours) { this.overtimeHours = overtimeHours; }

    public Double getOvertimeRate() { return overtimeRate; }
    public void setOvertimeRate(Double overtimeRate) { this.overtimeRate = overtimeRate; }

    public Double getOvertimePay() { return overtimePay; }
    public void setOvertimePay(Double overtimePay) { this.overtimePay = overtimePay; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
	public Double getAdvance() {
		return advance;
	}
	public void setAdvance(Double advance) {
		this.advance = advance;
	}
    
	public Double getNetAmount() {
        double otPay = (overtimeHours != null ? overtimeHours : 0) * (overtimeRate != null ? overtimeRate : 0);
        return (baseSalary != null ? baseSalary : 0) + otPay - (advance != null ? advance : 0);
    }
}
