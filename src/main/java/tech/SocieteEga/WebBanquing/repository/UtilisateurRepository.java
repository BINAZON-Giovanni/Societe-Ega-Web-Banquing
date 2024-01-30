package tech.SocieteEga.WebBanquing.repository;

import org.springframework.data.repository.CrudRepository;
import tech.SocieteEga.WebBanquing.entite.Utilisateur;

import java.util.Optional;

public interface UtilisateurRepository extends CrudRepository<Utilisateur, Integer> {
    Optional<Utilisateur> findByEmail(String email);
}
