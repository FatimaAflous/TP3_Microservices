package com.tp3.enseignant_service.Controller;

import com.tp3.enseignant_service.Entity.Enseignant;
import com.tp3.enseignant_service.Service.EnseignantService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enseignants")
@OpenAPIDefinition(
        info = @Info(
                title = "Gestion des enseignants",
                description = "Gérer les enseugnants et leurs informations",
                version = "1.0.0"
        ),
        servers = @Server(
                url = "http://localhost:8080/"
        )
)
public class EnseignantController {

    @Autowired
    private EnseignantService enseignantService;
    @GetMapping("/find")
    public ResponseEntity<Enseignant> getEnseignantByEmail(@RequestParam String email) {
        Enseignant enseignant = enseignantService.findByEmail(email);
        return ResponseEntity.ok(enseignant);
    }
    @GetMapping
    @Operation(
            summary = "Récupérer la liste des enseigannts",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Succès",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Enseignant.class))),
                    @ApiResponse(responseCode = "400", description = "Erreur dans la requête")
            }
    )
    public List<Enseignant> getAllEnseignants() {
        return enseignantService.getAllEnseignants();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Enseignant> getEnseignantById(@PathVariable Long id) {
        return enseignantService.getEnseignantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Enseignant> createEnseignant(@RequestBody Enseignant enseignant) {
        Enseignant createdEnseignant = enseignantService.createEnseignant(enseignant);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEnseignant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Enseignant> updateEnseignant(@PathVariable Long id, @RequestBody Enseignant enseignantDetails) {
        Enseignant updatedEnseignant = enseignantService.updateEnseignant(id, enseignantDetails);
        return ResponseEntity.ok(updatedEnseignant);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer un enseugnant par son ID",
            parameters = @Parameter(name = "id", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "enseignant supprimé avec succès"),
                    @ApiResponse(responseCode = "404", description = "enseignant non trouvé")
            }
    )
    public ResponseEntity<Void> deleteEnseignant(@PathVariable Long id) {
        enseignantService.deleteEnseignant(id);
        return ResponseEntity.noContent().build();
    }
}
