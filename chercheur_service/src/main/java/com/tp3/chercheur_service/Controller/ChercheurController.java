package com.tp3.chercheur_service.Controller;

import com.tp3.chercheur_service.Entity.Chercheur;
import com.tp3.chercheur_service.Service.ChercheurService;
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
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chercheurs")
@OpenAPIDefinition(
        info = @Info(
                title = "Gestion des Chercheurs",
                description = "Gérer les chercheurs et leurs informations",
                version = "1.0.0"
        ),
        servers = @Server(
                url = "http://localhost:8081/"
        )
)

public class ChercheurController {

    @Autowired
    private ChercheurService chercheurService;

    @PostMapping

    public ResponseEntity<Chercheur> add(@RequestBody Chercheur chercheur) {
        Chercheur newChercheur = chercheurService.createChercheur(chercheur);
        return ResponseEntity.ok(newChercheur);
    }

    @GetMapping
    @Operation(
            summary = "Récupérer la liste des chercheurs",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Succès",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Chercheur.class))),
                    @ApiResponse(responseCode = "400", description = "Erreur dans la requête")
            }
    )
    public ResponseEntity<List<Chercheur>> getAll() {
        List<Chercheur> chercheurList = chercheurService.getAllChercheurs();
        return ResponseEntity.ok(chercheurList);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Récupérer un chercheur par son ID",
            parameters = @Parameter(name = "id", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Chercheur récupéré avec succès",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Chercheur.class))),
                    @ApiResponse(responseCode = "404", description = "Chercheur non trouvé")
            }
    )
    public ResponseEntity<Chercheur> getById(@PathVariable Long id) {
        Chercheur chercheur = chercheurService.getChercheurById(id);
        return ResponseEntity.ok(chercheur);
    }

    @PutMapping("/{id}")

    public ResponseEntity<Chercheur> update(@PathVariable Long id, @RequestBody Chercheur chercheur) {
        Chercheur updatedChercheur = chercheurService.updateChercheur(id, chercheur);
        return ResponseEntity.ok(updatedChercheur);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer un chercheur par son ID",
            parameters = @Parameter(name = "id", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Chercheur supprimé avec succès"),
                    @ApiResponse(responseCode = "404", description = "Chercheur non trouvé")
            }
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        chercheurService.deleteChercheur(id);
        return ResponseEntity.ok().build();
    }
}
