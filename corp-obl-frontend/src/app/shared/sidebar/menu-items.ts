import {RouteInfo} from './sidebar.metadata';
import {AuthorityEnum} from '../common/api/enum/authority.enum';

export const ROUTES: RouteInfo[] = [
    {
        path: '',
        title: 'MENU.BACK_OFFICE',
        icon: 'mdi mdi-desktop-mac',
        class: 'has-arrow',
        ddclass: '',
        extralink: false,
        authorities: [AuthorityEnum[AuthorityEnum.CORPOBLIG_ADMIN], AuthorityEnum[AuthorityEnum.CORPOBLIG_BACKOFFICE_FOREIGN], AuthorityEnum[AuthorityEnum.CORPOBLIG_BACKOFFICE_INLAND], AuthorityEnum[AuthorityEnum.CORPOBLIG_USER_ADMIN_COMPANY]],
        submenu: [
            {
                path: '/back-office/company',
                title: 'MENU.COMPANY',
                icon: 'mdi mdi-briefcase',
                class: '',
                ddclass: '',
                extralink: false,
                submenu: [],
                authorities: [AuthorityEnum[AuthorityEnum.CORPOBLIG_ADMIN], AuthorityEnum[AuthorityEnum.CORPOBLIG_BACKOFFICE_FOREIGN], AuthorityEnum[AuthorityEnum.CORPOBLIG_BACKOFFICE_INLAND]]
            },
            {
                path: '/back-office/office',
                title: 'MENU.OFFICE',
                icon: 'mdi mdi-glassdoor',
                class: '',
                ddclass: '',
                extralink: false,
                submenu: [],
                authorities: [AuthorityEnum[AuthorityEnum.CORPOBLIG_ADMIN], AuthorityEnum[AuthorityEnum.CORPOBLIG_BACKOFFICE_FOREIGN], AuthorityEnum[AuthorityEnum.CORPOBLIG_BACKOFFICE_INLAND]]
            },
            {
                path: '/back-office/topic',
                title: 'MENU.TOPIC',
                icon: 'mdi mdi-network-question',
                class: '',
                ddclass: '',
                extralink: false,
                submenu: [],
                authorities: [AuthorityEnum[AuthorityEnum.CORPOBLIG_ADMIN], AuthorityEnum[AuthorityEnum.CORPOBLIG_BACKOFFICE_FOREIGN], AuthorityEnum[AuthorityEnum.CORPOBLIG_BACKOFFICE_INLAND]]
            },
            {
                path: '/back-office/consultant',
                title: 'MENU.CONSULTANT',
                icon: 'mdi mdi-account-settings',
                class: '',
                ddclass: '',
                extralink: false,
                submenu: [],
                authorities: [AuthorityEnum[AuthorityEnum.CORPOBLIG_ADMIN], AuthorityEnum[AuthorityEnum.CORPOBLIG_BACKOFFICE_FOREIGN], AuthorityEnum[AuthorityEnum.CORPOBLIG_BACKOFFICE_INLAND], AuthorityEnum[AuthorityEnum.CORPOBLIG_USER_ADMIN_COMPANY]]
            },
            {
                path: '/back-office/task',
                title: 'MENU.TASK',
                icon: 'mdi mdi-table-large',
                class: '',
                ddclass: '',
                extralink: false,
                submenu: [],
                authorities: [AuthorityEnum[AuthorityEnum.CORPOBLIG_ADMIN], AuthorityEnum[AuthorityEnum.CORPOBLIG_BACKOFFICE_FOREIGN]]
            },
            {
                path: '/back-office/office-task',
                title: 'MENU.OFFICE_TASK',
                icon: 'mdi mdi-file-document-box',
                class: '',
                ddclass: '',
                extralink: false,
                submenu: [],
                authorities: [AuthorityEnum[AuthorityEnum.CORPOBLIG_ADMIN], AuthorityEnum[AuthorityEnum.CORPOBLIG_BACKOFFICE_INLAND]]
            }
        ]
    },
    {
        path: '',
        title: 'MENU.FRONT_END',
        icon: 'mdi mdi-airplay',
        class: 'has-arrow',
        ddclass: '',
        extralink: false,
        authorities: [AuthorityEnum[AuthorityEnum.CORPOBLIG_ADMIN], AuthorityEnum[AuthorityEnum.CORPOBLIG_USER]],
        submenu: [
            {
                path: '/front-end/agenda',
                title: 'MENU.AGENDA_CONTROLLER',
                icon: 'mdi mdi-view-agenda',
                class: '',
                ddclass: '',
                extralink: false,
                submenu: [],
                authorities: [AuthorityEnum[AuthorityEnum.CORPOBLIG_ADMIN], AuthorityEnum[AuthorityEnum.CORPOBLIG_CONTROLLER]]
            },
            {
                path: '/front-end/agenda-controlled',
                title: 'MENU.AGENDA_CONTROLLED',
                icon: 'mdi mdi-view-column',
                class: '',
                ddclass: '',
                extralink: false,
                submenu: [],
                authorities: [AuthorityEnum[AuthorityEnum.CORPOBLIG_ADMIN], AuthorityEnum[AuthorityEnum.CORPOBLIG_CONTROLLED]]
            }
        ]
    }
];
