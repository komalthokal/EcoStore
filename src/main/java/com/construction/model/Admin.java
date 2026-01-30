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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;




@Entity
public class Admin {

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role = Role.ADMIN;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int aID;
	
	@NotBlank(message="Name is required")
	@Size(min=2,max=50,message="Name should be between 2 to 50 characters.")
	private String adName;
	
	@Column(unique = true)
	private String adUsername;
	
	@NotBlank(message="Email is required")
	@Email(message = "Invalid email format")
	private String adEmail;
	
	 @NotNull(message = "Contact number is required")
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

	 // getter and setter
	 public String getPassword() {
	     return password;
	 }

	 public void setPassword(String password) {
	     this.password = password;
	 }


	public Admin() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public Admin(int aID,
			@NotBlank(message = "Name is required") @Size(min = 2, max = 50, message = "Name should be between 2 to 50 characters.") String adName,
			String adUsername,
			@NotBlank(message = "Email is required") @Email(message = "Invalid email format") String adEmail,
			@NotBlank(message = "Contact number is required") @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number") @NotNull(message = "Contact number is required") @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number") String contact,
			@NotBlank(message = "Address is required") @Size(min = 10, max = 250, message = "Address must be 10–250 characters") String address) {
		super();
		this.aID = aID;
		this.adName = adName;
		this.adUsername = adUsername;
		this.adEmail = adEmail;
		this.contact = contact;
		this.address = address;
	}

	public Role getRole() {
	    return role;
	}

	public void setRole(Role role) {
	    this.role = role;
	}


	public int getaID() {
		return aID;
	}

	public void setaID(int aID) {
		this.aID = aID;
	}

	public String getAdName() {
		return adName;
	}

	public void setAdName(String adName) {
		this.adName = adName;
	}

	public String getAdEmail() {
		return adEmail;
	}

	public void setAdEmail(String adEmail) {
		this.adEmail = adEmail;
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
	
	

	public String getAdUsername() {
		return adUsername;
	}



	public void setAdUsername(String adUsername) {
		this.adUsername = adUsername;
	}



	@Override
	public String toString() {
		return "Admin [aID=" + aID + ", adName=" + adName + ", adUsername=" + adUsername + ", adEmail=" + adEmail
				+ ", contact=" + contact + ", address=" + address + "]";
	}



	
	 

}
