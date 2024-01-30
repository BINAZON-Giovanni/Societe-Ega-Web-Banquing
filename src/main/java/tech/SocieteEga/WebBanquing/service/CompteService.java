package tech.SocieteEga.WebBanquing.service;

import org.iban4j.IbanFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.SocieteEga.WebBanquing.entite.Compte;
import tech.SocieteEga.WebBanquing.repository.CompteRepository;

import java.time.LocalDate;
import java.util.List;
import org.iban4j.Iban;

@Service
public class CompteService {
    @Autowired
    private CompteRepository compteRepository;


    public List<Compte> getAllComptes() {
        return (List<Compte>) compteRepository.findAll();
    }
    public Compte getCompteByNumero(String numeroCompte) {
        return compteRepository.findByNumeroCompte(numeroCompte);
    }

    public Compte createCompte(Compte compte) {
        try {
            Iban iban = Iban.random();
            String numeroCompte = iban.toString();
            LocalDate dateCreation = LocalDate.now();
            compte.setSolde(0);
            compte.setNumeroCompte(numeroCompte);
            compte.setDateCreation(dateCreation);
            return compteRepository.save(compte);
        } catch (IbanFormatException e) {
            // Gérer l'exception liée au format IBAN
            e.printStackTrace(); // Vous pouvez personnaliser la gestion des erreurs ici
            return null; // Ou lancez une exception appropriée si nécessaire
        }


    }

    public boolean deleteCompte(String id) {
        compteRepository.deleteById(String.valueOf(id));
        return false;
    }

    public Compte getStatistiquesCompte(String numeroCompte) {
        Compte Compte = compteRepository.findByNumeroCompte(numeroCompte);
        Compte stats = new Compte();
        stats.setDernierDepot(Compte.getDernierDepot());
        stats.setDernierRetrait(Compte.getDernierRetrait());
        return stats;
    }

    public Compte updateCompte(String numeroCompte, Compte compte) {
        Compte compteEnBase = getCompteByNumero(numeroCompte);
        if (compteEnBase != null) {
            compteEnBase.setSolde(compte.getSolde());
            // Mettre à jour les autres propriétés du compte si nécessaire
            //compteEnBase.setDateCreation(LocalDate.now()); // Mise à jour de la date de dernière modification
            compteRepository.save(compteEnBase); // Sauvegarde du compte modifié en base de données
        }
        return compteEnBase;
    }


    public List<Compte> getComptesByUtilisateurEmail(String email) {
        return compteRepository.findComptesByUtilisateurEmail(email);
    }
}
