package tech.SocieteEga.WebBanquing.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tech.SocieteEga.WebBanquing.Type.TypeDeRole;
import tech.SocieteEga.WebBanquing.entite.Role;
import tech.SocieteEga.WebBanquing.entite.Utilisateur;
import tech.SocieteEga.WebBanquing.entite.Validation;
import tech.SocieteEga.WebBanquing.repository.UtilisateurRepository;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminService {

    private UtilisateurRepository utilisateurRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private ValidationService validationService;
    public void inscription(Utilisateur utilisateur) {

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
    }

    public void activation(final Map<String, String> activation) {
        final Validation validation = this.validationService.lireEnFonctionDuCode(activation.get("code"));
        if (Instant.now().isAfter(validation.getExpiration())) {
            throw new RuntimeException("Votre code a expiré");
        }
        final Utilisateur utilisateurActiver = this.utilisateurRepository.findById(validation.getUtilisateur().getId()).orElseThrow(() -> new RuntimeException("Utilisateur inconnu"));
        utilisateurActiver.setActif(true);
        this.utilisateurRepository.save(utilisateurActiver);
    }
}
