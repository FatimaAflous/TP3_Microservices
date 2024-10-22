package com.tp3.security_jwt_service.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    // Méthode pour générer un token JWT
    // Méthodes pour générer et valider les tokens
    public String generateToken(String username, String role) {
        System.out.println("Génération du token pour l'utilisateur : " + username);
        System.out.println("Rôle ajouté au token : " + role); // Ajout pour vérifier le rôle

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);  // Ajout du claim 'role'

        return Jwts.builder()
                .setClaims(claims)  // Ajout des claims personnalisés
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }


    // Méthode pour valider le token
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false; // Token invalide
        }
    }

    // Méthode pour récupérer le nom d'utilisateur à partir du token
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }


}
