import {RouteInfo} from './sidebar.metadata';

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
        submenu: [
            {
                path: '/back-office/company',
                title: 'Company',
                icon: 'mdi mdi-briefcase',
                class: '',
                ddclass: '',
                extralink: false,
                submenu: []
            },
            {
                path: '/back-office/office',
                title: 'Office',
                icon: 'mdi mdi-glassdoor',
                class: '',
                ddclass: '',
                extralink: false,
                submenu: []
            },
            {
                path: '/back-office/topic',
                title: 'Topic',
                icon: 'mdi mdi-network-question',
                class: '',
                ddclass: '',
                extralink: false,
                submenu: []
            },
            {
                path: '/back-office/consultant',
                title: 'Consultant',
                icon: 'mdi mdi-account-settings',
                class: '',
                ddclass: '',
                extralink: false,
                submenu: []
            },
            {
                path: '/back-office/task',
                title: 'Task',
                icon: 'mdi mdi-table-large',
                class: '',
                ddclass: '',
                extralink: false,
                submenu: []
            },
            {
                path: '/back-office/office-task',
                title: 'Office task',
                icon: 'mdi mdi-file-document-box',
                class: '',
                ddclass: '',
                extralink: false,
                submenu: []
            }
        ]
    }
];
