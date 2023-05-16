package com.sante.patienthistory.repository;

import com.mongodb.client.result.UpdateResult;
import com.sante.patienthistory.model.PatientHistory;

public interface INoteRepository {
    UpdateResult updateNote(PatientHistory patientHistory);

    UpdateResult deleteNote(PatientHistory patientHistory);
}
