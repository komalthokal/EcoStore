package com.construction.model;


import com.construction.controller.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Worker {


@Enumerated(EnumType.STRING)
@Column(nullable = false)
private Role role = Role.WORKER;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "worker_id")
	private Long workerId;
	

	@NotBlank(message="Name is required")
	@Size(min=2,max=50,message="Name should be between 2 to 50 characters.")
	private String workerName;
	
	@Column(unique = true)
	private String username;
	

	@NotBlank(message="Email is required")
	@Email(message = "Invalid email format")
	private String workerEmail;
	
	 @NotBlank(message = "Contact number is required")
	    @Pattern(
	        regexp = "^[6-9]\\d{9}$",
	        message = "Invalid mobile number"
	    )
	private String contact;
	 
	 @NotBlank(message = "Address is required")
	    @Size(min = 10, max = 250, message = "Address must be 10–250 characters")

	private String address;
	 
	 @NotBlank(message = "Password is required")
	 @Size(min = 6, message = "Password must be at least 6 characters")
	 private String password;
	 
	 @Column(nullable = false)
	 private Double baseSalary=0.0;

	 

	 public Double getBaseSalary() {
		return baseSalary;
	}

	public void setBaseSalary(Double baseSalary) {
		this.baseSalary = baseSalary;
	}

	// getter and setter
	 public String getPassword() {
	     return password;
	 }

	 public void setPassword(String password) {
	     this.password = password;
	 }


	public Worker() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public Worker(Role role, Long workerId,
			@NotBlank(message = "Name is required") @Size(min = 2, max = 50, message = "Name should be between 2 to 50 characters.") String workerName,
			String username,
			@NotBlank(message = "Email is required") @Email(message = "Invalid email format") String workerEmail,
			@NotBlank(message = "Contact number is required") @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number") String contact,
			@NotBlank(message = "Address is required") @Size(min = 10, max = 250, message = "Address must be 10–250 characters") String address,
			@NotBlank(message = "Password is required") @Size(min = 6, message = "Password must be at least 6 characters") String password,
			Double baseSalary) {
		super();
		this.role = role;
		this.workerId = workerId;
		this.workerName = workerName;
		this.username = username;
		this.workerEmail = workerEmail;
		this.contact = contact;
		this.address = address;
		this.password = password;
		this.baseSalary = baseSalary;
	}


	public Role getRole() {
	    return role;
	}

	public void setRole(Role role) {
	    this.role = role;
	}

	public Long getWorkerId() {
		return workerId;
	}

	public void setWorkerId(Long workerId) {
		this.workerId = workerId;
	}

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	public String getWorkerEmail() {
		return workerEmail;
	}

	public void setWorkerEmail(String workerEmail) {
		this.workerEmail = workerEmail;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	

	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "Worker [role=" + role + ", workerId=" + workerId + ", workerName=" + workerName + ", username="
				+ username + ", workerEmail=" + workerEmail + ", contact=" + contact + ", address=" + address
				+ ", password=" + password + ", baseSalary=" + baseSalary + "]";
	}







	
	 
	
	
	

}
