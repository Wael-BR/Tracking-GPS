import { Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { MapComponent } from './components/map/map.component';
import { VehicleManagementComponent } from './components/vehicle-management/vehicle-management.component';
import { OrderManagementComponent } from './components/order-management/order-management.component';

export const routes: Routes = [
  {
    path: '',
    component: AppComponent,
    children: [
      { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: DashboardComponent },
      { path: 'map', component: MapComponent },
      { path: 'vehicles', component: VehicleManagementComponent },
      { path: 'orders', component: OrderManagementComponent }
    ]
  }
];
