package tech.SocieteEga.WebBanquing.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tech.SocieteEga.WebBanquing.Type.TypeDeRole;
import tech.SocieteEga.WebBanquing.entite.Compte;
import tech.SocieteEga.WebBanquing.entite.Role;
import tech.SocieteEga.WebBanquing.entite.Utilisateur;
import tech.SocieteEga.WebBanquing.entite.Validation;
import tech.SocieteEga.WebBanquing.repository.UtilisateurRepository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UtilisateurService implements UserDetailsService {
    private UtilisateurRepository utilisateurRepository;
    private CompteService compteService;
    private BCryptPasswordEncoder passwordEncoder;
    private ValidationService validationService;


    public void activation(final Map<String, String> activation) {
        final Validation validation = this.validationService.lireEnFonctionDuCode(activation.get("code"));
        if (Instant.now().isAfter(validation.getExpiration())) {
            throw new RuntimeException("Votre code a expiré");
        }
        final Utilisateur utilisateurActiver = this.utilisateurRepository.findById(validation.getUtilisateur().getId()).orElseThrow(() -> new RuntimeException("Utilisateur inconnu"));
        utilisateurActiver.setActif(true);
        this.utilisateurRepository.save(utilisateurActiver);
    }

    @Override
    public Utilisateur loadUserByUsername(final String username) throws UsernameNotFoundException {
        return this.utilisateurRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Aucun utilisateur ne corespond à cet identifiant"));
    }

    public List<Utilisateur> getAllClients() {
        return (List<Utilisateur>) utilisateurRepository.findAll();
    }


    public Utilisateur updateUtilisateurByEmail(String email, Utilisateur utilisateurModifie) throws ChangeSetPersister.NotFoundException {
        Utilisateur utilisateurExistant = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé avec l'e-mail: " + email));

        // Mettez à jour les propriétés de l'utilisateur existant avec celles de l'utilisateur modifié
        utilisateurExistant.setNom(utilisateurModifie.getNom());
        utilisateurExistant.setMdp(utilisateurModifie.getMdp());
        utilisateurExistant.setActif(utilisateurModifie.isActif());
        // Ajoutez d'autres propriétés que vous souhaitez mettre à jour

        // Enregistrez l'utilisateur mis à jour dans la base de données
        return utilisateurRepository.save(utilisateurExistant);
    }

    public boolean deleteClient(Long id) {
        Utilisateur existingClient = utilisateurRepository.findById(Math.toIntExact(id)).orElse(null);
        if (existingClient == null) {
            return false;
        }
        utilisateurRepository.delete(existingClient);
        return true;
    }

    public void inscription(Utilisateur utilisateur, Compte compte) {

        if (!utilisateur.getEmail().contains("@")) {
            throw new RuntimeException("Votre mail invalide");
        }
        if (!utilisateur.getEmail().contains(".")) {
            throw new RuntimeException("Votre mail invalide");
        }

        final Optional<Utilisateur> utilisateurOptional = this.utilisateurRepository.findByEmail(utilisateur.getEmail());
        if (utilisateurOptional.isPresent()) {
            throw new RuntimeException("Votre mail est déjà utilisé");
        }

        final String mdpCrypte = this.passwordEncoder.encode(utilisateur.getMdp());

        utilisateur.setMdp(mdpCrypte);

        final Role roleUtilisateur = new Role();
        roleUtilisateur.setLibelle(TypeDeRole.UTILISATEUR);
        if (utilisateur.getRole() != null && utilisateur.getRole().getLibelle().equals(TypeDeRole.ADMINISTRATEUR)) {
            roleUtilisateur.setLibelle(TypeDeRole.ADMINISTRATEUR);
            utilisateur.setActif(true);
        }
        utilisateur.setRole(roleUtilisateur);

        utilisateur = this.utilisateurRepository.save(utilisateur);

        if (roleUtilisateur.getLibelle().equals(TypeDeRole.UTILISATEUR)) {
            this.validationService.enregistrer(utilisateur);
        }


        compte.setUtilisateur(utilisateur);
        compteService.createCompte(compte);
    }
}
