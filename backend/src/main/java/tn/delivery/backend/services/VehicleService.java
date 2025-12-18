package tn.delivery.backend.services;

import tn.delivery.backend.entities.Vehicle;
import tn.delivery.backend.repositories.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Optional<Vehicle> getVehicleById(Long id) {
        return vehicleRepository.findById(id);
    }

    public Vehicle createVehicle(Vehicle vehicle) {
        log.info("Création véhicule : {}", vehicle.getImmatriculation());
        return vehicleRepository.save(vehicle);
    }

    public Vehicle updateVehicle(Long id, Vehicle vehicleData) {
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow();
        vehicle.setMarque(vehicleData.getMarque());
        vehicle.setModele(vehicleData.getModele());
        vehicle.setType(vehicleData.getType());
        vehicle.setStatut(vehicleData.getStatut());
        vehicle.setConducteurNom(vehicleData.getConducteurNom());
        vehicle.setConducteurEmail(vehicleData.getConducteurEmail());
        log.info("Mise à jour véhicule : {}", id);
        return vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
        log.info("Suppression véhicule : {}", id);
    }

    public List<Vehicle> getVehiclesByStatut(String statut) {
        return vehicleRepository.findByStatut(statut);
    }
}