package com.duoc.backend.appointment;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentRequest(
        LocalDate date,
        LocalTime time,
        String reason,
        String veterinarian) {
}
