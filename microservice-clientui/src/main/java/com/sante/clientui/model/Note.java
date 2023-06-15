package com.sante.clientui.model;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Note other = (Note) obj;
	return Objects.equals(creationDate, other.creationDate) && Objects.equals(info, other.info);
    }
}
