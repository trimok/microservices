package com.sante.patient.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author trimok
 *
 *         Modélisation du patient
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Patient {

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * prenom
     */
    @Size(min = 2, max = 64)
    private String prenom;

    /**
     * nom
     */
    @Size(min = 2, max = 64)
    private String nom;

    /**
     * dateNaissance
     */
    @NotNull
    @Column(name = "date_naissance")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateNaissance;

    /**
     * genre (M/F)
     */
    @Size(min = 1, max = 1)
    private String genre;

    /**
     * adressePostale
     */
    @Column(name = "adresse_postale")
    @Size(min = 5, max = 128)
    private String adressePostale;

    /**
     * telephone
     */
    @Size(min = 8, max = 32)
    @Pattern(regexp = "[0-9]{2} [0-9]{2} [0-9]{2} [0-9]{2} [0-9]{2}")
    private String telephone;

    /**
     * toString
     */
    @Override
    public String toString() {
	return "Patient [prenom=" + prenom + ", nom=" + nom + ", dateNaissance=" + dateNaissance + ", genre=" + genre
		+ ", adressePostale=" + adressePostale + ", telephone=" + telephone + "]";
    }
}
