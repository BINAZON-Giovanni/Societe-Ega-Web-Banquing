package tech.SocieteEga.WebBanquing.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import tech.SocieteEga.WebBanquing.entite.Compte;
import tech.SocieteEga.WebBanquing.entite.Utilisateur;

import java.util.List;

public interface CompteRepository extends CrudRepository<Compte,String> {
    Compte findByNumeroCompte(String numeroCompte);

    List<Compte> findByUtilisateur(Utilisateur utilisateur);

    List<Compte> findComptesByUtilisateurEmail(@Param("email") String email);
}
