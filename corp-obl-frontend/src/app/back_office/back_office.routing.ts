import {Routes} from '@angular/router';
import {CompanyComponent} from './company/component/company.component';

export const  BackOfficeRoutes: Routes = [
  {
    path: '',
    children: [
      {
        path: 'company',
        component: CompanyComponent,
        data: {
          title: 'Company Management',
          urls: [
            { title: 'Dashboard', url: '/dashboard' },
            { title: 'Company management' }
          ]
        }
      }
    ]
  }
];
