package com.sante.expert.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sante.expert.model.Declencheur;

/**
 * @author trimok
 *
 *         Repository pour les regles métiers
 *
 */
@Repository
public interface RegleRepository extends JpaRepository<Declencheur, Long> {

    /**
     * @param nbDeclencheur : le nombre de déclencheurs (mots-clé médicaux)
     * @param genre         : le genre (M/F)
     * @param age           : agte du patient
     * @return : le risque sous forme d'entier
     */
    @Query("SELECT max(risque) FROM Regle r WHERE (r.nbDeclencheur <= :nbDeclencheur) AND (r.genre = :genre or r.genre is null) AND (r.minAge <= :age) AND (r.maxAge > :age)")
    int findRisque(@Param("nbDeclencheur") int nbDeclencheur, @Param("genre") String genre,
	    @Param("age") int age);
}
