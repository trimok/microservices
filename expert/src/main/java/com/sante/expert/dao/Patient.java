package com.sante.expert.dao;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @Column(name = "date_naissance")
    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateNaissance;

    @NotEmpty
    @Size(min = 1, max = 1)
    private String genre;

    @Column(name = "adresse_postale")
    private String adressePostale;

    private String telephone;

    @Override
    public String toString() {
	return "Patient [prenom=" + prenom + ", nom=" + nom + ", dateNaissance=" + dateNaissance + ", genre=" + genre
		+ ", adressePostale=" + adressePostale + ", telephone=" + telephone + "]";
    }
}