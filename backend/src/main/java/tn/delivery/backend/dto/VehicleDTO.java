package tn.delivery.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDTO {
    private Long id;
    private String immatriculation;
    private String marque;
    private String modele;
    private String type;
    private Double capacitePoids;
    private Double capaciteVolume;
    private String conducteurNom;
    private String conducteurTelephone;
    private String groupe;
    private String statut;
    private String couleurMarqueur;
    private LocalDateTime dateCreation;

    @JsonProperty("derniereLatitude")
    private Double derniereLatitude;

    @JsonProperty("derniereLongitude")
    private Double derniereLongitude;

    @JsonProperty("derniereVitesse")
    private Double derniereVitesse;

    @JsonProperty("derniereMisAJour")
    private LocalDateTime derniereMisAJour;
}
