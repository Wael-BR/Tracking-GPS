import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService, Order, Vehicle } from '../../services/api.service';

@Component({
  selector: 'app-order-management',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './order-management.component.html',
  styleUrls: ['./order-management.component.css']
})
export class OrderManagementComponent implements OnInit {
  orders: Order[] = [];
  vehicles: Vehicle[] = [];
  showForm = false;
  isEditMode = false;
  formData: Order = this.getEmptyOrder();
  constructor(private readonly apiService: ApiService) {}
  ngOnInit(): void {
    this.loadOrders();
    this.loadVehicles();
  }
  getEmptyOrder(): Order {
    return {
      numeroCommande: '',
      clientNom: '',
      adresseDepart: '',
      adresseArrivee: '',
      statut: 'en_attente'
    };
  }
  loadOrders(): void {
    this.apiService.getOrders().subscribe({
      next: (data: Order[]) => {
        this.orders = data;
      },
      error: (error) => console.error('Erreur:', error)
    });
  }
  loadVehicles(): void {
    this.apiService.getVehicles().subscribe({
      next: (data: Vehicle[]) => {
        this.vehicles = data;
      },
      error: (error) => console.error('Erreur:', error)
    });
  }
  openForm(order?: Order): void {
    this.showForm = true;
    if (order) {
      this.isEditMode = true;
      this.formData = { ...order };
    } else {
      this.isEditMode = false;
      this.formData = this.getEmptyOrder();
    }
  }
  saveOrder(): void {
    if (this.isEditMode && this.formData.id) {
      this.apiService.updateOrder(this.formData.id, this.formData).subscribe({
        next: () => {
          this.loadOrders();
          this.showForm = false;
        },
        error: (error) => console.error('Erreur:', error)
      });
    } else {
      this.apiService.createOrder(this.formData).subscribe({
        next: () => {
          this.loadOrders();
          this.showForm = false;
        },
        error: (error) => console.error('Erreur:', error)
      });
    }
  }
  deleteOrder(id: number | undefined): void {
    if (id && confirm('Supprimer cette commande ?')) {
      this.apiService.deleteOrder(id).subscribe({
        next: () => this.loadOrders(),
        error: (error) => console.error('Erreur:', error)
      });
    }
  }
  closeForm(): void {
    this.showForm = false;
  }
  getVehicleName(vehiculeId?: number): string {
    const vehicle = this.vehicles.find(v => v.id === vehiculeId);
    return vehicle ? vehicle.immatriculation : '-';
  }
}
