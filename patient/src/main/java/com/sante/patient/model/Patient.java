package com.sante.patient.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 64)
    private String prenom;

    @Size(min = 2, max = 64)
    private String nom;

    @Column(name = "date_naissance")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateNaissance;

    @Size(min = 1, max = 1)
    private String genre;

    @Column(name = "adresse_postale")
    @Size(min = 5, max = 128)
    private String adressePostale;

    @Size(min = 8, max = 32)
    @Pattern(regexp = "[0-9\s]*")
    private String telephone;
}
