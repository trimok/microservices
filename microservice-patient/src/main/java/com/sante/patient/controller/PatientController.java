package com.sante.patient.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sante.patient.model.Patient;
import com.sante.patient.service.IPatientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * @author trimok
 *
 *         Contrôleur de l'application
 *
 */
@RestController
@Tag(name = "Patients")
public class PatientController {

    /**
     * le service patient
     */
    @Autowired
    private IPatientService patientService;

    /**
     * Ajout d'un patient
     * 
     * @param patient : le patient à ajouter
     * @return : le patient ajouté (avec son id)
     */
    @Operation(summary = "Ajouter un patient")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "201")
    @ApiResponse(responseCode = "400")
    @ApiResponse(responseCode = "409")
    @PostMapping("/patient")
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody Patient patient) {
	Patient patientAdded = patientService.createPatient(patient);
	return new ResponseEntity<Patient>(patientAdded, HttpStatus.CREATED);
    }

    /**
     * Modification d'un patient
     * 
     * @param patient : le patient à modifier
     * @return : le patient modifié
     */
    @Operation(summary = "Modifier un patient")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "400")
    @PutMapping("/patient")
    public ResponseEntity<Patient> updatePatient(@Valid @RequestBody Patient patient) {
	Patient patientUpdated = patientService.updatePatient(patient);
	return new ResponseEntity<Patient>(patientUpdated, HttpStatus.OK);
    }

    /**
     * Obtention d'un patient
     * 
     * @param id : l'id du patient
     * @return : le patient
     */
    @Operation(summary = "Obtenir un patient")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "404")
    @GetMapping("/patient/{id}")
    public ResponseEntity<Patient> getPatient(@PathVariable(value = "id") Long id) {
	Patient patient = patientService.getPatient(id);
	return new ResponseEntity<Patient>(patient, HttpStatus.OK);
    }

    /**
     * Obtention de la liste des patients
     * 
     * @return : la liste des patients
     */
    @Operation(summary = "Obtenir la liste des patients")
    @ApiResponse(responseCode = "200")
    @GetMapping("/patient")
    public ResponseEntity<List<Patient>> getPatients() {
	List<Patient> patients = patientService.getPatients();
	return new ResponseEntity<List<Patient>>(patients, HttpStatus.OK);
    }

    /**
     * Suppression d'un patient
     * 
     * @param id : l'id du patient
     * @return : ok
     */
    @Operation(summary = "Supprimer un patient")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "404")
    @DeleteMapping("/patient/{id}")
    public ResponseEntity<Patient> deletePatient(@PathVariable(value = "id") Long id) {
	patientService.deletePatient(id);
	return ResponseEntity.ok().build();
    }

    /**
     * Suppression de tous les patients
     * 
     * @return : ok
     */
    // @Operation(summary = "Supprimer tous les patients")
    // @ApiResponse(responseCode = "200")
    // @ApiResponse(responseCode = "404")
    @DeleteMapping("/patient/admin/all")
    public ResponseEntity<Patient> deleteAllPatient() {
	patientService.deleteAllPatient();
	return ResponseEntity.ok().build();
    }
}
