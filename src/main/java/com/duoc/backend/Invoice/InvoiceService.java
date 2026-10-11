package com.duoc.backend.Invoice;

import com.duoc.backend.care.Care;
import com.duoc.backend.care.CareRepository;
import com.duoc.backend.medication.Medication;
import com.duoc.backend.medication.MedicationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private MedicationRepository medicationRepository;

    @Autowired
    private CareRepository careRepository;

    public Iterable<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id).orElse(null);
    }

    public Invoice saveInvoice(InvoiceRequest request) {
        List<Long> medicationIds = request.medicationIds() == null ? List.of() : request.medicationIds();
        List<Long> careIds = request.careIds() == null ? List.of() : request.careIds();

        List<Medication> validMedications = StreamSupport.stream(
                medicationRepository.findAllById(
                        medicationIds
                ).spliterator(), false
        ).collect(Collectors.toList());
        if (validMedications.size() != medicationIds.size()) {
            throw new IllegalArgumentException("Algunos medicamentos no existen en la base de datos.");
        }

        List<Care> validCares = StreamSupport.stream(
                careRepository.findAllById(
                        careIds
                ).spliterator(), false
        ).collect(Collectors.toList());
        if (validCares.size() != careIds.size()) {
            throw new IllegalArgumentException("Algunos servicios no existen en la base de datos.");
        }

        double totalCareCost = validCares.stream()
                .mapToDouble(Care::getCost)
                .sum();

        double totalMedicationCost = validMedications.stream()
                .mapToDouble(Medication::getCost)
                .sum();

        Invoice invoice = new Invoice(
                null,
                request.patientName(),
                request.date(),
                new ArrayList<>(validCares),
                new ArrayList<>(validMedications));
        invoice.setTime(request.time());
        invoice.setTotalCost(totalCareCost + totalMedicationCost);

        return invoiceRepository.save(invoice);
    }

    public void deleteInvoice(Long id) {
        invoiceRepository.deleteById(id);
    }
}