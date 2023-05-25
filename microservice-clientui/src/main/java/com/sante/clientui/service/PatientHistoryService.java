package com.sante.clientui.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.sante.clientui.model.PatientHistory;

@Service
// @FeignClient(name = "patient-history", url = "${patient-history.url}")
@FeignClient(name = "microservice-patienthistory")
public interface PatientHistoryService {
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
}
