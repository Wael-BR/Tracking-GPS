package tn.delivery.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerts", indexes = {
        @Index(name = "idx_alerts_vehicule", columnList = "vehicule_id"),
        @Index(name = "idx_alerts_statut", columnList = "statut"),
        @Index(name = "idx_alerts_lue", columnList = "lue")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "vehicule_id")
    private Vehicle vehicule;

    @NotBlank
    @Column(length = 50)
    private String type;

    @Column(length = 255)
    private String seuil;

    @Column(length = 20)
    private String statut = "active"; // active, resolue, ignoree

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation = LocalDateTime.now();

    @Column(name = "date_resolution")
    private LocalDateTime dateResolution;

    @Column
    private Boolean lue = false;
}
