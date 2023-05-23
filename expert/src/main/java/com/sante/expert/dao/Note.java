package com.sante.expert.dao;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Note implements Comparable<Note> {
    /**
     * Methode toString
     */
    @Override
    public String toString() {
	return "Note [creationDate=" + creationDate + ", info=" + info + "]";
    }

    /**
     * Date de création de la note
     */
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime creationDate = LocalDateTime.now();

    /**
     * Information
     */
    @NotEmpty
    private String info;

    /**
     * Comparaison pour les dates de notes (sert d'identifiant)
     */
    @Override
    public int compareTo(Note o) {
	return creationDate.isBefore(o.getCreationDate()) ? 1 : -1;
    }
}
