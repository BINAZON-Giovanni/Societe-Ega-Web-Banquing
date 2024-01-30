package tech.SocieteEga.WebBanquing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.SocieteEga.WebBanquing.Type.TypeDeTransaction;
import tech.SocieteEga.WebBanquing.repository.TransactionRepository;
import tech.SocieteEga.WebBanquing.entite.Compte;
import tech.SocieteEga.WebBanquing.entite.Transaction;
import tech.SocieteEga.WebBanquing.repository.CompteRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CompteRepository compteRepository;

    public List<Transaction> getAllTransactions() {
        return (List<Transaction>) transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsByCompteSource(String compteSource) {
        return transactionRepository.findTransactionsByCompteSource(compteSource);
    }

    public Compte Versement(String numeroCompte, Transaction versement ) {

        Compte compteToUpdate = compteRepository.findByNumeroCompte(numeroCompte);
        Transaction transaction = new Transaction();

        double nouveauSolde = compteToUpdate.getSolde() + versement.getMontant();
        compteToUpdate.setSolde(nouveauSolde);
        compteToUpdate.setDernierDepot(LocalDate.from(LocalDateTime.now()));

        transaction.setCompteSource(numeroCompte);
        transaction.setMontant(Double.valueOf(versement.getMontant()));
        transaction.setDateTransaction(LocalDate.from(LocalDateTime.now()));
        transaction.setTypeDeTransaction(TypeDeTransaction.VERSEMENT);

        transactionRepository.save(transaction);
        compteRepository.save(compteToUpdate);

        return compteToUpdate;
    }

    public Compte updateSoldeCompte(Compte compte) {
        compteRepository.save(compte);
        return compte;
    }




    public Compte Retrait(String numeroCompte, Transaction retrait ) {

        Compte compteToUpdate = compteRepository.findByNumeroCompte(numeroCompte);
        Transaction transaction = new Transaction();

        double nouveauSolde = compteToUpdate.getSolde() - retrait.getMontant();
        compteToUpdate.setSolde(nouveauSolde);
        compteToUpdate.setDernierRetrait(LocalDate.from(LocalDateTime.now()));

        transaction.setCompteSource(numeroCompte);
        transaction.setMontant(Double.valueOf(retrait.getMontant()));
        transaction.setDateTransaction(LocalDate.from(LocalDateTime.now()));
        transaction.setTypeDeTransaction(TypeDeTransaction.RETRAIT);

        transactionRepository.save(transaction);
        compteRepository.save(compteToUpdate);

        return compteToUpdate;
    }

    public Compte Virement(String numeroCompte, Transaction virement ) {

        Compte compteSrc = compteRepository.findByNumeroCompte(numeroCompte);

        Compte compteDst = compteRepository.findByNumeroCompte(virement.getCompteDestination());

        double nouveauSoldeSrc = compteSrc.getSolde() - virement.getMontant();
        double nouveauSoldeDst = compteDst.getSolde() + virement.getMontant();

        compteSrc.setSolde(nouveauSoldeSrc);
        compteDst.setSolde(nouveauSoldeDst);

        Transaction transaction = new Transaction();
        transaction.setCompteSource(numeroCompte);
        transaction.setCompteDestination(virement.getCompteDestination());
        transaction.setMontant(Double.valueOf(virement.getMontant()));
        transaction.setDateTransaction(LocalDate.from(LocalDateTime.now()));
        transaction.setTypeDeTransaction(TypeDeTransaction.VIREMENT);

        compteRepository.save(compteSrc);
        compteRepository.save(compteDst);

        transactionRepository.save(transaction);

        return compteSrc;

    }





    public void effectuerVirement(String numCompteSrc, String numCompteDest, double montant) {
        Compte compteSrc = compteRepository.findByNumeroCompte(numCompteSrc);
        Compte compteDest = compteRepository.findByNumeroCompte(numCompteDest);
        if(compteSrc == null || compteDest == null) {
            throw new IllegalArgumentException("Un ou plusieurs comptes n'existent pas");
        }
        if(compteSrc.getSolde() < montant) {
            throw new IllegalArgumentException("Solde insuffisant");
        }
        double nouveauSoldeSrc = compteSrc.getSolde() - montant;
        double nouveauSoldeDest = compteDest.getSolde() + montant;
        compteSrc.setSolde(nouveauSoldeSrc);
        compteDest.setSolde(nouveauSoldeDest);
        compteRepository.save(compteSrc);
        compteRepository.save(compteDest);
    }

    // Dans le service TransactionService
    public List<Transaction> getVersementsByCompteSourceAndType(String numeroCompte) {
        return transactionRepository.findByCompteSourceAndTypeDeTransaction(numeroCompte, TypeDeTransaction.VERSEMENT);
    }

    public List<Transaction> getRetraitByCompteSourceAndType(String numeroCompte) {
        return transactionRepository.findByCompteSourceAndTypeDeTransaction(numeroCompte, TypeDeTransaction.RETRAIT);
    }

    public List<Transaction> getVirementByCompteSourceAndType(String numeroCompte) {
        return transactionRepository.findByCompteSourceAndTypeDeTransaction(numeroCompte, TypeDeTransaction.VIREMENT);
    }



}
