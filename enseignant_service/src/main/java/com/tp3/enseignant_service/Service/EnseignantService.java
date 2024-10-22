package com.tp3.enseignant_service.Service;

import com.tp3.enseignant_service.Entity.Enseignant;
import com.tp3.enseignant_service.Repository.EnseignantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnseignantService {
    @Autowired
    private EnseignantRepository enseignantRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public List<Enseignant> getAllEnseignants() {
        return enseignantRepository.findAll();
    }

    public Optional<Enseignant> getEnseignantById(Long id) {
        return enseignantRepository.findById(id);
    }

    public Enseignant createEnseignant(Enseignant enseignant) {
        // Encoder le mot de passe avant de le sauvegarder
        enseignant.setMotDePasse(passwordEncoder.encode(enseignant.getMotDePasse()));
        return enseignantRepository.save(enseignant);
    }
    public Enseignant updateEnseignant(Long id, Enseignant enseignantDetails) {
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enseignant non trouvé"));

        enseignant.setNom(enseignantDetails.getNom());
        enseignant.setPrenom(enseignantDetails.getPrenom());
        enseignant.setCne(enseignantDetails.getCne());
        enseignant.setEmail(enseignantDetails.getEmail());

        // Encoder le mot de passe uniquement si celui-ci a été modifié
        if (!enseignantDetails.getMotDePasse().isEmpty()) {
            enseignant.setMotDePasse(passwordEncoder.encode(enseignantDetails.getMotDePasse()));
        }

        enseignant.setThematiqueRecherche(enseignantDetails.getThematiqueRecherche());
        enseignant.setRole(enseignantDetails.getRole());
        return enseignantRepository.save(enseignant);
    }
    public Enseignant findByEmail(String email) {
        // Utilise le repository pour trouver l'enseignant par email
        return enseignantRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Enseignant non trouvé avec l'email : " + email));
    }
    public void deleteEnseignant(Long id) {
        enseignantRepository.deleteById(id);
    }
}
