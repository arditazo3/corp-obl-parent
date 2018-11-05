import { Routes } from '@angular/router';

import { DashboardHomeComponent } from './dashboard_home/dashboard-home.component';

export const DashboardRoutes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        component: DashboardHomeComponent,
        data: {
          title: 'Corporate Obligations - Dashboard',
          urls: [
            { title: 'Dashboard', url: '/dashboard' }
          ]
        }
      }
    ]
  }
];
