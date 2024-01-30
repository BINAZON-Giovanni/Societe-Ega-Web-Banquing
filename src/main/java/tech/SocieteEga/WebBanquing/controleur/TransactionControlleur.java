package tech.SocieteEga.WebBanquing.controleur;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.SocieteEga.WebBanquing.service.TransactionService;
import tech.SocieteEga.WebBanquing.entite.Compte;
import tech.SocieteEga.WebBanquing.entite.Transaction;
import tech.SocieteEga.WebBanquing.service.CompteService;

import java.util.List;

@RestController
public class TransactionControlleur {

    @Autowired
    private CompteService compteService;

    @Autowired
    private TransactionService transactionService;

    ///////////////////////////////////////////////// CÔTÉ ADMINISTRATEUR ///////////////////////////////////////////////////////////////////////////
    @GetMapping(path = "admin/transaction/liste")
    public List<Transaction> getAlltransaction() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("admin/transaction/compte/compteSource")
    public ResponseEntity<List<Transaction>> getAdminTransactionsByCompteSource(@RequestParam("compteSource") String compteSource) {
        List<Transaction> transactions = transactionService.getTransactionsByCompteSource(compteSource);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("admin/transaction/versements/{numeroCompte}")
    public ResponseEntity<List<Transaction>> getAdminVersementsByCompteSource(@PathVariable String numeroCompte) {
        List<Transaction> versements = transactionService.getVersementsByCompteSourceAndType(numeroCompte);
        return ResponseEntity.ok(versements);
    }

    @GetMapping("admin/transaction/retrait/{numeroCompte}")
    public ResponseEntity<List<Transaction>> getAdminRetraitByCompteSource(@PathVariable String numeroCompte) {
        List<Transaction> retrait = transactionService.getRetraitByCompteSourceAndType(numeroCompte);
        return ResponseEntity.ok(retrait);
    }

    @GetMapping("admin/transaction/virement/{numeroCompte}")
    public ResponseEntity<List<Transaction>> getAdminvirementByCompteSource(@PathVariable String numeroCompte) {
        List<Transaction> virement = transactionService.getVirementByCompteSourceAndType(numeroCompte);
        return ResponseEntity.ok(virement);
    }



    /////////////////////////////////////////////////      CÔTÉ CLIENT         ///////////////////////////////////////////////////////////////////////////


    @GetMapping("transaction/compte/compteSource")
    public ResponseEntity<List<Transaction>> getTransactionsByCompteSource(@RequestParam("compteSource") String compteSource) {
        List<Transaction> transactions = transactionService.getTransactionsByCompteSource(compteSource);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("transaction/versement")
    public ResponseEntity<String> effectuerVersement(@RequestBody Transaction versement) {
        String numeroCompte = String.valueOf(versement.getCompteSource());
        double montant = versement.getMontant();
        if (montant <= 0) {
            return ResponseEntity.badRequest().body(" Montant invalide !");
        }
        Compte compte = compteService.getCompteByNumero(numeroCompte);
        if (compte != null) {
            transactionService.Versement(numeroCompte, versement);
            return ResponseEntity.ok("Le versement a été effectué avec succès !");
        } else {
            return ResponseEntity.badRequest().body("Le compte n'existe pas !");
        }
    }

    @PostMapping("transaction/retrait")
    public ResponseEntity<String> effectuerRetrait(@RequestBody Transaction retrait) {
        String numeroCompte = String.valueOf(retrait.getCompteSource());
        double montant = retrait.getMontant();
        if (montant <= 0) {
            return ResponseEntity.badRequest().body(" Montant invalide !");
        }
        Compte compte = compteService.getCompteByNumero(numeroCompte);
        if (compte != null) {
            transactionService.Retrait(numeroCompte, retrait);
            return ResponseEntity.ok("Le retrait  a bien  été effectué avec succès !");
        } else {
            return ResponseEntity.badRequest().body("Le compte n'existe pas !");
        }
    }

    @PostMapping("transaction/virement")
    public ResponseEntity<String> effectuerVirement(@RequestBody Transaction virement) {
        String numeroCompte = String.valueOf(virement.getCompteSource());
        double montant = virement.getMontant();
        if (montant <= 0) {
            return ResponseEntity.badRequest().body(" Montant invalide !");
        }
        Compte compte = compteService.getCompteByNumero(numeroCompte);
        if (compte != null) {
            transactionService.Virement(numeroCompte, virement);
            return ResponseEntity.ok("Le virement a bien  été effectué avec succès !");
        } else {
            return ResponseEntity.badRequest().body("Le compte n'existe pas !");
        }
    }

    @GetMapping("transaction/versements/{numeroCompte}")
    public ResponseEntity<List<Transaction>> getVersementsByCompteSource(@PathVariable String numeroCompte) {
        List<Transaction> versements = transactionService.getVersementsByCompteSourceAndType(numeroCompte);
        return ResponseEntity.ok(versements);
    }

    @GetMapping("transaction/retrait/{numeroCompte}")
    public ResponseEntity<List<Transaction>> getRetraitByCompteSource(@PathVariable String numeroCompte) {
        List<Transaction> retrait = transactionService.getRetraitByCompteSourceAndType(numeroCompte);
        return ResponseEntity.ok(retrait);
    }

    @GetMapping("transaction/virement/{numeroCompte}")
    public ResponseEntity<List<Transaction>> getvirementByCompteSource(@PathVariable String numeroCompte) {
        List<Transaction> virement = transactionService.getVirementByCompteSourceAndType(numeroCompte);
        return ResponseEntity.ok(virement);
    }




}
