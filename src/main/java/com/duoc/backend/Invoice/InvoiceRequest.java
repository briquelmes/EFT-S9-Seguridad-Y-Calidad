package com.duoc.backend.Invoice;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record InvoiceRequest(
        String patientName,
        LocalDate date,
        LocalTime time,
        List<Long> careIds,
        List<Long> medicationIds) {
}
