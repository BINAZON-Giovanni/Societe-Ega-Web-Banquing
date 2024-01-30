package tech.SocieteEga.WebBanquing.controleur;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tech.SocieteEga.WebBanquing.Type.TypeDeRole;
import tech.SocieteEga.WebBanquing.dto.AuthentificationDTO;
import tech.SocieteEga.WebBanquing.entite.Compte;
import tech.SocieteEga.WebBanquing.entite.Role;
import tech.SocieteEga.WebBanquing.entite.Utilisateur;
import tech.SocieteEga.WebBanquing.securite.JwtService;
import tech.SocieteEga.WebBanquing.service.UtilisateurService;

import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class UtilisateurControleur {

    private AuthenticationManager authenticationManager;
    private UtilisateurService utilisateurService;
    private JwtService jwtService;

    ///////////////////////////////////////////////// CÔTÉ ADMINISTRATEUR ///////////////////////////////////////////////////////////////////////////

    @PostMapping("admin/inscription")
    public ResponseEntity<Utilisateur> inscription(@RequestBody InscriptionEtCompteRequest request) {
        log.info("Inscription et création de compte : {}", request);

        // Validation des champs et traitement des erreurs si nécessaire

        Utilisateur utilisateur = request.getUtilisateur();
        Compte compte = request.getCompte();
        utilisateur.setRole(new Role(TypeDeRole.UTILISATEUR)); // Par défaut, définissez le rôle sur UTILISATEUR

        // Autres vérifications ou validations nécessaires avant l'inscription et la création du compte

        // Effectuez l'inscription avec le compte
        utilisateurService.inscription(utilisateur, compte);

        return ResponseEntity.ok(utilisateur);
    }


    @PostMapping(path = "admin/activation")
    public void activation(@RequestBody Map<String, String> activation) {
        this.utilisateurService.activation(activation);
    }

    @GetMapping(path="client")
    public ResponseEntity<List<Utilisateur>> getAllClients() {
        List<Utilisateur> utilisateurs = utilisateurService.getAllClients();
        return new ResponseEntity<>(utilisateurs, HttpStatus.OK);
    }

    @GetMapping(path = "client/{email}")
    public ResponseEntity<Utilisateur> getClientByEmail(@PathVariable String email) {
        Utilisateur utilisateur = (Utilisateur) utilisateurService.loadUserByUsername(email);
        if (utilisateur == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(utilisateur);
    }

    @PutMapping("admin/client/update/{email}")
    public ResponseEntity<Utilisateur> AdminupdateUtilisateurByEmail(
            @PathVariable String email,
            @RequestBody Utilisateur utilisateurModifie) throws ChangeSetPersister.NotFoundException {
        Utilisateur utilisateurMaj = utilisateurService.updateUtilisateurByEmail(email, utilisateurModifie);
        return ResponseEntity.ok(utilisateurMaj);
    }



    ///////////////////////////////////////////////// CÔTÉ CLIENT ///////////////////////////////////////////////////////////////////////////

    @PostMapping(path = "connexion")
    public Map<String, String> connexion(@RequestBody AuthentificationDTO authentificationDTO) {
        final Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authentificationDTO.username(), authentificationDTO.password())
        );

        if(authenticate.isAuthenticated()) {
            return this.jwtService.generate(authentificationDTO.username());
        }
        return null;
    }

    @PutMapping("client/update/{email}")
    public ResponseEntity<Utilisateur> updateUtilisateurByEmail(
            @PathVariable String email,
            @RequestBody Utilisateur utilisateurModifie) throws ChangeSetPersister.NotFoundException {
        Utilisateur utilisateurMaj = utilisateurService.updateUtilisateurByEmail(email, utilisateurModifie);
        return ResponseEntity.ok(utilisateurMaj);
    }










}
