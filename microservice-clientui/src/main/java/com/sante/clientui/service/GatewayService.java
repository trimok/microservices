package com.sante.clientui.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.sante.clientui.OAuthFeignConfig;
import com.sante.clientui.dao.PatientDao;
import com.sante.clientui.model.Patient;
import com.sante.clientui.model.PatientHistory;
import com.sante.clientui.model.Risque;

/**
 * @author trimok
 *
 *         Client Feign
 *
 */
@Service
@FeignClient(name = "MICROSERVICE-GATEWAY", configuration = OAuthFeignConfig.class)
public interface GatewayService {
    // PATIENT
    /**
     * Patient creation
     * 
     * @param patient : the patient to create
     * @return : the created patient
     */
    @PostMapping("/patient")
    public Patient createPatient(Patient patient);

    /**
     * Patient update
     * 
     * @param patient : the patient to update
     * @return : the updated patient
     */
    @PutMapping("/patient")
    public Patient updatePatient(Patient patient);

    /**
     * Delete patient
     * 
     * @param id : the patient id
     */
    @DeleteMapping("/patient/{id}")
    public void deletePatient(@PathVariable("id") Long id);

    /**
     * Delete all patients
     * 
     */
    @DeleteMapping("/patient/admin/all")
    public void deleteAllPatient();

    /**
     * Getting the list of patients
     * 
     * @return : the list of patients
     */
    @GetMapping("/patient")
    public List<Patient> getPatients();

    /**
     * Getting a patient
     * 
     * @param id : the patient id
     * @return : the patient
     */
    @GetMapping("/patient/{id}")
    public Patient getPatient(@PathVariable("id") Long id);

    // HISTORIQUE PATIENT
    /**
     * Creating a patient history (with one note)
     * 
     * @param patientHistory : the patient history with one note
     * @return : the created patient history
     */
    @PostMapping("/patientHistory")
    public PatientHistory createPatientHistory(PatientHistory patientHistory);

    /**
     * Updating a patient history (add one note)
     * 
     * @param patientHistory : the patient history with one note
     * @return : the updated patient history
     */
    @PutMapping("/patientHistory")
    public PatientHistory updatePatientHistory(PatientHistory patientHistory);

    /**
     * Create or update a patient history (with adding one note)
     * 
     * @param patientHistory : the patient history
     * @return : the created or updated patient history
     */
    @PostMapping("/patientHistory/add")
    public PatientHistory createUpdatePatientHistory(PatientHistory patientHistory);

    /**
     * Getting a patient history
     * 
     * @param id : the patient/patientHistory id
     * @return
     */
    @GetMapping("/patientHistory/{id}")
    public PatientHistory getPatientHistory(@PathVariable("id") Long id);

    /**
     * Delete a patient history
     * 
     * @param id : the patienthistory
     * @return
     */
    @DeleteMapping("/patientHistory/{id}")
    public ResponseEntity<PatientHistory> deletePatientHistory(@PathVariable(value = "id") Long id);

    /**
     * Delete all patient history
     * 
     * @return
     */
    @DeleteMapping("/patientHistory/admin/all")
    public ResponseEntity<PatientHistory> deleteAllPatientHistory();

    /**
     * Updating a note
     * 
     * @param patientHistory : the patient history with one note to be updated
     * @return : the updated patient history
     */
    @PutMapping("/patientHistory/note")
    public PatientHistory updateNote(PatientHistory patientHistory);

    /**
     * Deleting a note
     * 
     * @param patientHistory : the patient history with one note to be deleted
     * @return : the updated patient history
     */
    @DeleteMapping("/patientHistory/note")
    public PatientHistory deleteNote(PatientHistory patientHistory);

    // EXPERT
    /**
     * Getting the risk
     * 
     * @param patientDao : the patientDao
     * @return : the risk
     */
    @PostMapping("/expert")
    public Risque getRisque(PatientDao patientDao);
}
