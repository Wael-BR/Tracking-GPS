package tn.delivery.backend.repositories;

import tn.delivery.backend.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByImmatriculation(String immatriculation);
    List<Vehicle> findByStatut(String statut);
    List<Vehicle> findByGroupe(String groupe);
}