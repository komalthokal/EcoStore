package com.construction.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.construction.model.Trip;
import com.construction.model.Worker;
import com.construction.repository.TripRepository;
import com.construction.repository.WorkerRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/worker")
public class TripController {

    @Autowired
    private TripRepository tripRepository;
    
    @Autowired
    private WorkerRepository workerRepo;  // make sure this is declared at class level

    @GetMapping("/trip")
    public String showTripForm(Model model, HttpSession session) {
        // Check login and role
        String role = (String) session.getAttribute("ROLE");
        if (!"WORKER".equals(role)) {
            return "redirect:/login"; // not a worker
        }

        // Get the logged-in worker from session
        Worker worker = (Worker) session.getAttribute("USER");
        if (worker == null) {
            return "redirect:/login";
        }

        // Add attributes for Thymeleaf
        model.addAttribute("workerName", worker.getWorkerName());
        model.addAttribute("trip", new Trip());

        return "trip";  // points to templates/trip.html
    }

    @PostMapping("/saveTrip")
    public String saveTrip(
            @RequestParam String vehicleNo,
            @RequestParam String materialType,
            @RequestParam Double materialQuantity,
            @RequestParam String fromLocation,
            @RequestParam String toLocation,
            @RequestParam Double distanceKm,
            @RequestParam Double dieselUsed,
            @RequestParam Double dieselCost,
            @RequestParam Double tripIncome,
            @RequestParam String remarks,
            HttpSession session
    ) {
        // Get logged-in worker from session
        Worker worker = (Worker) session.getAttribute("USER");

        if (worker == null) {
            return "redirect:/login"; // not logged in
        }

        Trip trip = new Trip();
        trip.setWorkerId(worker.getWorkerId());   // ✅ correct Long workerId
        trip.setVehicleNo(vehicleNo);
        trip.setMaterialType(materialType);
        trip.setMaterialQuantity(materialQuantity);
        trip.setFromLocation(fromLocation);
        trip.setToLocation(toLocation);
        trip.setDistanceKm(distanceKm);
        trip.setDieselUsed(dieselUsed);
        trip.setDieselCost(dieselCost);
        trip.setTripIncome(tripIncome);
        trip.setRemarks(remarks);
        trip.setTripDate(LocalDate.now());

        tripRepository.save(trip);

        return "redirect:/worker/triphistory";
    }



    // 2️⃣ Show trips with optional date filter
    @GetMapping("/triphistory")
    public String tripHistory(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            Model model,
            HttpSession session
    ) {
        Worker worker = (Worker) session.getAttribute("USER");  // ✅ get the Worker object
        if (worker == null) return "redirect:/login";

        Long workerId = worker.getWorkerId();  // ✅ get the workerId

        List<Trip> trips;

        if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {
            trips = tripRepository.findByWorkerIdAndTripDateBetween(
                    workerId, LocalDate.parse(startDate), LocalDate.parse(endDate));
        } else {
            trips = tripRepository.findByWorkerId(workerId);
        }

        // Calculate total distance & income
        double totalDistance = trips.stream().mapToDouble(t -> t.getDistanceKm() != null ? t.getDistanceKm() : 0).sum();
        double totalIncome = trips.stream().mapToDouble(t -> t.getTripIncome() != null ? t.getTripIncome() : 0).sum();

        model.addAttribute("trips", trips);
        model.addAttribute("totalDistance", totalDistance);
        model.addAttribute("totalIncome", totalIncome);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "triphistory";  // points to templates/triphistory.html
    }


    // 3️⃣ Delete trip
    @GetMapping("/trip/delete/{id}")
    public String deleteTrip(@PathVariable Long id, HttpSession session) {
        Worker worker = (Worker) session.getAttribute("USER");
        if (worker == null) {
            return "redirect:/login";
        }

        Trip trip = tripRepository.findById(id).orElse(null);
        if (trip != null && trip.getWorkerId().equals(worker.getWorkerId())) {
            tripRepository.delete(trip);
        }

        return "redirect:/worker/triphistory";
    }

    // 4️⃣ Edit trip form
    @GetMapping("/trip/edit/{id}")
    public String editTripForm(@PathVariable Long id, Model model, HttpSession session) {
        Worker worker = (Worker) session.getAttribute("USER");
        if (worker == null) {
            return "redirect:/login";
        }

        Trip trip = tripRepository.findById(id).orElse(null);
        if (trip != null && trip.getWorkerId().equals(worker.getWorkerId())) {
            model.addAttribute("trip", trip);
            return "edit-trip"; // your edit form template
        }

        return "redirect:/worker/triphistory";
    }

    // 5️⃣ Update trip
    @PostMapping("/trip/update")
    public String updateTrip(@ModelAttribute Trip trip, HttpSession session) {
        Worker worker = (Worker) session.getAttribute("USER");
        if (worker == null) {
            return "redirect:/login";
        }

        Trip existing = tripRepository.findById(trip.getTripId()).orElse(null);
        if (existing != null && existing.getWorkerId().equals(worker.getWorkerId())) {
            trip.setWorkerId(worker.getWorkerId()); // ensure workerId doesn't change
            trip.setTripDate(existing.getTripDate()); // preserve original date
            tripRepository.save(trip);
        }

        return "redirect:/worker/triphistory";
    }

}
