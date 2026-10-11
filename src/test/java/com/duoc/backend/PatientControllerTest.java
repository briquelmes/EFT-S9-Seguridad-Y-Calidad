package com.duoc.backend;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.duoc.backend.patient.PatientService;
import com.duoc.backend.patient.PatientController;
import com.duoc.backend.patient.Patient;
import com.duoc.backend.patient.PatientRequest;

import java.util.Arrays;
import java.util.List;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PatientControllerTest {

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    public PatientControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPatients() {
        // Arrange
        Patient patient1 = new Patient();
        patient1.setId(1L);
        patient1.setName("Firulais");
        patient1.setSpecies("Perro");
        patient1.setBreed("Labrador");
        patient1.setAge(5);
        patient1.setOwner("Juan Pérez");

        Patient patient2 = new Patient();
        patient2.setId(2L);
        patient2.setName("Michi");
        patient2.setSpecies("Gato");
        patient2.setBreed("Siames");
        patient2.setAge(3);
        patient2.setOwner("Ana Gómez");

        List<Patient> patients = Arrays.asList(patient1, patient2);
        when(patientService.getAllPatients()).thenReturn(patients);

        // Act
        List<Patient> result = patientController.getAllPatients();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Firulais", result.get(0).getName());
        assertEquals("Michi", result.get(1).getName());
        verify(patientService, times(1)).getAllPatients();
    }

    @Test
    void testGetPatientById() {
        // Arrange
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setName("Firulais");
        patient.setSpecies("Perro");
        patient.setBreed("Labrador");
        patient.setAge(5);
        patient.setOwner("Juan Pérez");

        when(patientService.getPatientById(1L)).thenReturn(patient);

        // Act
        Patient result = patientController.getPatientById(1L);

        // Assert
        assertEquals("Firulais", result.getName());
        assertEquals("Perro", result.getSpecies());
        verify(patientService, times(1)).getPatientById(1L);
    }

    @Test
    void testSavePatient() {
        // Arrange
        Patient patient = new Patient();
        patient.setName("Firulais");
        patient.setSpecies("Perro");
        patient.setBreed("Labrador");
        patient.setAge(5);
        patient.setOwner("Juan Pérez");

        when(patientService.savePatient(any(Patient.class))).thenReturn(patient);

        Patient result = patientController.savePatient(
                new PatientRequest("Firulais", "Perro", "Labrador", 5, "Juan Pérez"));

        // Assert
        assertEquals("Firulais", result.getName());
        ArgumentCaptor<Patient> patientCaptor = ArgumentCaptor.forClass(Patient.class);
        verify(patientService, times(1)).savePatient(patientCaptor.capture());
        assertNull(patientCaptor.getValue().getId());
        assertEquals("Firulais", patientCaptor.getValue().getName());
        assertEquals("Juan Pérez", patientCaptor.getValue().getOwner());
    }

    @Test
    void testDeletePatient() {
        // Arrange
        Long patientId = 1L;

        // Act
        patientController.deletePatient(patientId);

        // Assert
        verify(patientService, times(1)).deletePatient(patientId);
    }
}
