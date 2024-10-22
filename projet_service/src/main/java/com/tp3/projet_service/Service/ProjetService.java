package com.tp3.projet_service.Service;

import com.tp3.projet_service.Entity.Projet;
import com.tp3.projet_service.Model.Enseignant;
import com.tp3.projet_service.Repository.ProjetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ProjetService {

    private final ProjetRepository projetRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public ProjetService(ProjetRepository projetRepository) {
        this.projetRepository = projetRepository;
        this.restTemplate = new RestTemplate(); // Vous pouvez aussi injecter cela via un @Bean
    }

    public Projet createProjet(Projet chercheur) {
        return projetRepository.save(chercheur);
    }

    public List<Projet> getAllProjets() {
        List<Projet> projetList = projetRepository.findAll();
        if (projetList != null) {
            for (Projet projet : projetList) {
                // Récupérer l'enseignant associé
                Enseignant enseignant = restTemplate.getForObject(
                        "http://localhost:8080/api/enseignants/" + projet.getIdEnseignant(), Enseignant.class);
                projet.setEnseignant(enseignant); // Associer l'enseignant récupéré au chercheur
            }
        }
        return projetList;
    }

    public Projet getProjetById(Long id) {
        Projet projet = projetRepository.findById(id).orElse(null);
        if (projet != null) {
            // Récupérer l'enseignant associé
            Enseignant enseignant = restTemplate.getForObject(
                    "http://localhost:8080/api/enseignants/" + projet.getIdEnseignant(), Enseignant.class);
            projet.setEnseignant(enseignant); // Associer l'enseignant récupéré au chercheur
        }
        return projet;
    }

    public Projet updateProjet(Long id, Projet projet) {
        return projetRepository.findById(id).map(existingProjet -> {
            existingProjet.setTitre(projet.getTitre());
            existingProjet.setDescription(projet.getDescription());
            existingProjet.setIdEnseignant(projet.getIdEnseignant());
            return projetRepository.save(existingProjet);
        }).orElseThrow(() -> new RuntimeException("Chercheur non trouvé"));
    }

    public void deleteProjet(Long id) {
        projetRepository.deleteById(id);
    }
}
