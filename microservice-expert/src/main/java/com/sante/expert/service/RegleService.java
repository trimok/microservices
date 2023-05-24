package com.sante.expert.service;

import static com.sante.expert.constants.Constants.RISQUE_NOT_FOUND;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sante.expert.dao.Patient;
import com.sante.expert.dao.PatientDao;
import com.sante.expert.exception.RisqueNotFoundException;
import com.sante.expert.model.Risque;
import com.sante.expert.repository.RegleRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * @author trimok
 *
 *         Le service qui permet de calculer le risque
 */
@Service
@Slf4j
public class RegleService implements IRegleService {

    /**
     * Le repository règle
     */
    @Autowired
    RegleRepository regleRepository;

    @Autowired
    public RegleService(RegleRepository regleRepository) {
	super();
	this.regleRepository = regleRepository;
    }

    /**
     * @param string : la chaine dont on doit enlever les accents
     * @return : la chaine avec les accents enlevés
     */
    public static String removeDiacriticalMarks(String string) {
	return Normalizer.normalize(string, Form.NFD)
		.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    /**
     * Permet de calculer le risque
     * 
     * @param patientDao : les données patient
     * @param keywords   : les mots-clés médicaux
     * @return : le risque calculé
     */
    @Override
    public Risque findRisque(PatientDao patientDao, List<String> keywords) {
	// Données age et genre
	Patient patient = patientDao.getPatient();
	int age = Period.between(patient.getDateNaissance(), LocalDate.now()).getYears();
	String genre = patient.getGenre();

	// Concaténation des notes + lower case
	String noteAll = patientDao.getPatientHistory().getNotes().stream().map(n -> n.getInfo())
		.collect(Collectors.joining(" ")).toLowerCase();
	// On enlève les accents
	String noteAllWithoutAccent = removeDiacriticalMarks(noteAll);

	// Comptage du nombre de termes médicaux
	int numKeywords = (int) keywords.stream().filter(k -> noteAllWithoutAccent.contains(k)).count();

	// Log
	log.info("age : " + age + ", genre : " + patient.getGenre() + ", numKeywords: " + numKeywords);

	// Obtention du risque
	int risqueInt = 0;

	try {
	    risqueInt = regleRepository.findRisque(numKeywords, genre, age);
	} catch (Exception e) {
	    throw new RisqueNotFoundException(RISQUE_NOT_FOUND);
	}

	return Risque.values()[risqueInt];
    }
}
