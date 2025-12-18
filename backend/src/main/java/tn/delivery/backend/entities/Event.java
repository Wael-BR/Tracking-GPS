package tn.delivery.backend.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events", indexes = {
        @Index(name = "idx_events_vehicule_time", columnList = "vehicule_id,time DESC"),
        @Index(name = "idx_events_type", columnList = "type")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime time = LocalDateTime.now();

    @Column(name = "vehicule_id", nullable = false)
    private Long vehiculeId;

    @Column(nullable = false, length = 50)
    private String type;

    @Column(columnDefinition = "TEXT")
    private String description;

    // ✅ CORRIGÉ : Pas de scale pour Double
    @Column(name = "latitude")
    private Double latitude;

    // ✅ CORRIGÉ : Pas de scale pour Double
    @Column(name = "longitude")
    private Double longitude;

    @Column(length = 20)
    private String severity = "info";

    @Column(columnDefinition = "jsonb")
    private String details;
}