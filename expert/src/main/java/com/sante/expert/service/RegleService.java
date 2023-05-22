package com.sante.expert.service;

import static com.sante.expert.constants.Constants.RISQUE_NOT_FOUND;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sante.expert.dao.Patient;
import com.sante.expert.dao.PatientDao;
import com.sante.expert.exception.RisqueNotFoundException;
import com.sante.expert.model.Regle;
import com.sante.expert.repository.RegleRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RegleService implements IRegleService {

    @Autowired
    RegleRepository regleRepository;

    @Override
    public Regle.Risque findRisque(PatientDao patientDao, List<String> keywords) {
	// Données age et genre
	Patient patient = patientDao.getPatient();
	int age = Period.between(patient.getDateNaissance(), LocalDate.now()).getYears();
	String genre = patient.getGenre();

	// Concaténation des notes
	String noteAll = patientDao.getPatientHistory().getNotes().stream().map(n -> n.getInfo())
		.collect(Collectors.joining(""));

	// Comptage du nombre de termes médicaux
	int numKeywords = (int) keywords.stream().filter(k -> noteAll.contains(k)).count();

	// Log
	log.info("age : " + age + ", genre : " + patient.getGenre() + ", numKeywords: " + numKeywords);

	// Obtention du risque
	int risqueInt = 0;

	try {
	    risqueInt = regleRepository.findRisque(numKeywords, genre, age);
	} catch (Exception e) {
	    throw new RisqueNotFoundException(RISQUE_NOT_FOUND);
	}

	return Regle.Risque.values()[risqueInt];
    }
}
