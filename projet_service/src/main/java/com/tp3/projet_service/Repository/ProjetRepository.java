package com.tp3.projet_service.Repository;

import com.tp3.projet_service.Entity.Projet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetRepository  extends JpaRepository<Projet, Long> {
}
