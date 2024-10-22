package com.tp3.projet_service.Controller;

import com.tp3.projet_service.Entity.Projet;
import com.tp3.projet_service.Service.ProjetService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projets")
@OpenAPIDefinition(
        info = @Info(
                title = "Gestion des projets",
                description = "Gérer les projets et leurs informations",
                version = "1.0.0"
        ),
        servers = @Server(
                url = "http://localhost:8083/"
        )
)
public class ProjetController {
    @Autowired
    private ProjetService projetService;
    @PostMapping
    public ResponseEntity<Projet> add(@RequestBody Projet projet) {
        Projet newProjet = projetService.createProjet(projet);
        return ResponseEntity.ok(newProjet);
    }
    @GetMapping
    @Operation(
            summary = "Récupérer la liste des projets",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Succès",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Projet.class))),
                    @ApiResponse(responseCode = "400", description = "Erreur dans la requête")
            }
    )
    public ResponseEntity<List<Projet>> getAll() {
        List<Projet> chercheurList = projetService.getAllProjets();
        return ResponseEntity.ok(chercheurList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Projet> getById(@PathVariable Long id) {
        Projet projet = projetService.getProjetById(id);
        return ResponseEntity.ok(projet);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Projet> update(@PathVariable Long id, @RequestBody Projet projet) {
        Projet updatedChercheur = projetService.updateProjet(id, projet);
        return ResponseEntity.ok(updatedChercheur);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer un projet par son ID",
            parameters = @Parameter(name = "id", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "projet supprimé avec succès"),
                    @ApiResponse(responseCode = "404", description = "projet non trouvé")
            }
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projetService.deleteProjet(id);
        return ResponseEntity.ok().build();
    }
}
