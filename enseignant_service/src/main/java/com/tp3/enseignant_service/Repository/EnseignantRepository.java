package com.tp3.enseignant_service.Repository;

import com.tp3.enseignant_service.Entity.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnseignantRepository  extends JpaRepository<Enseignant, Long> {
    Optional<Enseignant> findByEmail(String email);

}
