package com.sante.patienthistory.model;

import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author trimok
 *
 *         Classe modélisant le patient
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@CompoundIndex(name = "note_creation_date", def = "{'id': 1, 'notes.creationDate' : 1}", unique = true)
@Document(collection = "patientHistories")
public class PatientHistory {

    /**
     * id
     */
    @Id
    private Long id;

    /**
     * La liste des notes
     */
    @NotNull
    private SortedSet<Note> notes = new TreeSet<>();

    /**
     * toString
     */
    @Override
    public String toString() {
	return "PatientHistory [id=" + id + ", notes=" + notes + "]";
    }
}
