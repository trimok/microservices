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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@CompoundIndex(name = "note_creation_date", def = "{'id': 1, 'notes.creationDate' : 1}", unique = true)
@Document(collection = "patientHistories")
public class PatientHistory {

    @Id
    private Long id;

    @NotNull
    private SortedSet<Note> notes = new TreeSet<>();
}
