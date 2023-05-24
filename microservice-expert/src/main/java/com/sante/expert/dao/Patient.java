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

/**
 * @author trimok
 *
 *         classe Patient
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    /**
     * id
     */
    private Long id;

    /**
     * prénom
     */
    private String prenom;

    /**
     * nom
     */
    private String nom;

    /**
     * date de naissance
     */
    @Column(name = "date_naissance")
    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateNaissance;

    /**
     * genre (M/F)
     */
    @NotEmpty
    @Size(min = 1, max = 1)
    private String genre;

    /**
     * Adresse postale
     */
    @Column(name = "adresse_postale")
    private String adressePostale;

    /**
     * Telephone
     */
    private String telephone;

    /**
     * Methode toString
     */
    @Override
    public String toString() {
	return "Patient [prenom=" + prenom + ", nom=" + nom + ", dateNaissance=" + dateNaissance + ", genre=" + genre
		+ ", adressePostale=" + adressePostale + ", telephone=" + telephone + "]";
    }
}