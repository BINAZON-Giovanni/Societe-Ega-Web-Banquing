package tech.SocieteEga.WebBanquing.controleur;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.SocieteEga.WebBanquing.service.CompteService;
import tech.SocieteEga.WebBanquing.entite.Compte;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
public class CompteControlleur {

    @Autowired
    private CompteService compteService;


    ///////////////////////////////////////////////// CÔTÉ ADMINISTRATEUR ///////////////////////////////////////////////////////////////////////////

    @GetMapping(path = "admin/compte")
    public List<Compte> getAllComptes() {
        return compteService.getAllComptes();
    }

    @GetMapping(path = "admin/compte/{numeroCompte}")
    public ResponseEntity<Compte> getAdminCompteByNumero(@PathVariable String numeroCompte) {
        Compte compte = compteService.getCompteByNumero(numeroCompte);
        if (compte == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(compte);
    }

    @GetMapping(path = "admin/compte/client")
    public ResponseEntity<List<Compte>> getAdminComptesByUtilisateurEmail(@RequestParam("email") String email) {
        List<Compte> comptes = compteService.getComptesByUtilisateurEmail(email);
        return ResponseEntity.ok(comptes);
    }

    @PutMapping(path = "admin/compte/{numeroCompte}")
    public ResponseEntity<Compte> updateCompte(@PathVariable String numeroCompte, @RequestBody Compte compte) {
        Compte updatedCompte = compteService.updateCompte(numeroCompte, compte);
        if (updatedCompte == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(updatedCompte);
    }

    @DeleteMapping("admin/compte/{numeroCompte}")
    public ResponseEntity<Void> deleteCompte(@PathVariable String numeroCompte) {
        boolean isDeleted = compteService.deleteCompte(numeroCompte);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    ///////////////////////////////////////////////// CÔTÉ CLIENT ///////////////////////////////////////////////////////////////////////////




    @GetMapping(path = "compte/{numeroCompte}")
    public ResponseEntity<Compte> getCompteByNumero(@PathVariable String numeroCompte) {
        Compte compte = compteService.getCompteByNumero(numeroCompte);
        if (compte == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(compte);
    }

    @GetMapping(path = "compte/client")
    public ResponseEntity<List<Compte>> getComptesByUtilisateurEmail(@RequestParam("email") String email) {
        List<Compte> comptes = compteService.getComptesByUtilisateurEmail(email);
        return ResponseEntity.ok(comptes);
    }

    @PostMapping(path = "compte")
    public ResponseEntity<Compte> createCompte(@RequestBody Compte compte) {
        log.info("Création du compte : {}", compte);
        Compte savedCompte = compteService.createCompte(compte);
        return ResponseEntity.created(URI.create("/api/comptes/" + savedCompte.getNumeroCompte())).body(savedCompte);
    }






}
