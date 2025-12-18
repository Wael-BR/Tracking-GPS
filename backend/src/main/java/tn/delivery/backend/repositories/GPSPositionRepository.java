package tn.delivery.backend.repositories;

import tn.delivery.backend.entities.GPSPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GPSPositionRepository extends JpaRepository<GPSPosition, Long> {
    List<GPSPosition> findByVehiculeIdOrderByTimeDesc(Long vehiculeId);

    @Query("SELECT p FROM GPSPosition p WHERE p.vehiculeId = ?1 AND p.time >= ?2 AND p.time <= ?3 ORDER BY p.time ASC")
    List<GPSPosition> findByVehiculeIdAndTimeBetween(Long vehiculeId, LocalDateTime from, LocalDateTime to);

    @Query(value = "SELECT DISTINCT ON (vehicule_id) vehicule_id, latitude, longitude, vitesse, orientation, time " +
            "FROM gps_positions ORDER BY vehicule_id, time DESC", nativeQuery = true)
    List<GPSPosition> findLatestPositionsByVehicle();
}