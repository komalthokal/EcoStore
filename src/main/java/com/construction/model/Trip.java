package com.construction.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "trip_history")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripId;

    private Long workerId;
    private String vehicleNo;
    private LocalDate tripDate;

    private String materialType;
    private Double materialQuantity;

    private String fromLocation;
    private String toLocation;
    private Double distanceKm;

    private Double dieselUsed;
    private Double dieselCost;

    private Double tripIncome;
    private String remarks;
    
    
    
	public Trip() {
		super();
		// TODO Auto-generated constructor stub
	}



	public Trip(Long tripId, Long workerId, String vehicleNo, LocalDate tripDate, String materialType,
			Double materialQuantity, String fromLocation, String toLocation, Double distanceKm, Double dieselUsed,
			Double dieselCost, Double tripIncome, String remarks) {
		super();
		this.tripId = tripId;
		this.workerId = workerId;
		this.vehicleNo = vehicleNo;
		this.tripDate = tripDate;
		this.materialType = materialType;
		this.materialQuantity = materialQuantity;
		this.fromLocation = fromLocation;
		this.toLocation = toLocation;
		this.distanceKm = distanceKm;
		this.dieselUsed = dieselUsed;
		this.dieselCost = dieselCost;
		this.tripIncome = tripIncome;
		this.remarks = remarks;
	}



	public Long getTripId() {
		return tripId;
	}



	public void setTripId(Long tripId) {
		this.tripId = tripId;
	}



	public Long getWorkerId() {
		return workerId;
	}



	public void setWorkerId(Long workerId) {
		this.workerId = workerId;
	}



	public String getVehicleNo() {
		return vehicleNo;
	}



	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}



	public LocalDate getTripDate() {
		return tripDate;
	}



	public void setTripDate(LocalDate tripDate) {
		this.tripDate = tripDate;
	}



	public String getMaterialType() {
		return materialType;
	}



	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}



	public Double getMaterialQuantity() {
		return materialQuantity;
	}



	public void setMaterialQuantity(Double materialQuantity) {
		this.materialQuantity = materialQuantity;
	}



	public String getFromLocation() {
		return fromLocation;
	}



	public void setFromLocation(String fromLocation) {
		this.fromLocation = fromLocation;
	}



	public String getToLocation() {
		return toLocation;
	}



	public void setToLocation(String toLocation) {
		this.toLocation = toLocation;
	}



	public Double getDistanceKm() {
		return distanceKm;
	}



	public void setDistanceKm(Double distanceKm) {
		this.distanceKm = distanceKm;
	}



	public Double getDieselUsed() {
		return dieselUsed;
	}



	public void setDieselUsed(Double dieselUsed) {
		this.dieselUsed = dieselUsed;
	}



	public Double getDieselCost() {
		return dieselCost;
	}



	public void setDieselCost(Double dieselCost) {
		this.dieselCost = dieselCost;
	}



	public Double getTripIncome() {
		return tripIncome;
	}



	public void setTripIncome(Double tripIncome) {
		this.tripIncome = tripIncome;
	}



	public String getRemarks() {
		return remarks;
	}



	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}



	@Override
	public String toString() {
		return "Trip [tripId=" + tripId + ", workerId=" + workerId + ", vehicleNo=" + vehicleNo + ", tripDate="
				+ tripDate + ", materialType=" + materialType + ", materialQuantity=" + materialQuantity
				+ ", fromLocation=" + fromLocation + ", toLocation=" + toLocation + ", distanceKm=" + distanceKm
				+ ", dieselUsed=" + dieselUsed + ", dieselCost=" + dieselCost + ", tripIncome=" + tripIncome
				+ ", remarks=" + remarks + "]";
	}
	
	

    
}
