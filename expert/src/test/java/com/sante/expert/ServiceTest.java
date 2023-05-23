package com.sante.expert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sante.expert.dao.Note;
import com.sante.expert.dao.Patient;
import com.sante.expert.dao.PatientDao;
import com.sante.expert.dao.PatientHistory;
import com.sante.expert.exception.DeclencheurNotFoundException;
import com.sante.expert.exception.RisqueNotFoundException;
import com.sante.expert.model.Risque;
import com.sante.expert.repository.DeclencheurRepository;
import com.sante.expert.repository.RegleRepository;
import com.sante.expert.service.DeclencheurService;
import com.sante.expert.service.IDeclencheurService;
import com.sante.expert.service.IRegleService;
import com.sante.expert.service.RegleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(value = org.junit.jupiter.api.MethodOrderer.MethodName.class)
public class ServiceTest {

    private IRegleService regleService;
    private IDeclencheurService declencheurService;

    @Mock
    private RegleRepository regleRepository;

    @Mock
    private DeclencheurRepository declencheurRepository;

    private List<String> keywords = Util.getKeywords();

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private LocalDate dateNaissance = LocalDate.parse("04/12/1957", formatter);

    private DateTimeFormatter formatter_2 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private LocalDateTime creationDate = LocalDateTime.parse("04/12/1957 11:10:07", formatter_2);

    private Note note = new Note(creationDate, "Info");

    private Patient patientDatabase;
    private PatientHistory patientHistoryDatabase;

    private PatientDao patientDao;

    @BeforeAll()
    public void beforeAll() {
    }

    @BeforeEach
    public void beforeEach() {
	regleService = new RegleService(regleRepository);
	declencheurService = new DeclencheurService(declencheurRepository);

	patientDatabase = new Patient(1L, "Tristan", "Mokobodzki", dateNaissance, "M", "1 rue du Louvre, PARIS",
		"06 01 02 08 09 ");

	patientHistoryDatabase = new PatientHistory();
	patientHistoryDatabase.setId(1L);
	patientHistoryDatabase.getNotes().add(note);

	patientDao = new PatientDao(patientDatabase, patientHistoryDatabase);
    }

    /*********************** DECLENCHEUR SERVICE **************************/

    @Test
    public void declencheurService_findKeywords() {

	when(declencheurRepository.findKeywords()).thenReturn(keywords);
	List<String> keywordsFound = declencheurService.findKeywords();
	verify(declencheurRepository, times(1)).findKeywords();
	assertNotNull(keywordsFound);
	assertThat(keywordsFound.size()).isEqualTo(keywords.size());
    }

    @Test
    public void declencheurServic_DeclencheurNotFoundException_1() {

	when(declencheurRepository.findKeywords()).thenThrow(new DeclencheurNotFoundException("ACTION"));
	Executable action = () -> declencheurService.findKeywords();
	assertThrows(DeclencheurNotFoundException.class, action);
	verify(declencheurRepository, times(1)).findKeywords();
    }

    @Test
    public void declencheurServic_DeclencheurNotFoundException_2() {

	when(declencheurRepository.findKeywords()).thenReturn(new ArrayList<String>());
	Executable action = () -> declencheurService.findKeywords();
	assertThrows(DeclencheurNotFoundException.class, action);
	verify(declencheurRepository, times(1)).findKeywords();
    }

    /*********************** REGLE SERVICE **************************/

    @Test
    public void regleService_findRisque() {

	when(regleRepository.findRisque(any(int.class), any(String.class), any(int.class)))
		.thenReturn(Risque.APPARITION_PRECOCE.ordinal());
	Risque risque = regleService.findRisque(patientDao, keywords);
	verify(regleRepository, times(1)).findRisque(any(int.class), any(String.class), any(int.class));
	log.info("Risque : " + risque);
	assertNotNull(risque);
    }

    @Test
    public void regleService_RisqueNotFoundException() {

	when(regleRepository.findRisque(any(int.class), any(String.class), any(int.class)))
		.thenThrow(new RisqueNotFoundException("RISQUE_NOT_FOUND"));
	Executable action = () -> regleService.findRisque(patientDao, keywords);
	assertThrows(RisqueNotFoundException.class, action);
    }
}
