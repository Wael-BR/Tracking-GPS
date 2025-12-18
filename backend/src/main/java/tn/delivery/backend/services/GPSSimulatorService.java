package tn.delivery.backend.services;


import tn.delivery.backend.entities.GPSPosition;
import tn.delivery.backend.entities.Vehicle;
import tn.delivery.backend.repositories.GPSPositionRepository;
import tn.delivery.backend.repositories.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
public class GPSSimulatorService {
    private final GPSPositionRepository gpsPositionRepository;
    private final VehicleRepository vehicleRepository;
    private final Random random = new Random();

    // Routes simulées pour Tunis - Sfax
    private static final double TUNIS_LAT = 36.8065;
    private static final double TUNIS_LNG = 10.1815;
    private static final double SFAX_LAT = 34.7405;
    private static final double SFAX_LNG = 10.7673;

    @Scheduled(fixedRate = 10000) // Toutes les 10 secondes
    public void simulateGPSData() {
        List<Vehicle> vehicles = vehicleRepository.findByStatut("actif");

        for (Vehicle vehicle : vehicles) {
            // Simuler mouvement entre Tunis et Sfax
            double lat = TUNIS_LAT + (SFAX_LAT - TUNIS_LAT) * (0.1 + random.nextDouble() * 0.8);
            double lng = TUNIS_LNG + (SFAX_LNG - TUNIS_LNG) * (0.1 + random.nextDouble() * 0.8);
            double vitesse = 30 + random.nextDouble() * 70; // 30-100 km/h
            double orientation = random.nextDouble() * 360;

            GPSPosition position = GPSPosition.builder()
                    .vehiculeId(vehicle.getId())
                    .latitude(lat)
                    .longitude(lng)
                    .vitesse(vitesse)
                    .orientation(orientation)
                    .altitude(50.0 + random.nextDouble() * 20)
                    .source("simule")
                    .time(LocalDateTime.now())
                    .build();

            gpsPositionRepository.save(position);
            vehicle.setDerniereLatitude(lat);
            vehicle.setDerniereLongitude(lng);
            vehicle.setDerniereVitesse(vitesse);
            vehicle.setDerniereMisAJour(LocalDateTime.now());
        }
    }
}