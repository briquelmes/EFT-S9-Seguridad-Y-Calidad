package com.duoc.backend;

import com.duoc.backend.patient.PatientController;
import com.duoc.backend.patient.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import com.duoc.backend.patient.Patient;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(PatientController.class)
@SpringJUnitConfig(PatientController.class)
class PatientControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PatientService patientService;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"}) // Simula un usuario autenticado
    void testGetAllPatientsWithMockBearerToken() throws Exception {
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
        patient2.setName("Tom");
        patient2.setSpecies("Gato");
        patient2.setBreed("Comun");
        patient2.setAge(5);
        patient2.setOwner("Montse");

        when(patientService.getAllPatients()).thenReturn(Arrays.asList(patient1, patient2));

        // Act & Assert
        mockMvc.perform(get("/patient")
                .header("Authorization", "Bearer mock-token") // Mock token
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Firulais"))
                .andExpect(jsonPath("$[1].name").value("Tom"));

        verify(patientService, times(1)).getAllPatients();
    }
}
