import { Routes } from '@angular/router';

import { Dashboard8Component } from './dashboard8/dashboard8.component';

export const DashboardRoutes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        component: Dashboard8Component,
        data: {
          title: 'Dashboard',
          urls: [
            { title: 'Dashboard', url: '/dashboard' },
            { title: 'Dashboard' }
          ]
        }
      }
    ]
  }
];
