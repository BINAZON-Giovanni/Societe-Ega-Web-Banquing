package tech.SocieteEga.WebBanquing.repository;

import org.springframework.data.repository.CrudRepository;
import tech.SocieteEga.WebBanquing.entite.Validation;

import java.util.Optional;

public interface ValidationRepository extends CrudRepository<Validation, Integer> {

    Optional<Validation> findByCode(String code);
}
