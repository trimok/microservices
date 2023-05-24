package com.sante.clientui.dao;

import com.sante.clientui.model.Patient;
import com.sante.clientui.model.PatientHistory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientDao {
    private Patient patient;
    private PatientHistory patientHistory;
}