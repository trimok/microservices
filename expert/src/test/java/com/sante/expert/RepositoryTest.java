package com.sante.expert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.sante.expert.model.Declencheur;
import com.sante.expert.model.Risque;
import com.sante.expert.repository.DeclencheurRepository;
import com.sante.expert.repository.RegleRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DataJpaTest
@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(value = org.junit.jupiter.api.MethodOrderer.MethodName.class)

public class RepositoryTest {
    @Autowired
    private DeclencheurRepository declencheurRepository;

    @Autowired
    private RegleRepository regleRepository;

    @BeforeAll
    public void beforeAll() {
    }

    @BeforeEach
    public void beforeEach() {
    }

    @AfterEach
    public void afterEach() {
    }

    @Test
    public void regleRepository_findRisque() {
	Risque risque = null;
	try {
	    risque = Risque.values()[regleRepository.findRisque(5, "M", 70)];
	} catch (Exception e) {
	    e.printStackTrace();
	}
	log.info("Risque :" + risque);
	assertNotNull(risque);
    }

    @Test
    public void declencheurRepository_findKeywords() {
	List<Declencheur> keywords = null;
	try {
	    keywords = declencheurRepository.findAll();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	assertNotNull(keywords);
	log.info("nb keywords = " + keywords.size());
	log.info(keywords.stream().map(d -> d.getKeyword().toString()).collect(Collectors.joining(", ")));
	assertThat(keywords.size()).isGreaterThan(0);
    }
}
