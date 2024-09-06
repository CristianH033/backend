package com.linktic.api.repositories;

import com.linktic.api.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByReservationTimeBetweenAndServiceIdAndCustomerId(
            LocalDateTime start, LocalDateTime end, Long serviceId, Long customerId);

    List<Reservation> findByReservationTimeBetweenAndServiceId(
            LocalDateTime start, LocalDateTime end, Long serviceId);

    List<Reservation> findByReservationTimeBetweenAndCustomerId(
            LocalDateTime start, LocalDateTime end, Long customerId);

    List<Reservation> findByServiceIdAndCustomerId(Long serviceId, Long customerId);

    List<Reservation> findByReservationTimeBetween(LocalDateTime start, LocalDateTime end);

    List<Reservation> findByServiceId(Long serviceId);

    List<Reservation> findByCustomerId(Long customerId);
}