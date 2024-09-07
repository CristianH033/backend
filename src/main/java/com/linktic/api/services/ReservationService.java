package com.linktic.api.services;

import com.linktic.api.entities.Reservation;
import com.linktic.api.exceptions.NotFoundException;
import com.linktic.api.repositories.CustomerRepository;
import com.linktic.api.repositories.ReservationRepository;
import com.linktic.api.repositories.ServiceRepository;
import com.linktic.api.requests.ReservationRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ServiceRepository serviceRepository;

    public Reservation createReservation(ReservationRequest reservationReq) {
        validateReservation(reservationReq);

        Reservation reservation = new Reservation();
        reservation.setCustomer(customerRepository.findById(reservationReq.customer_id).get());
        reservation.setService(serviceRepository.findById(reservationReq.service_id).get());
        reservation.setReservationTime(reservationReq.reservation_time);
        reservation.setStatus(reservationReq.status);

        return reservationRepository.save(reservation);
    }

    public Reservation updateReservation(Long id, ReservationRequest reservationDetails) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        validateReservation(reservationDetails);
        reservation.setCustomer(customerRepository.findById(reservationDetails.customer_id).get());
        reservation.setService(serviceRepository.findById(reservationDetails.service_id).get());
        reservation.setReservationTime(reservationDetails.reservation_time);
        reservation.setStatus(reservationDetails.status);
        return reservationRepository.save(reservation);
    }

    public Reservation patchReservation(Long id, ReservationRequest reservationDetails) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (reservationDetails.customer_id != null) {
            reservation.setCustomer(customerRepository.findById(reservationDetails.customer_id).get());
        }

        if (reservationDetails.service_id != null) {
            reservation.setService(serviceRepository.findById(reservationDetails.service_id).get());
        }

        if (reservationDetails.reservation_time != null) {
            if (reservationDetails.reservation_time.isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Reservation time cannot be in the past");
            }

            reservation.setReservationTime(reservationDetails.reservation_time);
        }

        if (reservationDetails.status != null) {
            reservation.setStatus(reservationDetails.status);
        }

        return reservationRepository.save(reservation);
    }

    public void cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        reservation.setStatus("CANCELLED");
        reservationRepository.save(reservation);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
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

    private void validateReservation(ReservationRequest reservation) {
        if (!customerRepository.existsById(reservation.customer_id)) {
            throw new RuntimeException("Customer not found");
        }
        if (!serviceRepository.existsById(reservation.service_id)) {
            throw new RuntimeException("Service not found");
        }

        if (reservation.reservation_time != null
                && reservation.reservation_time.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Reservation time cannot be in the past");
        }
    }
}
