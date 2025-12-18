package tn.delivery.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, length = 50)
    private String numeroCommande;

    @NotBlank
    @Column(length = 100)
    private String clientNom;

    @Column(length = 100)
    private String clientEmail;

    @Column(name = "client_telephone", length = 20)
    private String clientTelephone;

    @Column(name = "client_adresse")
    private String clientAdresse;

    @Column(name = "adresse_depart")
    private String adresseDepart;

    @Column(name = "adresse_arrivee")
    private String adresseArrivee;

    // ✅ CORRIGÉ : Pas de scale pour Double
    @Column(name = "latitude_depart")
    private Double latitudeDepart;

    // ✅ CORRIGÉ : Pas de scale pour Double
    @Column(name = "longitude_depart")
    private Double longitudeDepart;

    // ✅ CORRIGÉ : Pas de scale pour Double
    @Column(name = "latitude_arrivee")
    private Double latitudeArrivee;

    // ✅ CORRIGÉ : Pas de scale pour Double
    @Column(name = "longitude_arrivee")
    private Double longitudeArrivee;

    @ManyToOne
    @JoinColumn(name = "vehicule_id")
    private Vehicle vehicule;

    @Column(length = 30)
    private String statut = "en_attente";

    @Column(columnDefinition = "TEXT")
    private String descriptionColis;

    // ✅ CORRIGÉ : Pas de scale pour Double
    @Column(name = "poids")
    private Double poids;

    // ✅ CORRIGÉ : Pas de scale pour Double
    @Column(name = "volume")
    private Double volume;

    @Column(length = 20)
    private String priorite = "normal";

    @Column(name = "date_creation")
    private LocalDateTime dateCreation = LocalDateTime.now();

    @Column(name = "date_livraison_souhaitee")
    private LocalDate dateLivraisonSouhaitee;

    @Column(name = "date_livraison_reelle")
    private LocalDateTime dateLivraisonReelle;

    @Column(name = "lien_partage_public")
    private UUID lienPartagePublic = UUID.randomUUID();

    @Column(name = "lien_expire_le")
    private LocalDateTime lienExpireLe;
}