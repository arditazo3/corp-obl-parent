import {Routes} from '@angular/router';
import {CompanyComponent} from '../back_office/company/component/company.component';
import {CompanyCreateEditComponent} from '../back_office/company/component/company-create-edit/company-create-edit.component';
import {AgendaComponent} from './agenda/agenda.component';

export const FrontEndRoutes: Routes = [
    {
        path: '',
        children: [
            {
                path: 'agenda',
                component: AgendaComponent,
                data: {
                    title: 'Agenda Management',
                    urls: [
                        {title: 'Back Office', url: '/front-end/agenda'},
                        {title: 'Agenda management'}
                    ]
                }
            }
        ]
    }
];