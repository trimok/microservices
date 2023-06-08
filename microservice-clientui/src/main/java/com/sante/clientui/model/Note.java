package com.sante.clientui.model;

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
 *         Note
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Note implements Comparable<Note> {
    /**
     * creationDate = id note
     */
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @NotNull
    private LocalDateTime creationDate = LocalDateTime.now();

    /**
     * information
     */
    @NotEmpty
    private String info;

    /**
     * Compare method
     */
    @Override
    public int compareTo(Note o) {
	return creationDate.isBefore(o.getCreationDate()) ? 1 : -1;
    }
}
