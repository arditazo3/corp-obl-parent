import {CompanyComponent} from './company/component/company.component';
import {PerfectScrollbarModule} from 'ngx-perfect-scrollbar';
import {BackOfficeRoutes} from './back_office.routing';
import {RouterModule} from '@angular/router';
import {DragulaModule} from 'ng2-dragula';
import {QuillModule} from 'ngx-quill';
import {NgbModalModule, NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {CalendarModule} from 'angular-calendar';
import {CompanyTableComponent} from './company/component/company-table/company-table.component';
import {CompanyService} from './company/service/company.service';
import {NgxDatatableModule} from '@swimlane/ngx-datatable';
import {SweetAlert2Module} from '@toverux/ngx-sweetalert2';
import {CompanyCreateEditComponent} from './company/component/company-create-edit/company-create-edit.component';
import {UserService} from '../user/service/user.service';
import {NgSelectModule} from '@ng-select/ng-select';
import { CompanyAssociateUsersComponent } from './company/component/company-associate-users/company-associate-users.component';
import { OfficeComponent } from './office/component/office.component';
import { OfficeTableComponent } from './office/component/office-table/office-table.component';
import {OfficeService} from './office/service/office.service';
import { OfficeCreateEditComponent } from './office/component/office-create-edit/office-create-edit.component';
import { TopicTableComponent } from './topic/component/topic-table/topic-table.component';
import {TopicComponent} from './topic/component/topic.component';
import {TopicService} from './topic/service/topic.service';
import { TopicCreateUpdateComponent } from './topic/component/topic-create-update/topic-create-update.component';
import { ConsultantComponent } from './consultant/component/consultant.component';
import { ConsultantTableComponent } from './consultant/component/consultant-table/consultant-table.component';
import { ConsultantCreateUpdateComponent } from './consultant/component/consultant-create-update/consultant-create-update.component';
import {ConsultantService} from './consultant/service/consultant.service';
import {TopicConsultantComponent} from './consultant/component/topic-consultant/topic-consultant.component';
import {TaskTemplateCreateUpdateComponent} from './tasktemplate/component/tasktemplate-create-update/task-template-create-update.component';
import {TaskTemplateComponent} from './tasktemplate/component/task-template.component';
import {TaskTemplateService} from './tasktemplate/service/tasktemplate.service';
import {TranslationService} from '../shared/common/translation/translation.service';
import {FileUploadModule} from 'ng2-file-upload';
import { ConfigurationTaskComponent } from './task/component/configuration-task/configuration-task.component';
import { TaskComponent } from './task/component/task.component';
import {TaskService} from './task/service/task.service';

@NgModule({
    imports: [
        FormsModule,
        ReactiveFormsModule,
        NgSelectModule,
        CommonModule,
        NgbModule,
        NgbModalModule.forRoot(),
        CalendarModule.forRoot(),
        NgxDatatableModule,
        QuillModule,
        DragulaModule,
        RouterModule.forChild(BackOfficeRoutes),
        PerfectScrollbarModule,
        FileUploadModule,
        SweetAlert2Module.forRoot({
            buttonsStyling: false,
            customClass: 'modal-content',
            confirmButtonClass: 'btn btn-primary',
            cancelButtonClass: 'btn'
        }),
    ],
    declarations: [
        CompanyComponent,
        CompanyTableComponent,
        CompanyCreateEditComponent,
        CompanyAssociateUsersComponent,
        OfficeComponent,
        OfficeTableComponent,
        OfficeCreateEditComponent,
        TopicComponent,
        TopicTableComponent,
        TopicCreateUpdateComponent,
        ConsultantComponent,
        ConsultantTableComponent,
        ConsultantCreateUpdateComponent,
        TopicConsultantComponent,
        TaskTemplateComponent,
        TaskTemplateCreateUpdateComponent,
        ConfigurationTaskComponent,
        TaskComponent
    ],
    providers: [
        CompanyService,
        UserService,
        OfficeService,
        TopicService,
        ConsultantService,
        TaskTemplateService,
        TranslationService,
        TaskService
    ]
})
export class BackOfficeModule {
}
