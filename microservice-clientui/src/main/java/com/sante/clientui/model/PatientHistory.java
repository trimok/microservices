package com.sante.clientui.model;

import java.util.SortedSet;
import java.util.TreeSet;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author trimok
 *
 *         PatientHistory
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientHistory {

    /**
     * id patient and patienthistory
     */
    @NotNull
    private Long id;

    /**
     * Collection of notes
     */
    @NotNull
    private SortedSet<Note> notes = new TreeSet<>();
}
