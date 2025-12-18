import { Component, OnInit, ViewChild, ElementRef, OnDestroy, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService, Vehicle } from '../../services/api.service';
import { Subscription } from 'rxjs';
import * as L from 'leaflet';

// Fix pour les icônes Leaflet
const iconRetinaUrl = 'assets/marker-icon-2x.png';
const iconUrl = 'assets/marker-icon.png';
const shadowUrl = 'assets/marker-shadow.png';
const iconDefault = L.icon({
  iconRetinaUrl,
  iconUrl,
  shadowUrl,
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  tooltipAnchor: [16, -28],
  shadowSize: [41, 41]
});

L.Marker.prototype.setIcon(iconDefault);

@Component({
  selector: 'app-map',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('mapContainer') mapContainer!: ElementRef;

  map: L.Map | null = null;
  markers: L.Marker[] = [];
  vehicles: Vehicle[] = [];
  selectedVehicle: Vehicle | null = null;
  popups: { [key: number]: L.Popup } = {};

  private intervalId: any;
  private subscription: Subscription | null = null;

  // Constante Tunis
  private readonly TUNIS_LAT = 36.8065;
  private readonly TUNIS_LNG = 10.1815;

  constructor(private readonly apiService: ApiService) {}

  ngOnInit(): void {
    this.loadVehicles();
    this.intervalId = setInterval(() => this.loadVehicles(), 5000);
  }

  ngAfterViewInit(): void {
    this.initMap();
  }

  ngOnDestroy(): void {
    if (this.intervalId) clearInterval(this.intervalId);
    if (this.subscription) this.subscription.unsubscribe();
  }

  initMap(): void {
    // Créer la carte centrée sur Tunis
    this.map = L.map(this.mapContainer.nativeElement).setView(
      [this.TUNIS_LAT, this.TUNIS_LNG],
      8
    );

    // Ajouter OpenStreetMap (gratuit, sans API key)
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
      maxZoom: 19
    }).addTo(this.map);

    // Ajouter contrôles
    L.control.zoom({ position: 'topright' }).addTo(this.map);
    L.control.scale().addTo(this.map);
  }

  loadVehicles(): void {
    this.subscription = this.apiService.getVehicles().subscribe({
      next: (data: Vehicle[]) => {
        this.vehicles = data;
        if (this.map) {
          this.updateMarkers();
        }
      },
      error: error => console.error('Erreur chargement véhicules:', error)
    });
  }

  updateMarkers(): void {
    // Supprimer anciens marqueurs
    this.markers.forEach(marker => {
      this.map?.removeLayer(marker);
    });
    this.markers = [];
    this.popups = {};

    // Ajouter nouveaux marqueurs
    this.vehicles.forEach(vehicle => {
      if (
        vehicle.derniereLatitude &&
        vehicle.derniereLongitude &&
        this.map
      ) {
        // Créer une icône personnalisée avec la couleur du véhicule
        const customIcon = this.createCustomIcon(vehicle.couleurMarqueur || '#FF0000');

        const marker = L.marker(
          [vehicle.derniereLatitude, vehicle.derniereLongitude],
          { icon: customIcon }
        );

        // Contenu du popup
        const popupContent = `
          <div class="vehicle-popup">
            <h4>${vehicle.immatriculation}</h4>
            <p><strong>Conducteur:</strong> ${vehicle.conducteurNom || 'N/A'}</p>
            <p><strong>Vitesse:</strong> ${(vehicle.derniereVitesse || 0).toFixed(2)} km/h</p>
            <p><strong>Statut:</strong> ${vehicle.statut}</p>
            <p><strong>Position:</strong><br>
              ${vehicle.derniereLatitude?.toFixed(6)}, ${vehicle.derniereLongitude?.toFixed(6)}
            </p>
            <p><strong>MAJ:</strong> ${new Date(vehicle.derniereMisAJour || Date.now()).toLocaleString('fr-FR')}</p>
          </div>
        `;

        const popup = L.popup().setContent(popupContent);
        this.popups[vehicle.id!] = popup;

        marker.bindPopup(popup);
        marker.bindTooltip(vehicle.immatriculation || 'Véhicule', { permanent: false, direction: 'top' });

        // Événement au clic
        marker.on('click', () => {
          this.selectedVehicle = vehicle;
        });

        marker.addTo(this.map);
        this.markers.push(marker);
      }
    });
  }

  // Créer une icône SVG personnalisée avec couleur
  private createCustomIcon(color: string): L.Icon {
    const svg = `
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="32" height="32" fill="${color}">
        <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm0-13c-2.76 0-5 2.24-5 5s2.24 5 5 5 5-2.24 5-5-2.24-5-5-5z"/>
      </svg>
    `;

    return L.icon({
      iconUrl: 'data:image/svg+xml;base64,' + btoa(svg),
      iconSize: [32, 32],
      iconAnchor: [16, 32],
      popupAnchor: [0, -32],
      tooltipAnchor: [16, -28]
    });
  }

  // Centrer la carte sur un véhicule
  centerOnVehicle(vehicle: Vehicle): void {
    if (vehicle.derniereLatitude && vehicle.derniereLongitude && this.map) {
      this.map.setView([vehicle.derniereLatitude, vehicle.derniereLongitude], 12);

      // Ouvrir le popup
      const popup = this.popups[vehicle.id!];
      if (popup) {
        popup.setLatLng([vehicle.derniereLatitude, vehicle.derniereLongitude]);
        popup.openOn(this.map);
      }
    }
  }

  // Dessiner une route (exemple)
  drawRoute(start: [number, number], end: [number, number]): void {
    if (this.map) {
      const polyline = L.polyline([start, end], {
        color: '#667eea',
        weight: 3,
        opacity: 0.7,
        dashArray: '5, 10'
      }).addTo(this.map);

      this.map.fitBounds(polyline.getBounds());
    }
  }
}
