import {Routes} from '@angular/router';

import {FullComponent} from './layouts/full/full.component';
import {BlankComponent} from './layouts/blank/blank.component';
import {AuthGuard} from './authentication/service/auth_guard.service';

export const Approutes: Routes = [
  {
    path: '',
    component: FullComponent,
    canActivate: [AuthGuard],
    children: [
      { path: '', redirectTo: '/dashboard/trendy', pathMatch: 'full' },
      {
        path: 'dashboard',
        loadChildren: './dashboards/dashboard.module#DashboardModule'
      },
      {
        path: 'starter',
        loadChildren: './starter/starter.module#StarterModule'
      },
    ]
  },
  {
    path: '',
    component: BlankComponent,
    children: [
      {
        path: 'authentication',
        loadChildren:
          './authentication/authentication.module#AuthenticationModule'
      }
    ]
  },
  {
    path: '**',
    redirectTo: '/authentication/404'
  }
];
