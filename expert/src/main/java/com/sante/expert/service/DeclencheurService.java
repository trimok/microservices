package com.sante.expert.service;

import static com.sante.expert.constants.Constants.DECLENCHEUR_NOT_FOUND;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sante.expert.exception.DeclencheurNotFoundException;
import com.sante.expert.repository.DeclencheurRepository;

@Service
public class DeclencheurService implements IDeclencheurService {

    @Autowired
    private DeclencheurRepository declencheurRepository;

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
