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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    private Long id;

    @Size(min = 2, max = 64)
    private String prenom;

    @Size(min = 2, max = 64)
    private String nom;

    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateNaissance;

    @Size(min = 1, max = 1)
    private String genre;

    @Size(min = 5, max = 128)
    private String adressePostale;

    @Size(min = 8, max = 20)
    @Pattern(regexp = "[0-9\s]*")
    private String telephone;
}
