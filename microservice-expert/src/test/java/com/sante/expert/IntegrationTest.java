package com.sante.expert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.sante.expert.dao.PatientDao;
import com.sante.expert.model.Risque;
import com.sante.expert.service.IDeclencheurService;
import com.sante.expert.service.IRegleService;

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

    @Autowired
    IRegleService regleService;

    @Autowired
    IDeclencheurService declencheurService;
    private List<String> keywords = Util.getKeywords();

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

		Arguments.arguments(25, "M", 0, Risque.AUCUN_RISQUE),
		Arguments.arguments(25, "M", 1, Risque.AUCUN_RISQUE),
		Arguments.arguments(25, "M", 2, Risque.AUCUN_RISQUE),

		Arguments.arguments(25, "F", 0, Risque.AUCUN_RISQUE),
		Arguments.arguments(25, "F", 1, Risque.AUCUN_RISQUE),
		Arguments.arguments(25, "F", 2, Risque.AUCUN_RISQUE),
		Arguments.arguments(25, "F", 3, Risque.AUCUN_RISQUE),

		Arguments.arguments(45, "M", 0, Risque.AUCUN_RISQUE),
		Arguments.arguments(45, "M", 1, Risque.AUCUN_RISQUE),

		Arguments.arguments(45, "F", 0, Risque.AUCUN_RISQUE),
		Arguments.arguments(45, "F", 1, Risque.AUCUN_RISQUE),

		Arguments.arguments(45, "M", 2, Risque.RISQUE_LIMITE),
		Arguments.arguments(45, "M", 3, Risque.RISQUE_LIMITE),
		Arguments.arguments(45, "M", 4, Risque.RISQUE_LIMITE),
		Arguments.arguments(45, "M", 5, Risque.RISQUE_LIMITE),

		Arguments.arguments(45, "F", 2, Risque.RISQUE_LIMITE),
		Arguments.arguments(45, "F", 3, Risque.RISQUE_LIMITE),
		Arguments.arguments(45, "F", 4, Risque.RISQUE_LIMITE),
		Arguments.arguments(45, "F", 5, Risque.RISQUE_LIMITE),

		Arguments.arguments(25, "M", 3, Risque.DANGER),
		Arguments.arguments(25, "M", 4, Risque.DANGER),

		Arguments.arguments(25, "F", 4, Risque.DANGER),
		Arguments.arguments(25, "F", 5, Risque.DANGER),
		Arguments.arguments(25, "F", 6, Risque.DANGER),

		Arguments.arguments(45, "M", 6, Risque.DANGER),
		Arguments.arguments(45, "M", 7, Risque.DANGER),
		Arguments.arguments(45, "F", 6, Risque.DANGER),
		Arguments.arguments(45, "F", 7, Risque.DANGER),

		Arguments.arguments(25, "M", 5, Risque.APPARITION_PRECOCE),
		Arguments.arguments(25, "M", 6, Risque.APPARITION_PRECOCE),
		Arguments.arguments(25, "M", 7, Risque.APPARITION_PRECOCE),

		Arguments.arguments(25, "F", 7, Risque.APPARITION_PRECOCE),
		Arguments.arguments(25, "F", 8, Risque.APPARITION_PRECOCE),
		Arguments.arguments(25, "F", 9, Risque.APPARITION_PRECOCE),

		Arguments.arguments(45, "M", 8, Risque.APPARITION_PRECOCE),
		Arguments.arguments(45, "M", 9, Risque.APPARITION_PRECOCE),
		Arguments.arguments(45, "M", 10, Risque.APPARITION_PRECOCE),

		Arguments.arguments(45, "F", 8, Risque.APPARITION_PRECOCE),
		Arguments.arguments(45, "F", 9, Risque.APPARITION_PRECOCE),
		Arguments.arguments(45, "F", 10, Risque.APPARITION_PRECOCE));
    }

    @ParameterizedTest(name = "Risque patient : âge : {0}, genre : {1}, Nb declencheurs : {2}, devrait être : {3}")
    @MethodSource("findRisqueParametersProvider")
    public void findRisque(int age, String genre, int nbDeclencheur, Risque expectedRisque) throws Exception {

	// WHEN
	PatientDao patientDao = Util.getPatientDao(genre, age, nbDeclencheur);

	MvcResult mvcResult = mockMvc
		.perform(MockMvcRequestBuilders.post("/expert").content(Util.mapper.writeValueAsString(patientDao))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(200)).andReturn();

	// THEN
	Risque risque = Util.getRisqueFromMvcResult(mvcResult);
	assertNotNull(risque);
	log.info("expected risque : " + expectedRisque + ", calculated risque : " + risque);
	assertThat(expectedRisque.ordinal()).isEqualTo(risque.ordinal());
    }

    @Test
    public void findRisque_RisqueNotFoundException() throws Exception {

	PatientDao patientDao = Util.getPatientDao("A", 300, 0);

	mockMvc.perform(MockMvcRequestBuilders.post("/expert")
		.content(Util.mapper.writeValueAsString(patientDao))
		.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(404)).andReturn();
    }

}