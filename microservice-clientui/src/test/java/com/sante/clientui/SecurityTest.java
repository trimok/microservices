package com.sante.clientui;

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
public class SecurityTest {

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
    public void getPatients_access_forbidden() throws Exception {

	if (oidcUser == null) {
	    oidcUser = Util.getOidcUser(oAuth2AuthorizedClientService, clientRegistrationRepository, "nogateway");
	}

	mockMvc
		.perform(MockMvcRequestBuilders.get("/")
			.with(oidcLogin().oidcUser(oidcUser)))
		.andExpect(status().is(200))
		.andExpect(model().attributeExists("error_get_list_patient_access_forbidden"))
		.andExpect(view().name("home"));

    }

    @Test
    public void getPatientHistory_access_forbidden()
	    throws Exception {

	if (oidcUser == null) {
	    oidcUser = Util.getOidcUser(oAuth2AuthorizedClientService, clientRegistrationRepository, "patient");
	}

	mockMvc
		.perform(MockMvcRequestBuilders.get("/notes/1")
			.with(oidcLogin().oidcUser(oidcUser))
			.with(csrf()))
		.andExpect(status().is(302))
		.andExpect(model().attributeExists("error_get_patient_history_access_forbidden"));
    }

    @Test
    public void showRisqueForm_access_forbidden() throws Exception {
	if (oidcUser == null) {
	    oidcUser = Util.getOidcUser(oAuth2AuthorizedClientService, clientRegistrationRepository, "history");
	}
	Risque risque = Risque.AUCUN_RISQUE;

	mockMvc
		.perform(MockMvcRequestBuilders.get("/risque/1").with(oidcLogin().oidcUser(oidcUser)))
		.andExpect(status().is(302))
		.andExpect(model().attributeExists("error_get_risque_access_forbidden"));

    }
}
