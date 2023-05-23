package com.sante.expert.dao;

import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.data.annotation.Id;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author trimok
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientHistory {

    /**
     * Methode toString
     */
    @Override
    public String toString() {
	return "PatientHistory [id=" + id + ", notes=" + notes + "]";
    }

    /**
     * id
     */
    @Id
    private Long id;

    /**
     * L'ensemble des notes de l'historique du patient
     */
    @NotNull
    private SortedSet<Note> notes = new TreeSet<>();
}
