package com.sante.expert;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.sante.expert.dao.PatientDao;
import com.sante.expert.model.Risque;
import com.sante.expert.service.IDeclencheurService;
import com.sante.expert.service.IRegleService;

@ContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(value = org.junit.jupiter.api.MethodOrderer.MethodName.class)
public class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    IRegleService regleService;

    @Autowired
    IDeclencheurService declencheurService;
    private List<String> keywords = Util.getKeywords();

    private final static String ROLE_EXPERT_USER = "ROLE_EXPERT_USER";

    @BeforeEach
    public void beforeEach() {
    }

    @AfterEach
    public void afterEach() {
    }

    /*************************** RISQUE ************************************/
    /**
     * Arguments Provider for the method findRisque
     * 
     * @return : the list of arguments
     */
    public static Stream<Arguments> findRisqueParametersProvider() {
	// GIVEN
	return Stream.of(
		// AGE, GENRE, NBDECLENCHEUR, RISQUE
		Arguments.arguments(25, "M", 0, Risque.AUCUN_RISQUE));
    }

    /************** TEST INVALID TOKEN **********************/
    @ParameterizedTest(name = "Test invalid token : âge : {0}, genre : {1}, Nb declencheurs : {2}, devrait être : {3}")
    @MethodSource("findRisqueParametersProvider")
    public void testInvalidToken(int age, String genre, int nbDeclencheur, Risque expectedRisque) throws Exception {

	// WHEN
	PatientDao patientDao = Util.getPatientDao(genre, age, nbDeclencheur);

	mockMvc
		.perform(MockMvcRequestBuilders.post("/expert")
			.with(jwt().authorities(new SimpleGrantedAuthority("INVALID_TOKEN")))
			.content(Util.mapper.writeValueAsString(patientDao))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(403)).andReturn();
    }
}
