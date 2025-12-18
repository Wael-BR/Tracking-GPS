package tn.delivery.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "gps_positions", indexes = {
        @Index(name = "idx_positions_vehicule_time", columnList = "vehicule_id,time DESC"),
        @Index(name = "idx_positions_commande", columnList = "commande_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GPSPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime time = LocalDateTime.now();

    @Column(name = "vehicule_id", nullable = false)
    private Long vehiculeId;

    // ✅ CORRIGÉ : Pas de scale pour Double
    @Column(nullable = false)
    private Double latitude;

    // ✅ CORRIGÉ : Pas de scale pour Double
    @Column(nullable = false)
    private Double longitude;

    // ✅ CORRIGÉ : Pas de scale pour Double
    @Column(name = "vitesse")
    private Double vitesse;

    // ✅ CORRIGÉ : Pas de scale pour Double
    @Column(name = "orientation")
    private Double orientation;

    // ✅ CORRIGÉ : Pas de scale pour Double
    @Column(name = "altitude")
    private Double altitude;

    @Column(length = 20)
    private String source = "simule";

    @Column(name = "commande_id")
    private Long commandeId;
}
