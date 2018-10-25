import {RouteInfo} from './sidebar.metadata';
import {AuthorityEnum} from '../common/api/enum/authority.enum';

export const ROUTES: RouteInfo[] = [
    {
        path: '/dashboard',
        title: 'Dashboard',
        icon: 'mdi mdi-view-dashboard',
        class: '',
        ddclass: '',
        extralink: false,
        submenu: []
    },
    {
        path: '',
        title: 'Back Office',
        icon: 'mdi mdi-desktop-mac',
        class: 'has-arrow',
        ddclass: '',
        extralink: false,
        authorities: [AuthorityEnum[AuthorityEnum.CORPOBLIG_ADMIN], AuthorityEnum[AuthorityEnum.CORPOBLIG_BACKOFFICE_FOREIGN], AuthorityEnum[AuthorityEnum.CORPOBLIG_BACKOFFICE_INLAND]],
        submenu: [
            {
                path: '/back-office/company',
                title: 'Company',
                icon: 'mdi mdi-briefcase',
                class: '',
                ddclass: '',
                extralink: false,
                submenu: [],
                authorities: [AuthorityEnum[AuthorityEnum.CORPOBLIG_ADMIN], AuthorityEnum[AuthorityEnum.CORPOBLIG_BACKOFFICE_FOREIGN], AuthorityEnum[AuthorityEnum.CORPOBLIG_BACKOFFICE_INLAND]]
            },
            {
                path: '/back-office/office',
                title: 'Office',
                icon: 'mdi mdi-glassdoor',
                class: '',
                ddclass: '',
                extralink: false,
                submenu: [],
                authorities: [AuthorityEnum[AuthorityEnum.CORPOBLIG_ADMIN], AuthorityEnum[AuthorityEnum.CORPOBLIG_BACKOFFICE_FOREIGN], AuthorityEnum[AuthorityEnum.CORPOBLIG_BACKOFFICE_INLAND]]
            },
            {
                path: '/back-office/topic',
                title: 'Topic',
                icon: 'mdi mdi-network-question',
                class: '',
                ddclass: '',
                extralink: false,
                submenu: [],
                authorities: [AuthorityEnum[AuthorityEnum.CORPOBLIG_ADMIN], AuthorityEnum[AuthorityEnum.CORPOBLIG_BACKOFFICE_FOREIGN], AuthorityEnum[AuthorityEnum.CORPOBLIG_BACKOFFICE_INLAND]]
            },
            {
                path: '/back-office/consultant',
                title: 'Consultant',
                icon: 'mdi mdi-account-settings',
                class: '',
                ddclass: '',
                extralink: false,
                submenu: [],
                authorities: [AuthorityEnum[AuthorityEnum.CORPOBLIG_ADMIN], AuthorityEnum[AuthorityEnum.CORPOBLIG_BACKOFFICE_FOREIGN], AuthorityEnum[AuthorityEnum.CORPOBLIG_BACKOFFICE_INLAND]]
            },
            {
                path: '/back-office/task',
                title: 'Task',
                icon: 'mdi mdi-table-large',
                class: '',
                ddclass: '',
                extralink: false,
                submenu: [],
                authorities: [AuthorityEnum[AuthorityEnum.CORPOBLIG_ADMIN], AuthorityEnum[AuthorityEnum.CORPOBLIG_BACKOFFICE_FOREIGN]]
            },
            {
                path: '/back-office/office-task',
                title: 'Office task',
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
        title: 'Front End',
        icon: 'mdi mdi-airplay',
        class: 'has-arrow',
        ddclass: '',
        extralink: false,
        authorities: [AuthorityEnum[AuthorityEnum.CORPOBLIG_ADMIN], AuthorityEnum[AuthorityEnum.CORPOBLIG_USER]],
        submenu: [
            {
                path: '/front-end/agenda',
                title: 'Agenda Controller',
                icon: 'mdi mdi-view-agenda',
                class: '',
                ddclass: '',
                extralink: false,
                submenu: [],
                authorities: [AuthorityEnum[AuthorityEnum.CORPOBLIG_ADMIN], AuthorityEnum[AuthorityEnum.CORPOBLIG_CONTROLLER]]
            }
        ]
    }
];
