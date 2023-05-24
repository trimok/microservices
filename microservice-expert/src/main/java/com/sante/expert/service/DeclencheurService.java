package com.sante.expert.service;

import static com.sante.expert.constants.Constants.DECLENCHEUR_NOT_FOUND;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sante.expert.exception.DeclencheurNotFoundException;
import com.sante.expert.repository.DeclencheurRepository;

/**
 * @author trimok
 *
 *         Le service qui permet de récupérer les mots-clés
 */
@Service
public class DeclencheurService implements IDeclencheurService {

    /**
     * Le repository correspondant
     */
    @Autowired
    private DeclencheurRepository declencheurRepository;

    @Autowired
    public DeclencheurService(DeclencheurRepository declencheurRepository) {
	super();
	this.declencheurRepository = declencheurRepository;
    }

    /**
     * Retourne la liste des mots-clés
     */
    @Override
    public List<String> findKeywords() {
	List<String> keywords = new ArrayList<>();

	try {
	    keywords = declencheurRepository.findKeywords();
	} catch (Exception e) {
	    throw new DeclencheurNotFoundException(DECLENCHEUR_NOT_FOUND);
	}

	if (keywords == null || keywords.isEmpty()) {
	    throw new DeclencheurNotFoundException(DECLENCHEUR_NOT_FOUND);
	}

	return keywords;
    }
}
