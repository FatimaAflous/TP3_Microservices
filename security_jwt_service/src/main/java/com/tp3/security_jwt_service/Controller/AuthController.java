package com.tp3.security_jwt_service.Controller;
import org.springframework.security.core.GrantedAuthority;
import com.tp3.security_jwt_service.configuration.JwtTokenProvider;
import com.tp3.security_jwt_service.model.AuthRequest;
import com.tp3.security_jwt_service.model.AuthResponse;
import com.tp3.security_jwt_service.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/login")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
private CustomUserDetailsService customUserDetailsService;
    @PostMapping
    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        try {
            // Afficher les données reçues
            System.out.println("Données reçues : " + authRequest.toString());

            // Authentification de l'utilisateur
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(authRequest.getEmail());

            // Vérification du mot de passe
            if (!passwordEncoder.matches(authRequest.getMotDePass(), userDetails.getPassword())) {
                throw new RuntimeException("Échec de l'authentification : Mot de passe incorrect");
            }

            // Récupération du rôle de l'utilisateur
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            String role = authorities.iterator().next().getAuthority();  // Récupérer le premier rôle

            // Génération du token avec le rôle récupéré
            String token = jwtTokenProvider.generateToken(authRequest.getEmail(), role);

            return new AuthResponse(token);

        } catch (AuthenticationException e) {
            System.out.println("Échec de l'authentification : " + e.getMessage());
            throw new RuntimeException("Échec de l'authentification : " + e.getMessage());
        }
    }


}
