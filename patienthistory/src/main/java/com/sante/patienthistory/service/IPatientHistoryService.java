package com.sante.patienthistory.service;

import com.sante.patienthistory.model.PatientHistory;

public interface IPatientHistoryService {

    PatientHistory createPatientHistory(PatientHistory patientHistory);

    PatientHistory getPatientHistory(Long id);

    PatientHistory updatePatientHistory(PatientHistory patientHistory);

    PatientHistory createUpdatePatientHistory(PatientHistory patientHistory);

    PatientHistory updateNote(PatientHistory patientHistory);

    PatientHistory deleteNote(PatientHistory patientHistory);
}
