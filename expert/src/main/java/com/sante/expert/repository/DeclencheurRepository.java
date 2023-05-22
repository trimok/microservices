package com.sante.expert.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sante.expert.model.Declencheur;

@Repository
public interface DeclencheurRepository extends JpaRepository<Declencheur, Long> {
    @Query("SELECT keyword FROM Declencheur")
    List<String> findKeywords();
}
