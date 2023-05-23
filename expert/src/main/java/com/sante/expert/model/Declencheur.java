package com.sante.expert.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author trimok
 *
 *         Classe declencheur, modélise les mots-clé médicaux
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Declencheur {

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * le keyword médical
     */
    @Size(min = 3, max = 20)
    private String keyword;
}