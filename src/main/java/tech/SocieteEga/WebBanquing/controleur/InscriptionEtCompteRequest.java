package tech.SocieteEga.WebBanquing.controleur;

import tech.SocieteEga.WebBanquing.entite.Utilisateur;
import tech.SocieteEga.WebBanquing.entite.Compte;

public class InscriptionEtCompteRequest {
    private Utilisateur utilisateur;
    private Compte compte;

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }


    public Compte getCompte() {
        return compte;
    }
}
