package com.linktic.api.requests;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

public class ReservationRequest {
    public Long customer_id;
    public Long service_id;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    public LocalDateTime reservation_time;
    public String status;
}
