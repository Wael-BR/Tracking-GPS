import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService, Vehicle, Order } from '../../services/api.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit, OnDestroy {
  vehicles: Vehicle[] = [];
  orders: Order[] = [];

  vehiculesActifs = 0;
  vehiculesEnCours = 0;
  commandesEnAttente = 0;
  commandesEnCours = 0;
  commandesLivrees = 0;

  private subscriptions: Subscription[] = [];
  private intervalId: any;

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    this.loadData();
    this.intervalId = setInterval(() => this.loadData(), 10000);
  }

  ngOnDestroy(): void {
    if (this.intervalId) clearInterval(this.intervalId);
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  loadData(): void {
    const vehicleSub = this.apiService.getVehicles().subscribe(
      (data: Vehicle[]) => {
        this.vehicles = data;
        this.vehiculesActifs = data.filter(v => v.statut === 'actif').length;
        this.vehiculesEnCours = data.filter(v => v.derniereVitesse && v.derniereVitesse > 5).length;
      },
      error => console.error('Erreur chargement véhicules:', error)
    );

    const orderSub = this.apiService.getOrders().subscribe(
      (data: Order[]) => {
        this.orders = data;
        this.commandesEnAttente = data.filter(o => o.statut === 'en_attente').length;
        this.commandesEnCours = data.filter(o => o.statut === 'en_cours').length;
        this.commandesLivrees = data.filter(o => o.statut === 'livree').length;
      },
      error => console.error('Erreur chargement commandes:', error)
    );

    this.subscriptions.push(vehicleSub, orderSub);
  }
}
