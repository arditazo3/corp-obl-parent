import {Routes} from '@angular/router';
import {CompanyComponent} from './company/component/company.component';
import {CompanyCreateEditComponent} from './company/component/company-create-edit/company-create-edit.component';
import {CompanyAssociateUsersComponent} from './company/component/company-associate-users/company-associate-users.component';

export const BackOfficeRoutes: Routes = [
    {
        path: '',
        children: [
            {
                path: 'company',
                component: CompanyComponent,
                data: {
                    title: 'Company Management',
                    urls: [
                        {title: 'Back Office', url: '/back-office/company'},
                        {title: 'Company management'}
                    ]
                }
            },
            {
                path: 'company/create',
                component: CompanyCreateEditComponent,
                data: {
                    title: 'Company Management',
                    urls: [
                        {title: 'Back Office', url: '/back-office/company'},
                        {title: 'Company management'}
                    ]
                }
            },
            {
                path: 'company/edit',
                component: CompanyCreateEditComponent,
                data: {
                    title: 'Company Management',
                    urls: [
                        {title: 'Back Office', url: '/back-office/company'},
                        {title: 'Company management'}
                    ]
                }
            },
            {
                path: 'company/associate-users-company',
                component: CompanyAssociateUsersComponent,
                data: {
                    title: 'Company Management',
                    urls: [
                        {title: 'Back Office', url: '/back-office/company'},
                        {title: 'Company management'}
                    ]
                }
            }
        ]
    }
];
