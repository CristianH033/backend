package com.linktic.api.services;

import com.linktic.api.entities.Reservation;
import com.linktic.api.exceptions.NotFoundException;
import com.linktic.api.repositories.CustomerRepository;
import com.linktic.api.repositories.ReservationRepository;
import com.linktic.api.repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ServiceRepository serviceRepository;

    public Reservation createReservation(Reservation reservation) {
        validateReservation(reservation);
        return reservationRepository.save(reservation);
    }

    public Reservation updateReservation(Long id, Reservation reservationDetails) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        validateReservation(reservationDetails);
        reservation.setCustomer(reservationDetails.getCustomer());
        reservation.setService(reservationDetails.getService());
        reservation.setReservationTime(reservationDetails.getReservationTime());
        reservation.setStatus(reservationDetails.getStatus());
        return reservationRepository.save(reservation);
    }

    public void cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        reservation.setStatus("CANCELLED");
        reservationRepository.save(reservation);
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reservation", "id", id));
    }

    public List<Reservation> getReservations(LocalDate date, Long serviceId, Long customerId) {
        if (date != null && serviceId != null && customerId != null) {
            return reservationRepository.findByReservationTimeBetweenAndServiceIdAndCustomerId(
                    date.atStartOfDay(), date.plusDays(1).atStartOfDay(), serviceId, customerId);
        } else if (date != null && serviceId != null) {
            return reservationRepository.findByReservationTimeBetweenAndServiceId(
                    date.atStartOfDay(), date.plusDays(1).atStartOfDay(), serviceId);
        } else if (date != null && customerId != null) {
            return reservationRepository.findByReservationTimeBetweenAndCustomerId(
                    date.atStartOfDay(), date.plusDays(1).atStartOfDay(), customerId);
        } else if (serviceId != null && customerId != null) {
            return reservationRepository.findByServiceIdAndCustomerId(serviceId, customerId);
        } else if (date != null) {
            return reservationRepository.findByReservationTimeBetween(
                    date.atStartOfDay(), date.plusDays(1).atStartOfDay());
        } else if (serviceId != null) {
            return reservationRepository.findByServiceId(serviceId);
        } else if (customerId != null) {
            return reservationRepository.findByCustomerId(customerId);
        } else {
            return reservationRepository.findAll();
        }
    }

    private void validateReservation(Reservation reservation) {
        if (!customerRepository.existsById(reservation.getCustomer().getId())) {
            throw new RuntimeException("Customer not found");
        }
        if (!serviceRepository.existsById(reservation.getService().getId())) {
            throw new RuntimeException("Service not found");
        }
    }
}
