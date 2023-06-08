package com.sante.clientui.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

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
 *         Patient
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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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
    @Size(min = 5, max = 128)
    private String adressePostale;

    /**
     * telephone
     */
    @Size(min = 8, max = 20)
    @Pattern(regexp = "[0-9]{2} [0-9]{2} [0-9]{2} [0-9]{2} [0-9]{2}")
    private String telephone;
}
