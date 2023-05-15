package com.sante.clientui.model;

import java.util.SortedSet;
import java.util.TreeSet;

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

    @NotNull
    private Long id;

    @NotNull
    private SortedSet<Note> notes = new TreeSet<>();
}
