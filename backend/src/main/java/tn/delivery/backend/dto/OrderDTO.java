package tn.delivery.backend.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private Long id;
    private String numeroCommande;
    private String clientNom;
    private String clientEmail;
    private String clientTelephone;
    private String clientAdresse;
    private String adresseDepart;
    private String adresseArrivee;
    private Double latitudeDepart;
    private Double longitudeDepart;
    private Double latitudeArrivee;
    private Double longitudeArrivee;
    private Long vehiculeId;
    private String vehiculeImmatriculation;
    private String statut;
    private String descriptionColis;
    private Double poids;
    private Double volume;
    private String priorite;
    private LocalDateTime dateCreation;
    private LocalDate dateLivraisonSouhaitee;
    private UUID lienPartagePublic;
}