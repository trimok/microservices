package com.sante.patient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sante.patient.exception.PatientNoContentException;
import com.sante.patient.exception.PatientNotFoundException;
import com.sante.patient.model.Patient;
import com.sante.patient.repository.PatientRepository;
import com.sante.patient.service.IPatientService;
import com.sante.patient.service.PatientService;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(value = org.junit.jupiter.api.MethodOrderer.MethodName.class)
public class ServiceTest {

    private Timestamp now = Timestamp.from(Instant.now());

    private IPatientService patientService;

    @Mock
    private PatientRepository patientRepository;

    private Patient patient;
    private Patient patientDatabase;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private LocalDate dateNaissance = LocalDate.parse("04/12/1957", formatter);

    @BeforeEach
    public void beforeEach() {
	patientService = new PatientService(patientRepository);

	patient = new Patient(null, "Tristan", "Mokobodzki", dateNaissance, "M", "1 rue du Louvre, PARIS",
		"06 01 02 08 09 ");

	patientDatabase = new Patient(1L, "Tristan", "Mokobodzki", dateNaissance, "M", "1 rue du Louvre, PARIS",
		"06 01 02 08 09 ");
    }

    /*********************** PATIENT SERVICE **************************/

    @Test
    public void patientServiceCreatePatient() {

	when(patientRepository.save(any(Patient.class))).thenReturn(patientDatabase);
	Patient patientSaved = patientService.createPatient(patient);
	verify(patientRepository, times(1)).save(any(Patient.class));
	assertNotNull(patientSaved.getId());
	assertThat(patientSaved.toString()).isEqualTo(patientDatabase.toString());
    }

    @Test
    public void patientServiceCreatePatient_PatientNoContentException() {

	when(patientRepository.save(any(Patient.class))).thenReturn(null);
	Executable action = () -> patientService.createPatient(patient);
	assertThrows(PatientNoContentException.class, action);
	verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    public void patientServiceUpdatePatient() {

	when(patientRepository.findById(any(Long.class))).thenReturn(Optional.of(patientDatabase));
	when(patientRepository.save(any(Patient.class))).thenReturn(patientDatabase);
	Patient patientUpdated = patientService.updatePatient(patientDatabase);
	verify(patientRepository, times(1)).findById(any(Long.class));
	verify(patientRepository, times(1)).save(any(Patient.class));
	assertThat(patientUpdated.toString()).isEqualTo(patientDatabase.toString());
    }

    @Test
    public void patientServiceUpdatePatient_PatientNoContentException() {

	when(patientRepository.findById(any(Long.class))).thenReturn(Optional.of(patientDatabase));
	when(patientRepository.save(any(Patient.class))).thenReturn(null);
	Executable action = () -> patientService.updatePatient(patientDatabase);
	assertThrows(PatientNoContentException.class, action);
	verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    public void patientServiceUpdatePatient_PatientNotFoundException() {

	when(patientRepository.findById(any(Long.class))).thenReturn(Optional.empty());
	Executable action = () -> patientService.updatePatient(patientDatabase);
	assertThrows(PatientNotFoundException.class, action);
	verify(patientRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void patientServiceDeletePatient() {

	when(patientRepository.findById(any(Long.class))).thenReturn(Optional.of(patientDatabase));
	patientService.deletePatient(patientDatabase.getId());
	verify(patientRepository, times(1)).findById(any(Long.class));
	verify(patientRepository, times(1)).deleteById(any(Long.class));
    }

    @Test
    public void patientServiceDeletePatient_PatientNotFoundException() {

	when(patientRepository.findById(any(Long.class))).thenReturn(Optional.empty());
	Executable action = () -> patientService.deletePatient(patientDatabase.getId());
	assertThrows(PatientNotFoundException.class, action);
	verify(patientRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void patientServiceGetPatients() {

	when(patientRepository.findAll()).thenReturn(Arrays.asList(patientDatabase));
	List<Patient> patients = patientService.getPatients();
	assertNotNull(patients);
	assertThat(patients.size() == 1);
	verify(patientRepository, times(1)).findAll();
    }

    @Test
    public void patientServiceGetPatientById() {

	when(patientRepository.findById(any(Long.class))).thenReturn(Optional.of(patientDatabase));
	Patient patient = patientService.getPatient(patientDatabase.getId());
	assertNotNull(patient);
	assertThat(patient.toString().equals(patientDatabase.toString()));
	verify(patientRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void patientServiceGetPatientById_PatientNotFoundException() {

	when(patientRepository.findById(any(Long.class))).thenReturn(Optional.empty());
	Executable action = () -> patientService.getPatient(patientDatabase.getId());
	assertThrows(PatientNotFoundException.class, action);
	verify(patientRepository, times(1)).findById(any(Long.class));
    }
}
