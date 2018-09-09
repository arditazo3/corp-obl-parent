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

@NgModule({
    imports: [
        NgSelectModule,
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        NgbModule,
        NgbModalModule.forRoot(),
        CalendarModule.forRoot(),
        NgxDatatableModule,
        QuillModule,
        DragulaModule,
        RouterModule.forChild(BackOfficeRoutes),
        PerfectScrollbarModule,
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
        ConsultantComponent
    ],
    providers: [
        CompanyService,
        UserService,
        OfficeService,
        TopicService
    ]
})
export class BackOfficeModule {
}
