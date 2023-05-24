package com.sante.clientui.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.sante.clientui.model.Patient;

@Service
@FeignClient(name = "patient", url = "${patient.url}")
public interface PatientService {

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
}
