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

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	PatientHistory other = (PatientHistory) obj;

	for (Note note : notes) {
	    boolean noteFound = false;
	    for (Note noteOther : other.getNotes()) {
		if (note.equals(noteOther)) {
		    noteFound = true;
		    break;
		}
	    }
	    if (!noteFound) {
		return false;
	    }
	}

	for (Note noteOther : other.getNotes()) {
	    boolean noteFound = false;
	    for (Note note : notes) {
		if (noteOther.equals(note)) {
		    noteFound = true;
		    break;
		}
	    }
	    if (!noteFound) {
		return false;
	    }
	}

	return true;
    }
}
