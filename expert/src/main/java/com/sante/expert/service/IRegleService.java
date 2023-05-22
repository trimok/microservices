package com.sante.expert.service;

import java.util.List;

import com.sante.expert.dao.PatientDao;
import com.sante.expert.model.Risque;

public interface IRegleService {
    Risque findRisque(PatientDao patientDao, List<String> keywords);
}
