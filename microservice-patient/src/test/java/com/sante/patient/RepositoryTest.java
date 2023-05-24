package com.sante.patient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.sante.patient.model.Patient;
import com.sante.patient.repository.PatientRepository;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(value = org.junit.jupiter.api.MethodOrderer.MethodName.class)

public class RepositoryTest {
    @Autowired
    private PatientRepository patientRepository;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private LocalDate dateNaissance = LocalDate.parse("04/12/1957", formatter);

    private Patient patient = new Patient(null, "Tristan", "Mokobodzki", dateNaissance, "M", "1 rue du Louvre, PARIS",
	    "06 01 02 08 09");

    @BeforeAll
    public void beforeAll() {
	patientRepository.deleteAll();
    }

    @BeforeEach
    public void beforeEach() {

    }

    @AfterEach
    public void afterEach() {
	patientRepository.deleteAll();
    }

    @Test
    public void createPatient() {
	Patient patientSaved = patientRepository.save(patient);
	assertNotNull(patientSaved.getId());
	assertThat(patientSaved.toString().equals(patient.toString()));
    }

    @Test
    public void updatePatient() {
	Patient patientToBeUpdated = patientRepository.save(patient);
	String newPrenom = "Daniel";
	patientToBeUpdated.setPrenom(newPrenom);
	Patient patientUpdated = patientRepository.save(patientToBeUpdated);
	assertThat(patientUpdated.getPrenom().equals(newPrenom));
    }

    @Test
    public void getPatient() {
	Patient patientDatabase = patientRepository.save(patient);
	Optional<Patient> patient = patientRepository.findById(patientDatabase.getId());
	assertTrue(patient.isPresent());
    }

    @Test
    public void getPatients() {
	patientRepository.save(patient);
	List<Patient> listResult = patientRepository.findAll();
	assertTrue(listResult.size() == 1);
    }

    @Test
    public void deletePatient() {
	Patient patientSaved = patientRepository.save(patient);
	Long id = patientSaved.getId();
	patientRepository.deleteById(patientSaved.getId());
	Optional<Patient> patient = patientRepository.findById(id);
	assertFalse(patient.isPresent());
    }
}
