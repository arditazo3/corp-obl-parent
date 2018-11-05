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
                    title: 'Corporate Obligations - Agenda Controller',
                    urls: [
                        {title: 'MENU.AGENDA_CONTROLLER', url: '/front-end/agenda'}
                    ]
                }
            },
            {
                path: 'agenda-controlled',
                component: AgendaControlledComponent,
                data: {
                    title: 'Corporate Obligations - Agenda Controlled',
                    urls: [
                        {title: 'MENU.AGENDA_CONTROLLED', url: '/front-end/agenda'}
                    ]
                }
            }
        ]
    }
];