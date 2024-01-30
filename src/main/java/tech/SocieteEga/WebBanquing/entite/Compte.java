package tech.SocieteEga.WebBanquing.entite;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.SocieteEga.WebBanquing.Type.TypeDeCompte;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Table(name = "compte")
@Entity
public class Compte {
    @Id
    @Column(name = "numero_compte")
    private String numeroCompte;

    @Column(name = "solde")
    private double solde;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @Column(name = "date_dernier_depot")
    private LocalDate dernierDepot;

    @Column(name = "date_dernier_retrait")
    private LocalDate dernierRetrait;

    @Column(name="TypeCompte" )
    private TypeDeCompte typeCompte;

    @ManyToOne
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;

}
