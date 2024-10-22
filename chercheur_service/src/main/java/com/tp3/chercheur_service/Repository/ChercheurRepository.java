package com.tp3.chercheur_service.Repository;

import com.tp3.chercheur_service.Entity.Chercheur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChercheurRepository extends JpaRepository<Chercheur, Long> {
}
