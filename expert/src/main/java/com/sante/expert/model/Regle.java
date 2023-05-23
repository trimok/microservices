package com.sante.expert.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author trimok
 *
 *         Classe Regle, modélise les règles métiers
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Regle {

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de déclencheurs (minimum)
     */
    @Column(name = "nb_declencheur")
    private int nbDeclencheur;

    /**
     * genre (H/F)
     */
    private String genre;

    /**
     * age minimum pour la regle
     */
    @Column(name = "min_age")
    private int minAge;

    /**
     * age maximum pour la regle (200 si pas de maximum)
     */
    @Column(name = "max_age")
    private int maxAge;

    /**
     * Le risque correspondant à la règle métier
     */
    private int risque;
}
