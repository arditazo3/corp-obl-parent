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

export const BackOfficeRoutes: Routes = [
    {
        path: '',
        children: [
            {
                path: 'company',
                component: CompanyComponent,
                data: {
                    title: 'Company Management',
                    urls: [
                        {title: 'Back Office', url: '/back-office/company'},
                        {title: 'Company management'}
                    ]
                }
            },
            {
                path: 'company/create',
                component: CompanyCreateEditComponent,
                data: {
                    title: 'Company Management',
                    urls: [
                        {title: 'Back Office', url: '/back-office/company'},
                        {title: 'Company management'}
                    ]
                }
            },
            {
                path: 'company/edit',
                component: CompanyCreateEditComponent,
                data: {
                    title: 'Company Management',
                    urls: [
                        {title: 'Back Company', url: '/back-office/company'},
                        {title: 'Company management'}
                    ]
                }
            },
            {
                path: 'company/associate-users-company',
                component: CompanyAssociateUsersComponent,
                data: {
                    title: 'Company Management',
                    urls: [
                        {title: 'Back Office', url: '/back-office/company'},
                        {title: 'Company management'}
                    ]
                }
            },
            {
                path: 'office',
                component: OfficeComponent,
                data: {
                    title: 'Office Management',
                    urls: [
                        {title: 'Back Office', url: '/back-office/office'},
                        {title: 'Office management'}
                    ]
                }
            },
            {
                path: 'office/create',
                component: OfficeCreateEditComponent,
                data: {
                    title: 'Company Management',
                    urls: [
                        {title: 'Back Office', url: '/back-office/office'},
                        {title: 'Company management'}
                    ]
                }
            },
            {
                path: 'office/edit',
                component: OfficeCreateEditComponent,
                data: {
                    title: 'Office Management',
                    urls: [
                        {title: 'Back Office', url: '/back-office/office'},
                        {title: 'Office management'}
                    ]
                }
            },
            {
                path: 'topic',
                component: TopicComponent,
                data: {
                    title: 'Topic Management',
                    urls: [
                        {title: 'Back Office', url: '/back-office/topic'},
                        {title: 'Topic management'}
                    ]
                }
            },
            {
                path: 'topic/create',
                component: TopicCreateUpdateComponent,
                data: {
                    title: 'Company Management',
                    urls: [
                        {title: 'Back Office', url: '/back-office/topic'},
                        {title: 'Company management'}
                    ]
                }
            },
            {
                path: 'topic/edit',
                component: TopicCreateUpdateComponent,
                data: {
                    title: 'Topic Management',
                    urls: [
                        {title: 'Back Office', url: '/back-office/topic'},
                        {title: 'Topic management'}
                    ]
                }
            },
            {
                path: 'consultant',
                component: ConsultantComponent,
                data: {
                    title: 'Consultant Management',
                    urls: [
                        {title: 'Back Office', url: '/back-office/consultant'},
                        {title: 'Consultant management'}
                    ]
                }
            },
            {
                path: 'consultant/create',
                component: ConsultantCreateUpdateComponent,
                data: {
                    title: 'Consultant Management',
                    urls: [
                        {title: 'Back Office', url: '/back-office/consultant'},
                        {title: 'Consultant management'}
                    ]
                }
            },
            {
                path: 'consultant/edit',
                component: ConsultantCreateUpdateComponent,
                data: {
                    title: 'Consultant Management',
                    urls: [
                        {title: 'Back Office', url: '/back-office/consultant'},
                        {title: 'Consultant management'}
                    ]
                }
            },
            {
                path: 'task-template',
                component: TaskTemplateComponent,
                data: {
                    title: 'Task template Management',
                    urls: [
                        {title: 'Back Office', url: '/back-office/task-template'},
                        {title: 'Task template management'}
                    ]
                }
            },
            {
                path: 'task-template/create',
                component: TaskTemplateCreateUpdateComponent,
                data: {
                    title: 'Task template Management',
                    urls: [
                        {title: 'Back Office', url: '/back-office/task-template'},
                        {title: 'Task template management'}
                    ]
                }
            },
            {
                path: 'task-template/edit',
                component: TaskTemplateCreateUpdateComponent,
                data: {
                    title: 'Task template Management',
                    urls: [
                        {title: 'Back Office', url: '/back-office/task-template'},
                        {title: 'Task template management'}
                    ]
                }
            },
            {
                path: 'task',
                component: TaskComponent,
                data: {
                    title: 'Task template Management',
                    urls: [
                        {title: 'Back Office', url: '/back-office/task-template'},
                        {title: 'Task template management'}
                    ]
                }
            },
            {
                path: 'task/create-edit',
                component: TaskTemplateCreateUpdateComponent,
                data: {
                    title: 'Task Management',
                    urls: [
                        {title: 'Back Office', url: '/back-office/task-template'},
                        {title: 'Task template management'}
                    ]
                }
            },
            {
                path: 'office-task',
                component: OfficeTaskComponent,
                data: {
                    title: 'Office Task Management',
                    urls: [
                        {title: 'Back Office', url: '/back-office/office-task'},
                        {title: 'Task template management'}
                    ]
                }
            }
        ]
    }
];
