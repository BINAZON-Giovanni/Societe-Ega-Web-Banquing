package tech.SocieteEga.WebBanquing.entite;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.SocieteEga.WebBanquing.Type.TypeDeTransaction;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue
    @Column(name = "numero_transaction")
    private Integer numéroTransaction;

    private LocalDate dateTransaction;

    private TypeDeTransaction typeDeTransaction;

    private Double montant;

    private String compteSource;

    private String compteDestination;

}
