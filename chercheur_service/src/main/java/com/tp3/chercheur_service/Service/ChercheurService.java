package com.tp3.chercheur_service.Service;

import com.tp3.chercheur_service.Entity.Chercheur;
import com.tp3.chercheur_service.Model.Enseignant;
import com.tp3.chercheur_service.Model.Projet;
import com.tp3.chercheur_service.Repository.ChercheurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ChercheurService {
    private final ChercheurRepository chercheurRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public ChercheurService(ChercheurRepository chercheurRepository) {
        this.chercheurRepository = chercheurRepository;
        this.restTemplate = new RestTemplate(); // Vous pouvez aussi injecter cela via un @Bean
    }

    public Chercheur createChercheur(Chercheur chercheur) {
        return chercheurRepository.save(chercheur);
    }

    public List<Chercheur> getAllChercheurs() {
        List<Chercheur> chercheurList = chercheurRepository.findAll();
        if (chercheurList != null) {
            for (Chercheur c : chercheurList) {
                Enseignant enseignant = restTemplate.getForObject("http://localhost:8080/api/enseignants/" + c.getIdEnseignant(), Enseignant.class);
                Projet projet = restTemplate.getForObject("http://localhost:8083/api/projets/" + c.getIdProjet(), Projet.class);
                c.setEnseignant(enseignant);
                c.setProjet(projet);
            }
        }
        return chercheurList;
    }

    public Chercheur getChercheurById(Long id) {
        Chercheur chercheur = chercheurRepository.findById(id).orElse(null);
        if (chercheur != null) {
            Enseignant enseignant = restTemplate.getForObject("http://localhost:8080/api/enseignants/" + chercheur.getIdEnseignant(), Enseignant.class);
            Projet projet = restTemplate.getForObject("http://localhost:8083/api/projets/" + chercheur.getIdProjet(), Projet.class);

            // Log pour vérifier l'ID du projet
            System.out.println("ID Projet: " + chercheur.getIdProjet());
            System.out.println("Projet récupéré: " + projet);  // Cela devrait afficher l'objet projet

            chercheur.setEnseignant(enseignant);
            chercheur.setProjet(projet);
        }
        return chercheur;
    }


    public Chercheur updateChercheur(Long id, Chercheur newChercheur) {
        return chercheurRepository.findById(id).map(chercheur -> {
            chercheur.setNom(newChercheur.getNom());
            chercheur.setPrenom(newChercheur.getPrenom());
            chercheur.setNumeroInscription(newChercheur.getNumeroInscription());
            chercheur.setRole(newChercheur.getRole());
            chercheur.setIdEnseignant(newChercheur.getIdEnseignant());
            chercheur.setIdProjet(newChercheur.getIdProjet());
            return chercheurRepository.save(chercheur);
        }).orElseThrow(() -> new RuntimeException("Chercheur non trouvé"));
    }

    public void deleteChercheur(Long id) {
        chercheurRepository.deleteById(id);
    }
}
