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
  }
];
