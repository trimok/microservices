package com.sante.patient.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sante.patient.model.Patient;
import com.sante.patient.service.IPatientService;

@RestController
public class PatientController {

    @Autowired
    private IPatientService patientService;

    @PostMapping("/patient")
    public Patient createPatient(@RequestBody Patient patient) {
	return patientService.createPatient(patient);
    }

    @PutMapping("/patient")
    public Patient updatePatient(@RequestBody Patient patient) {
	return patientService.updatePatient(patient);
    }

    @DeleteMapping("/patient/{id}")
    public void deletePatient(@PathVariable(value = "id") Long id) {
	patientService.deletePatient(id);
    }

    @GetMapping("/patient")
    public List<Patient> getPatients() {
	return patientService.getPatients();
    }

    @GetMapping("/patient/{id}")
    public Patient getPatient(@PathVariable(value = "id") Long id) {
	return patientService.getPatient(id);
    }
}
