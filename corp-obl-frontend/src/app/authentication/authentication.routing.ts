import { Routes } from '@angular/router';

import { NotfoundComponent } from './404/not-found.component';
import { LockComponent } from './lock/lock.component';
import { LoginComponent } from './login/login.component';

export const AuthenticationRoutes: Routes = [
  {
    path: '',
    children: [
      {
        path: '404',
        component: NotfoundComponent
      },
      {
        path: 'lock',
        component: LockComponent
      },
      {
        path: 'login',
        component: LoginComponent
      },
      {
        path: 'logout',
        redirectTo: 'login',
        component: LoginComponent,
        data: {
          title: 'Admin - Corporate obligations',
          urls: [
            { title: 'Admin', url: '/' },
            { title: 'Admin - Corporate obligations' }
          ]
        }
      }
    ]
  }
];
