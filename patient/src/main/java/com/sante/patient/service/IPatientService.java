package com.sante.patient.service;

import java.util.List;

import com.sante.patient.model.Patient;

/**
 * @author trimok
 *
 *         Interface de service pour les operations CRUD sur le patient
 *
 */
public interface IPatientService {

    /**
     * Ajout d'un patient
     * 
     * @param patient : le patient à ajouter
     * @return : le patient ajouté (avec son id)
     */
    Patient createPatient(Patient patient);

    /**
     * Modification d'un patient
     * 
     * @param patient : le patient à modifier
     * @return : le patient modifié
     */
    Patient updatePatient(Patient patient);

    /**
     * Suppression d'un patient
     * 
     * @param id : id du patient
     */
    void deletePatient(Long id);

    /**
     * Obtention de la liste des patients
     * 
     * @return : la liste des patients
     */
    List<Patient> getPatients();

    /**
     * Obtention d'un patient
     * 
     * @param id : l'id du patient
     * @return : le patient
     */
    Patient getPatient(Long id);

    /**
     * Suppression de tous les patients
     */
    void deleteAllPatient();
}
