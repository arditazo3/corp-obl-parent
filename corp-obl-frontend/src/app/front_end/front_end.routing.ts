import {Routes} from '@angular/router';
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