package com.sante.patienthistory.model;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author trimok
 *
 *         Classe modélisant une note
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Note implements Comparable<Note> {

    /**
     * Date de création (sert d'id)
     */
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @NotNull
    private LocalDateTime creationDate = LocalDateTime.now();

    /**
     * Information liée à la note
     */
    @NotEmpty
    private String info;

    /**
     * Comparaison entre les dates
     */
    @Override
    public int compareTo(Note o) {
	return creationDate.isBefore(o.getCreationDate()) ? 1 : -1;
    }

    /**
     * toString
     */
    @Override
    public String toString() {
	return "Note [creationDate=" + creationDate + ", info=" + info + "]";
    }
}
