package com.sante.clientui;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.sante.clientui.dao.PatientDao;
import com.sante.clientui.model.Note;
import com.sante.clientui.model.Patient;
import com.sante.clientui.model.PatientHistory;
import com.sante.clientui.model.Risque;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(value = org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    private OidcUser oidcUser;

    private Patient patientDatabase;
    private Patient patientDatabaseUpdated;
    private Patient patientDatabaseNotValid;
    private Patient patient;
    private Patient patientNotValid;

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private LocalDateTime creationDate = LocalDateTime.parse("04/12/1957 11:10:07", dateTimeFormatter);
    private LocalDateTime creationDateNoteAdd = LocalDateTime.parse("04/12/1997 11:10:07", dateTimeFormatter);
    private LocalDateTime creationDateNoteUpdated = LocalDateTime.parse("04/12/1977 11:10:07", dateTimeFormatter);

    private Note note = new Note(creationDate, "Info");
    private Note noteAdd = new Note(creationDateNoteAdd, "Info note add");
    private Note noteUpdated = new Note(creationDate, "Info note updated");

    private PatientHistory patientHistoryDatabase = null;
    private PatientHistory patientHistoryDatabaseUpdated = null;
    private PatientHistory patientHistoryNoDatabase = null;
    private PatientHistory patientHistoryDatabaseWithoutNotes = null;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private LocalDate dateNaissance = LocalDate.parse("04/12/1957", formatter);

    private PatientDao patientDaoDatabase;

    String[] totalAuthoritiesString = { "ROLE_GATEWAY_USER", "ROLE_PATIENT_HISTORY_USER", "ROLE_PATIENT_USER",
	    "ROLE_EXPERT_USER" };

    String[] totalRolesString = { "GATEWAY_USER", "PATIENT_HISTORY_USER", "PATIENT_USER",
	    "EXPERT_USER" };

    List<GrantedAuthority> totalAuthorities = Arrays.stream(totalAuthoritiesString)
	    .map(a -> (GrantedAuthority) new SimpleGrantedAuthority(a)).toList();

    @BeforeEach
    public void beforeEach() {
	patient = new Patient(null, "Tristan", "Mokobodzki", dateNaissance, "M", "1 rue du Louvre, PARIS",
		"06 01 02 08 09");

	patientNotValid = new Patient(null, "Tristan", "Mokobodzki", dateNaissance, "M", "1 rue du Louvre, PARIS",
		"06 01 02 08");

	patientDatabase = new Patient(1L, "Tristan", "Mokobodzki", dateNaissance, "M", "1 rue du Louvre, PARIS",
		"06 01 02 08 09");

	patientDatabaseUpdated = new Patient(1L, "Tristan", "Mokobodzki", dateNaissance, "M", "2 rue du Louvre, PARIS",
		"06 01 02 08 09");

	patientDatabaseNotValid = new Patient(1L, "Tristan", "Mokobodzki", dateNaissance, "M", "1 rue du Louvre, PARIS",
		"06 01 02 08");

	patientHistoryDatabase = new PatientHistory();
	patientHistoryDatabase.setId(1L);
	patientHistoryDatabase.getNotes().add(note);

	patientHistoryDatabaseUpdated = new PatientHistory();
	patientHistoryDatabaseUpdated.setId(1L);
	patientHistoryDatabaseUpdated.getNotes().add(noteUpdated);

	patientHistoryNoDatabase = new PatientHistory();
	patientHistoryNoDatabase.getNotes().add(noteAdd);

	patientHistoryDatabaseWithoutNotes = new PatientHistory();
	patientHistoryDatabaseWithoutNotes.setId(1L);

	patientDaoDatabase = new PatientDao(patientDatabase, patientHistoryDatabase);
	oidcUser = null;
    }

    @AfterEach
    public void afterEach() {
	oidcUser = null;
    }

    @Test
    @Order(-100)
    public void getTokens() {

	String[] tokens = Util.getTokens();
	for (String token : tokens) {
	    assertNotNull(token);
	}
    }

    @Test
    public void deleteAllPatient() throws Exception {

	if (oidcUser == null) {
	    oidcUser = Util.getOidcUser(oAuth2AuthorizedClientService, clientRegistrationRepository, "total");
	}

	mockMvc
		.perform(MockMvcRequestBuilders.get("/deleteAllPatient")
			.with(oidcLogin().oidcUser(oidcUser))
			.with(csrf()))
		.andExpect(status().is(302))
		.andExpect(model().attributeDoesNotExist("error_delete_all_patient"));
    }

    @Test
    public void getPatients() throws Exception {

	if (oidcUser == null) {
	    oidcUser = Util.getOidcUser(oAuth2AuthorizedClientService, clientRegistrationRepository, "total");
	}

	mockMvc
		.perform(MockMvcRequestBuilders.get("/")
			.with(oidcLogin().oidcUser(oidcUser)))
		.andExpect(status().is(200))
		.andExpect(model().attributeExists("patients"))
		.andExpect(model().attributeDoesNotExist("error_get_list_patient"))
		.andExpect(model().attributeDoesNotExist("error_get_list_patient_access_forbidden"))
		.andExpect(view().name("home"));

    }

    // Verification d'un patient : on suppose que la liste des patients est formée
    // d'un seul élément (ou de 0 élément si patient == null)
    public void verifyPatient(Patient patient) throws Exception {

	if (oidcUser == null) {
	    oidcUser = Util.getOidcUser(oAuth2AuthorizedClientService, clientRegistrationRepository, "total");
	}

	if (patient != null) {
	    mockMvc
		    .perform(MockMvcRequestBuilders.get("/")
			    .with(oidcLogin().oidcUser(oidcUser)))
		    .andExpect(status().is(200))
		    .andExpect(model().attributeExists("patients"))
		    .andExpect(model().attribute("patients", hasSize(1)))
		    .andExpect(model().attribute("patients", hasItem(Matchers.equalTo(patient))))
		    .andExpect(model().attributeDoesNotExist("error_get_list_patient"))
		    .andExpect(model().attributeDoesNotExist("error_get_list_patient_access_forbidden"))
		    .andExpect(view().name("home"));

	} else {
	    mockMvc
		    .perform(MockMvcRequestBuilders.get("/")
			    .with(oidcLogin().oidcUser(oidcUser)))
		    .andExpect(status().is(200))
		    .andExpect(model().attributeExists("patients"))
		    .andExpect(model().attribute("patients", hasSize(0)))
		    .andExpect(model().attributeDoesNotExist("error_get_list_patient"))
		    .andExpect(model().attributeDoesNotExist("error_get_list_patient_access_forbidden"))
		    .andExpect(view().name("home"));
	}

    }

    @Test
    public void savePatient() throws Exception {
	if (oidcUser == null) {
	    oidcUser = Util.getOidcUser(oAuth2AuthorizedClientService, clientRegistrationRepository, "total");
	}
	savePatient(true);
    }

    public void savePatient(boolean deleteAllPatient) throws Exception {

	if (oidcUser == null) {
	    oidcUser = Util.getOidcUser(oAuth2AuthorizedClientService, clientRegistrationRepository, "total");
	}

	// Suppression des patients
	if (deleteAllPatient) {
	    deleteAllPatient();
	}

	mockMvc
		.perform(MockMvcRequestBuilders.post("/save")
			.flashAttr("patient", patient)
			.with(oidcLogin().oidcUser(oidcUser))
			.with(csrf()))
		.andExpect(status().is(302))
		.andExpect(model().attributeDoesNotExist("error_add_patient"))
		.andExpect(model().attributeDoesNotExist("error_add_patient_access_forbidden"))
		.andExpect(view().name("redirect:/"));

	// Vérification du patient
	verifyPatient(patient);
    }

    @Test
    public void updatePatient() throws Exception {

	if (oidcUser == null) {
	    oidcUser = Util.getOidcUser(oAuth2AuthorizedClientService, clientRegistrationRepository, "total");
	}

	// Suppression des patients
	deleteAllPatient();

	// Création d'un patient
	savePatient(false);

	mockMvc
		.perform(MockMvcRequestBuilders.post("/update")
			.flashAttr("patient", patientDatabaseUpdated)
			.with(oidcLogin().oidcUser(oidcUser))
			.with(csrf()))
		.andExpect(status().is(302))
		.andExpect(model().attributeDoesNotExist("error_update_patient"))
		.andExpect(view().name("redirect:/"));

	// Vérification du patient
	verifyPatient(patientDatabaseUpdated);
    }

    @Test
    public void deletePatient_withoutPatientHistory() throws Exception {
	if (oidcUser == null) {
	    oidcUser = Util.getOidcUser(oAuth2AuthorizedClientService, clientRegistrationRepository, "total");
	}

	// Suppression des patients
	deleteAllPatient();

	// Création d'un patient
	savePatient(false);

	mockMvc
		.perform(MockMvcRequestBuilders.get("/delete/1")
			.with(oidcLogin().oidcUser(oidcUser))
			.with(csrf()))
		.andExpect(status().is(302))
		.andExpect(model().attributeDoesNotExist("error_delete_patient"))
		.andExpect(model().attributeDoesNotExist("error_delete_patient_history"))
		.andExpect(view().name("redirect:/"));

	// Vérification que le patient a bien été supprimé
	verifyPatient(null);
    }

    @Test
    public void deleteAllPatientHistory() throws Exception {

	if (oidcUser == null) {
	    oidcUser = Util.getOidcUser(oAuth2AuthorizedClientService, clientRegistrationRepository, "total");
	}

	mockMvc
		.perform(MockMvcRequestBuilders.get("/deleteAllPatientHistory")
			.with(oidcLogin().oidcUser(oidcUser))
			.with(csrf()))
		.andExpect(status().is(302))
		.andExpect(model().attributeDoesNotExist("error_delete_all_patient_history"));
    }

    @Test
    public void getPatientHistory() throws Exception {
	if (oidcUser == null) {
	    oidcUser = Util.getOidcUser(oAuth2AuthorizedClientService, clientRegistrationRepository, "total");
	}

	getPatientHistory(true, true, true);
    }

    public void getPatientHistory(boolean deleteAllPatient, boolean deletePatientHistory, boolean savePatientHistory)
	    throws Exception {

	// Suppression des patient + patient history
	if (deleteAllPatient) {
	    deleteAllPatient();
	}
	if (deletePatientHistory) {
	    deleteAllPatientHistory();
	}

	// Création d'un patient history (+ patient)
	if (savePatientHistory) {
	    savePatientHistory(false, false);
	}

	if (oidcUser == null) {
	    oidcUser = Util.getOidcUser(oAuth2AuthorizedClientService, clientRegistrationRepository, "total");
	}

	mockMvc
		.perform(MockMvcRequestBuilders.get("/notes/1")
			.with(oidcLogin().oidcUser(oidcUser))
			.with(csrf()))
		.andExpect(status().is(200))
		.andExpect(model().attributeExists("patient"))
		.andExpect(model().attributeExists("patientHistory"))
		.andExpect(view().name("note_home"));
    }

    // Verification d'un patient : on suppose que l'on a un historique de patient
    public void verifyPatientHistory(PatientHistory patientHistory) throws Exception {

	if (oidcUser == null) {
	    oidcUser = Util.getOidcUser(oAuth2AuthorizedClientService, clientRegistrationRepository, "total");
	}

	if (patientHistory != null) {
	    mockMvc
		    .perform(MockMvcRequestBuilders.get("/notes/1")
			    .with(oidcLogin().oidcUser(oidcUser)))
		    .andExpect(status().is(200))
		    .andExpect(model().attributeExists("patientHistory"))
		    .andExpect(model().attribute("patientHistory", Matchers.equalTo(patientHistory)))
		    .andExpect(view().name("note_home"));

	}
    }

    @Test
    public void savePatientHistory() throws Exception {

	if (oidcUser == null) {
	    oidcUser = Util.getOidcUser(oAuth2AuthorizedClientService, clientRegistrationRepository, "total");
	}

	savePatientHistory(true, true);

	// Vérification du patient history
	verifyPatientHistory(patientHistoryDatabase);
    }

    public void savePatientHistory(boolean deleteAllPatient, boolean deleteAllPatientHistory) throws Exception {
	if (oidcUser == null) {
	    oidcUser = Util.getOidcUser(oAuth2AuthorizedClientService, clientRegistrationRepository, "total");
	}

	// Suppression des patients et des patient history
	if (deleteAllPatient) {
	    deleteAllPatient();
	}
	if (deleteAllPatientHistory) {
	    deleteAllPatientHistory();
	}

	// Création d'un patient
	savePatient(false);

	mockMvc.perform(MockMvcRequestBuilders.post("/note/save")
		.flashAttr("patientHistory", patientHistoryDatabaseWithoutNotes)
		.flashAttr("note", note).with(oidcLogin().oidcUser(oidcUser))
		.with(csrf()))
		.andExpect(status().is(200))
		.andExpect(model().attributeExists("patient"))
		.andExpect(model().attributeExists("patientHistory"))
		.andExpect(view().name("note_home"));

    }

    @Test
    public void updateNote() throws Exception {

	if (oidcUser == null) {
	    oidcUser = Util.getOidcUser(oAuth2AuthorizedClientService, clientRegistrationRepository, "total");
	}

	// Suppression des patients et des patients history
	deleteAllPatient();
	deleteAllPatientHistory();

	// Création d'un patient history et d'un patient
	savePatientHistory(false, false);

	// Update de la note
	mockMvc
		.perform(MockMvcRequestBuilders.post("/note/update")
			.flashAttr("patientHistory", patientHistoryDatabase).flashAttr("note", noteUpdated)
			.with(oidcLogin().oidcUser(oidcUser))
			.with(csrf()))
		.andExpect(status().is(302))
		.andExpect(model().attributeDoesNotExist("error_update_note"))
		.andExpect(view().name("redirect:/homenotes/1"));

	// Vérification du patient history
	verifyPatientHistory(patientHistoryDatabaseUpdated);
    }

    @Test
    public void deleteNote() throws Exception {

	if (oidcUser == null) {
	    oidcUser = Util.getOidcUser(oAuth2AuthorizedClientService, clientRegistrationRepository, "total");
	}

	// Suppression des patients et des patients history
	deleteAllPatient();
	deleteAllPatientHistory();

	// Création d'un patient history et d'un patient
	savePatientHistory(false, false);

	// Suppression de la note
	mockMvc
		.perform(MockMvcRequestBuilders.get("/note/delete/1/" + creationDate.toString()))
		.andExpect(status().is(302));

	// Vérification du patient history
	verifyPatientHistory(patientHistoryDatabaseWithoutNotes);

    }

    @Test
    public void showRisqueForm() throws Exception {
	if (oidcUser == null) {
	    oidcUser = Util.getOidcUser(oAuth2AuthorizedClientService, clientRegistrationRepository, "total");
	}

	// Suppression des patients et des patients history
	deleteAllPatient();
	deleteAllPatientHistory();

	// Création d'un patient history et d'un patient
	savePatientHistory(false, false);

	Risque risque = Risque.AUCUN_RISQUE;

	mockMvc
		.perform(MockMvcRequestBuilders.get("/risque/1").with(oidcLogin().oidcUser(oidcUser)))
		// .andExpect(status().is(200))
		.andExpect(model().attributeExists("patientDao"))
		.andExpect(model().attributeExists("risque"))
		.andExpect(model().attribute("risque", Matchers.equalTo(risque)))
		.andExpect(view().name("risque"));

    }
}
