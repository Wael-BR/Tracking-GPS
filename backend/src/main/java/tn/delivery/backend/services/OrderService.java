package tn.delivery.backend.services;

import tn.delivery.backend.entities.Order;
import tn.delivery.backend.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order createOrder(Order order) {
        log.info("Création commande : {}", order.getNumeroCommande());
        return orderRepository.save(order);
    }

    public Order updateOrder(Long id, Order orderData) {
        Order order = orderRepository.findById(id).orElseThrow();
        order.setStatut(orderData.getStatut());
        order.setVehicule(orderData.getVehicule());
        order.setClientNom(orderData.getClientNom());
        log.info("Mise à jour commande : {}", id);
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
        log.info("Suppression commande : {}", id);
    }

    public Optional<Order> getPublicOrder(UUID lien) {
        return orderRepository.findByLienPartagePublic(lien);
    }

    public List<Order> getOrdersByStatut(String statut) {
        return orderRepository.findByStatut(statut);
    }
}