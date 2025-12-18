import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Vehicle {
  conducteurTelephone?: string;
  id?: number;
  immatriculation: string;
  marque: string;
  modele: string;
  type: string;
  capacitePoids?: number;
  conducteurNom?: string;
  statut: string;
  couleurMarqueur: string;
  derniereLatitude?: number;
  derniereLongitude?: number;
  derniereVitesse?: number;
  derniereMisAJour?: Date;
}

export interface Order {
  id?: number;
  numeroCommande: string;
  clientNom: string;
  clientEmail?: string;
  clientAdresse?: string;
  clientTelephone?: string;
  adresseDepart: string;
  adresseArrivee: string;
  latitudeDepart?: number;
  longitudeDepart?: number;
  latitudeArrivee?: number;
  longitudeArrivee?: number;
  vehiculeId?: number;
  vehiculeImmatriculation?: string;
  statut: string;
  descriptionColis?: string;
  poids?: number;
  priorite?: string;
  lienPartagePublic?: string;
}

export interface GPSPosition {
  id?: number;
  vehiculeId: number;
  latitude: number;
  longitude: number;
  vitesse?: number;
  orientation?: number;
  time: Date;
  source: string;
  commandeId?: number;
}

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private readonly apiUrl = 'http://localhost:8081/api';

  constructor(private readonly http: HttpClient) {}

  // === VEHICLES ===
  getVehicles(): Observable<Vehicle[]> {
    return this.http.get<Vehicle[]>(`${this.apiUrl}/vehicles`);
  }

  getVehicle(id: number): Observable<Vehicle> {
    return this.http.get<Vehicle>(`${this.apiUrl}/vehicles/${id}`);
  }

  createVehicle(vehicle: Vehicle): Observable<Vehicle> {
    return this.http.post<Vehicle>(`${this.apiUrl}/vehicles`, vehicle);
  }

  updateVehicle(id: number, vehicle: Vehicle): Observable<Vehicle> {
    return this.http.put<Vehicle>(`${this.apiUrl}/vehicles/${id}`, vehicle);
  }

  deleteVehicle(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/vehicles/${id}`);
  }

  // === ORDERS ===
  getOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.apiUrl}/orders`);
  }

  getOrder(id: number): Observable<Order> {
    return this.http.get<Order>(`${this.apiUrl}/orders/${id}`);
  }

  createOrder(order: Order): Observable<Order> {
    return this.http.post<Order>(`${this.apiUrl}/orders`, order);
  }

  updateOrder(id: number, order: Order): Observable<Order> {
    return this.http.put<Order>(`${this.apiUrl}/orders/${id}`, order);
  }

  deleteOrder(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/orders/${id}`);
  }

  getPublicOrder(lien: string): Observable<Order> {
    return this.http.get<Order>(`${this.apiUrl}/orders/public/${lien}`);
  }
}
