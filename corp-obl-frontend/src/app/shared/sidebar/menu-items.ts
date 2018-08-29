import { RouteInfo } from './sidebar.metadata';

export const ROUTES: RouteInfo[] = [
  {
    path: '',
    title: 'Dashboard',
    icon: 'mdi mdi-view-dashboard',
    class: 'has-arrow',
    ddclass: '',
    extralink: false,
    submenu: [
      {
        path: '/dashboard/trendy',
        title: 'Trendy',
        icon: 'mdi mdi-adjust',
        class: '',
        ddclass: '',
        extralink: false,
        submenu: []
      },
    ]
  },
  {
    path: '',
    title: 'Back office',
    icon: 'mdi mdi-desktop-mac',
    class: 'has-arraw',
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
      }
    ]
  }
];
