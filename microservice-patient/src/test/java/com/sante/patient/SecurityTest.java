package com.sante.patient;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.sante.patient.model.Patient;
import com.sante.patient.service.IPatientService;

@ContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(value = org.junit.jupiter.api.MethodOrderer.MethodName.class)
public class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    IPatientService patientService;

    private final static String ROLE_PATIENT_USER = "ROLE_PATIENT_USER";

    private Map<String, Object> sessionAttrs = null;

    private Patient patientDatabase;
    private Patient patient;
    private Patient patientToBeSavedNotValid;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private LocalDate dateNaissance = LocalDate.parse("04/12/1957", formatter);

    @BeforeEach
    public void beforeEach() {

	patient = new Patient(null, "Tristan", "Mokobodzki", dateNaissance, "M", "1 rue du Louvre, PARIS",
		"06 01 02 08 09");

	patientDatabase = new Patient(1L, "Tristan", "Mokobodzki", dateNaissance, "M", "1 rue du Louvre, PARIS",
		"06 01 02 08 09");

	patientToBeSavedNotValid = new Patient(null, "Tristan", "Mokobodzki", dateNaissance, "MF",
		"1 rue du Louvre, PARIS",
		"06 01 02 08 09");
    }

    @AfterEach
    public void afterEach() {
	patientService.deleteAllPatient();
    }

    /***************************
     * TEST INVALID TOKEN
     ************************************/

    @Test
    public void testInvalidToken() throws Exception {

	// Creation d'un patient
	patientService.createPatient(patient);

	mockMvc
		.perform(MockMvcRequestBuilders.get("/patient")
			.with(jwt().authorities(new SimpleGrantedAuthority("INVALID_TOKEN"))))
		.andExpect(status().is(403)).andReturn();

    }
}
