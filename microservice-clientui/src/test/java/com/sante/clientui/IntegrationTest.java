package com.sante.clientui;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.sante.clientui.dao.PatientDao;
import com.sante.clientui.model.Note;
import com.sante.clientui.model.Patient;
import com.sante.clientui.model.PatientHistory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(value = org.junit.jupiter.api.MethodOrderer.MethodName.class)
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private Patient patientDatabase;
    private Patient patientDatabaseNotValid;
    private Patient patient;
    private Patient patientNotValid;

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private LocalDateTime creationDate = LocalDateTime.parse("04/12/1957 11:10:07", dateTimeFormatter);
    private LocalDateTime creationDateNoteAdd = LocalDateTime.parse("04/12/1997 11:10:07", dateTimeFormatter);

    private Note note = new Note(creationDate, "Info");
    private Note noteAdd = new Note(creationDateNoteAdd, "Info note add");

    private PatientHistory patientHistoryDatabase = null;
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

	patientDatabaseNotValid = new Patient(1L, "Tristan", "Mokobodzki", dateNaissance, "M", "1 rue du Louvre, PARIS",
		"06 01 02 08");

	patientHistoryDatabase = new PatientHistory();
	patientHistoryDatabase.setId(1L);
	patientHistoryDatabase.getNotes().add(note);

	patientHistoryNoDatabase = new PatientHistory();
	patientHistoryNoDatabase.getNotes().add(noteAdd);

	patientHistoryDatabaseWithoutNotes = new PatientHistory();
	patientHistoryDatabaseWithoutNotes.setId(1L);

	patientDaoDatabase = new PatientDao(patientDatabase, patientHistoryDatabase);

    }

    @Test
    public void getPatients() throws Exception {

	String token = Util.getTotalToken();
	Map<String, Object> claims = new HashMap<>();
	claims.put("sub", "total");
	OidcIdToken oidcToken = new OidcIdToken(token, null, null, claims);
	OidcUserAuthority authority = new OidcUserAuthority(oidcToken);
	List<GrantedAuthority> listAuthorities = Arrays.asList(authority, new SimpleGrantedAuthority("SCOPE_openid"));
	OidcUser oidcUser = new DefaultOidcUser(listAuthorities, oidcToken, "sub");

	mockMvc
		.perform(MockMvcRequestBuilders.get("/")
			.with(oidcLogin().oidcUser(oidcUser)))
		.andExpect(status().is(200))
		.andExpect(model().attributeExists("patients"))
		.andExpect(model().attributeDoesNotExist("error_get_list_patient"))
		.andExpect(model().attributeDoesNotExist("error_get_list_patient_access_forbidden"))
		.andExpect(view().name("home"));

    }
}
