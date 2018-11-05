import {Routes} from '@angular/router';
import {CompanyComponent} from './company/component/company.component';
import {CompanyCreateEditComponent} from './company/component/company-create-edit/company-create-edit.component';
import {CompanyAssociateUsersComponent} from './company/component/company-associate-users/company-associate-users.component';
import {OfficeComponent} from './office/component/office.component';
import {OfficeCreateEditComponent} from './office/component/office-create-edit/office-create-edit.component';
import {TopicComponent} from './topic/component/topic.component';
import {TopicCreateUpdateComponent} from './topic/component/topic-create-update/topic-create-update.component';
import {ConsultantComponent} from './consultant/component/consultant.component';
import {ConsultantCreateUpdateComponent} from './consultant/component/consultant-create-update/consultant-create-update.component';
import {TaskTemplateCreateUpdateComponent} from './tasktemplate/component/tasktemplate-create-update/task-template-create-update.component';
import {TaskTemplateComponent} from './tasktemplate/component/task-template.component';
import {TaskComponent} from './task/component/task.component';
import {OfficeTaskComponent} from './office-task/component/office-task.component';
import {QuickConfigurationComponent} from './office-task/component/quick-configuration/quick-configuration.component';

export const BackOfficeRoutes: Routes = [
    {
        path: '',
        children: [
            {
                path: 'company',
                component: CompanyComponent,
                data: {
                    title: 'Corporate Obligations - Company',
                    urls: [
                        {title: 'MENU.COMPANY', url: '/back-office/company'}
                    ]
                }
            },
            {
                path: 'company/create',
                component: CompanyCreateEditComponent,
                data: {
                    title: 'Corporate Obligations - Company',
                    urls: [
                        {title: 'MENU.COMPANY', url: '/back-office/company'}
                    ]
                }
            },
            {
                path: 'company/edit',
                component: CompanyCreateEditComponent,
                data: {
                    title: 'Corporate Obligations - Company',
                    urls: [
                        {title: 'MENU.COMPANY', url: '/back-office/company'}
                    ]
                }
            },
            {
                path: 'company/associate-users-company',
                component: CompanyAssociateUsersComponent,
                data: {
                    title: 'Corporate Obligations - Company',
                    urls: [
                        {title: 'MENU.COMPANY', url: '/back-office/company'}
                    ]
                }
            },
            {
                path: 'office',
                component: OfficeComponent,
                data: {
                    title: 'Corporate Obligations - Office',
                    urls: [
                        {title: 'MENU.OFFICE', url: '/back-office/office'}
                    ]
                }
            },
            {
                path: 'office/create',
                component: OfficeCreateEditComponent,
                data: {
                    title: 'Corporate Obligations - Office',
                    urls: [
                        {title: 'MENU.OFFICE', url: '/back-office/office'}
                    ]
                }
            },
            {
                path: 'office/edit',
                component: OfficeCreateEditComponent,
                data: {
                    title: 'Corporate Obligations - Office',
                    urls: [
                        {title: 'MENU.OFFICE', url: '/back-office/office'}
                    ]
                }
            },
            {
                path: 'topic',
                component: TopicComponent,
                data: {
                    title: 'Corporate Obligations - Topic',
                    urls: [
                        {title: 'MENU.TOPIC', url: '/back-office/topic'}
                    ]
                }
            },
            {
                path: 'topic/create',
                component: TopicCreateUpdateComponent,
                data: {
                    title: 'Corporate Obligations - Topic',
                    urls: [
                        {title: 'MENU.TOPIC', url: '/back-office/topic'}
                    ]
                }
            },
            {
                path: 'topic/edit',
                component: TopicCreateUpdateComponent,
                data: {
                    title: 'Corporate Obligations - Topic',
                    urls: [
                        {title: 'MENU.TOPIC', url: '/back-office/topic'}
                    ]
                }
            },
            {
                path: 'consultant',
                component: ConsultantComponent,
                data: {
                    title: 'Corporate Obligations - Consultant',
                    urls: [
                        {title: 'MENU.CONSULTANT', url: '/back-office/consultant'}
                    ]
                }
            },
            {
                path: 'consultant/create',
                component: ConsultantCreateUpdateComponent,
                data: {
                    title: 'Corporate Obligations - Consultant',
                    urls: [
                        {title: 'MENU.CONSULTANT', url: '/back-office/consultant'}
                    ]
                }
            },
            {
                path: 'consultant/edit',
                component: ConsultantCreateUpdateComponent,
                data: {
                    title: 'Corporate Obligations - Consultant',
                    urls: [
                        {title: 'MENU.CONSULTANT', url: '/back-office/consultant'}
                    ]
                }
            },
            {
                path: 'task-template',
                component: TaskTemplateComponent,
                data: {
                    title: 'Corporate Obligations - Task Template',
                    urls: [
                        {title: 'MENU.TASK', url: '/back-office/task-template'}
                    ]
                }
            },
            {
                path: 'task-template/create',
                component: TaskTemplateCreateUpdateComponent,
                data: {
                    title: 'Corporate Obligations - Task Template',
                    urls: [
                        {title: 'MENU.TASK', url: '/back-office/task-template'}
                    ]
                }
            },
            {
                path: 'task-template/edit',
                component: TaskTemplateCreateUpdateComponent,
                data: {
                    title: 'Corporate Obligations - Task Template',
                    urls: [
                        {title: 'MENU.TASK', url: '/back-office/task-template'}
                    ]
                }
            },
            {
                path: 'task',
                component: TaskComponent,
                data: {
                    title: 'Corporate Obligations - Task',
                    urls: [
                        {title: 'MENU.TASK', url: '/back-office/task-template'}
                    ]
                }
            },
            {
                path: 'task/create-edit',
                component: TaskTemplateCreateUpdateComponent,
                data: {
                    title: 'Corporate Obligations - Task',
                    urls: [
                        {title: 'MENU.TASK', url: '/back-office/task-template'}
                    ]
                }
            },
            {
                path: 'office-task',
                component: OfficeTaskComponent,
                data: {
                    title: 'Corporate Obligations - Office Task',
                    urls: [
                        {title: 'MENU.OFFICE_TASK', url: '/back-office/office-task'}
                    ]
                }
            },
            {
                path: 'quick-configuration/create',
                component: QuickConfigurationComponent,
                data: {
                    title: 'Corporate Obligations - Quick Configuration',
                    urls: [
                        {title: 'MENU.TASK', url: '/back-office/office-task'}
                    ]
                }
            },
            {
                path: 'quick-configuration/edit',
                component: QuickConfigurationComponent,
                data: {
                    title: 'Corporate Obligations - Quick Configuration',
                    urls: [
                        {title: 'MENU.TASK', url: '/back-office/office-task'}
                    ]
                }
            }
        ]
    }
];
