package com.sante.expert.service;

import java.util.List;

import com.sante.expert.dao.PatientDao;
import com.sante.expert.model.Risque;

/**
 * @author trimok
 *
 *         L'interface de service qui permet de calculer le risque
 */
public interface IRegleService {
    /**
     * @param patientDao : les données patient
     * @param keywords   : les mots-clés médicaux
     * @return : le risque calculé
     */
    Risque findRisque(PatientDao patientDao, List<String> keywords);
}
