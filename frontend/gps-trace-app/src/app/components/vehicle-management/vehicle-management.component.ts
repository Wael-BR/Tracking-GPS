import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService, Vehicle } from '../../services/api.service';

@Component({
  selector: 'app-vehicle-management',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './vehicle-management.component.html',
  styleUrls: ['./vehicle-management.component.css']
})
export class VehicleManagementComponent implements OnInit {
  vehicles: Vehicle[] = [];
  showForm = false;
  isEditMode = false;
  formData: Vehicle = this.getEmptyVehicle();

  constructor(private readonly apiService: ApiService) {}

  ngOnInit(): void {
    this.loadVehicles();
  }

  getEmptyVehicle(): Vehicle {
    return {
      immatriculation: '',
      marque: '',
      modele: '',
      type: 'camion',
      statut: 'actif',
      couleurMarqueur: '#FF0000'
    };
  }

  loadVehicles(): void {
    this.apiService.getVehicles().subscribe({
      next: (data: Vehicle[]) => {
        this.vehicles = data;
      },
      error: (error) => console.error('Erreur:', error)
    });
  }

  openForm(vehicle?: Vehicle): void {
    this.showForm = true;
    if (vehicle) {
      this.isEditMode = true;
      this.formData = { ...vehicle };
    } else {
      this.isEditMode = false;
      this.formData = this.getEmptyVehicle();
    }
  }

  saveVehicle(): void {
    if (this.isEditMode && this.formData.id) {
      this.apiService.updateVehicle(this.formData.id, this.formData).subscribe({
        next: () => {
          this.loadVehicles();
          this.showForm = false;
        },
        error: (error) => console.error('Erreur mise à jour:', error)
      });
    } else {
      this.apiService.createVehicle(this.formData).subscribe({
        next: () => {
          this.loadVehicles();
          this.showForm = false;
        },
        error: (error) => console.error('Erreur création:', error)
      });
    }
  }

  deleteVehicle(id: number | undefined): void {
    if (id && confirm('Supprimer ce véhicule ?')) {
      this.apiService.deleteVehicle(id).subscribe({
        next: () => this.loadVehicles(),
        error: (error) => console.error('Erreur suppression:', error)
      });
    }
  }

  closeForm(): void {
    this.showForm = false;
  }
}
