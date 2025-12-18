package tn.delivery.backend.repositories;

import tn.delivery.backend.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByNumeroCommande(String numeroCommande);
    List<Order> findByStatut(String statut);
    Optional<Order> findByLienPartagePublic(UUID lien);
    List<Order> findByVehiculeId(Long vehiculeId);
}