package com.sante.expert;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TreeSet;

import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sante.expert.dao.Note;
import com.sante.expert.dao.Patient;
import com.sante.expert.dao.PatientDao;
import com.sante.expert.dao.PatientHistory;
import com.sante.expert.model.Risque;

public class Util {

    public static List<String> getKeywords() {
	return List.of("Fumeur", "Poids", "Anormal", "Cholestérol", "Vertige", "Rechute", "Réaction", "Anticorps",
		"Hémoglobine A1C", "Microalbumine", "Taille");
    }

    public static class Mapper extends ObjectMapper {
	/**
	 * 
	 */
	private static final long serialVersionUID = 10L;

	/**
	 * 
	 */
	public Mapper() {
	    // Management for the LocalDate serialization / deserialization
	    this.registerModule(new JavaTimeModule());
	}
    }

    public static Mapper mapper = new Mapper();

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static LocalDate dateNaissance = LocalDate.parse("04/12/1957", formatter);

    private static Patient patientDatabase;
    private static PatientHistory patientHistoryDatabase;

    private static PatientDao patientDao;

    static {
	patientDatabase = new Patient(1L, "Tristan", "Mokobodzki", dateNaissance, "M", "1 rue du Louvre, PARIS",
		"06 01 02 08 09");

	patientHistoryDatabase = new PatientHistory();
	patientHistoryDatabase.setId(1L);

	patientDao = new PatientDao(patientDatabase, patientHistoryDatabase);
    }

    public static Risque getRisqueFromMvcResult(MvcResult mvcResult)
	    throws UnsupportedEncodingException, JsonMappingException, JsonProcessingException {
	String json = mvcResult.getResponse().getContentAsString();
	Risque risque = mapper.readValue(json, Risque.class);
	return risque;
    }

    public static PatientDao getPatientDao(String genre, int age, int nbDeclencheur) {
	LocalDate dateNaissance = LocalDate.now().minusYears(age);
	patientDao.getPatient().setGenre(genre);
	patientDao.getPatient().setDateNaissance(dateNaissance);
	patientDao.getPatientHistory().setNotes(new TreeSet<>());
	List<String> keywords = getKeywords();
	for (int i = 0; i < nbDeclencheur; i++) {
	    Note note = new Note(LocalDateTime.now(), keywords.get(i));
	    patientDao.getPatientHistory().getNotes().add(note);
	}
	return patientDao;
    }
}
