package com.sante.patienthistory.repository;

import com.mongodb.client.result.UpdateResult;
import com.sante.patienthistory.model.PatientHistory;

/**
 * @author trimok
 *
 *         Interface custom Repository pour l'historique de patient (pour
 *         accéder à la sous-structure des notes)
 */
public interface PatientHistoryCustomRepository {
    /**
     * Mise à jour d'une note
     * 
     * @param patientHistory : l'historique des patients
     * @return : un objet UpdateResult
     */
    UpdateResult updateNote(PatientHistory patientHistory);

    /**
     * Suppression d'une note
     * 
     * @param patientHistory : l'historique des patients
     * @return : un objet UpdateResult
     */
    UpdateResult deleteNote(PatientHistory patientHistory);
}
