package com.sante.patienthistory.controller;

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

import com.sante.patienthistory.model.PatientHistory;
import com.sante.patienthistory.service.IPatientHistoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * @author trimok
 *
 *
 *         Le contrôleur de l'application
 */
@Tag(name = "Historique du patient")
@RestController
public class PatientHistoryController {
    /**
     * Le service correspondant
     */
    @Autowired
    private IPatientHistoryService patientHistoryService;

    /**
     * Ajouter une note (création d'un historique de patient)
     * 
     * @param patientHistory : historique du patient
     * @return : historique du patient
     */
    @Operation(summary = "Ajouter une note (création d'un historique de patient)")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "201")
    @ApiResponse(responseCode = "400")
    @PostMapping("/patientHistory")
    public ResponseEntity<PatientHistory> createPatientHistory(@Valid @RequestBody PatientHistory patientHistory) {
	PatientHistory patientHistoryAdded = patientHistoryService.createPatientHistory(patientHistory);
	return new ResponseEntity<PatientHistory>(patientHistoryAdded, HttpStatus.CREATED);
    }

    /**
     * Ajouter une note (modification d'un historique de patient)
     * 
     * @param patientHistory : historique du patient
     * @return : historique du patient
     */
    @Operation(summary = "Ajouter une note (modification d'un historique de patient)")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "400")
    @PutMapping("/patientHistory")
    public ResponseEntity<PatientHistory> updatePatientHistory(@Valid @RequestBody PatientHistory patientHistory) {
	PatientHistory patientHistoryUpdated = patientHistoryService.updatePatientHistory(patientHistory);
	return new ResponseEntity<PatientHistory>(patientHistoryUpdated, HttpStatus.OK);
    }

    /**
     * 
     * Ajouter une note (Création et/ou modification d'un historique de patient)
     * 
     * @param patientHistory : historique du patient
     * @return : historique du patient
     */
    @Operation(summary = "Ajouter une note (Création et/ou modification d'un historique de patient)")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "400")
    @ApiResponse(responseCode = "201")
    @PostMapping("/patientHistory/add")
    public ResponseEntity<PatientHistory> createUpdatePatientHistory(
	    @Valid @RequestBody PatientHistory patientHistory) {
	PatientHistory patientHistoryUpdated = patientHistoryService.createUpdatePatientHistory(patientHistory);
	return new ResponseEntity<PatientHistory>(patientHistoryUpdated, HttpStatus.OK);
    }

    /**
     * Obtenir un historique de patient
     * 
     * @param id : l'id du patient
     * @return : historique du patient
     */
    @Operation(summary = "Obtenir un historique de patient")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "404")
    @GetMapping("/patientHistory/{id}")
    public ResponseEntity<PatientHistory> getPatientHistory(@PathVariable(value = "id") Long id) {
	PatientHistory patientHistory = patientHistoryService.getPatientHistory(id);
	return new ResponseEntity<PatientHistory>(patientHistory, HttpStatus.OK);
    }

    /**
     * Modifier une note
     * 
     * @param patientHistory : l'historique du patient (id), avec une seule note (la
     *                       note à modifier)
     * @return : l'historique du patient complet
     */
    @Operation(summary = "Modifier une note")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "400")
    @PutMapping("/patientHistory/note")
    public ResponseEntity<PatientHistory> updateNote(@Valid @RequestBody PatientHistory patientHistory) {
	PatientHistory patientHistoryUpdated = patientHistoryService.updateNote(patientHistory);
	return new ResponseEntity<PatientHistory>(patientHistoryUpdated, HttpStatus.OK);
    }

    /**
     * Supprimer une note
     * 
     * @param patientHistory : l'historique du patient (id), avec une seule note (la
     *                       note à supprimer)
     * @return : l'historique du patient complet
     */
    @Operation(summary = "Supprimer une note")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "400")
    @DeleteMapping("/patientHistory/note")
    public ResponseEntity<PatientHistory> deleteNote(@Valid @RequestBody PatientHistory patientHistory) {
	PatientHistory patientHistoryUpdated = patientHistoryService.deleteNote(patientHistory);
	return new ResponseEntity<PatientHistory>(patientHistoryUpdated, HttpStatus.OK);
    }

    /**
     * Suppression d'un historique de patient
     * 
     * @param id : l'id de l'historique de patient à supprimer
     * @return : ok
     */
    @Operation(summary = "Supprimer un historique de patient")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "404")
    @DeleteMapping("/patientHistory/{id}")
    public ResponseEntity<PatientHistory> deletePatientHistory(@PathVariable(value = "id") Long id) {
	patientHistoryService.deletePatientHistory(id);
	return ResponseEntity.ok().build();
    }

    /**
     * Suppression de tous les patients
     * 
     * @return : ok
     */
    @Operation(summary = "Supprimer tous les historiques de patients")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "404")
    @DeleteMapping("/patientHistory/admin/all")

    public ResponseEntity<PatientHistory> deleteAllPatientHistory() {
	patientHistoryService.deleteAllPatientHistory();
	return ResponseEntity.ok().build();
    }
}
