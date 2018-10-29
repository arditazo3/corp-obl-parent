import {Routes} from '@angular/router';
import {AgendaComponent} from './agenda/agenda.component';
import {AgendaControlledComponent} from './agenda-controlled/agenda-controlled.component';

export const FrontEndRoutes: Routes = [
    {
        path: '',
        children: [
            {
                path: 'agenda',
                component: AgendaComponent,
                data: {
                    title: 'Agenda Controller Management',
                    urls: [
                        {title: 'Back Office', url: '/front-end/agenda'},
                        {title: 'Agenda management'}
                    ]
                }
            },
            {
                path: 'agenda-controlled',
                component: AgendaControlledComponent,
                data: {
                    title: 'Agenda Controlled Management',
                    urls: [
                        {title: 'Back Office', url: '/front-end/agenda'},
                        {title: 'Agenda management'}
                    ]
                }
            }
        ]
    }
];