package com.construction.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.construction.model.Attendance;
import com.construction.model.Trip;

public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findByWorkerId(Long workerId);

    List<Trip> findByWorkerIdAndTripDateBetween(Long workerId, LocalDate start, LocalDate end);

	List<Trip> findByTripDate(LocalDate today);
}
