package com.tp3.security_jwt_service.service;

import com.tp3.security_jwt_service.model.Enseignant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private RestTemplate restTemplate;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername appelé avec username : " + username);

        // URL pour appeler l'API du microservice Enseignant
        String url = "http://localhost:8080/api/enseignants/find?email=" + username;
        System.out.println("Appel API avec URL : " + url);

        // Appel API pour récupérer les informations de l'enseignant
        Enseignant enseignant = restTemplate.getForObject(url, Enseignant.class);

        // Si l'enseignant n'est pas trouvé
        if (enseignant == null) {
            System.out.println("Enseignant non trouvé pour username : " + username);
            throw new UsernameNotFoundException("Enseignant non trouvé : " + username);
        }

        // Suivi des informations de l'enseignant
        System.out.println("Enseignant trouvé : " + enseignant.getEmail());
        String motDePasse = enseignant.getMotDePasse(); // Mot de passe récupéré
        System.out.println("Mot de passe récupéré de la base de données : " + motDePasse);

        // Retourne les informations sous forme d'objets UserDetails
        return new User(enseignant.getEmail(), motDePasse, getAuthorities(enseignant.getRole()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        System.out.println("Attribution des autorités pour le rôle : " + role);
        return List.of(new SimpleGrantedAuthority(role));
    }

}
