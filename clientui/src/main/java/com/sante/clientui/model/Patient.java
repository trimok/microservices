package com.sante.clientui.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    private Long id;

    private String prenom;

    private String nom;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateNaissance;

    private String genre;

    private String adressePostale;

    private String telephone;
}
