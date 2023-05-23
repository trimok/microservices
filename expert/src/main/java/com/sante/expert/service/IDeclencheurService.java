package com.sante.expert.service;

import java.util.List;

/**
 * @author trimok
 * 
 *         L'interface de service qui permet de récupérer les mots-clés
 *
 */
public interface IDeclencheurService {

    /**
     * @return : la liste des mots-clés
     */
    List<String> findKeywords();

}
