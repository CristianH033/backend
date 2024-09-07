package com.linktic.api.controllers;

import com.linktic.api.entities.Reservation;
import com.linktic.api.requests.ReservationRequest;
import com.linktic.api.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    @CrossOrigin(origins = "*")
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationRequest reservationReq) {
        return ResponseEntity.ok(reservationService.createReservation(reservationReq));
    }

    @PutMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Reservation> updateReservation(@PathVariable Long id,
            @RequestBody ReservationRequest reservationReq) {
        return ResponseEntity.ok(reservationService.updateReservation(id, reservationReq));
    }

    @PatchMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Reservation> patchReservation(@PathVariable Long id,
            @RequestBody ReservationRequest reservationReq) {
        return ResponseEntity.ok(reservationService.patchReservation(id, reservationReq));
    }

    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @CrossOrigin(origins = "*")
    public ResponseEntity<List<Reservation>> getReservations(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Long serviceId,
            @RequestParam(required = false) Long customerId) {
        return ResponseEntity.ok(reservationService.getReservations(date, serviceId, customerId));
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    @PostMapping("/{id}/cancel")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }
}
