package com.sante.patienthistory.service;

import com.sante.patienthistory.model.PatientHistory;

/**
 * @author trimok
 *
 *         Interface de service pour les opérations sur l'historique du patinet
 *
 */
public interface IPatientHistoryService {

    /**
     * Ajout d'une note avec Création d'un historique de patient
     * 
     * @param patientHistory : l'historique de patient à créer (avec une seule note)
     * @return : l'historique de patient créé
     */
    PatientHistory createPatientHistory(PatientHistory patientHistory);

    /**
     * Obtention d'un historique de patient
     * 
     * @param id : id de l'historique
     * @return : l'historique de patient
     */
    PatientHistory getPatientHistory(Long id);

    /**
     * Ajout d'une note avec Modification d'un historique de patient
     * 
     * @param patientHistory : l'historique de patient (avec une seule note)
     * @return : l'historique du patient
     */
    PatientHistory updatePatientHistory(PatientHistory patientHistory);

    /**
     * Ajout d'une note (création ou modification de l'historique du patient)
     * 
     * @param patientHistory : l'historique de patient (avec une seule note)
     * @return : l'historique du patient
     */
    PatientHistory createUpdatePatientHistory(PatientHistory patientHistory);

    /**
     * Mise à jour d'une note
     * 
     * @param patientHistory : l'historique du patient, avec la note à modifier
     * @return : l'historique du patient
     */
    PatientHistory updateNote(PatientHistory patientHistory);

    /**
     * Suppression d'une note
     * 
     * @param patientHistory : l'historique du patient, avec la note à supprimer
     * @return : l'historique du patient
     */
    PatientHistory deleteNote(PatientHistory patientHistory);

    /**
     * Suppression de l'historique de patient, par son id
     * 
     * @param id : l'id de l'historique de patient
     */
    void deletePatientHistory(Long id);

    /**
     * Suppression de tous les historiques de patient
     */
    void deleteAllPatientHistory();
}
