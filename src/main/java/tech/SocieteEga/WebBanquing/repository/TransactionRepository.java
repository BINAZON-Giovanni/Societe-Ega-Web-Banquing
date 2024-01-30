package tech.SocieteEga.WebBanquing.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import tech.SocieteEga.WebBanquing.Type.TypeDeTransaction;
import tech.SocieteEga.WebBanquing.entite.Transaction;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction,Integer> {
    List<Transaction> findTransactionsByCompteSource(@Param("compteSource") String compteSource);

    List<Transaction> findByCompteSource(String numeroCompte);

    List<Transaction> findByCompteSourceAndTypeDeTransaction(String numeroCompte, TypeDeTransaction typeDeTransaction);
}
