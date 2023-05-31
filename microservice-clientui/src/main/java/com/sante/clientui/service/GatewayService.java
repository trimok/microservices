package com.sante.clientui.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.sante.clientui.ClientuiFeignConfig;
import com.sante.clientui.dao.PatientDao;
import com.sante.clientui.model.Patient;
import com.sante.clientui.model.PatientHistory;
import com.sante.clientui.model.Risque;

@Service
@FeignClient(name = "MICROSERVICE-GATEWAY", configuration = ClientuiFeignConfig.class)
public interface GatewayService {
    // PATIENT
    @PostMapping("/patient")
    public Patient createPatient(Patient patient);

    @PutMapping("/patient")
    public Patient updatePatient(Patient patient);

    @DeleteMapping("/patient/{id}")
    public void deletePatient(@PathVariable("id") Long id);

    @GetMapping("/patient")
    public List<Patient> getPatients();

    @GetMapping("/patient/{id}")
    public Patient getPatient(@PathVariable("id") Long id);

    // HISTORIQUE PATIENT
    @PostMapping("/patientHistory")
    public PatientHistory createPatientHistory(PatientHistory patientHistory);

    @PutMapping("/patientHistory")
    public PatientHistory updatePatientHistory(PatientHistory patientHistory);

    @PostMapping("/patientHistory/add")
    public PatientHistory createUpdatePatientHistory(PatientHistory patientHistory);

    @GetMapping("/patientHistory/{id}")
    public PatientHistory getPatientHistory(@PathVariable("id") Long id);

    @PutMapping("/patientHistory/note")
    public PatientHistory updateNote(PatientHistory patientHistory);

    @DeleteMapping("/patientHistory/note")
    public PatientHistory deleteNote(PatientHistory patientHistory);

    // EXPERT
    @PostMapping("/expert")
    public Risque getRisque(PatientDao patientDao);
}
