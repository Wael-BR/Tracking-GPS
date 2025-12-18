package tn.delivery.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, length = 20)
    private String immatriculation;

    @Column(length = 50)
    private String marque;

    @Column(length = 50)
    private String modele;

    @Column(length = 30)
    private String type;

    // ✅ CORRIGÉ : Pas de scale pour Double
    @Column(name = "capacite_poids")
    private Double capacitePoids;

    // ✅ CORRIGÉ : Pas de scale pour Double
    @Column(name = "capacite_volume")
    private Double capaciteVolume;

    @Column(name = "conducteur_nom", length = 100)
    private String conducteurNom;

    @Column(name = "conducteur_telephone", length = 20)
    private String conducteurTelephone;

    @Column(name = "conducteur_email", length = 100)
    private String conducteurEmail;

    @Column(length = 50)
    private String groupe;

    @Column(length = 20)
    private String statut = "actif";

    @Column(name = "couleur_marqueur", length = 7)
    private String couleurMarqueur = "#FF0000";

    @Column(name = "date_creation")
    private LocalDateTime dateCreation = LocalDateTime.now();

    @Transient
    private Double derniereLatitude;

    @Transient
    private Double derniereLongitude;

    @Transient
    private Double derniereVitesse;

    @Transient
    private LocalDateTime derniereMisAJour;
}
