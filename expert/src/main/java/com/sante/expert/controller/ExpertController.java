package com.sante.expert.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sante.expert.dao.PatientDao;
import com.sante.expert.model.Risque;
import com.sante.expert.service.IDeclencheurService;
import com.sante.expert.service.IRegleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Expert Diabète")
public class ExpertController {

    @Autowired
    private IDeclencheurService declencheurService;

    @Autowired
    private IRegleService regleService;

    @Operation(summary = "Obtenir un niveau de risque")
    @ApiResponse(responseCode = "400")
    @ApiResponse(responseCode = "404")
    @PostMapping("/expert")
    public ResponseEntity<Risque> getRisque(@Valid @RequestBody PatientDao patientDao) {
	List<String> keywords = declencheurService.findKeywords();
	Risque risque = regleService.findRisque(patientDao, keywords);
	return new ResponseEntity<Risque>(risque, HttpStatus.OK);
    }
}
