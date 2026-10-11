package com.duoc.backend;

import com.duoc.backend.Invoice.Invoice;
import com.duoc.backend.Invoice.InvoiceController;
import com.duoc.backend.Invoice.InvoiceService;
import com.duoc.backend.care.Care;
import com.duoc.backend.medication.Medication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InvoiceControllerTest {

    @Mock
    private InvoiceService invoiceService;

    @InjectMocks
    private InvoiceController invoiceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllInvoices() {
        // Arrange
        LocalDate date1 = LocalDate.parse("2025-04-28");
        LocalDate date2 = LocalDate.parse("2025-04-29");
        Invoice invoice1 = new Invoice(1L, "Patient1", date1, Arrays.asList(), Arrays.asList());
        Invoice invoice2 = new Invoice(2L, "Patient2", date2, Arrays.asList(), Arrays.asList());
        when(invoiceService.getAllInvoices()).thenReturn(Arrays.asList(invoice1, invoice2));

        // Act
        List<Invoice> result = invoiceController.getAllInvoices();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(invoiceService, times(1)).getAllInvoices();
    }

    @Test
    void testGetInvoiceById() {
        // Arrange
        LocalDate date1 = LocalDate.parse("2025-04-28");
        Invoice invoice = new Invoice(1L, "Patient1", date1, Arrays.asList(), Arrays.asList());
        when(invoiceService.getInvoiceById(1L)).thenReturn(invoice);

        // Act
        Invoice result = invoiceController.getInvoiceById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Patient1", result.getPatientName());
        verify(invoiceService, times(1)).getInvoiceById(1L);
    }

    @Test
    void testSaveInvoice() {
        // Arrange
        LocalDate date1 = LocalDate.parse("2025-04-28");
        Invoice invoice = new Invoice(1L, "Patient1", date1, Arrays.asList(), Arrays.asList());
        when(invoiceService.saveInvoice(invoice)).thenReturn(invoice);

        // Act
        Invoice result = invoiceController.saveInvoice(invoice);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(invoiceService, times(1)).saveInvoice(invoice);
    }

    @Test
    void testDeleteInvoice() {
        // Act
        invoiceController.deleteInvoice(1L);

        // Assert
        verify(invoiceService, times(1)).deleteInvoice(1L);
    }

    @Test
    void testGenerateInvoicePdf() {
        // Arrange
        LocalDate date1 = LocalDate.parse("2025-04-28");
        Care care = new Care("Consulta", 50.0);
        Medication medication = new Medication("Antibiótico", 30.0);
        Invoice invoice = new Invoice(1L, "Patient1", date1, Arrays.asList(care), Arrays.asList(medication));
        when(invoiceService.getInvoiceById(1L)).thenReturn(invoice);

        // Act
        ResponseEntity<byte[]> response = invoiceController.generateInvoicePdf(1L);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getHeaders().getContentType());
        assertEquals("application/pdf", response.getHeaders().getContentType().toString());
        verify(invoiceService, times(1)).getInvoiceById(1L);
    }
}