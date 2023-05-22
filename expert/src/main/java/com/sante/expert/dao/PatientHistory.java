package com.sante.expert.dao;

import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.data.annotation.Id;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientHistory {

    @Override
    public String toString() {
	return "PatientHistory [id=" + id + ", notes=" + notes + "]";
    }

    @Id
    private Long id;

    @NotNull
    private SortedSet<Note> notes = new TreeSet<>();
}
