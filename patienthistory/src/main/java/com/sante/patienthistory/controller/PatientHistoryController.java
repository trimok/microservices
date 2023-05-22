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

@Tag(name = "Historique du patient")
@RestController
public class PatientHistoryController {
    @Autowired
    private IPatientHistoryService patientHistoryService;

    @Operation(summary = "Ajouter une note (création d'un historique de patient)")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "201")
    @ApiResponse(responseCode = "400")
    @PostMapping("/patientHistory")
    public ResponseEntity<PatientHistory> createPatientHistory(@Valid @RequestBody PatientHistory patientHistory) {
	PatientHistory patientHistoryAdded = patientHistoryService.createPatientHistory(patientHistory);
	return new ResponseEntity<PatientHistory>(patientHistoryAdded, HttpStatus.CREATED);
    }

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

    @Operation(summary = "Obtenir un historique de patient")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "404")
    @GetMapping("/patientHistory/{id}")
    public ResponseEntity<PatientHistory> getPatientHistory(@PathVariable(value = "id") Long id) {
	PatientHistory patientHistory = patientHistoryService.getPatientHistory(id);
	return new ResponseEntity<PatientHistory>(patientHistory, HttpStatus.OK);
    }

    @Operation(summary = "Modifier une note")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "400")
    @PutMapping("/patientHistory/note")
    public ResponseEntity<PatientHistory> updateNote(@Valid @RequestBody PatientHistory patientHistory) {
	PatientHistory patientHistoryUpdated = patientHistoryService.updateNote(patientHistory);
	return new ResponseEntity<PatientHistory>(patientHistoryUpdated, HttpStatus.OK);
    }

    @Operation(summary = "Supprimer une note")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "400")
    @DeleteMapping("/patientHistory/note")
    public ResponseEntity<PatientHistory> deleteNote(@Valid @RequestBody PatientHistory patientHistory) {
	PatientHistory patientHistoryUpdated = patientHistoryService.deleteNote(patientHistory);
	return new ResponseEntity<PatientHistory>(patientHistoryUpdated, HttpStatus.OK);
    }

    @Operation(summary = "Supprimer un historique de patient")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "404")
    @DeleteMapping("/patientHistory/{id}")
    public ResponseEntity<PatientHistory> deletePatientHistory(@PathVariable(value = "id") Long id) {
	patientHistoryService.deletePatientHistory(id);
	return ResponseEntity.ok().build();
    }
}
